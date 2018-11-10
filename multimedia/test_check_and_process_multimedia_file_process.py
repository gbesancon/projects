import unittest
import check_and_process_multimedia_file

class Test_CheckAndProcessMultimediaFile(unittest.TestCase):
    TEST_FOLDER_PATH = "D:\\gbesancon\\00-temp\\Photos"

    def test_main_no_args_process(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH, "-p"])
        self.assertTrue(valid)

    def test_main_set_dates_process(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH, "--set-dates", "-p"])
        self.assertTrue(valid)

    def test_main_set_dates_use_folder_date_process(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH, "--set-dates", "--use-folder-date", "-p"])
        self.assertTrue(valid)

    def test_main_set_dates_rename_files_process(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH, "--set-dates", "--rename-files", "-p"])
        self.assertTrue(valid)

    def test_main_set_dates_use_folder_date_rename_files_process(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH, "--set-dates", "--use-folder-date", "--rename-files", "-p"])
        self.assertTrue(valid)

    def test_main_set_dates_move_files_rename_files_process(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH, "--set-dates", "--move-files", "--rename-files", "-p"])
        self.assertTrue(valid)

    def test_main_set_dates_use_folder_date_move_files_rename_files_process(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH, "--set-dates", "--use-folder-date", "--move-files", "--rename-files", "-p"])
        self.assertFalse(valid)

if __name__ == '__main__':
    unittest.main()