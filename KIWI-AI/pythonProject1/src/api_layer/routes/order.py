# src/api_layer/routes/order.py

from fastapi import APIRouter, HTTPException
from src.api_layer.models.order_item import OrderRequest, OrderResponse
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
        print("response['data']",response['data'])

        items = response['data']['items']
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
            # message=request.sentence,  # Original sentence for reference
            order=order_items,
            response="주문을 추가하였습니다"  # Placeholder response message for TTS or frontend
        )

        return response

        # Return the formatted response
        # return {
        #     "sentence": request.sentence,
        #     "order_items": order_items
        # }

    except Exception as e:
        # Handle exceptions gracefully with an HTTP error response
        raise HTTPException(status_code=500, detail=str(e))

    #     response = OrderResponse(
    #         category=1,  # Assuming the category is "order"
    #         need_temp=have_temp,
    #         message=sentence,
    #         order=processed_order,
    #         response=""  # Example response for TTS
    #     )
    #
    #     # Format the response
    #     return {
    #         "sentence": response['data'],
    #         "order_items": [
    #             {
    #                 "menuId": item['menuId'],
    #                 "count": item['count'],
    #                 "options": item['options']
    #             } for item in response['data']['items']
    #         ]
    #     }
    #
    # except Exception as e:
    #     raise HTTPException(status_code=500, detail=str(e))
