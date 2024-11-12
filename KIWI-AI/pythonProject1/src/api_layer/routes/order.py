# src/api_layer/routes/order.py

from fastapi import APIRouter, HTTPException
from src.api_layer.models.order_item import OrderRequest, OrderResponse, OrderResponseItem, OrderOption
from src.utils.nlp_processor import NLPProcessor
from src.infrastructure.database import Database

# Initialize the router
order_router = APIRouter()

# Database and NLPProcessor initialization
db = Database()
db.connect()  # Connect to the database
nlp_processor = NLPProcessor(db)

@order_router.post("/process_order")
async def process_order(request: OrderRequest):
    """
    Process a user's order request.
    """
    try:
        # Process the sentence with NLPProcessor
        response = nlp_processor.process_request(request)
        print("response",response)
        # Ensure the response has items
        if "items" not in response['data']:
            raise HTTPException(status_code=400, detail="Invalid order format")
        print("response['data']",response['data'],request)

        items = response['data']['items']
        order_items = []
        print("request",request)
        if request.order_items:
            print("request.order_items",request.order_items)
            for prev_item in request.order_items:
                print("prev_item",prev_item)
                order_items.append(OrderResponseItem(
                    menuId=prev_item['menuId'],
                    count=prev_item['count'],
                    option=OrderOption(  # Populate the options (e.g., shot, sugar)
                        shot=prev_item.get('shot', False),  # Default False if not specified
                        sugar=prev_item.get('sugar', False)  # Default False if not specified
                    ),
                    # temperature=item.get("temp", "default")  # Default to "default" if temperature is not specified
                ))

        for item in items:
            order_items.append(OrderResponseItem(
                menuId=item['menuId'],
                count=item['count'],
                option=OrderOption(  # Populate the options (e.g., shot, sugar)
                    shot=item.get('shot', False),  # Default False if not specified
                    sugar=item.get('sugar', False)  # Default False if not specified
                ),
                # temperature=item.get("temp", "default")  # Default to "default" if temperature is not specified
            ))
        print("order_items",order_items)


        # Check if any item requires a temperature preference
        need_temp = any(item['temperature'] != "default" for item in order_items)

        # Build OrderResponse
        response = OrderResponse(
            category=1,  # Assuming category "1" represents an order
            need_temp=need_temp,
            message=request.sentence,  # Original sentence for reference
            order=order_items,
            response="주문을 추가하였습니다"  # Placeholder response message for TTS or frontend
        )

        return response

    except Exception as e:
        # Handle exceptions gracefully with an HTTP error response
        raise HTTPException(status_code=500, detail=str(e))
