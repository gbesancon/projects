DOCKER_IMAGE=fortune-cowsay-lolcat-server

all: setup build

setup:
	sudo apt install docker.io
	echo fetch http://www.pixelbeat.org/scripts/ansi2html.sh
	rm -f ansi2html.sh
	wget http://www.pixelbeat.org/scripts/ansi2html.sh

build:
	docker build -t $(DOCKER_IMAGE) .

start:
	docker run -p 4242:80 -d $(DOCKER_IMAGE)

clean:
	rm -f ansi2html.sh
	docker rmi $(DOCKER_IMAGE)

