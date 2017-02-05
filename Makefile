DOCKER_IMAGE=gbesancon/fortune-cowsay-lolcat-server

all: build

docker.io:
	dpkg -s $@ ; \
	if [ $$? != 0 ] ; \
	then \
		sudo apt install $@ ; \
	fi
	touch $@

ansi2html.sh:
        wget http://www.pixelbeat.org/scripts/ansi2html.sh

build: docker.io ansi2html.sh Dockerfile
	sudo docker build -t $(DOCKER_IMAGE) .

start:
	sudo docker run -p 4242:80 -d $(DOCKER_IMAGE)

clean:
	rm -f ansi2html.sh
	sudo docker rmi $(DOCKER_IMAGE)

