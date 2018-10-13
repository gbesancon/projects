import unittest
import rename_multimedia_file

class Test_RenameMultimediaFile(unittest.TestCase):
    def test_rename_files_in_folders(self):
        valid = rename_multimedia_file.main(["-d", "H:\\02-Photos\\01-Personal\\2018\\2018-10-04b - La Villa Saint Tropez", "-v"])
        self.assertTrue(valid, True)

if __name__ == '__main__':
    unittest.main()