import re
import datetime
import file
import os

def is_year_folder_name(folder_name):
    valid = True
    match = re.match(r"^\d\d\d\d$", folder_name)
    if match:
        valid = True
    else:
        valid = False
    return valid

def is_valid_dated_folder_name(folder_name):
    valid = True
    date = None
    match = re.match(r"^(\d\d\d\d-\d\d-\d\d)[a-z]?\s-\s.*$", folder_name)
    if match:
        valid = True
        try:
            date = datetime.datetime.strptime(match.group(1), r'%Y-%m-%d')
        except:
            valid = False
            date = None
    else:
        valid = False
    return (valid, date)

def has_valid_dated_folder_name(file_path):
    valid = False
    folder_name = ""
    while not valid and not is_year_folder_name(folder_name):
        folder_path = file.get_folder_path(file_path)
        folder_name = file.get_folder_name(file_path)
        (valid, date) = is_valid_dated_folder_name(folder_name)
        file_path = folder_path
    return (valid, date)

def get_date_from_folder_name(file_path):
    (_, date) = has_valid_dated_folder_name(file_path)
    return date

def is_valid_period_dated_folder_name(folder_name):
    valid = True
    beginning_date = None
    end_date = None
    match = re.match(r"^(\d\d\d\d-\d\d-\d\d)[a-z]?_(\d\d\d\d-\d\d-\d\d)[a-z]?\s-\s.*$", folder_name)
    if match:
        valid = True
        try:
            beginning_date = datetime.datetime.strptime(match.group(1), r'%Y-%m-%d')
            end_date = datetime.datetime.strptime(match.group(2), r'%Y-%m-%d') + datetime.timedelta(days=1) - datetime.timedelta(microseconds=1)
        except:
            valid = False
            beginning_date = None
            end_date = None
    else:
        valid = False
    return (valid, beginning_date, end_date)

def has_valid_period_dated_folder_name(file_path):
    valid = False
    folder_name = ""
    while not valid and not is_year_folder_name(folder_name):
        folder_path = file.get_folder_path(file_path)
        folder_name = file.get_folder_name(file_path)
        (valid, date) = is_valid_dated_folder_name(folder_name)
        if valid:
            beginning_date = date
            end_date = date + datetime.timedelta(days=1) - datetime.timedelta(microseconds=1)
        else:
            (valid, beginning_date, end_date) = is_valid_period_dated_folder_name(folder_name)
        file_path = folder_path
    return (valid, beginning_date, end_date)

def validate_file_location(file_date, file_path):
    file_location_valid = False
    (folder_date_valid, folder_date) = has_valid_dated_folder_name(file_path)
    if folder_date_valid:
        if file_date:
            if file_date.year == folder_date.year and file_date.month == folder_date.month and file_date.day == folder_date.day:
                file_location_valid = True
    else:
        (folder_period_date_valid, folder_period_beginning_date, folder_period_end_date) = has_valid_period_dated_folder_name(file_path)
        if folder_period_date_valid:
            if file_date:
                if file_date >= folder_period_beginning_date and file_date <= folder_period_end_date:
                    file_location_valid = True
    return file_location_valid
    
def check_multimedia_file_date(file_path, file_date):
    file_date_valid = True
    file_error_message = None
    if file_date_valid:
        (file_date_valid, file_date, file_error_message) = file.check_file_dates(file_path, "File date", lambda f : file_date, "Folder date", get_date_from_folder_name, 24*60*60)
    if file_date_valid:
        (file_date_valid, file_date, file_error_message) = file.check_file_dates(file_path, "File date", lambda f : file_date, "Creation date", file.get_creation_date)
    if file_date_valid:
        (file_date_valid, file_date, file_error_message) = file.check_file_dates(file_path, "File date", lambda f : file_date, "Modification date", file.get_modification_date)
    return (file_date_valid, file_date, file_error_message)
    
def is_valid_file_name(file_path, file_extension_prefix):
    valid = True
    file_name = file.get_file_name(file_path)
    file_name_no_extension = os.path.splitext(file_name)[0]
    file_extension = file.get_file_extension(file_path)
    prefix = file_extension_prefix[file_extension]
    match = re.match(r"^" + prefix + r"\d\d\d\d\d$", file_name_no_extension)
    if match:
        valid = True
    else:
        valid = False
    return valid
    
def check_multimedia_file_name(file_path, file_extension_prefix):
    valid = False
    error_message = None
    valid = is_valid_file_name(file_path, file_extension_prefix)
    if not valid:
        error_message = "Filename incorrect"
    return (valid, error_message)