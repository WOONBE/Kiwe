# # src/commands/new_order_command.py
# from fastapi import HTTPException
# from src.api_layer.models.order_item import OrderRequest, OrderResponse, OrderResponseItem, OrderOption
# from src.infrastructure.database import Database
# class OptionOrderCommand:
#     def __init__(self, infrastructure):
#         self.database = infrastructure['database']
#         self.menu_data = self.fetch_menu_combinations()  # Fetch menu data with menu_id, name, etc.
#         # print("hiiiiiiiiiiii", self.menu_data)
#
#
#     def fetch_menu_combinations(self):
#         """Fetch unique menu combinations from the database, including menu_id and menu_name."""
#         menu_data = self.database.get_unique_menu_combinations()
#         if menu_data:
#             # Convert to a dictionary for quick lookup by menu_name
#             return {item['menu_id']: item for item in menu_data}
#         return {}
#
#     def find_menu_id_with_temp(self, menu_name, temperature):
#         """Fetch menu_id based on menu_name and temperature (if needed)."""
#         print("self.menu_data.values()", self.menu_data.values())
#         for menu_info in self.menu_data.values():
#             # print("menu_info['menu_name']",menu_info['menu_name'],menu_info['hot_or_ice'])
#             if menu_info['menu_name'] in menu_name:
#                 # If temperature matches or is not specified, return menu_id
#                 # print("menu_info['hot_or_ice'']", menu_info["hot_or_ice"], temperature,"menu_info[name]",menu_info["menu_name"])
#                 if menu_info['hot_or_ice'] == temperature or temperature == "default":
#                     # print("menu_info['menu_id'']",menu_info["menu_id"])
#                     return menu_info["menu_id"]
#                 else:
#                     continue
#         return None
#
#     def execute(self, order_data, request: OrderRequest):
#         """Process a new order and verify required options."""
#         print("Received option_order_data:", order_data)  # Debug print to check the structure
#         # print("order_data['items']",order_data['items'])
#
#         lst = order_data
#
#         items = order_data['items']
#         if not items:
#             raise HTTPException(status_code=400, detail="No items found in the order")
#
#         order_items = []
#         need_temp = 1
#         check = []
#
#         previous_orders = request.order_items
#
#         for pr in previous_orders:
#             for menu_id, menu_info in self.menu_data.items():
#                 if pr.menuId == menu_id:
#                     name = menu_info['menu_name']
#                     for comp in order_data['items']:
#                         if name == comp.get('menu_name'):
#                             temp = comp.get("temp")
#                             new_id = self.find_menu_id_with_temp(name,temp)
#
#                             if comp.option.get('shot') == 1 or pr.option.get('shot') == 1:
#                                 add_shot = 1
#                             else:
#                                 add_shot = 0
#                             if comp.option.get('sugar') == 1 or pr.option.get('sugar') == 1:
#                                 add_sugar = 1
#                             else:
#                                 add_sugar = 0
#                             order_option = OrderOption(
#                                 shot=add_shot,
#                                 sugar=add_sugar
#                             )
#                             print("hi")
#                             print("order_option", order_option)
#                             order_items.append(OrderResponseItem(
#                                 menuId=new_id,
#                                 count=pr.count,
#                                 option=order_option
#                             ))
#                         else:
#                             check.append(name)
#                             need_temp = 0
#         if need_temp:
#             response_text = f"{check}의 온도를 알려주세요"
#         else:
#             response_text = "주문이 입력되었습니다."
#         response = OrderResponse(
#             category=1,
#             need_temp=need_temp,
#             message=request.sentence,
#             order=order_items,
#             response=response_text
#         )
#         return response
#
# database = Database()  # Create an actual Database instance
# infrastructure = {"database": database}
#
#
#
#
#         #
#         # # Check if any item requires a temperature preference
#         # print("rq.items",request.order_items)
#         # if request.order_items[0].menuId == 0:
#         #     pass
#         # else:
#         #     for prev_item in request.order_items:
#         #         print("prev_item",prev_item)
#         #         order_option = OrderOption(
#         #             shot=prev_item.option.get('shot'),
#         #             sugar=prev_item.option.get('sugar')
#         #         )
#         #         print("hi")
#         #         print("order_option",order_option)
#         #         order_items.append(OrderResponseItem(
#         #             menuId=prev_item.menuId,
#         #             count=prev_item.count,
#         #             option=order_option
#         #         ))
#         #
#         #
#         # for item in order_data['items']:
#         #     # Validate required fields in each item
#         #     if 'menuId' not in item or 'count' not in item or 'options' not in item:
#         #         raise HTTPException(status_code=400, detail="Missing fields in one or more order items")
#         #     print("temp",item.get("temp"))
#         #     # Extract and process item details
#         #     menu_id = item['menuId']
#         #     menu_name = item.get('menu_name')  # Default if menu_name is missing
#         #     count = item['count']
#         #     options = item['options']
#         #     temperature = item.get("temp")  # Default to "default" if temperature is not specified
#         #
#         #     # Check for "spike" temperature and add to issues if found
#         #     if temperature == "spike":
#         #         check.append(menu_name)
#         #         need_temp = True
#         #
#         #     # Create OrderOption instance for options
#         #     order_option = OrderOption(
#         #         shot=options.get("shot", False),
#         #         sugar=options.get("sugar", False)
#         #     )
#         #     print("current order order_option", order_option)
#         #     # Append the processed order item
#         #     order_items.append(OrderResponseItem(
#         #         menuId=menu_id,
#         #         count=count,
#         #         option=order_option
#         #     ))
#         # print("order_items",order_items)
#         #
#         # # Build OrderResponse
#         #
#         # if need_temp:
#         #     response_text = f"{check}의 온도를 확인해주세요"
#         # else:
#         #     response_text = "주문을 추가하였습니다"
#         #
#         # # Create the final OrderResponse
#         # response = OrderResponse(
#         #     category=1,
#         #     need_temp=need_temp,
#         #     message=request.sentence,
#         #     order=order_items,
#         #     response=response_text
#         # )
#         # print("response",response)
#         # return response
