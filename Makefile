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
	sudo docker build -t $(DOCKER_IMAGE) .
	touch $@

start: build
	sudo docker run -p 4242:80 -d $(DOCKER_IMAGE)

clean:
	sudo docker rmi $(DOCKER_IMAGE)
	rm build
