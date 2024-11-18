from typing import List, Dict

class Retriever:
    def __init__(self, data: Dict):
        """
        Initializes the retriever with the given data.
        `data` could be the menu combinations (from a database or loaded as JSON).
        """
        self.data = data

    def retrieve(self, query: str) -> List[str]:
        """
        Retrieves relevant menu items or combinations based on the user's query.
        This could be as simple as keyword matching or more advanced techniques (e.g., fuzzy search).
        """
        relevant_items = []

        # Simple keyword matching (can be replaced with more sophisticated techniques)
        for item, info in self.data.items():
            if query.lower() in info['menu_name'].lower():  # Search in 'menu_name' or any other field
                relevant_items.append(item)

        return relevant_items
