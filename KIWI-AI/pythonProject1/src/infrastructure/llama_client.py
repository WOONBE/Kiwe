# src/infrastructure/llama_client.py

import httpx
from fastapi import HTTPException


class LLaMAClient:
    def __init__(self, config):
        self.base_url = config.get('llama_url', "https://f6e0-34-125-63-65.ngrok-free.app")

    async def get_recommendation(self, message: str):
        async with httpx.AsyncClient() as client:
            try:
                llama_request = {
                    "sentence": message
                }
                print("now in")
                response = await client.post(
                    f"{self.base_url}/generate",
                    json=llama_request,
                    timeout=10.0
                )
                print("got response")
                response.raise_for_status()
                return response.json()["response"]

            except Exception as e:
                raise HTTPException(status_code=500, detail=f"LLaMA server error: {str(e)}")