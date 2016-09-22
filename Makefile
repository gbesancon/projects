RUN_ECLIPSE_ANT_RUNNER=../eclipse-tips/eclipse-ant/run_eclipse_ant_runner.sh
ECLIPSE_DIR=../tools/eclipse

all: org.benhur.utility.database.dependencies/dist/org.benhur.utility.database.dependencies.zip org.benhur.utility.database.dependencies.dgml/dist/org.benhur.utility.database.dependencies.dgml-v*.zip org.benhur.utility.database.dependencies.dsm/dist/org.benhur.utility.database.dependencies.dsm-v*.zip

org.benhur.utility.database.dependencies/dist/org.benhur.utility.database.dependencies.zip:
	$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) org.benhur.utility.database.dependencies/scripts/build.ant 

org.benhur.utility.database.dependencies.dgml/dist/org.benhur.utility.database.dependencies.dgml-v*.zip:
	$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) org.benhur.utility.database.dependencies.dgml/scripts/build.ant 

org.benhur.utility.database.dependencies.dsm/dist/org.benhur.utility.database.dependencies.dsm-v*.zip:
	$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) org.benhur.utility.database.dependencies.dsm/scripts/build.ant 

clean:
	rm -rf */dist 

