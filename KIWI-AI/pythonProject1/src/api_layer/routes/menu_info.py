from fastapi import APIRouter
import json
from pathlib import Path

router = APIRouter()

# Define the path to the 'data' folder relative to 'menu_info.py'
data_folder_path = Path(__file__).parent.parent.parent / "data"  # Going up 3 levels to 'project-root'

# Set the paths for your JSON files
nutrition_data_path = data_folder_path / "nutrition_facts.json"
allergy_data_path = data_folder_path / "allergy_levels.json"

# Read the JSON files
with open(nutrition_data_path, "r", encoding="utf-8") as file:
    nutrition_data = json.load(file)

with open(allergy_data_path, "r", encoding="utf-8") as file:
    allergy_data = json.load(file)

# Example of fetching nutrition facts for a menu item
@router.get("/menu/{item_name}/nutrition")
async def get_nutrition(item_name: str):
    nutrition = nutrition_data.get(item_name)
    if not nutrition:
        return {"message": f"Nutrition facts for {item_name} not found."}
    return nutrition

# Example of fetching allergy information for a menu item
@router.get("/menu/{item_name}/allergy")
async def get_allergy(item_name: str):
    allergy = allergy_data.get(item_name)
    if not allergy:
        return {"message": f"Allergy information for {item_name} not found."}
    return allergy
