# src/service_layer/service_router.py

from src.commands.new_order_command import NewOrderCommand
from src.commands.fix_order_command import FixOrderCommand
from src.commands.recommendation_command import RecommendationCommand
from src.commands.explanation_command import ExplanationCommand
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
        processed_data = self.nlp_processor.process_request(request.sentence)

        request_type = processed_data["request_type"]
        data = processed_data["data"]
        print("request_type, data", request_type, data)

        if request_type == "order":
            command = NewOrderCommand(self.infrastructure)
            ans = await command.execute(data, request)
            print("ans", ans)
            return ans
        elif request_type == "modify_or_delete":
            command = FixOrderCommand(self.infrastructure)
            print("data", data)
            return await command.execute(data)
        elif request_type == "recommendation":
            command = RecommendationCommand(self.infrastructure)
            return await command.execute(data)
        elif request_type == "explanation":
            command = ExplanationCommand(self.infrastructure)
            return await command.execute(data)
        elif request_type == "check_order":
            return {"status": "success", "message": "Order status is confirmed"}
        elif request_type == "next_step":
            return {"status": "success", "message": "Proceeding to the next step"}
        else:
            return {"status": "unknown", "message": "Unknown request type"}
