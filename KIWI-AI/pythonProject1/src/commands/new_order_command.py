# src/commands/new_order_command.py
from fastapi import HTTPException
from src.api_layer.models.order_item import OrderRequest, OrderResponse, OrderResponseItem, OrderOption

class NewOrderCommand:
    def __init__(self, infrastructure):
        self.database = infrastructure['database']

    def execute(self, order_data, request: OrderRequest):
        """Process a new order and verify required options."""
        print("Received order_data:", order_data)  # Debug print to check the structure

        items = order_data['items']
        if not items:
            raise HTTPException(status_code=400, detail="No items found in the order")

        order_items = []
        need_temp = 1
        check = []

        # Check if any item requires a temperature preference
        print("rq.items",request.order_items)
        if request.order_items == [] or request.order_items[0].menuId == 0:
            pass
        else:
            for prev_item in request.order_items:
                order_option = OrderOption(
                    shot=prev_item.option.get('shot'),
                    sugar=prev_item.option.get('sugar')
                )
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
            # temperature = item.get("temp")  # Default to "default" if temperature is not specified

            # Check for "spike" temperature and add to issues if found
            if item.get("temp") == "spike":
                print("spike")
                check.append(menu_name)
                need_temp = 0

            # Create OrderOption instance for options
            order_option = OrderOption(
                shot=options.get("shot", 0),
                sugar=options.get("sugar", 0)
            )
            # Append the processed order item
            order_items.append(OrderResponseItem(
                menuId=menu_id,
                count=count,
                option=order_option
            ))
        print("order_items",order_items,type(need_temp))
        print("need_temp", need_temp, type(need_temp))
        # Build OrderResponse

        if need_temp == 0:
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
