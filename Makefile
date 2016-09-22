RUN_ECLIPSE_ANT_RUNNER=../eclipse-tips/eclipse-ant/run_eclipse_ant_runner.sh
ECLIPSE_DIR=../tools/eclipse

all: org.benhur.utility.visualstudio.projectdependencies/dist/org.benhur.utility.visualstudio.projectdependencies.zip org.benhur.utility.visualstudio.projectdependencies.dgml/dist/org.benhur.utility.visualstudio.projectdependencies.dgml-v*.zip org.benhur.utility.visualstudio.projectdependencies.dsm/dist/org.benhur.utility.visualstudio.projectdependencies.dsm-v*.zip

org.benhur.utility.visualstudio.projectdependencies/dist/org.benhur.utility.visualstudio.projectdependencies.zip:
	$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) org.benhur.utility.visualstudio.projectdependencies/scripts/build.ant 

org.benhur.utility.visualstudio.projectdependencies.dgml/dist/org.benhur.utility.visualstudio.projectdependencies.dgml-v*.zip:
	$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) org.benhur.utility.visualstudio.projectdependencies.dgml/scripts/build.ant 

org.benhur.utility.visualstudio.projectdependencies.dsm/dist/org.benhur.utility.visualstudio.projectdependencies.dsm-v*.zip:
	$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) org.benhur.utility.visualstudio.projectdependencies.dsm/scripts/build.ant 

clean:
	rm -rf */dist 

