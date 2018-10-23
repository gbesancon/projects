def print_file_message(file_path, message):
    print(file_path + ": " + message)

def print_file_messages(messages):
    for file_path in messages:
        for message in messages[file_path]:
            print_file_message(file_path, message)

def add_file_messages(messages, messages_to_add):
    for file_path in messages_to_add:
        for message_to_add in messages_to_add[file_path]:
            add_file_message(messages, file_path, message_to_add)

def add_file_message(messages, file_path, message_to_add):
    if not file_path in messages:
        messages[file_path] = []
    if not message_to_add in messages[file_path]:
        messages[file_path] += [message_to_add]
    
def delete_file_message(messages, file_path):
    if file_path in messages:
        del messages[file_path]
