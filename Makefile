TEXS=resume_en.tex resume_es.tex resume_fr.tex
PDFS=$(TEXS:.tex=.pdf)

all: $(PDFS) 

%.pdf: %.tex
	echo Generate PDF for $<
	-pdflatex -synctex=1 -interaction=nonstopmode $< > $<.log
	test -f $@
	rm -f $(<:.tex=.aux) $(<:.tex=.out) $(<:.tex=.synctex.gz) $(<:.tex=.fls) $(<:.tex=.fdb_latexmk) $(<:.tex=.log) $(<:.tex=.tex.log)

clean:
	rm -f $(PDFS) $(TEXS:.tex=.aux) $(TEXS:.tex=.out) $(TEXS:.tex=.synctex.gz) $(TEXS:.tex=.log) $(TEXS:.tex=.tex.log)
