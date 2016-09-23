all: youtube-dl

youtube-dl:
	if [ ! -d $@ ] ; \
	then \
		git clone https://github.com/rg3/youtube-dl.git ; \
	fi
	if [ ! -f $@/youtube-dl ] ; \
	then \
		cd $@ && \
		make ; \
	fi 

clean:
	rm -rf youtube-dl

