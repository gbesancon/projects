from __future__ import print_function
import argparse
import sys

import multimedia_file

def rename_files_in_folders(folder_pathes, verbose, dry_run):
    print()

def main(argv):
    parser = argparse.ArgumentParser()
    parser.add_argument('-d', '--directory', nargs='+', help="Directory", required=True)
    parser.add_argument('-v', '--verbose', help="Verbose", default=True, action="store_true")
    parser.add_argument('--dry-run', help="DRY RUN", default=True)
    args = parser.parse_args(argv)
    rename_files_in_folders(args.directory, args.verbose, args.dry_run)

if __name__ == "__main__":
    main(sys.argv[1:])