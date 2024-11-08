# src/utils/logger.py

import logging
import yaml


def setup_logger(logging_config):
    """
    Set up logging based on the configuration provided.

    :param logging_config: Dictionary with logging configuration details.
    """
    level = logging_config.get("level", "INFO")
    format = logging_config.get("format", "%(asctime)s - %(name)s - %(levelname)s - %(message)s")
    datefmt = logging_config.get("datefmt", "%Y-%m-%d %H:%M:%S")

    logging.basicConfig(level=getattr(logging, level.upper(), "INFO"),
                        format=format,
                        datefmt=datefmt)
    logger = logging.getLogger(__name__)
    logger.info("Logger initialized with level: %s", level)


def get_logger(name):
    """
    Create and return a logger with the specified name.

    :param name: The name of the logger.
    :return: Logger instance.
    """
    return logging.getLogger(name)
