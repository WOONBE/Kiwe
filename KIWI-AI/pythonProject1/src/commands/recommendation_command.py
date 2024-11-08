# src/commands/recommendation_command.py

from src.commands.base_command import BaseCommand


class RecommendationCommand(BaseCommand):
    def __init__(self, infrastructure):
        """
        Initialize with infrastructure components like retriever and generator for recommendations.
        """
        super().__init__(infrastructure)
        self.retriever = infrastructure.get("retriever")  # Used for retrieving relevant recommendations
        self.generator = infrastructure.get("generator")  # Used for generating recommendation text

    def execute(self, data):
        """
        Execute the recommendation command by retrieving and generating recommendations.

        :param data: Dictionary containing user preferences or context for recommendations.
        :return: Dictionary with the recommendation response.
        """
        user_preferences = data.get("user_preferences", [])

        # Step 1: Retrieve recommendations based on user preferences or history
        if self.retriever:
            recommendations = self.retriever.retrieve_recommendations(user_preferences)
        else:
            return {"status": "error", "message": "Unable to retrieve recommendations"}

        # Step 2: Generate a recommendation response based on retrieved data
        if self.generator and recommendations:
            recommendation_text = self.generator.generate(recommendations)
            return {"status": "success", "recommendation": recommendation_text}

        # If no recommendations are found
        return {"status": "error", "message": "No recommendations available"}
