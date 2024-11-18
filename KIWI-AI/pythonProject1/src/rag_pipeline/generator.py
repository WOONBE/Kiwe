from typing import List
from transformers import GPT2LMHeadModel, GPT2Tokenizer


class Generator:
    def __init__(self):
        """
        Initializes the generator with a pre-trained language model.
        Here we use GPT-2 as an example, but this can be any suitable LLM.
        """
        self.tokenizer = GPT2Tokenizer.from_pretrained("gpt2")
        self.model = GPT2LMHeadModel.from_pretrained("gpt2")

    def generate_response(self, query: str, context: List[str]) -> str:
        """
        Generates a response based on the query and the context (retrieved documents).
        """
        # Combine the query with the retrieved context to provide the model with enough information
        context_str = "\n".join(context)
        input_text = f"Query: {query}\nContext:\n{context_str}\nResponse:"

        # Tokenize the input text and generate a response using GPT-2 (or any other LLM)
        inputs = self.tokenizer.encode(input_text, return_tensors="pt")
        outputs = self.model.generate(inputs, max_length=150, num_return_sequences=1)

        # Decode the generated response
        response = self.tokenizer.decode(outputs[0], skip_special_tokens=True)
        return response


# Example usage:
generator = Generator()
query = "What are the nutritional facts of 디카페인 카페모카?"
context = ["디카페인 카페모카: 210 calories, 30g carbs, 6g protein, 9g fat, 25g sugars"]
response = generator.generate_response(query, context)
print(response)
