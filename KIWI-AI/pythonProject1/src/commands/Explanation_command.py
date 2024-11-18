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
            print("menu data",i["menu_name"],i["menu_desc"])
            menu_infos.append(i["menu_name"] + "에 대한 설명은 다음과 같습니다. " + i["menu_desc"])
        try:
            menu_info_str = ", ".join(menu_infos)
            if self.llama_client:
                context = f"{menu_infos}"
                recommendation = await self.llama_client.get_recommendation(context)
                return {"status": "success", "message": recommendation}
            return {"status": "success", "message": f"{menu_info_str}" }
        except Exception as e:
            print(f"Error in recommendation command: {str(e)}")
            return {"status": "No server", "message": f"{menu_info_str}"}
