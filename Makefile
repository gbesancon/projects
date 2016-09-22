RUN_ECLIPSE_ANT_RUNNER=../eclipse-tips/eclipse-ant/run_eclipse_ant_runner.sh
ECLIPSE_DIR=../tools/eclipse

all: CodinGame_20130727/dist/CodinGame_20130727.zip CodinGame_20130921/dist/CodinGame_20130921.zip CodinGame_20150206/dist/CodinGame_20150206.zip

CodinGame_20130727/dist/CodinGame_20130727.zip: CodinGame_20130727/scripts/build.ant
	$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) $<

CodinGame_20130921/dist/CodinGame_20130921.zip: CodinGame_20130921/scripts/build.ant
	$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) $<

CodinGame_20150206/dist/CodinGame_20150206.zip: CodinGame_20150206/scripts/build.ant
	$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) $<

clean:
	rm -rf */dist 

