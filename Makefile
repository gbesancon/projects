TEXS=development_process_cheatsheet.tex feature_design_cheatsheet.tex how_to_win_friends_and_influence_people_cheasheet.tex
PDFS=$(TEXS:.tex=.pdf)
PACKAGES=texmaker texlive-full

all: texmaker $(PDFS) 

texmaker:
	dpkg -s $@ ; \
 	if [ $$? != 0 ] ; \
	then \
		sudo apt install $@ ; \
	fi
	touch $@

texlive-full:
	dpkg -s $@ ; \
 	if [ $$? != 0 ] ; \
	then \
		sudo apt install $@ ; \
	fi
	touch $@

%.pdf: texlive-full %.tex
	echo Generate PDF for $(word 2,$^)
	pdflatex -synctex=1 -interaction=nonstopmode $(word 2,$^) > $(word 2,$^).log

clean:
	rm -f $(PDFS) $(TEXS:.tex=.aux) $(TEXS:.tex=.log) $(TEXS:.tex=.out) $(TEXS:.tex=.synctex.gz) $(TEXS:.tex=.tex.log) setup

