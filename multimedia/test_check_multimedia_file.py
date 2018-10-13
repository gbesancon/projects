import unittest
import check_multimedia_file

class Test_CheckMultimediaFile(unittest.TestCase):
    def test_check_files_in_folders(self):
        valid = check_multimedia_file.main(["-d", "H:\\02-Photos\\01-Personal\\2018\\2018-10-04b - La Villa Saint Tropez", "-v"])
        self.assertTrue(valid, False)

if __name__ == '__main__':
    unittest.main()