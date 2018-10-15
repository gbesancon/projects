import unittest
import multimedia_file

class Test_CheckMultimediaFile(unittest.TestCase):
    def test_check_and_process_files_in_folders(self):
        (valid, _) = multimedia_file.check_and_process_files_in_folders(["H:\\02-Photos\\01-Personal\\2014"], False, False)
        self.assertFalse(valid)

    def test_get_date_from_file_or_folder(self):
        date = multimedia_file.get_date_from_file_or_folder("H:\\02-Photos\\01-Personal\\2018\\2018-05-12 - Afternoon Tea\\Sarah Connors\\IMG_20181005_105030.jpg")
        date = multimedia_file.get_date_from_file_or_folder("H:\\02-Photos\\01-Personal\\2018\\2018-05-12 - Afternoon Tea\\Sarah Connors\\p00002.jpg")
        date = multimedia_file.get_date_from_file_or_folder("H:\\02-Photos\\01-Personal\\2018\\2018-05-12b - Afternoon Tea\\Eleftheria Filippaiou\\p00002.jpg")
        date = multimedia_file.get_date_from_file_or_folder("H:\\02-Photos\\01-Personal\\2018\\Eleftheria Filippaiou\\p00002.jpg")
        self.assertIsNone(date)

if __name__ == '__main__':
    unittest.main()