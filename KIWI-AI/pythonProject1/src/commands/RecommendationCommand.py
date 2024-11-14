# from src.commands.base_command import BaseCommand
#
#
# class RecommendationCommand(BaseCommand):
#     def __init__(self, infrastructure):
#         super().__init__(infrastructure)
#         self.retriever = infrastructure.get("retriever")
#         self.llama_client = infrastructure.get("llama_client")
#
#     async def execute(self, data):
#         try:
#             user_preferences = data.get("user_preferences", [])
#
#             # Get menu data from retriever (Elasticsearch)
#             if self.retriever:
#                 menu_data = self.retriever.search(
#                     index="menu_index",
#                     body={
#                         "query": {
#                             "match_all": {}  # Or use specific query based on preferences
#                         }
#                     }
#                 )
#
#             # Get recommendation from LLaMA
#             if self.llama_client:
#                 context = f"Menu items: {menu_data}\nUser preferences: {user_preferences}"
#                 recommendation = await self.llama_client.get_recommendation(context)
#                 return {"status": "success", "recommendation": recommendation}
#
#             return {"status": "error", "message": "Services unavailable"}
#
#         except Exception as e:
#             return {"status": "error", "message": str(e)}