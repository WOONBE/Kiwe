# src/commands/fix_order_command.py

from src.commands.base_command import BaseCommand


class FixOrderCommand(BaseCommand):
    def __init__(self, infrastructure):
        """
        Initialize with infrastructure components, like database access.
        """
        super().__init__(infrastructure)
        self.database = infrastructure.get("database")

    def execute(self, data):
        """
        Execute the fix command by modifying or deleting an existing order.

        :param data: Dictionary containing details like action_type (modify/delete), cart_item, and updates.
        :return: Response message indicating success or failure.
        """
        action_type = data.get("action_type")
        cart_item = data.get("cart_item")
        updates = data.get("updates")

        # Handle delete action
        if action_type == "delete":
            success = self.database.delete_order_item(cart_item)
            if success:
                return {"status": "success", "message": f"Item {cart_item} deleted successfully"}
            else:
                return {"status": "error", "message": f"Failed to delete item {cart_item}"}

        # Handle modify action
        elif action_type == "modify" and updates:
            success = self.database.update_order_item(cart_item, updates)
            if success:
                return {"status": "success", "message": f"Item {cart_item} updated successfully"}
            else:
                return {"status": "error", "message": f"Failed to update item {cart_item}"}

        # If action type is unknown
        return {"status": "error", "message": "Invalid action type or missing data"}
