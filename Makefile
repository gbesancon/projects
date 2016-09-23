TEXS=development_process_cheatsheet.tex feature_design_cheatsheet.tex how_to_win_friends_and_influence_people_cheasheet.tex
PDFS=$(TEXS:.tex=.pdf)
PACKAGES=texmaker texlive-full

all: texmaker $(PDFS) 

texmaker:
	dpkg -s texmaker ; \
 	if [ $$? != 0 ] ; \
	then \
		sudo apt install texmaker ; \
	fi
	touch texmaker

texlive-full:
	dpkg -s texlive-full ; \
 	if [ $$? != 0 ] ; \
	then \
		sudo apt install texlive-full ; \
	fi
	touch texlive-full

%.pdf: texlive-full %.tex
	echo Generate PDF for $(word 2,$^)
	pdflatex -synctex=1 -interaction=nonstopmode $(word 2,$^) > $(word 2,$^).log

clean:
	rm -f $(PDFS) $(TEXS:.tex=.aux) $(TEXS:.tex=.log) $(TEXS:.tex=.out) $(TEXS:.tex=.synctex.gz) $(TEXS:.tex=.tex.log) setup

