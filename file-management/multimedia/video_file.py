from typing import List
import multimedia_file

class VideoFile(multimedia_file.MultimediaFile):
    PREFIX:str = "VID"
    EXTENSIONS: List[str] = [ 
        ".3gp",
        ".avi",
        ".flv",
        ".mov",
        ".mp4",
        ".mpeg",
        ".mpg",
        ".mts",
        ".ogv",
        ".wmv"
    ]

    def __init__(self, file_path: str):
        super().__init__(file_path)
    
    def get_prefix(self) -> str:
        return VideoFile.PREFIX