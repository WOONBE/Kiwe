# src/commands/Recommendation_Command.py
from src.commands.base_command import BaseCommand

class RecommendationCommand(BaseCommand):
    def __init__(self, infrastructure):
        super().__init__(infrastructure)
        self.retriever = infrastructure.get("retriever")
        self.llama_client = infrastructure.get("llama_client")

    # Properly implement the abstract method
    async def execute(self, data,request):
        """
        Execute the recommendation command by retrieving and generating recommendations.

        :param data: Dictionary containing user preferences or context for recommendations.
        :return: Dictionary with the recommendation response.
        """
        # print("data",data)
        # print("data length",len(data),data)

        # print("data", data)

        # menu_response = []
        menu_id = []
        menu_name = []
        menu_info = []
        for i in data:
            # print("menu data",i["menu_name"],i["menu_desc"],i["hot_or_ice"],type(i["hot_or_ice"]))
            menu_name.append(i["menu_name"]+" ")
            # menu_info.append(i["menu_desc"]+" ")
            menu_id.append(i["menu_id"])

        menu_list_str = ", ".join(menu_name)  # 메뉴 리스트를 쉼표로 연결된 문자열로 변환

        # menu_response = [*menu_name,*menu_info]
        # try:
        #     print("self.llama_client",self.llama_client)
        #     if self.llama_client:
        #         print("llama_client")
        #         context = f"이 메뉴들을 {request.age}대 연령이 이해하기 쉽게 추천해줘 {data} 꼭 메뉴 설명만 작성해야해 \n"
        #         recommendation = await self.llama_client.get_recommendation(context)
        #         return {"status": "success", "message": f"{menu_id} {recommendation}"}
        return {"status": "client fail", "message": f"{menu_id} 오늘의 추천 메뉴는 {menu_list_str} 입니다."}
        # except Exception as e:
        #     print(f"Error in recommendation command: {str(e)}")
        #     return {"status": "No server", "message": f"{menu_id} {menu_response}"}