# src/commands/base_command.py

from abc import ABC, abstractmethod


class BaseCommand(ABC):
    def __init__(self, infrastructure):
        """
        Initialize with infrastructure components shared across commands.

        :param infrastructure: Dictionary of shared resources like database and LLM model.
        """
        self.infrastructure = infrastructure
        self.database = infrastructure.get("database")
        self.llm_model = infrastructure.get("llm_model")

    @abstractmethod
    def execute(self, data):
        """
        Abstract method for executing a command. Must be implemented by subclasses.

        :param data: Dictionary containing relevant information for executing the command.
        :return: Dictionary with the response for the command.
        """
        pass
