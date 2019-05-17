import sys
import os

def main(argv):
    for i in range(1,int(argv[0])+1):
        problem_folderpath = os.path.join('problems',str(i))
        if not os.path.exists(problem_folderpath):
            os.makedirs(problem_folderpath)
        readme_md_filepath = os.path.join(problem_folderpath,"README.md")
        if not os.path.exists(readme_md_filepath):
            readme_txt_filepath = os.path.join(problem_folderpath,"README.txt")
            if os.path.exists(readme_txt_filepath):
                os.rename(readme_txt_filepath, readme_md_filepath)
            else:
                with open(readme_md_filepath, "w") as readme_file: 
                    readme_file.write("Daily Coding Problem: Problem #" + str(i) + "\n")
        problem_filepath = os.path.join(problem_folderpath,"problem_" + str(i) + ".py")
        if not os.path.exists(problem_filepath):
            with open(problem_filepath, "w") as problem_file: 
                problem_file.write("import sys\n\n")
                problem_file.write("def main(argv):\n\tpass\n\n")
                problem_file.write("if __name__ == \"__main__\":\n\tmain(sys.argv[1:])\n")

if __name__ == "__main__":
    main(sys.argv[1:])