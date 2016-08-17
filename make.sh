#!/bin/bash

check_tools ()
{
  command -v echo > /dev/null 2>&1 || { echo >&2 "echo is not installed.  Aborting."; exit 1; }
  command -v pdflatex > /dev/null 2>&1 || { echo >&2 "pdflatex is not installed.  Aborting."; exit 1; }
}

generate_pdf ()
{
  echo Generate PDF for $1
  pdflatex -synctex=1 -interaction=nonstopmode $1 > $1.log
}

REFERENCE_FOLDER=`pwd`
check_tools

for FILE in resume_en.tex resume_fr.tex
do 
  generate_pdf $FILE 
done

