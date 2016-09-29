DOCKER_IMAGE=gbesancon/pi

all: build

docker.io:
	dpkg -s $@ ; \
	if [ $$? != 0 ] ; \
	then \
		sudo apt install $@ ; \
	fi
	touch $@ 

build: docker.io Dockerfile
	docker build -t $(DOCKER_IMAGE) .

start: build
	docker run -p 4242:80 -d $(DOCKER_IMAGE)

clean:
	docker rmi $(DOCKER_IMAGE)

