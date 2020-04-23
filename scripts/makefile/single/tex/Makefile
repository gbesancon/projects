include ../scripts/makefile/single/Makefile

PDFS=$(TEXS:.tex=.pdf)

clean:
	rm -f $(PDFS) $(TEXS:.tex=.aux) $(TEXS:.tex=.log) $(TEXS:.tex=.out) $(TEXS:.tex=.synctex.gz) $(TEXS:.tex=.tex.log)

build: $(PDFS) 

%.pdf: %.tex
	echo Generate PDF for $<
	pdflatex -synctex=1 -interaction=nonstopmode $< > $<.log
	rm -f $(<:.tex=.aux) $(<:.tex=.out) $(<:.tex=.synctex.gz) $(<:.tex=.log) $(<:.tex=.tex.log)

build-dev: build
