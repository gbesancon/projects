RUN_ECLIPSE_ANT_RUNNER=../eclipse-tips/eclipse-ant/run_eclipse_ant_runner.sh
ECLIPSE_DIR=../tools/eclipse

all: org.benhur.utility/dist/org.benhur.utility.zip org.benhur.utility.dgml/dist/org.benhur.utility.dgml.zip org.benhur.utility.dsm/dist/org.benhur.utility.dsm.zip org.benhur.utility.build/updatesite-org.benhur.utility.updatesite.feature-*.zip 
 
org.benhur.utility/dist/org.benhur.utility.zip:
	$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) org.benhur.utility/scripts/build.ant

org.benhur.utility.dgml/dist/org.benhur.utility.dgml.zip:
	$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) org.benhur.utility.dgml/scripts/build.ant

org.benhur.utility.dsm/dist/org.benhur.utility.dsm.zip:
	$(RUN_ECLIPSE_ANT_RUNNER) $(ECLIPSE_DIR) org.benhur.utility.dsm/scripts/build.ant

org.benhur.utility.build/updatesite-org.benhur.utility.updatesite.feature-*.zip:
	cd org.benhur.utility.build && ../$(RUN_ECLIPSE_ANT_RUNNER) ../$(ECLIPSE_DIR) build.ant

clean:
	rm -rf */dist */work 

