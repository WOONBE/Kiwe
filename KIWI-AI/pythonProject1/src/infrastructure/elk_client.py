# src/infrastructure/elk_client.py

from elasticsearch import Elasticsearch

class ELKClient:
    def __init__(self, config):
        """
        Initialize ELKClient with Elasticsearch configuration.

        :param config: Dictionary containing Elasticsearch configuration settings (host, user, password).
        """
        self.config = config
        # Include the scheme in the hosts URL directly
        self.client = Elasticsearch(
            hosts=[f"https://{config['host']}"],
            http_auth=(config["user"], config["password"])
        )

    def retrieve_recommendations(self, preferences):
        """
        Retrieve recommendations from Elasticsearch based on user preferences.

        :param preferences: List of user preferences for personalized recommendations.
        :return: List of recommended items.
        """
        try:
            # Example query based on user preferences
            response = self.client.search(
                index="recommendations",  # Ensure this index exists in Elasticsearch
                body={
                    "query": {
                        "bool": {
                            "must": [
                                {"match": {"tags": " ".join(preferences)}}
                            ]
                        }
                    }
                }
            )

            # Extract and return the list of recommendations
            hits = response["hits"]["hits"]
            recommendations = [hit["_source"]["item_name"] for hit in hits]
            return recommendations
        except Exception as e:
            print(f"Error retrieving recommendations: {e}")
            return []

    def log_interaction(self, user_id, interaction_data):
        """
        Log user interactions in Elasticsearch (optional, for analytics).

        :param user_id: Unique identifier for the user.
        :param interaction_data: Dictionary containing interaction details.
        :return: Boolean indicating success or failure of logging.
        """
        try:
            self.client.index(
                index="user_interactions",
                body={
                    "user_id": user_id,
                    "interaction": interaction_data,
                    "timestamp": "now"
                }
            )
            return True
        except Exception as e:
            print(f"Error logging interaction: {e}")
            return False
