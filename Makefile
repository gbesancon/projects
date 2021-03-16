JAVA_PROJECT_NAMES=amazon capgemini-fruitshop codingame codurance database-dependencies eclipse-utility jpmorgan-super-simple-stocks visualstudio-projectdependencies
ANDROID_PROJECT_NAMES=bbingo
MARKDOWN_PROJECT_NAMES=42
TEX_PROJECT_NAMES=cheatsheet cv
PROJECT_NAMES=${JAVA_PROJECT_NAMES} ${ANDROID_PROJECT_NAMES} ${MARKDOWN_PROJECT_NAMES} ${TEX_PROJECT_NAMES}

DOCKER_IMAGE_PREFIX=gbesancon
DOCKER_STACK_NAME=projects
GCP_PROJECT_NAME=gbesancon
ENVIRONMENT_NAME=localhost

include scripts/makefile/composite/Makefile
include scripts/makefile/composite/service/Makefile