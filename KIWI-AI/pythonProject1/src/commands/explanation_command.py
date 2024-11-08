# src/commands/explanation_command.py

from src.commands.base_command import BaseCommand


class ExplanationCommand(BaseCommand):
    def __init__(self, infrastructure):
        """
        Initialize with infrastructure components, including retriever and generator for RAG.
        """
        super().__init__(infrastructure)
        self.retriever = infrastructure.get("retriever")  # For document retrieval in RAG
        self.generator = infrastructure.get("generator")  # For text generation in RAG

    def execute(self, data):
        """
        Execute the explanation command by retrieving relevant information and generating a response.

        :param data: Dictionary containing details needed for the explanation, like the item name.
        :return: Dictionary with the response message and explanation.
        """
        item_name = data.get("menu_item")

        # Step 1: Retrieve related information based on the item
        if self.retriever and item_name:
            documents = self.retriever.retrieve(item_name)
        else:
            return {"status": "error", "message": "Unable to retrieve information"}

        # Step 2: Generate an explanation based on retrieved documents
        if self.generator and documents:
            explanation = self.generator.generate(documents)
            return {"status": "success", "explanation": explanation}

        # If no documents found or generation fails
        return {"status": "error", "message": "No explanation available for the requested item"}
