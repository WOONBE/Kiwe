# src/api_layer/routes/order.py

from fastapi import APIRouter, HTTPException
from src.api_layer.models.order_item import OrderRequest
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

        # Ensure the response has items
        if "items" not in response['data']:
            raise HTTPException(status_code=400, detail="Invalid order format")

        # Format the response
        return {
            "sentence": response['data'],
            "order_items": [
                {
                    "menuTitle": item['menuTitle'],
                    "count": item['count'],
                    "options": item['options']
                } for item in response['data']['items']
            ]
        }

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
