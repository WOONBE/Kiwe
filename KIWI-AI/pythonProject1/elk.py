# from konlpy.tag import Okt
#
# # Initialize the Okt analyzer - keep this as a global instance
# okt = Okt()
#
# # Pre-compile all the lookup sets and maps for faster access
# QUANTITIES = {
#     "한": 1, "두": 2, "세": 3, "네": 4, "다섯": 5,
#     "한잔": 1, "한개": 1, "한 개": 1, "한 잔": 1,
#     "두잔": 2, "두개": 2, "두 잔": 2, "두 개": 2,
#     "세잔": 3, "세개": 3, "세 잔": 3, "세 개": 3,
#     "네잔": 4, "네개": 4, "네 잔": 4, "네 개": 4,
#     "다섯잔": 5, "다섯개": 5, "다섯 잔": 5, "다섯 개": 5
# }
#
# TEMPERATURES = {"아이스": "iced", "핫": "hot"}
# MENU_ITEMS = frozenset(["아메리카노", "카페라떼", "메가리카노", "초코케익"])
# UNIT_WORDS = frozenset(["개", "잔"])
#
#
# def parse_order(text):
#     tokens = okt.pos(text)
#     items = []
#     current_item = {"menu": None, "quantity": 1, "temperature": None}
#
#     i = 0
#     tokens_len = len(tokens)
#
#     while i < tokens_len:
#         word, tag = tokens[i]
#
#         # Fast temperature check
#         if word in TEMPERATURES:
#             current_item["temperature"] = TEMPERATURES[word]
#             i += 1
#             continue
#
#         # Fast menu check with combined tokens
#         if i + 1 < tokens_len:
#             combined = word + tokens[i + 1][0]
#             if combined in MENU_ITEMS:
#                 current_item["menu"] = combined
#                 items.append(current_item)
#                 current_item = {"menu": None, "quantity": 1, "temperature": None}
#                 i += 2
#                 continue
#
#         if word in MENU_ITEMS:
#             current_item["menu"] = word
#             items.append(current_item)
#             current_item = {"menu": None, "quantity": 1, "temperature": None}
#             i += 1
#             continue
#
#         # Optimized quantity check
#         if word in QUANTITIES:
#             if items:
#                 items[-1]["quantity"] = QUANTITIES[word]
#                 # Skip the unit word if it exists
#                 if i + 1 < tokens_len and tokens[i + 1][0] in UNIT_WORDS:
#                     i += 2
#                 else:
#                     i += 1
#                 continue
#
#         # Handle number + unit combinations
#         if i + 1 < tokens_len:
#             combined = word + " " + tokens[i + 1][0]
#             if combined in QUANTITIES and items:
#                 items[-1]["quantity"] = QUANTITIES[combined]
#                 i += 2
#                 continue
#
#         i += 1
#
#     return items
#
#
# # Test function with timing
# if __name__ == "__main__":
#     import time
#
#     order_text = "아이스 카페라떼 한개 주세요"
#
#     start_time = time.time()
#     result = parse_order(order_text)
#     end_time = time.time()
#
#     print(f"Execution time: {(end_time - start_time) * 1000:.2f}ms")
#     print("Results:", result)
def parse_order(text):
    # Pre-compile all the lookup sets and maps for faster access
    QUANTITIES = {
        "한": 1, "두": 2, "세": 3, "네": 4, "다섯": 5,
        "한잔": 1, "한개": 1, "한 개": 1, "한 잔": 1,
        "두잔": 2, "두개": 2, "두 잔": 2, "두 개": 2,
        "세잔": 3, "세개": 3, "세 잔": 3, "세 개": 3,
        "네잔": 4, "네개": 4, "네 잔": 4, "네 개": 4,
        "다섯잔": 5, "다섯개": 5, "다섯 잔": 5, "다섯 개": 5
    }

    TEMPERATURES = {"아이스": "iced", "핫": "hot"}
    MENU_ITEMS = {"아메리카노", "카페라떼", "메가리카노", "초코케익"}

    # Simple tokenization by splitting on spaces
    tokens = text.replace(",", " ").replace("랑", " ").replace("주세요", "").strip().split()

    items = []
    current_item = {"menu": None, "quantity": 1, "temperature": None}

    i = 0
    while i < len(tokens):
        token = tokens[i]

        # Check temperature
        if token in TEMPERATURES:
            current_item["temperature"] = TEMPERATURES[token]
            i += 1
            continue

        # Check menu items
        if token in MENU_ITEMS:
            current_item["menu"] = token
            items.append(current_item.copy())
            current_item = {"menu": None, "quantity": 1, "temperature": None}
            i += 1
            continue

        # Check quantities
        # Try two-word quantity first
        if i + 1 < len(tokens) and token + " " + tokens[i + 1] in QUANTITIES:
            if items:
                items[-1]["quantity"] = QUANTITIES[token + " " + tokens[i + 1]]
            i += 2
            continue
        # Try single-word quantity
        elif token in QUANTITIES:
            if items:
                items[-1]["quantity"] = QUANTITIES[token]
            i += 1
            continue

        i += 1

    return items


# Test function with timing
if __name__ == "__main__":
    import time

    # Test cases
    test_cases = [
        "아이스 카페라떼 한개 주세요",
        "아이스 카페라떼 한개랑 핫 아메리카노 두잔, 초코케익 한 개랑 메가리카노 다섯개 주세요"
    ]

    for order_text in test_cases:
        print(f"\nTesting order: {order_text}")
        start_time = time.time()
        result = parse_order(order_text)
        end_time = time.time()

        print(f"Execution time: {(end_time - start_time) * 1000:.2f}ms")
        print("Results:", result)