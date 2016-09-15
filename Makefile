TEXS=resume_en.tex resume_fr.tex resume_es.tex
PDFS=$(TEXS:.tex=.pdf)

all: $(PDFS)

setup:
	sudo apt install texmaker texlive-full

%.pdf: %.tex
	echo Generate PDF for $<
	pdflatex -synctex=1 -interaction=nonstopmode $< > $<.log

clean:
	rm -f $(PDFS) $(TEXS:.tex=.aux) $(TEXS:.tex=.log) $(TEXS:.tex=.out) $(TEXS:.tex=.synctex.gz) $(TEXS:.tex=.tex.log)

