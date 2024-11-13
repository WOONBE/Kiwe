from pydantic import BaseModel
from typing import Union, List, Dict, Optional

class OrderItem(BaseModel):
    menuId: int
    count: int
    option: Optional[Dict[str, Union[str, bool]]] = {}  # Optional field with default as empty dict


class OrderRequest(BaseModel):
    sentence: str
    need_temp: int
    order_items: List[OrderItem]  # List of order items

# Response Model
class OrderOption(BaseModel):
    shot: bool  # Whether the "shot" option is selected
    sugar: bool  # Whether the "sugar" option is selected

class OrderResponseItem(BaseModel):
    menuId: int  # Name of the menu item, e.g., "카페라떼"
    count: int  # The quantity of the item
    option: OrderOption  # The options selected for the item

class OrderResponse(BaseModel):
    category: int  # 1: order, 2: fix/delete, 3: suggestion, 4: explanation
    need_temp: int  # Whether temperature needs to be considered
    message: str
    order: List[OrderResponseItem]  # The processed list of orders
    response: str  # The response, e.g., for TTS (Text-to-Speech)