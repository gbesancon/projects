DOCKER_IMAGE=pi

all: setup build

setup:
	sudo apt install docker.io

build:
	docker build -t $(DOCKER_IMAGE) .

start:
	docker run -p 4242:80 -d $(DOCKER_IMAGE)

clean:
	docker rmi $(DOCKER_IMAGE)

