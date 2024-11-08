# src/api_layer/api.py

from fastapi import APIRouter, HTTPException

from src.service_layer.service_router import ServiceRouter
from src.infrastructure.database import Database
from src.api_layer.models.order_item import OrderItem, OrderRequest  # Import the model

# from src.infrastructure.elk_client import

api_router = APIRouter()

# Initialize infrastructure
database = Database()  # Initialize with database configurations
# llm_model = retrieve_recommendations()  # Initialize LLM model (e.g., for RAG pipeline)
infrastructure = {
    "database": database #,
    # "llm_model": llm_model
}

# Initialize the service router
service_router = ServiceRouter(infrastructure,database)


# @api_router.post("/process")
# async def process_request(sentence: str):
#     """
#     API endpoint to process a user's sentence and route it to the appropriate service.
#
#     :param sentence: The input sentence or query from the user.
#     :return: Response from the ServiceRouter.
#     """
#     try:
#         # Route the sentence through the Service Router
#         response = service_router.route_request(sentence)
#         return response
#     except Exception as e:
#         raise HTTPException(status_code=500, detail=str(e))



# @api_router.post("/order")
# async def create_order(item: OrderItem):
#     # Access item attributes
#     return {
#         "menuTitle": item.menuTitle,
#         "menuPrice": item.menuPrice,
#         "count": item.count,
#         "option": item.option
#     }


@api_router.post("/process_order")
async def process_order(request: OrderRequest):
    """
    Endpoint to process a user's sentence and multiple order items.

    :param request: The input object that contains a sentence and multiple order items.
    :return: The parsed details including sentence and order items.
    """
    try:

        sentence = request.sentence
        order_items = request.order_items

        response = service_router.route_request(request)
        # Access the sentence and order_items from the request


        # Here, you would typically process the sentence and order_items
        # For now, just return the data back as a demonstration
        return {
            "sentence": response.sentence,
            "order_items": [
                {
                    "menuTitle": item.menuTitle,
                    "count": item.count,
                    "option": item.option
                } for item in order_items
            ]
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))