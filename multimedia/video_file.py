import file

VIDEO_PREFIX = "v"
VIDEO_EXTENSION_PREFIX = {    
    ".avi" : VIDEO_PREFIX,
    ".mp4" : VIDEO_PREFIX,
    ".mov" : VIDEO_PREFIX,
    ".mpg" : VIDEO_PREFIX,
    ".mpeg": VIDEO_PREFIX,
    ".mts" : VIDEO_PREFIX,
    ".wmv" : VIDEO_PREFIX,
    ".3gp" : VIDEO_PREFIX,
    ".flv" : VIDEO_PREFIX,
    ".ogv" : VIDEO_PREFIX,
}

def is_video_file(file_path):
    file_extension = file.get_file_extension(file_path)
    return file_extension in VIDEO_EXTENSION_PREFIX

def set_video_file_date(file_path, file_date):
    file.set_file_date(file_path, file_date)