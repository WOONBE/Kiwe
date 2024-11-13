# src/service_layer/service_router.py

from src.commands.new_order_command import NewOrderCommand
from src.commands.fix_order_command import FixOrderCommand
from src.commands.recommendation_command import RecommendationCommand
from src.commands.explanation_command import ExplanationCommand
from src.commands.option_order_command import OptionOrderCommand

from src.utils.nlp_processor import NLPProcessor
from src.api_layer.models.order_item import OrderRequest


class ServiceRouter:
    def __init__(self, infrastructure, db):
        self.infrastructure = infrastructure
        self.nlp_processor = NLPProcessor(db)

    def route_request(self, request: OrderRequest):
        """
        Route the request based on NLP-detected intent.
        """
        processed_data = self.nlp_processor.process_request(request)

        request_type = processed_data["request_type"]
        data = processed_data["data"]

        if request.need_temp == 0:
            print("request.need_temp", request.need_temp)
            command = OptionOrderCommand(self.infrastructure)
            ans = command.execute(data, request)
            print("option ans", ans)
            return ans
        elif request_type == "order":
            command = NewOrderCommand(self.infrastructure)
            ans = command.execute(data, request)
            print("ans", ans)
            return ans
        elif request_type == "modify_or_delete":
            command = FixOrderCommand(self.infrastructure)
            return command.execute(data)
        elif request_type == "recommendation":
            command = RecommendationCommand(self.infrastructure)
            return command.execute(data)
        elif request_type == "explanation":
            command = ExplanationCommand(self.infrastructure)
            return command.execute(data)
        elif request_type == "check_order":
            return {"status": "success", "message": "Order status is confirmed"}
        elif request_type == "next_step":
            return {"status": "success", "message": "Proceeding to the next step"}
        else:
            return {"status": "unknown", "message": "Unknown request type"}
