ECLIPSE_DIR=~/02-Tools/eclipse-modeling-neon-R-linux-gtk-x86_64
PROJECTS="org.benhur.utility.database.dependencies" "org.benhur.utility.database.dependencies.dgml" "org.benhur.utility.database.dependencies.dsm"

all: setup build

setup:

build:
	for PROJECT in $(PROJECTS); do \
		../eclipse-tips/eclipse-ant/run_eclipse_ant_runner.sh $(ECLIPSE_DIR) $$PROJECT/scripts/build.ant ; \
	done

clean:
	rm -rf */dist 

