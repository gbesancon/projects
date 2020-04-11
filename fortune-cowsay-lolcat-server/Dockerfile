# Ubuntu
FROM ubuntu
MAINTAINER Gilles Besancon

RUN apt-get update \
&& apt-get install -y wget fortunes cowsay lolcat gawk netcat

ADD ansi2html.sh fortune-cowsay-lolcat-server.sh /home/

EXPOSE 80

CMD ["/bin/bash","/home/fortune-cowsay-lolcat-server.sh","80"]
