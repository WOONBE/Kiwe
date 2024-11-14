# src/main.py

import os
import yaml
import requests
from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager

from src.service_layer.service_router import ServiceRouter
# from src.utils.logger import setup_logger, get_logger
from src.utils.nlp_processor import NLPProcessor
from src.utils.response_formatter import format_success_response, format_error_response
from src.api_layer.models.order_item import OrderRequest, OrderResponse, OrderOption, OrderResponseItem
from src.api_layer.models.suggest_item import SuggestRequest, SuggestResponse
from src.api_layer.api import api_router  # Import the APIRouter from api.py
from src.infrastructure.llama_client import LLaMAClient
from src.infrastructure.elk_client import ELKClient
from src.infrastructure.database import Database
from typing import Dict, Optional
from pydantic import BaseModel

# Load environment variables
load_dotenv()

# Initialize FastAPI app
app = FastAPI(title="Café Order System", lifespan="lifespan")

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Load configurations
def load_config():
    with open('config/config.yaml') as file:
        return yaml.safe_load(file)

config = load_config()
# setup_logger(config['logging'])
llama_client = LLaMAClient(config['llama'])

# Initialize Database and ELK client
database = Database()
database.connect()  # Ensure connection to the database

elk_client = ELKClient(config['elk'])
infrastructure = {
    "database": database,
    "retriever": elk_client,
    "llama_client": llama_client
}

# Initialize NLP Processor and Service Layer
nlp_processor = NLPProcessor(database)  # Pass Database instance to NLPProcessor
service_router = ServiceRouter(infrastructure,database)
app.include_router(api_router)

@asynccontextmanager
async def lifespan(app: FastAPI):
    """
    Lifespan context manager that handles startup and shutdown events.
    """
    # Startup logic
    database.connect()  # Connect to the database during startup
    print("Database connection established.")
    print("hi!")
    yield  # The application runs here

    # Shutdown logic
    database.close()  # Close the database connection during shutdown
    print("Database connection closed.")

# Use the lifespan context manager with the FastAPI app
app = FastAPI(title="Café Order System", lifespan=lifespan)


@app.post("/order")
async def process_order(order_request: OrderRequest):
    """
    Process an order request and return a structured response.
    """
    try:

        if order_request.need_temp == 0:
            order_option = OrderOption(
                shot=False,
                sugar=False
            )
            # Append the processed order
            item = OrderResponseItem(
                menuId=0,
                count=0,
                option=order_option
            )
            response_text = "잘못된 접근입니다."
            # Create the final OrderResponse
            response = OrderResponse(
                category=1,
                need_temp=1,
                message=order_request.sentence,
                order=[item],
                response=response_text
            )
        else:
            response = await service_router.route_request(order_request)
            return response

        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"error response  {str(e)}")

# @app.post("/option")
# async def option_order(order_request: OrderRequest):
#     """
#     Process an order request and return a structured response.
#
#     return은 id, count, option 가지고 있고, response에서 음료 이름들 가지고 있다.
#     비교시 각 id의 이름들과 비교하며 새로들어온 입력을 우선으로 넣는다.
#     """
#     if order_request.need_temp == 0:
#         response = service_router.route_request(order_request)
#         return response
#     else:
#         order_option = OrderOption(
#             shot=0,
#             sugar=0
#         )
#         # Append the processed order
#         item = OrderResponseItem(
#             menuId=0,
#             count=0,
#             option=order_option
#         )
#         response_text = "잘못된 접근입니다."
#         # Create the final OrderResponse
#         response = OrderResponse(
#             category=1,
#             need_temp=0,
#             message=order_request.sentence,
#             order=[item],
#             response=response_text
#         )
#
#     return response


@app.post("/suggest")
async def process_suggest(order_request: OrderRequest):
    """
    Process an order request and return a structured response.
    """
    try:

        if order_request.need_temp == 0:
            order_option = OrderOption(
                shot=False,
                sugar=False
            )
            # Append the processed order
            item = OrderResponseItem(
                menuId=0,
                count=0,
                option=order_option
            )
            response_text = "잘못된 접근입니다."
            # Create the final OrderResponse
            response = OrderResponse(
                category=1,
                need_temp=1,
                message=order_request.sentence,
                order=[item],
                response=response_text
            )
        else:
            response = service_router.route_request(order_request)
            return response

        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"error response  {str(e)}")

@app.post("/recommend")
async def get_recommendation(request: SuggestRequest):
    try:
        response = await service_router.route_request(request)
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

# @app.post("/explanation")
# async def get_explanation(request: SuggestRequest):
#     try:
#         response = await service_router.route_request(request)
#         return response
#     except Exception as e:
#         raise HTTPException(status_code=500, detail=str(e))
