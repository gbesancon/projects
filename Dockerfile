# Ubuntu
FROM ubuntu
MAINTAINER Gilles Besancon

RUN apt-get update \
&& apt-get install -y nodejs npm
RUN npm install midijs three

ADD src/main.js /home/

CMD ["/usr/bin/nodejs","main.js"]
