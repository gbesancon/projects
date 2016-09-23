DOCKER_IMAGE=pi

all: build

docker.io:
	dpkg -s docker.io ; \
	if [ $$? != 0 ] ; \
	then \
		sudo apt install docker.io ; \
	fi
	touch docker.io 

build: docker.io Dockerfile
	docker build -t $(DOCKER_IMAGE) .

start: build
	docker run -p 4242:80 -d $(DOCKER_IMAGE)

clean:
	docker rmi $(DOCKER_IMAGE)

