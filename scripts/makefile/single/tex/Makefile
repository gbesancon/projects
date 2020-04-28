include ../scripts/makefile/single/Makefile

PDFS=$(TEXS:.tex=.pdf)

clean:
	rm -f $(PDFS) $(TEXS:.tex=.aux) $(TEXS:.tex=.log) $(TEXS:.tex=.out) $(TEXS:.tex=.synctex.gz) $(TEXS:.tex=.tex.log)

build-dev: build

build: $(PDFS) 

%.pdf: %.tex
	echo Generate PDF for $<
	# https://github.com/thomasWeise/docker-texlive
	-sudo docker run --rm -v `pwd`:/doc/ thomasweise/texlive pdflatex -synctex=1 -interaction=nonstopmode $< > $<.log
	if [ ! -f $@ ] ; then \
		exit 1 ; \
	fi
	#sudo docker run --rm -v `pwd`:/doc/ thomasweise/texlive /bin/pdflatex.sh $< > $<.log
	rm -f $(<:.tex=.aux) $(<:.tex=.out) $(<:.tex=.synctex.gz) $(<:.tex=.log) $(<:.tex=.tex.log)

ci-build-dev: clean build-dev

ci-build: clean build
