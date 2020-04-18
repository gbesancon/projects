#!/bin/bash
set -e
set -x

PROJECT_NAME=$1

BUILD_FOLDERPATH=build
TEST_RESULTS_FOLDERPATH=${BUILD_FOLDERPATH}/test-results
TEST_RESULTS_PREFIX=test

if [ -d ${BUILD_FOLDERPATH} ] ; then rm -r ${BUILD_FOLDERPATH} ; fi
mkdir ${BUILD_FOLDERPATH}
mkdir ${TEST_RESULTS_FOLDERPATH}

go test -v -covermode=count -coverprofile ${TEST_RESULTS_FOLDERPATH}/${TEST_RESULTS_PREFIX}.coverage `go list ./...` 2>&1 | tee ${TEST_RESULTS_FOLDERPATH}/${TEST_RESULTS_PREFIX}-coverage.output
# Generate the coverage heatmap
go tool cover -html=${TEST_RESULTS_FOLDERPATH}/${TEST_RESULTS_PREFIX}.coverage -o ${TEST_RESULTS_FOLDERPATH}/${TEST_RESULTS_PREFIX}-coverage-heatmap.html
# Generate JUnit xml coverage results
cat ${TEST_RESULTS_FOLDERPATH}/${TEST_RESULTS_PREFIX}-coverage.output | go-junit-report > ${TEST_RESULTS_FOLDERPATH}/${TEST_RESULTS_PREFIX}.xml
# Copy test results to standardized name expected by the pipeline.
cp ${TEST_RESULTS_FOLDERPATH}/${TEST_RESULTS_PREFIX}.xml ${TEST_RESULTS_FOLDERPATH}/TEST-${PROJECT_NAME}.xml
# Generate the coverage index.html, which will be shown on the vsts code coverage tab.
gocov convert ${TEST_RESULTS_FOLDERPATH}/${TEST_RESULTS_PREFIX}.coverage | gocov-html > ${TEST_RESULTS_FOLDERPATH}/${TEST_RESULTS_PREFIX}-coverage.html
# Convert the coverage result to cobertura format
gocov convert ${TEST_RESULTS_FOLDERPATH}/${TEST_RESULTS_PREFIX}.coverage | gocov-xml > ${TEST_RESULTS_FOLDERPATH}/${TEST_RESULTS_PREFIX}-coverage.xml
# Copy test coverage results to standardized name expected by the pipeline.
cp ${TEST_RESULTS_FOLDERPATH}/${TEST_RESULTS_PREFIX}-coverage.xml ${TEST_RESULTS_FOLDERPATH}/coberturaTestReport.xml