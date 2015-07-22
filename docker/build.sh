mkdir content
cp ../fortune-cowsay-lolcat-server.sh content/
cp ../ansi2html.sh content/
docker build -t fortune-cowsay-lolcat-server .
rm -rf content/
