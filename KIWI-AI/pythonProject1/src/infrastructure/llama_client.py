# src/infrastructure/llama_client.py

import httpx
from fastapi import HTTPException


class LLaMAClient:
    def __init__(self, config):
        self.base_url = config.get('llama_url', "https://98c6-34-75-59-52.ngrok-free.app")

    async def get_recommendation(self, message: str):
        async with httpx.AsyncClient() as client:
            try:
                llama_request = {
                    "sentence": message
                }

                response = await client.post(
                    f"{self.base_url}/generate",
                    json=llama_request,
                    timeout=60.0
                )
                response.raise_for_status()
                return response.json()["response"]

            except Exception as e:
                raise HTTPException(status_code=500, detail=f"LLaMA server error: {str(e)}")