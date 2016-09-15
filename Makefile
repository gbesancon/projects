RUN_ECLIPSE_ANT_RUNNER=../eclipse-tips/eclipse-ant/run_eclipse_ant_runner.sh
ECLIPSE_DIR=~/02-Tools/eclipse-modeling-neon-R-linux-gtk-x86_64
ANT_FILES="CodinGame_20130727/scripts/build.ant" "CodinGame_20130921/scripts/build.ant" "CodinGame_20150206/scripts/build.ant"

all: setup build

setup:

build:
	for ANT_FILE in $(ANT_FILES); do \
		$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) $$ANT_FILE ; \
	done

clean:
	rm -rf */dist 

