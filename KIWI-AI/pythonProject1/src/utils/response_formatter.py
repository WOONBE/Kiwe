# src/utils/response_formatter.py

def format_success_response(data, message="Request successful"):
    """
    Format a standardized success response.

    :param data: The main data content for the response.
    :param message: A message describing the success.
    :return: Dictionary formatted as a successful response.
    """
    return {
        "status": "success",
        "message": message,
        "data": data
    }


def format_error_response(error_message, error_code=500):
    """
    Format a standardized error response.

    :param error_message: The error message describing the issue.
    :param error_code: The HTTP error code for the response.
    :return: Dictionary formatted as an error response.
    """
    return {
        "status": "error",
        "error_code": error_code,
        "message": error_message
    }