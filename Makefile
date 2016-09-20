RUN_ECLIPSE_ANT_RUNNER=../eclipse-tips/eclipse-ant/run_eclipse_ant_runner.sh
ECLIPSE_DIR=../tools/eclipse
ANT_FILES="org.benhur.utility.database.dependencies/scripts/build.ant" "org.benhur.utility.database.dependencies.dgml/scripts/build.ant" "org.benhur.utility.database.dependencies.dsm/scripts/build.ant"

all: setup build

setup:

build:
	for ANT_FILE in $(ANT_FILES); do \
		$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) $$ANT_FILE ; \
	done

clean:
	rm -rf */dist 

