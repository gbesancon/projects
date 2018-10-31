def print_file_messages(messages):
    for folder_path in messages:
        print(folder_path + ":")
        for file_name in messages[folder_path]:
            print(" - " + file_name + ": " + ', '.join(message for message in messages[folder_path][file_name]))

def add_file_messages(messages, messages_to_add):
    for folder_path in messages_to_add:
        for file_name in messages_to_add[folder_path]:
            for message_to_add in messages_to_add[folder_path][file_name]:
                add_file_message(messages, folder_path, file_name, message_to_add)

def add_file_message(messages, folder_path, file_name, message_to_add):
    if not folder_path in messages:
        messages[folder_path] = {}
    if not file_name in messages[folder_path]:
        messages[folder_path][file_name] = []
    if not message_to_add in messages[folder_path][file_name]:
        messages[folder_path][file_name].append(message_to_add)

