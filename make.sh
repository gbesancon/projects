#!/bin/sh

for FILE in resume_en.tex resume_fr.tex
do 
  pdflatex -synctex=1 -interaction=nonstopmode $FILE 
done

