JAVA_PROJECT_NAMES=amazon capgemini-fruitshop codingame codurance jpmorgan-super-simple-stocks eclipse-utility database-dependencies visualstudio-projectdependencies
TEX_PROJECT_NAMES=cheatsheet cv
PROJECT_NAMES=${JAVA_PROJECT_NAMES} ${TEX_PROJECT_NAMES}

DOCKER_IMAGE_PREFIX=gbesancon
DOCKER_STACK_NAME=projects
GCP_PROJECT_NAME=gbesancon
ENVIRONMENT_NAME=localhost

include scripts/makefile/composite/Makefile
include scripts/makefile/composite/service/Makefile