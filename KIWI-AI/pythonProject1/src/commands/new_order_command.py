# src/commands/new_order_command.py
from fastapi import HTTPException
from src.api_layer.models.order_item import OrderRequest, OrderResponse, OrderResponseItem, OrderOption

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

        order_items = []
        need_temp = False
        check = []

        #     {
        #         "menuId": item['menuId'],
        #         "menuName": item['menu_name'],
        #         "count": item['count'],
        #         "options": item['options'],
        #         "temperature": item.get("temp", "default")  # Default to "default" if temperature is not specified
        #     } for item in items
        # ]

        # Check if any item requires a temperature preference
        order_items = []
        print("rq.items",request.order_items)
        for prev_item in request.order_items:
            print("prev_item",prev_item)
            order_option = OrderOption(
                shot=prev_item.option.get('shot'),
                sugar=prev_item.option.get('sugar')
            )
            print("order_option",order_option)
            order_items.append(OrderResponseItem(
                menuId=prev_item.menuId,
                count=prev_item.count,
                option=order_option
            ))


        for item in order_data['items']:
            # Validate required fields in each item
            if 'menuId' not in item or 'count' not in item or 'options' not in item:
                raise HTTPException(status_code=400, detail="Missing fields in one or more order items")

            # Extract and process item details
            menu_id = item['menuId']
            menu_name = item.get('menu_name')  # Default if menu_name is missing
            count = item['count']
            options = item['options']
            temperature = item.get("temp")  # Default to "default" if temperature is not specified

            # Check for "spike" temperature and add to issues if found
            if temperature == "spike":
                check.append(menu_name)
                need_temp = True

            # Create OrderOption instance for options
            order_option = OrderOption(
                shot=options.get("shot", False),
                sugar=options.get("sugar", False)
            )

            # Append the processed order item
            order_items.append(OrderResponseItem(
                menuId=menu_id,
                count=count,
                option=order_option,
                temperature=temperature
            ))
        print("order_items",order_items)
        # for item in order_items:
        #     if item['temperature'] == "spike":
        #         check.append(item['menuName'])
        #         need_temp = True
        # need_temp = any(item['temperature'] == "spike" for item in order_items)



        # Build OrderResponse

        if need_temp:
            response_text = f"{check}의 온도를 확인해주세요"
        else:
            response_text = "주문을 추가하였습니다"

        # Create the final OrderResponse
        response = OrderResponse(
            category=1,
            need_temp=need_temp,
            message=request.sentence,
            order=order_items,
            response=response_text
        )
        print("response",response)
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
