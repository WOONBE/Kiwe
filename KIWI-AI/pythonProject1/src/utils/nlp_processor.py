import re
from fastapi import HTTPException
from konlpy.tag import Okt
from src.infrastructure.database import Database
from src.api_layer.models.order_item import OrderItem, OrderRequest


class NLPProcessor:
    # Mapping Korean quantity words to numbers
    quantities1 = [
        ("하나", 1), ("둘", 2), ("셋", 3), ("넷", 4), ("다섯", 5),
        ("여섯", 6), ("일곱", 7), ("여덟", 8), ("아홉", 9), ("열", 10),
        ("열하나", 11), ("열둘", 12), ("열셋", 13), ("열넷", 14), ("열다섯", 15),
        ("스물", 20), ("서른", 30), ("마흔", 40), ("쉰", 50), ("예순", 60),
        ("일곱개", 7), ("여덟개", 8), ("아홉개", 9), ("열개", 10),
        ("한개", 1), ("두개", 2), ("세개", 3), ("네개", 4), ("다섯개", 5),
        ("여섯개", 6), ("열개", 10),
        ("두번", 2), ("세번", 3), ("네번", 4),
        ("몇개", 0),  # This could be used to imply a user asking for an uncertain quantity
        ("몇개씩", 0),  # For plural usage asking for how many per item
    ]

    def __init__(self, db: Database):
        self.okt = Okt()  # Initialize a Korean tokenizer/pos-tagger
        self.db = db  # Store the database instance
        self.menu_items = self.fetch_menu_items()  # Preload menu items
        self.menu_data = self.fetch_menu_combinations()  # Fetch menu data with menu_id, name, etc.
        self.options = self.fetch_options()  # Placeholder for dynamic options fetching

        print("Menu Items:", self.menu_items)  # Debugging print
        print("Menu Data:", self.menu_data)    # Debugging print

    def fetch_menu_items(self):
        """Fetch menu items from the database."""
        menu_data = self.db.get_unique_menu_names()
        return [item['menu_name'] for item in menu_data] if menu_data else []

    def fetch_options(self):
        """Fetch available options (e.g., add-ons, customizations) from the database."""
        return ["shot", "sugar"]  # Only the options we are interested in (shot, sugar)

    def fetch_menu_combinations(self):
        """Fetch unique menu combinations from the database, including menu_id and menu_name."""
        menu_data = self.db.get_unique_menu_combinations()
        if menu_data:
            # Convert to a dictionary for quick lookup by menu_name
            return {item['menu_name']: item for item in menu_data}
        return {}

    def extract_menu_item_and_options(self, sentence):
        """Extract the menu item, options, and temperature preference."""
        for menu_name, menu_info in self.menu_data.items():
            if menu_name in sentence:
                sentence = sentence.replace(menu_name, '')  # Remove menu item name from sentence
                menu_id = menu_info["menu_id"]  # Retrieve menu_id based on name

                # Check for temperature keywords in the sentence
                temperature = "hot" if "따뜻한" in sentence else "cold" if "차가운" in sentence else "default"

                # Remove temperature keywords to clean up remaining sentence
                sentence = sentence.replace("따뜻한", "").replace("차가운", "")

                return menu_id, temperature, sentence

        # Raise an error if no menu item is found
        raise HTTPException(status_code=400, detail="No matching menu item found in the sentence.")

    def process_request(self, request: OrderRequest):
        """Process a Korean input sentence to extract intent and structured data."""
        order_type = self.detect_order_type(request.sentence)
        if order_type == "unknown":
            return {"request_type": order_type, "data": "다시 말씀해 주세요"}
        else:
            data = self.extract_data(request.sentence, order_type, request.order_items)
            if data is None:
                return {"request_type": order_type, "data": "다시 말씀해 주세요"}
            return {"request_type": order_type, "data": data}

    def detect_order_type(self, sentence):
        """Identify the type of request based on sentence content."""
        if re.search(r"(다음|다음 단계|진행)", sentence):
            return "next_step"
        elif re.search(r"(변경|수정|삭제|취소|다른 걸로)", sentence):
            return "modify_or_delete"
        elif re.search(r"(확인|주문내역|장바구니 확인)", sentence):
            return "check_order"
        elif re.search(r"(추천|좋은거|뭐 없나)", sentence):
            return "recommendation"
        elif re.search(r"(주문|주세요|요)", sentence):
            return "order"
        else:
            return "unknown"

    def extract_data(self, sentence, order_type, orders):
        """Extract information based on order type."""
        if order_type == "order":
            items = self.extract_multiple_orders(sentence)  # Handle multiple items
            return {"items": items}

        elif order_type == "modify_or_delete":
            action_type = self.extract_modification_type(sentence)
            cart_item = self.extract_cart_item(sentence)
            updates = self.extract_updates(sentence) if action_type == "modify" else {}
            return {"action_type": action_type, "cart_item": cart_item, "updates": updates}

        elif order_type == "check_order":
            return {"confirmation": True}

        elif order_type == "next_step":
            return {"proceed": True}

        return {}

    def extract_multiple_orders(self, sentence):
        """Extract multiple items with different options for each order."""
        items = []
        quantity = self.extract_quantity(sentence)  # Extract the total quantity of items
        remaining_sentence = sentence

        for _ in range(quantity):
            # Each iteration extracts one order (with its options)
            menuId, tmp, remaining_sentence = self.extract_menu_item_and_options(remaining_sentence)

            numbers = self.extract_quantity(remaining_sentence)
            options = self.extract_options_for_each_item(remaining_sentence)
            items.append({
                "menuId": menuId,
                "temp": tmp,
                "count": numbers,  # As we're processing one item at a time
                "options": options
            })

        return items

    def extract_quantity(self, sentence):
        """Detect the quantity mentioned in the sentence."""
        for quantity_word, quantity_value in self.quantities1:
            if quantity_word in sentence:
                return quantity_value
        return 1  # Default to 1 if no quantity is mentioned

    # def extract_menu_item_and_options(self, sentence):
    #     """Extract the menu item and options for the current order."""
    #     for menu_item in self.menu_items:
    #         try:
    #             if menu_item in sentence:
    #                 sentence = sentence.replace(menu_item, '')  # Remove menu item for further processing
    #                 # find Id of menu
    #                 # check if word about tempretature is in
    #                 return menu_item, sentence
    #         except Exception as e:
    #             raise HTTPException(status_code=500, detail=f"no matching menu {str(e)}")
        # return None, sentence  # If no menu item is found

    def extract_options_for_each_item(self, sentence):
        """Extract options (shot and sugar) for the current order."""
        options = {"shot": False, "sugar": False}  # Default options

        # Check for sugar-related options
        if "설탕" in sentence or "설탕 추가" in sentence or "설탕 넣어줘" in sentence:
            options["sugar"] = True

        # Check for shot-related options
        if "샷 추가" in sentence or "샷 더 넣어줘" in sentence:
            options["shot"] = True

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