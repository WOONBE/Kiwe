# src/main.py

import os
import yaml
import requests
from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from contextlib import asynccontextmanager

from src.service_layer.service_router import ServiceRouter
from src.infrastructure.database import Database
from src.infrastructure.elk_client import ELKClient
# from src.utils.logger import setup_logger, get_logger
from src.utils.nlp_processor import NLPProcessor
from src.utils.response_formatter import format_success_response, format_error_response

from src.api_layer.models.order_item import OrderRequest,OrderResponse

from src.api_layer.api import api_router  # Import the APIRouter from api.py

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

# Initialize Database and ELK client
database = Database()
database.connect()  # Ensure connection to the database

elk_client = ELKClient(config['elk'])
infrastructure = {
    "database": database,
    "retriever": elk_client
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
        print("order_request",order_request)
        # Route request through ServiceRouter
        response = service_router.route_request(order_request)
        print("response",response)
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"error response  {str(e)}")




class PromptRequest(BaseModel):
    prompt: str

# Define the function to send prompt to LLM
def send_prompt_to_llm(prompt: str):
    """Send a prompt to the LLM server and get a response."""
    try:
        response = requests.post("http://70.12.130.121:9988/infer", json={"prompt": prompt})
        response.raise_for_status()  # Ensure a 200 status
        return response.json().get("response")
    except requests.exceptions.RequestException as e:
        print(f"Error: {e}")
        return None

# Define an endpoint for handling prompts
@app.post("/get_llm_response")
async def get_llm_response(prompt_request: PromptRequest):
    """
    Send a prompt to the LLM server and return its response.
    """
    response = send_prompt_to_llm(prompt_request.prompt)
    if response:
        return {"response": response}
    else:
        raise HTTPException(status_code=500, detail="Failed to get a response from the LLM server.")

# @app.post("/process")
# async def process_request(sentence: str):
#     try:
#         nlp_result = nlp_processor.process_sentence(sentence)
#         response = service_router.route_request(sentence) #nlp_result["request_type"], nlp_result["data"])
#         return format_success_response(response)
#     except Exception as e:
#         logger = get_logger(__name__)
#         logger.error(f"Error processing request: {e}")
#         raise HTTPException(status_code=500, detail=format_error_response(str(e)))

# Close database connection on shutdown
# @app.on_event("shutdown")
# def shutdown_event():
#     database.close()
#     print("Database connection closed.")
