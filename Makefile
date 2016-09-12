all: setup build

setup:
	if [ ! -d youtube-dl ] ; \
	then \
		git clone https://github.com/rg3/youtube-dl.git ; \
	fi
	if [ ! -f youtube-dl/youtube-dl ] ; \
	then \
		cd youtube-dl && \
		make ; \
	fi 

build:

clean:
	rm -rf youtube-dl

