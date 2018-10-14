import unittest
import multimedia_file

class Test_CheckMultimediaFile(unittest.TestCase):
    def test_check_files_in_folders(self):
        valid = multimedia_file.main(["-d", "H:\\02-Photos\\01-Personal\\2018"])
        self.assertFalse(valid)

if __name__ == '__main__':
    unittest.main()