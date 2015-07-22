# Ubuntu
FROM ubuntu
MAINTAINER Gilles Besancon

RUN apt-get update \
&& apt-get install -y wget fortunes cowsay lolcat gawk netcat \
&& wget -O /home/fortune-cowsay-lolcat-server.sh https://raw.githubusercontent.com/gbesancon/scripts/master/web/fortune-cowsay-lolcat-server.sh

EXPOSE 80

CMD ["/bin/bash","/home/fortune-cowsay-lolcat-server.sh","80"]
