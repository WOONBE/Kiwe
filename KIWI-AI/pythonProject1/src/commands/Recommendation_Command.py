# src/commands/Recommendation_Command.py
from src.commands.base_command import BaseCommand

class RecommendationCommand(BaseCommand):
    def __init__(self, infrastructure):
        super().__init__(infrastructure)
        self.retriever = infrastructure.get("retriever")
        self.llama_client = infrastructure.get("llama_client")

    # Properly implement the abstract method
    async def execute(self, data):
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
            menu_info.append(i["menu_desc"]+" ")
            menu_id.append(i["menu_id"])
        menu_response = [*menu_name,*menu_info]
        try:
            if self.llama_client:
                print("llama_client")
                context = f"Menu items: {data}\nUser preferences: {[]}"
                recommendation = await self.llama_client.get_recommendation(context)
                return {"status": "success", "message": f"{menu_id} {recommendation}"}
            return {"status": "client fail", "message": f"{menu_id} {menu_response}"}
        except Exception as e:
            print(f"Error in recommendation command: {str(e)}")
            return {"status": "No server", "message": f"{menu_id} {menu_response}"}