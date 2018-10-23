import unittest
import check_and_process_multimedia_file

class Test_CheckAndProcessMultimediaFile(unittest.TestCase):
    def test_check_and_process_files_in_folders(self):
        (valid, _, _) = check_and_process_multimedia_file.check_and_process_files_in_folders(folder_pathes=["D:\\gbesancon\\00-temp\\2014"], process=False, verbose=False)
        self.assertFalse(valid)

if __name__ == '__main__':
    unittest.main()