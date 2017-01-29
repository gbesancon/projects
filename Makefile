RUN_ECLIPSE_ANT_RUNNER=../ant-eclipse/eclipse-ant/run_eclipse_ant_runner.sh
ECLIPSE_DIR=../tools/eclipse_linux

all: Problem1_20170129/dist/Problem1_20170129.zip Problem2_20170129/dist/Problem1_20170129.zip 

Problem1_20170129/dist/Problem1_20170129.zip: Problem1_20170129/scripts/build.ant
	$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) $<

Problem2_20170129/dist/Problem2_20170129.zip: Problem2_20170129/scripts/build.ant
	$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) $<
clean:
	rm -rf */dist 

