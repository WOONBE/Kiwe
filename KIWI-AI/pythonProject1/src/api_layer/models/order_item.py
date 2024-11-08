from pydantic import BaseModel
from typing import List, Dict, Optional

class OrderItem(BaseModel):
    menuTitle: str
    count: int
    option: Optional[Dict[str, str]] = {}  # Optional field with default as empty dict

class OrderRequest(BaseModel):
    sentence: str
    order_items: List[OrderItem]  # List of order items