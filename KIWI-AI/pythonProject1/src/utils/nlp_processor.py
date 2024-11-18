import re
from fastapi import HTTPException
from konlpy.tag import Okt
from src.infrastructure.database import Database
from src.api_layer.models.order_item import OrderItem, OrderRequest, OrderResponseItem, OrderOption
import json
from pathlib import Path


class NLPProcessor:

    special_categories = ["디저트", "스무디&프라페", "에이드", "신상품"]

    # Mapping Korean quantity words to numbers
    QUANTITIES = [
        ("하나", 1), ("둘", 2), ("셋", 3), ("넷", 4), ("다섯", 5),
        ("여섯", 6), ("일곱", 7), ("여덟", 8), ("아홉", 9), ("열", 10),
        ("열하나", 11), ("열둘", 12), ("열셋", 13), ("열넷", 14), ("열다섯", 15),
        ("스물", 20), ("서른", 30),
        ("일곱개", 7), ("여덟개", 8), ("아홉개", 9), ("열개", 10),
        ("한개", 1), ("두개", 2), ("세개", 3), ("네개", 4), ("다섯개", 5),("한 개", 1), ("두 개", 2), ("세 개", 3), ("네 개", 4),
        ("여섯개", 6), ("열개", 10),("한잔", 1), ("두잔", 2), ("세잔", 3), ("네잔", 4), ("다섯잔", 5),
        ("여섯잔", 6),("일곱잔", 7),("여덜잔", 8),("아홉잔", 9), ("열잔", 10), ("한 잔", 1), ("두 잔", 2), ("세 잔", 3), ("네 잔", 4), ("다섯 잔", 5),
        ("여섯 잔", 6),("일곱 잔", 7),("여덜 잔", 8),("아홉 잔", 9), ("열 잔", 10),
        ("두번", 2), ("세번", 3), ("네번", 4),
        ("몇개", 0),  # This could be used to imply a user asking for an uncertain quantity
        ("몇개씩", 0),  # For plural usage asking for how many per item
    ]

    TEMPERATURES = {
        # Hot keywords
        "따뜻한": "HOT", "뜨거운": "HOT", "뜨겁게": "HOT", "뜨끈한": "HOT", "따뜻하게": "HOT", "핫": "HOT",
        "따뜻한거": "HOT", "뜨거운거": "HOT", "뜨겁은거": "HOT", "뜨끈한거": "HOT",
        # Cold keywords
        "차가운": "ICE", "시원한": "ICE", "얼음": "ICE", "아이스": "ICE", "차갑게": "ICE",
        "차가운거": "ICE", "시원한거": "ICE", "차가운걸로": "ICE", "시원한걸로": "ICE"
    }

    SEPARATORS = ["랑", "이랑", "하고", "그리고", "에", "요"]




    def __init__(self, db: Database):
        self.okt = Okt()  # Initialize a Korean tokenizer/pos-tagger
        self.db = db  # Store the database instance
        self.menu_items = self.fetch_menu_items()  # Preload menu items
        self.menu_data = self.fetch_menu_combinations()  # Fetch menu data with menu_id, name, etc.
        self.options = self.fetch_options()  # Placeholder for dynamic options fetching
        self.menu_descs = self.fetch_menu_descs()
        self.menu_nutri = self.fetch_menu_nutri()
        print("hiiiiiiiiiiii")

        self.menu_temp_to_id = {}  # Dictionary for menu+temp combinations
        self.menu_to_default_id = {}  # Dictionary for default menu ids
        for menu_id, info in self.menu_data.items():
            menu_name = info["menu_name"]
            temp = info["hot_or_ice"]
            category = info["menu_category"]

            # Store menu+temp combination
            key = f"{menu_name}_{temp}" if temp else menu_name
            self.menu_temp_to_id[key] = menu_id

            # Store default menu id (prefer ICE if available)
            if menu_name not in self.menu_to_default_id or temp == "ICE":
                self.menu_to_default_id[menu_name] = menu_id


    def fetch_menu_items(self):
        """Fetch menu items from the database."""
        menu_data_dist = self.db.get_unique_menu_names()
        return [item['menu_name'] for item in menu_data_dist] if menu_data_dist else []

    def fetch_options(self):
        """Fetch available options (e.g., add-ons, customizations) from the database."""
        return ["shot", "sugar"]  # Only the options we are interested in (shot, sugar)

    def fetch_menu_descs(self):
        """Fetch menu items from the database."""
        menu_desc = self.db.get_unique_menu_names_descs()
        return [{'menu_name': item['menu_name'],'menu_desc': item['menu_desc']} for item in menu_desc] if menu_desc else []



    def fetch_menu_combinations(self):
        """Fetch unique menu combinations from the database, including menu_id and menu_name."""
        menu_data = self.db.get_unique_menu_combinations()
        if menu_data:
            # Convert to a dictionary for quick lookup by menu_name
            return {item['menu_id']: item for item in menu_data}
        return {}

    def fetch_menu_nutri(self):
        """Fetch menu items with nutrition and allergy data from the JSON files."""
        data_folder_path = Path(__file__).parent.parent.parent / "data"  # Adjust path based on your project structure
        nutrition_data_path = data_folder_path / "nutrition_facts.json"
        allergy_data_path = data_folder_path / "allergy_levels.json"

        # Read JSON files
        with open(nutrition_data_path, "r", encoding="utf-8") as file:
            nutrition_data = json.load(file)

        with open(allergy_data_path, "r", encoding="utf-8") as file:
            allergy_data = json.load(file)

        # Combine the data into one list of dictionaries
        menu_nutri = []
        for menu_name, nutrition in nutrition_data.items():
            # Fetch the allergy information from allergy_data (defaults to ["none"] if not found)
            allergies = allergy_data.get(menu_name, ["none"])

            # Create the combined menu item data
            menu_item = {
                "menu_name": menu_name,
                "nutrition": nutrition,  # nutrition is a dictionary, directly added
                "allergy": allergies  # allergy is a list, directly added
            }

            # Add the menu item to the list
            menu_nutri.append(menu_item)

        return menu_nutri

    def process_request(self, request):
        """Process a Korean input sentence to extract intent and structured data."""

        order_type = self.detect_order_type(request.sentence)
        print("order_type",order_type)
        if order_type == "unknown":
            return {"request_type": order_type, "data": "다시 말씀해 주세요"}

        else:
            data = self.extract_data(request, order_type)
            # print("extracted data",data)
            if data is None:
                return {"request_type": order_type, "data": "다시 말씀해 주세요"}
            return {"request_type": order_type, "data": data}

    def detect_order_type(self, sentence):
        """Identify the type of request based on sentence content."""
        if re.search(r"(다음|다음 단계|진행)", sentence):
            return "next_step"
        elif re.search(r"(주문|주세요)", sentence):
            return "order"
        elif re.search(r"(추천|좋은거|뭐 없나|뭐 있나요)", sentence):
            return "recommendation"
        elif re.search(r"(뭐야|설명)", sentence):
            return "explanation"
        elif re.search(r"(변경|수정|삭제|취소|다른 걸로)", sentence):
            return "modify_or_delete"
        elif re.search(r"(확인|주문내역|장바구니 확인)", sentence):
            return "check_order"
        else:
            return "unknown"

    def extract_data(self, request, order_type):
        """Extract information based on order type."""
        if order_type == "order":
            items = self.extract_multiple_orders(request.sentence)  # Handle multiple items
            return {"items": items}
        elif order_type == "recommendation":
            print("suggest_tmp")
            suggest_items = self.extract_suggestions(request)
            # print("suggest_items",suggest_items)
            return suggest_items
        elif order_type == "explanation":
            explain_items = self.extract_explanations(request.sentence)
            print("explain_items",explain_items)
            return explain_items
        elif order_type == "modify_or_delete":
            action_type = self.extract_modification_type(request.sentence)
            cart_item = self.extract_cart_item(request.sentence)
            updates = self.extract_updates(request.sentence) if action_type == "modify" else {}
            return {"action_type": action_type, "cart_item": cart_item, "updates": updates}

        elif order_type == "check_order":
            return {"confirmation": True}

        elif order_type == "next_step":
            return {"proceed": True}

        return {}

    def find_menu_item(self, menu_name, temperature):
        """
        Find menu_id based on menu name and temperature.
        First checks category, then handles temperature accordingly.
        """
        if not menu_name:
            return None

        # Find all menu IDs for this menu name
        menu_ids = [
            (menu_id, info) for menu_id, info in self.menu_data.items()
            if info["menu_name"] == menu_name
        ]

        if not menu_ids:
            return None

        # Check if it's a special category
        is_special = menu_ids[0][1]["menu_category"] in self.special_categories

        if is_special:
            # For special categories, just return the first matching menu_id
            return menu_ids[0][0]
        else:
            # For non-special categories, check temperature
            if temperature and temperature != "default":
                # Try to find exact temperature match
                for menu_id, info in menu_ids:
                    if info["hot_or_ice"] == temperature:
                        return menu_id

            # If no temperature specified or no match found, use "spike"
            # First try to find a "spike" temperature
            for menu_id, info in menu_ids:
                if info["hot_or_ice"] == "spike":
                    return menu_id

            # If no spike version exists, return the first menu_id
            return menu_ids[0][0]




    # def preprocess_text(self, text):
    #     """
    #     Preprocess text to handle concatenated words and remove connectors.
    #     """
    #     # First, normalize spaces in basic quantity phrases
    #     text = re.sub(r'(\b한|두|세|네|다섯|여섯|일곱|여덟|아홉|열)\s+(잔|개|번)', r'\1\2', text)
    #
    #     # Replace common order endings
    #     text = text.replace("주세요", "").replace(",", " ").strip()
    #
    #     # Handle quantities with connectors in a single step
    #     # This pattern matches quantities followed by units and optional connectors
    #     pattern = r'(하나|둘|셋|넷|다섯|여섯|일곱|여덟|아홉|열|한|두|세|네|다섯|여섯|일곱|여덟|아홉|열)(잔|개|번)(?:(이랑|랑|하고)\s*)?'
    #
    #     def replace_with_space(match):
    #         quantity = match.group(1)
    #         unit = match.group(2)
    #         # Keep the quantity and unit together, add space after
    #         return f"{quantity}{unit} "
    #
    #     # Apply the replacement
    #     text = re.sub(pattern, replace_with_space, text)
    #
    #     # Clean up any remaining connectors with spaces around them
    #     for sep in self.SEPARATORS:
    #         text = f" {text} ".replace(f" {sep} ", " ")
    #
    #     # Clean up extra spaces
    #     text = " ".join(text.split())
    #
    #     return text
    #
    # def parse_order(self, text):
    #     # Preprocess text first
    #     text = self.preprocess_text(text)
    #
    #     # First extract and remove temperature from the text
    #     current_temp = None
    #     for temp_word, temp_value in self.TEMPERATURES.items():
    #         if temp_word in text:
    #             current_temp = temp_value
    #             text = text.replace(temp_word, '').strip()
    #
    #     segments = text.split()
    #     items = []
    #
    #     i = 0
    #     while i < len(segments):
    #         # Check menu items
    #         menu_found = False
    #         for menu_name in sorted(set(info["menu_name"] for info in self.menu_data.values()), key=len,
    #                                 reverse=True):
    #             if menu_name in " ".join(segments[i:i + 2]):  # Look ahead for two-word menu names
    #                 menu_id = self.find_menu_item(menu_name, current_temp)
    #                 if menu_id:
    #                     # Get the category to determine how to handle temperature
    #                     category = self.menu_data[menu_id]["menu_category"]
    #                     temp_to_use = "default" if category in self.special_categories else (
    #                                 current_temp or "spike")
    #
    #                     # Look for quantity after the menu name
    #                     pending_quantity = 1  # Default quantity
    #                     next_idx = i + len(menu_name.split())
    #                     if next_idx < len(segments):
    #                         next_word = segments[next_idx].strip()
    #                         for quantity_word, quantity_value in self.QUANTITIES:
    #                             if next_word == quantity_word.replace(" ", ""):
    #                                 pending_quantity = quantity_value
    #                                 i = next_idx + 1  # Move past the quantity word
    #                                 break
    #                         else:
    #                             i = next_idx  # If no quantity found, just move past the menu name
    #                     else:
    #                         i = next_idx
    #
    #                     items.append({
    #                         "menu": menu_name,
    #                         "menuId": menu_id,
    #                         "temp": temp_to_use,
    #                         "count": pending_quantity,
    #                         "options": {"shot": 0, "sugar": 0}
    #                     })
    #                     menu_found = True
    #                     break
    #
    #         if not menu_found:
    #             i += 1
    #
    #     return items

    def preprocess_text(self, text):
        """
        Preprocess text to handle concatenated words and remove connectors.
        """
        # First, handle temperature words
        temp_found = None
        for temp_word in sorted(self.TEMPERATURES.keys(), key=len, reverse=True):
            if temp_word in text:
                temp_found = self.TEMPERATURES[temp_word]
                text = text.replace(temp_word, '').strip()
                break

        # Normalize spaces in basic quantity phrases
        text = re.sub(r'(\b한|두|세|네|다섯|여섯|일곱|여덟|아홉|열)\s+(잔|개|번)', r'\1\2', text)

        # Replace common order endings
        text = text.replace("주세요", "").replace(",", " ").strip()

        # Handle quantities with connectors in a single step
        pattern = r'(하나|둘|셋|넷|다섯|여섯|일곱|여덟|아홉|열|한|두|세|네|다섯|여섯|일곱|여덟|아홉|열)(잔|개|번)(?:(이랑|랑|하고)\s*)?'

        def replace_with_space(match):
            quantity = match.group(1)
            unit = match.group(2)
            return f"{quantity}{unit} "

        # Apply the replacement
        text = re.sub(pattern, replace_with_space, text)

        # Clean up any remaining connectors with spaces around them
        for sep in self.SEPARATORS:
            text = f" {text} ".replace(f" {sep} ", " ")

        # Clean up extra spaces
        text = " ".join(text.split())

        return text, temp_found

    def parse_order(self, text):
        # Preprocess text first
        text, current_temp = self.preprocess_text(text)

        segments = text.split()
        items = []

        i = 0
        while i < len(segments):
            # Check menu items
            menu_found = False
            for menu_name in sorted(set(info["menu_name"] for info in self.menu_data.values()), key=len, reverse=True):
                if menu_name in " ".join(segments[i:i + 2]):  # Look ahead for two-word menu names
                    menu_id = self.find_menu_item(menu_name, current_temp)
                    if menu_id:
                        # Get the category to determine how to handle temperature
                        category = self.menu_data[menu_id]["menu_category"]
                        temp_to_use = "default" if category in self.special_categories else (current_temp or "spike")

                        # Look for quantity after the menu name
                        pending_quantity = 1  # Default quantity
                        next_idx = i + len(menu_name.split())
                        if next_idx < len(segments):
                            next_word = segments[next_idx].strip()
                            for quantity_word, quantity_value in self.QUANTITIES:
                                if next_word == quantity_word.replace(" ", ""):
                                    pending_quantity = quantity_value
                                    i = next_idx + 1  # Move past the quantity word
                                    break
                            else:
                                i = next_idx  # If no quantity found, just move past the menu name
                        else:
                            i = next_idx

                        items.append({
                            "menu": menu_name,
                            "menuId": menu_id,
                            "temp": temp_to_use,
                            "count": pending_quantity,
                            "options": {"shot": 0, "sugar": 0}
                        })
                        menu_found = True
                        break

            if not menu_found:
                i += 1

        return items

    def extract_multiple_orders(self, sentence):
        """Extract multiple orders using the optimized parser."""
        try:
            items = self.parse_order(sentence)
            if not items:
                raise HTTPException(status_code=400, detail="No valid orders found in the sentence.")
            # Combine duplicate items with same menu and temperature
            combined_items = {}
            for item in items:
                key = (item["menu"], item["menuId"], item["temp"])
                if key in combined_items:
                    combined_items[key]["count"] += item["count"]
                else:
                    combined_items[key] = item
            return list(combined_items.values())
        except Exception as e:
            raise HTTPException(status_code=400, detail=str(e))

    def extract_options_for_each_item(self, sentence):
        """Extract options (shot and sugar) for the current order."""
        options = {"shot": 0, "sugar": 0}  # Default options

        # Check for sugar-related options
        if "설탕" in sentence or "설탕 추가" in sentence or "설탕 넣어줘" in sentence:
            options["sugar"] = 1

        # Check for shot-related options
        if "샷 추가" in sentence or "샷 더 넣어줘" in sentence:
            options["shot"] = 1

        return options

    def extract_modification_type(self, sentence):
        """Identify modification type as delete or update."""
        if re.search(r"(삭제|취소)", sentence):
            return "delete"
        elif re.search(r"(변경|수정|다른 걸로)", sentence):
            return "modify"
        return None

    def extract_cart_item(self, sentence):
        """Identify which cart item to modify or delete."""
        for menu_item in self.menu_items:
            try:
                # Check if the menu item is in the sentence
                if menu_item in sentence:
                    # If found, return the item and exit early
                    return menu_item
            except Exception as e:
                # Raise an HTTPException if any unexpected error occurs
                raise HTTPException(status_code=500, detail=f"Error processing menu item '{menu_item}': {str(e)}")

        # If no item matches, raise an HTTPException with a 400 status code
        raise HTTPException(status_code=400, detail="No matching menu item found for modification or deletion.")

    def extract_updates(self, sentence):
        """Extract specific updates for a modification request."""
        updates = {}
        if "샷 추가" in sentence:
            updates["shot"] = True
        if "설탕 추가" in sentence:
            updates["sugar"] = True
        return updates

    def extract_temperature(self,sentence):
        """Identify temperature preference from the sentence using lists of keywords."""
        # Lists of keywords for temperature preferences
        hot_words = ["따뜻한", "뜨거운", "뜨겁게", "뜨끈한", "따뜻하게", "따뜻한거","뜨거운거"]
        ice_words = ["차가운", "시원한", "얼음", "아이스", "차갑게", "차가운거","시원한거"]

        # Check if any hot word is in the sentence
        for word in hot_words:
            if word in sentence:
                sentence = sentence.replace(word, '')  # Remove the hot word from the sentence
                return "HOT", sentence

        # Check if any cold word is in the sentence
        for word in ice_words:
            if word in sentence:
                sentence = sentence.replace(word, '')  # Remove the cold word from the sentence
                return "ICE", sentence

        # Default case if no temperature keywords are found
        return "default", sentence

    def extract_suggestions(self, request):
        """Extract suggestions based on temperature preference and age."""
        # print("extract_suggestions",request)
        temperature = self.extract_suggest_keyworrds(request.sentence)
        # print("test temp prev")
        if temperature == "default":
            # If no temperature preference, use general suggestion
            suggests = self.db.get_suggest_age_order_menu(request.age)
        else:
            # If temperature preference exists, use temperature-specific suggestion
            suggests = self.db.get_suggest_age_temp_order_menu(request.age, temperature)
        # print("suggests",suggests)
        if suggests is None:
            raise HTTPException(status_code=500, detail="Error fetching suggestions from database")

        return list(suggests)

    def extract_suggest_keyworrds(self, sentence):
        """Extract temperature preference from suggestion request."""
        hot_words = ["따뜻한거", "따뜻한", "뜨거운", "뜨겁게", "뜨끈한", "따뜻하게"]
        ice_words = ["차가운", "시원한", "시원한거", "얼음", "아이스", "차갑게", "차가운거"]

        temp = "default"

        # Check if any hot word is in the sentence
        for word in hot_words:
            if word in sentence:
                sentence = sentence.replace(word, '')  # Remove the hot word from the sentence
                temp = "HOT"

        # Check if any cold word is in the sentence
        for word in ice_words:
            if word in sentence:
                sentence = sentence.replace(word, '')  # Remove the cold word from the sentence
                temp = "ICE"

        print("온도",temp)
        # Default case if no temperature keywords are found
        return temp

    def extract_explanations(self, sentence):
        """Extract explanations based on the menu items mentioned in the sentence."""
        print("sentence:", sentence)
        menus = []

        # Loop through the menu descriptions (menu_descs)
        for menu_info in self.menu_descs:
            # Use regular expression to find the full menu_name as a whole word in the sentence
            pattern = re.escape(menu_info['menu_name'])
            match = re.search(pattern, sentence)

            if match:
                # Remove the matched menu name from the sentence to process remaining words
                sentence = sentence.replace(menu_info['menu_name'], '')

                # Find the menu item in the nutrition and allergy data (menu_nutri)
                menu_nutri_info = next(
                    (item for item in self.menu_nutri if item['menu_name'] == menu_info['menu_name']), None)

                if menu_nutri_info:
                    # Append combined data to the response
                    menus.append({
                        "menu_name": menu_info['menu_name'],
                        "menu_desc": menu_info['menu_desc'],
                        "nutrition": menu_nutri_info['nutrition'],
                        "allergy": menu_nutri_info['allergy']
                    })
                else:
                    # If no nutrition data is found, just include the description
                    menus.append({
                        "menu_name": menu_info['menu_name'],
                        "menu_desc": menu_info['menu_desc'],
                        "nutrition": "영양 정보가 없습니다",
                        "allergy": "알레르기 정보가 없습니다."
                    })

        print("menu_list + desc_list + nutrition_allergy:", menus)
        return menus
