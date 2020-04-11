all: youtube-dl

youtube-dl:
	if [ ! -d $@ ] ; \
	then \
		git clone https://github.com/rg3/youtube-dl.git ; \
	fi
	if [ ! -f $@/youtube-dl ] ; \
	then \
		sudo apt-get install -y pandoc mplayer vlc && \
		cd $@ && \
		make ; \
	fi 

sudo apt-get install 

clean:
	rm -rf youtube-dl

