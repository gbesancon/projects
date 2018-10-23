import file

AUDIO_PREFIX = "a"
AUDIO_EXTENSION_PREFIX = {    
    ".mp3" : AUDIO_PREFIX,
    ".wav" : AUDIO_PREFIX,
    ".ogg" : AUDIO_PREFIX,
}

def is_audio_file(file_path):
    file_extension = file.get_file_extension(file_path)
    return file_extension in AUDIO_EXTENSION_PREFIX

def set_audio_file_date(file_path, file_date):
    file.set_file_date(file_path, file_date)