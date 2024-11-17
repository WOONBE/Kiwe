import os
from dotenv import load_dotenv
import mysql.connector
from mysql.connector import Error

# Load environment variables from .env file
load_dotenv()

class Database:
    def __init__(self):
        """
        Initialize the database connection using environment variables.
        """
        self.config = {
            "host": os.getenv("MYSQL_HOST"),
            "user": os.getenv("MYSQL_USER"),
            "password": os.getenv("MYSQL_PASSWORD"),
            "database": os.getenv("MYSQL_DB_NAME"),
            "port": os.getenv("MYSQL_PORT", 3306)  # Default to 3306 if MYSQL_PORT is not set
        }
        self.connection = None

    def connect(self):
        """Connect to the database."""
        try:
            self.connection = mysql.connector.connect(
                host=self.config["host"],
                user=self.config["user"],
                password=self.config["password"],
                database=self.config["database"],
                port=self.config["port"]
            )

            # Test the connection by running a simple query (e.g., SELECT 1)
            cursor = self.connection.cursor()
            cursor.execute("SELECT 1")
            result = cursor.fetchone()
            if result:
                print("Database connection completed successfully!")
            cursor.close()

        except Error as e:
            print(f"Error connecting to database: {e}")
            self.connection = None

    def close(self):
        """Close the database connection."""
        if self.connection:
            self.connection.close()

    def get_unique_menu_categories(self):
        """Fetch unique menu categories from the database."""
        if not self.connection:
            self.connect()

        cursor = self.connection.cursor(dictionary=True)
        try:
            cursor.execute("SELECT DISTINCT menu_category FROM menu")
            categories = cursor.fetchall()
            return categories
        except Error as e:
            print(f"Error fetching menu categories: {e}")
            return None
        finally:
            cursor.close()

    def get_unique_menu_descriptions(self):
        """Fetch unique menu descriptions from the database."""
        if not self.connection:
            self.connect()

        cursor = self.connection.cursor(dictionary=True)
        try:
            cursor.execute("SELECT DISTINCT menu_desc FROM menu")
            descriptions = cursor.fetchall()
            return descriptions
        except Error as e:
            print(f"Error fetching menu descriptions: {e}")
            return None
        finally:
            cursor.close()

    def get_unique_menu_names(self):
        """Fetch unique menu names from the database."""
        if not self.connection:
            self.connect()

        cursor = self.connection.cursor(dictionary=True)
        try:
            cursor.execute("SELECT DISTINCT menu_name FROM menu")
            menu_names = cursor.fetchall()
            return menu_names
        except Error as e:
            print(f"Error fetching menu names: {e}")
            return None
        finally:
            cursor.close()

    def get_unique_menu_combinations(self):
        """Fetch unique combinations of menu category, name, and description."""
        if not self.connection:
            self.connect()

        cursor = self.connection.cursor(dictionary=True)
        try:
            cursor.execute("""
                SELECT menu_id, menu_category, menu_name, menu_desc, hot_or_ice
                FROM menu
            """)
            combinations = cursor.fetchall()
            return combinations
        except Error as e:
            print(f"Error fetching unique menu combinations: {e}")
            return None
        finally:
            cursor.close()

    def get_suggest_age_order_menu(self, age):
        """Fetch unique combinations of menu category, name, and description based on age (rounded to the nearest 10)."""

        # print("hello?")
        if not self.connection:
            # print("connection error")
            self.connect()

        # Round age to the nearest multiple of 10
        age = round(age / 10) * 10

        cursor = self.connection.cursor(dictionary=True)
        try:
            query = """
                SELECT m.*
                FROM order_menu om
                JOIN orders o ON o.id = om.order_id
                JOIN menu m ON om.menu_id = m.menu_id
                WHERE o.age = %s
                ORDER BY om.quantity DESC
                LIMIT 3;
            """

            # Execute the query with the rounded age parameter
            cursor.execute(query, (age,))
            combinations = cursor.fetchall()
            return combinations
        except Error as e:
            print(f"Error fetching unique menu combinations: {e}")
            return None
        finally:
            cursor.close()

    def get_suggest_age_temp_order_menu(self, age, temperature):
        """Fetch unique combinations of menu category, name, and description based on age (rounded to the nearest 10) and temperature."""
        if not self.connection:
            self.connect()

        # Round age to the nearest multiple of 10
        age = round(age / 10) * 10
        cursor = self.connection.cursor(dictionary=True)
        try:
            query = """
                SELECT m.* 
                FROM order_menu om
                JOIN orders o ON o.id = om.order_id
                JOIN menu m ON om.menu_id = m.menu_id
                WHERE o.age = %s AND m.hot_or_ice = %s
                ORDER BY om.quantity DESC
                LIMIT 3;
            """

            # Execute the query with the rounded age and temperature parameters
            cursor.execute(query, (age, temperature))
            combinations = cursor.fetchall()
            return combinations
        except Error as e:
            print(f"Error fetching unique menu combinations: {e}")
            return None
        finally:
            cursor.close()

    def get_unique_menu_names_descs(self):
        """Fetch unique menu names from the database."""
        if not self.connection:
            self.connect()

        cursor = self.connection.cursor(dictionary=True)
        try:
            cursor.execute("SELECT DISTINCT menu_name, menu_desc FROM menu")
            menu_names = cursor.fetchall()
            return menu_names
        except Error as e:
            print(f"Error fetching menu names: {e}")
            return None
        finally:
            cursor.close()