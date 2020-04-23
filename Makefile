JAVA_PROJECT_NAMES=amazon capgemini-fruitshop codingame codurance jpmorgan-super-simple-stocks eclipse-utility database-dependencies visualstudio-projectdependencies
TEX_PROJECT_NAMES=cheatsheet cv
PROJECTS=${TEX_PROJECT_NAMES} ${JAVA_PROJECT_NAMES}
DOCKER_IMAGE_PREFIX=gbesancon
ENVIRONMENT_NAME=localhost
include scripts/composite/Makefile
include scripts/composite/service/Makefile