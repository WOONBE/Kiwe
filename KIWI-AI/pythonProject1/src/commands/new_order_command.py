# src/commands/new_order_command.py
from fastapi import HTTPException
from src.api_layer.models.order_item import OrderRequest, OrderResponse

# this code needs change to get data from DB

# class NewOrderCommand:
#     def __init__(self, infrastructure):
#         self.database = infrastructure['database']
#
#     def execute(self, order_data, request: OrderRequest):
#         """Process a new order and verify required options."""
#         # order_id = order_data.get("order_id")
#         # menu_item = order_data.get("menuTitle")
#         # options = order_data.get("options", {})
#         print("order_data",order_data)
#
#         items = order_data['data']['items']
#         order_items = [
#             {
#                 "menuId": item['menuId'],
#                 "count": item['count'],
#                 "options": item['options'],
#                 "temperature": item.get("temp", "default")  # Default to "default" if temperature is not specified
#             } for item in items
#         ]
#
#         # Check if any item requires a temperature preference
#         need_temp = any(item['temperature'] != "default" for item in order_items)
#
#         # Build OrderResponse
#         response = OrderResponse(
#             category=1,  # Assuming category "1" represents an order
#             need_temp=need_temp,
#             # message=request.sentence,  # Original sentence for reference
#             order=order_items,
#             response="주문을 추가하였습니다"  # Placeholder response message for TTS or frontend
#         )
#
#         return response


class NewOrderCommand:
    def __init__(self, infrastructure):
        self.database = infrastructure['database']

    def execute(self, order_data, request: OrderRequest):
        """Process a new order and verify required options."""
        print("Received order_data:", order_data)  # Debug print to check the structure

        # Check if 'data' and 'items' are in the order_data
        # if 'data' not in order_data:
        #     raise HTTPException(status_code=400, detail="'data' key missing in order data")
        # if 'items' not in order_data['data']:
        #     raise HTTPException(status_code=400, detail="'items' key missing in order data['data']")

        items = order_data['items']
        if not items:
            raise HTTPException(status_code=400, detail="No items found in the order")

        order_items = [
            {
                "menuId": item['menuId'],
                "count": item['count'],
                "options": item['options'],
                "temperature": item.get("temp", "default")  # Default to "default" if temperature is not specified
            } for item in items
        ]

        # Check if any item requires a temperature preference
        need_temp = any(item['temperature'] != "default" for item in order_items)

        # Build OrderResponse
        response = OrderResponse(
            category=1,  # Assuming category "1" represents an order
            need_temp=need_temp,
            order=order_items,
            response="주문을 추가하였습니다"  # Placeholder response message for TTS or frontend
        )

        return response


        # items = order_data.get("items")
        # first_item = items[0]
        # menu_item = first_item.get("menuTitle")
        # options = first_item.get("options", {})
        # quantity = first_item.get("count")
        #
        # # if request.order_items[0].menuTitle=="string":
        # #     request.order_items[0] =  {
        # #         "menuTitle": menu_item,
        # #         "count": quantity,
        # #         "option": options
        # #     }
        # # else:
        # if request.order_items[0].menuId==5:
        #     order = order_data
        # else:
        #     order = request.order_items
        #     order += order_data
        #
        # return {
        #     "status": "success",
        #     "message": "Order placed successfully",
        #     "order":order
            # "menuTitle": menu_item,
            # "count": quantity,
            # "option": options
        # }

        # Verify required options for the menu item
        # required_options = self.database.get_required_options(menu_item)

        # missing_options = [opt for opt in required_options if opt not in options]


        # if "hot" in options or "small" in options:
        #     missing_options = "사이즈"
        # else:
        #     missing_options = "온도"
        #
        # if missing_options:
        #     return {
        #         "status": "incomplete",
        #         "message": f"Missing required options: {', '.join(missing_options)}",
        #         # "required_options": required_options
        #     }

        # Store the order in the database
        # success = self.database.create_order(order_id, menu_item, options)
        # if success:
        #     return {"status": "success", "message": "Order placed successfully", "menu_item":menu_item, "options":options}
        # else:
        #     return {"status": "error", "message": "Failed to place the order"}
