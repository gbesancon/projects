from typing import Dict, List

def print_file_messages(messages: Dict[str, Dict[str, List[str]]]):
    for folder_path in messages:
        print(folder_path + ":")
        for file_name in messages[folder_path]:
            print(" - " + file_name + ": " + ', '.join(message for message in messages[folder_path][file_name]))

def add_file_message(messages: Dict[str, Dict[str, List[str]]], folder_path: str, file_name: str, message_to_add: str):
    if not folder_path in messages:
        messages[folder_path] = {}
    if not file_name in messages[folder_path]:
        messages[folder_path][file_name] = []
    if not message_to_add in messages[folder_path][file_name]:
        messages[folder_path][file_name].append(message_to_add)

