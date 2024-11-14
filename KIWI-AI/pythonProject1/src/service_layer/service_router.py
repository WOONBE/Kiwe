# src/service_layer/service_router.py

from src.commands.new_order_command import NewOrderCommand
from src.commands.fix_order_command import FixOrderCommand
from src.commands.Recommendation_Command import RecommendationCommand
from src.commands.Explanation_command import ExplanationCommand
# from src.commands.option_order_command import OptionOrderCommand

from src.utils.nlp_processor import NLPProcessor
from src.api_layer.models.order_item import OrderRequest


class ServiceRouter:
    def __init__(self, infrastructure, db):
        self.infrastructure = infrastructure
        self.nlp_processor = NLPProcessor(db)

    async def route_request(self, request):  # Fixed indentation here
        """
        Route the request based on NLP-detected intent.
        """
        processed_data = self.nlp_processor.process_request(request)

        request_type = processed_data["request_type"]
        data = processed_data["data"]

        if request_type == "order":
            command = NewOrderCommand(self.infrastructure)
            ans = await command.execute(data, request)
            return ans
        elif request_type == "recommendation":
            command = RecommendationCommand(self.infrastructure)
            return await command.execute(data)
        elif request_type == "explanation":
            command = ExplanationCommand(self.infrastructure)
            return await command.execute(data)
        elif request_type == "modify_or_delete":
            command = FixOrderCommand(self.infrastructure)
            return await command.execute(data)
        else:
            return {"status": "unknown", "message": "Unknown request type"}
