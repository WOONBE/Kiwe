from pydantic import BaseModel
from typing import Union, List, Dict, Optional


# 뭘 가져와야 하지
# 추천의 기능 - 
# 1. 범위 및 연령 확인 
# 2. SQL로 추천 제품 3개 받기 
# 3. 메뉴명과 정보 바탕으로 llm 응답 받기 
# 4. 추천 메뉴 리스트 반환 -> 화면용 10개와 음성용 3개 따로?

# extra - 거기서 해당 메뉴 주문시 해당 라인 중 가장 비슷한 걸로 추가

# class OrderItem(BaseModel):
#     menuId: int
#     count: int
#     option: Optional[Dict[str, Union[str, bool]]] = {}  # Optional field with default as empty dict


class SuggestRequest(BaseModel):
    sentence: str
    age: int


# Response Model
# class OrderOption(BaseModel):
#     shot: int  # Whether the "shot" option is selected
#     sugar: int  # Whether the "sugar" option is selected

# class OrderResponseItem(BaseModel):
#     menuId: int  # Name of the menu item, e.g., "카페라떼"
#     count: int  # The quantity of the item
#     suggest_menus: []  # The options selected for the item

class SuggestResponse(BaseModel):
    category: int  # 1: order, 2: fix/delete, 3: suggestion, 4: explanation
    need_temp: int  # Whether temperature needs to be considered
    message: str
    suggest: List  # 메뉴 이름 리스트
    response: str  # The response, e.g., for TTS (Text-to-Speech)