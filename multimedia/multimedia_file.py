audio_extension_prefix = {    
    ".mp3" : "a",
    ".wav" : "a",
    ".ogg" : "a",
}

picture_extension_prefix = {    
    ".bmp" : "p",
    ".jpg" : "p",
    ".jpeg": "p",
    ".gif" : "p",
    ".png" : "p",
    ".tif" : "p",
    ".tiff": "p",
    ".orf" : "p",
}

video_extension_prefix = {    
    ".avi" : "v",
    ".mp4" : "v",
    ".mov" : "v",
    ".mpg" : "v",
    ".mpeg": "v",
    ".mts" : "v",
    ".wmv" : "v",
    ".3gp" : "v",
    ".flv" : "v",
    ".ogv" : "a",
}

extension_prefix = {}
for d in [audio_extension_prefix, picture_extension_prefix, video_extension_prefix]:
  extension_prefix.update(d)
  