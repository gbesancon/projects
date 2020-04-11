import unittest
import check_and_process_multimedia_file

class Test_CheckAndProcessMultimediaFile(unittest.TestCase):
    TEST_FOLDER_PATH = "D:\\gbesancon\\00-temp\\Photos"

    def test_main_no_args(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH])
        self.assertTrue(valid)

    def test_main_set_dates(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH, "--set-dates"])
        self.assertTrue(valid)

    def test_main_set_dates_use_folder_date(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH, "--set-dates", "--use-folder-date"])
        self.assertTrue(valid)

    def test_main_set_dates_rename_files(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH, "--set-dates", "--rename-files"])
        self.assertTrue(valid)

    def test_main_set_dates_use_folder_date_rename_files(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH, "--set-dates", "--use-folder-date", "--rename-files"])
        self.assertTrue(valid)

    def test_main_set_dates_move_files_rename_files(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH, "--set-dates", "--move-files", "--rename-files"])
        self.assertTrue(valid)

    def test_main_set_dates_use_folder_date_move_files_rename_files(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH, "--set-dates", "--use-folder-date", "--move-files", "--rename-files"])
        self.assertTrue(valid)

if __name__ == '__main__':
    unittest.main()