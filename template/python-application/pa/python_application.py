"""
Python application
"""
import argparse

class PythonApplication:
    """
    Python application
    """

    def __init__(self):
        """
        Initialize
        """
        print("Python application")

    # pylint: disable=R0201
    def __parse_args(self, args):
        """
        Parse CLI argument
        """
        parser = argparse.ArgumentParser(
            description='Python application.')
        parser.add_argument('-i', '--input', help="Input")
        parser.add_argument('-o', '--output', help="Output")
        return parser.parse_args(args)


    def __execute(self, parsed_args):
        """
        Execute
        """
        self.execute(parsed_args.input, parsed_args.output)

    def execute(self, _input, _output):
        """
        Execute
        """

    def main(self, args):
        """
        Main method of the application
        """
        parsed_args = self.__parse_args(args)
        self.__execute(parsed_args)
