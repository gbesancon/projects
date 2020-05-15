from typing import List
import multimedia_file

class AudioFile(multimedia_file.MultimediaFile):
    PREFIX: str = "AUD"
    EXTENSIONS: List[str] = [    
        ".mp3",
        ".ogg",
        ".wav"
    ]

    def __init__(self, file_path: str):
        super().__init__(file_path)

    def get_prefix(self) -> str:
        return AudioFile.PREFIX