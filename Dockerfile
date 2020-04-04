FROM ubuntu:18.04

RUN sudo apt-get install texlive-full texmaker

VOLUME [ "/src" ]

WORKDIR /src
