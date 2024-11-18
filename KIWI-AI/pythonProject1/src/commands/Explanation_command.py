# src/commands/Explanation_command.py

from src.commands.base_command import BaseCommand


class ExplanationCommand(BaseCommand):
    def __init__(self, infrastructure):
        """
        Initialize with infrastructure components, including retriever and generator for RAG.
        """
        super().__init__(infrastructure)
        self.retriever = infrastructure.get("retriever")  # For document retrieval in RAG
        self.llama_client = infrastructure.get("llama_client")

    async def execute(self, data):
        """
        Execute the explanation command by retrieving relevant information and generating a response.

        :param data: Dictionary containing details needed for the explanation, like the item name.
        :return: Dictionary with the response message and explanation.
        """
        print()
        print("data length",len(data))
        # print("data", data)

        menu_infos = []



        for i in data:
            nutrition_str = ", ".join(f"{key}: {value}" for key, value in i["nutrition"].items())
            # Convert allergy list to a string (e.g., 'dairy, gluten')
            allergy_str = ", ".join(i["allergy"])
            print("menu data",i["menu_name"],i["menu_desc"])
            menu_infos.append(f"{i['menu_name']}에 대한 설명은 다음과 같습니다. {i['menu_desc']} "
                              f"영양 정보: {nutrition_str} 알레르기 정보: {allergy_str}")
            # menu_infos.append(i["menu_name"] + "에 대한 설명은 다음과 같습니다. " + i["menu_desc"] + ' ' + i["nutrition"] + i["allergy"])
        menu_info_str = ", ".join(menu_infos)
        try:
            if self.llama_client:
                context = f"{menu_infos}"
                recommendation = await self.llama_client.get_recommendation(context)
                return {"status": "success", "message": recommendation}
            return {"status": "success", "message": f"{menu_info_str}" }
        except Exception as e:
            print(f"Error in recommendation command: {str(e)}")
            return {"status": "No server", "message": f"{menu_info_str}"}



# {
#   "1": {
#    "name" :  "디카페인 아메리카노",
#     "nutrition": {
#       "calories": 5,
#       "carbs": 1,
#       "protein": 0,
#       "fat": 0,
#       "sugars": 0
#     },
#     "allergy": ["none"]
#   },
#   "2": {
#    "name" :  "디카페인 꿀아메리카노",
#     "nutrition": {
#       "calories": 120,
#       "carbs": 28,
#       "protein": 0,
#       "fat": 0,
#       "sugars": 28
#     },
#     "allergy": ["honey"]
#   "5": {
#    "name" :  "디카페인 카페라떼",
#     "nutrition": {
#       "calories": 150,
#       "carbs": 14,
#       "protein": 8,
#       "fat": 7,
#       "sugars": 12
#     },
#     "allergy": ["dairy"]
#   "9": {
#    "name" :  "디카페인 카페모카",
#     "nutrition": {
#       "calories": 210,
#       "carbs": 30,
#       "protein": 6,
#       "fat": 9,
#       "sugars": 25
#     },
#     "allergy": ["dairy", "gluten"]
#   },
#   "32": {
#    "name" :  "스모어블랙쿠키프라페",
#     "nutrition": {
#       "calories": 350,
#       "carbs": 50,
#       "protein": 5,
#       "fat": 15,
#       "sugars": 40
#     },
#     "allergy": ["gluten", "dairy", "nuts"]
#   },
#   "44": {
#    "name" :  "수박 주스",
#     "nutrition": {
#       "calories": 180,
#       "carbs": 45,
#       "protein": 1,
#       "fat": 0,
#       "sugars": 35
#     },
#     "allergy": ["none"]
#   },
#   "148": {
#    "name" : "감자빵",
#     "nutrition": {
#       "calories": 250,
#       "carbs": 40,
#       "protein": 5,
#       "fat": 8,
#       "sugars": 2
#     },
#     "allergy": ["gluten", "dairy"]
#   },
#   "157": {
#    "name" : "핫도그",
#     "nutrition": {
#       "calories": 300,
#       "carbs": 35,
#       "protein": 12,
#       "fat": 12,
#       "sugars": 3
#     },
#     "allergy": ["gluten", "dairy", "soy"]
#   }
# }