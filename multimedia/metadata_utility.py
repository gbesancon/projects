import datetime

CREATION_DATE = "creation_date"
DATE_TIME_ORIGINAL = "date_time_original"
DATE_TIME_DIGITIZED = "date_time_digitized"

def has_metadata(file_path):
    return get_metadata(file_path) is not None

def get_metadata(file_path):
    metadata = None
    try:
        from hachoir.parser import createParser
        parser = createParser(file_path)
        if parser:
            from hachoir.metadata import extractMetadata
            metadata = extractMetadata(parser)
    except:
        pass
    return metadata

def has_metadata_key(file_path, key):
    return len(get_metadata_values(file_path, key)) > 0

def get_metadata_values(file_path, key):
    values = []
    metadata = get_metadata(file_path)
    if metadata:
        if key in metadata._Metadata__data:
            metadata_values = metadata._Metadata__data[key].values
            if len(metadata_values) > 0:
                for metadata_value in metadata_values:
                    values.append(metadata_value.value)
    return values

def get_metadata_value(file_path, key, index=0):
    value = None
    values = get_metadata_values(file_path, key)
    if len(values) > index:
        value = values[index]
    return value

def set_metadata_value(file_path, key, value):
    try:
        from hachoir.parser import createParser
        parser = createParser(file_path)
        if parser:
            from hachoir.editor import createEditor
            editor = createEditor(parser)
    except:
        pass
    