import unittest
import check_and_process_multimedia_file

class Test_CheckAndProcessMultimediaFile(unittest.TestCase):
    TEST_FOLDER_PATH = "D:\\gbesancon\\00-temp\\2007"

    def test_main(self):
        valid = check_and_process_multimedia_file.main(["-d", self.TEST_FOLDER_PATH])
        self.assertFalse(valid)

    def test_check_and_process_files_in_folders(self):
        (valid, _, _) = check_and_process_multimedia_file.check_and_process_files_in_folders(folder_pathes=[self.TEST_FOLDER_PATH], set_dates=True, use_folder_date=True, move_files=True, rename_files=True, process=False, verbose=False)
        self.assertFalse(valid)

if __name__ == '__main__':
    unittest.main()