# src/commands/new_order_command.py

from src.api_layer.models.order_item import OrderRequest

# this code needs change to get data from DB

class NewOrderCommand:
    def __init__(self, infrastructure):
        self.database = infrastructure['database']

    def execute(self, order_data, request: OrderRequest):
        """Process a new order and verify required options."""
        # order_id = order_data.get("order_id")
        # menu_item = order_data.get("menuTitle")
        # options = order_data.get("options", {})
        print("order_data",order_data)



        items = order_data.get("items")
        first_item = items[0]
        menu_item = first_item.get("menuTitle")
        options = first_item.get("options", {})
        quantity = first_item.get("count")

        if request.order_items[0].menuTitle=="string":
            request.order_items[0] =  {
                "menuTitle": menu_item,
                "count": quantity,
                "option": options
            }


        return {
            "status": "success",
            "message": "Order placed successfully",
            "menuTitle": menu_item,
            "count": quantity,
            "option": options
        }

        # Verify required options for the menu item
        # required_options = self.database.get_required_options(menu_item)

        # missing_options = [opt for opt in required_options if opt not in options]


        # if "hot" in options or "small" in options:
        #     missing_options = "사이즈"
        # else:
        #     missing_options = "온도"
        #
        # if missing_options:
        #     return {
        #         "status": "incomplete",
        #         "message": f"Missing required options: {', '.join(missing_options)}",
        #         # "required_options": required_options
        #     }

        # Store the order in the database
        # success = self.database.create_order(order_id, menu_item, options)
        # if success:
        #     return {"status": "success", "message": "Order placed successfully", "menu_item":menu_item, "options":options}
        # else:
        #     return {"status": "error", "message": "Failed to place the order"}
