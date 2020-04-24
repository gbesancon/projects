all: ci-build

.PHONY: setup
setup:
	for project_name in ${PROJECT_NAMES} ; do \
		$(MAKE) -C $$project_name $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done

.PHONY: clean
clean:
	for project_name in ${PROJECT_NAMES} ; do \
		$(MAKE) -C $$project_name $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done

.PHONY: build-dev
build-dev:
	for project_name in ${PROJECT_NAMES} ; do \
		$(MAKE) -C $$project_name $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done

.PHONY: build
build:
	for project_name in ${PROJECT_NAMES} ; do \
		$(MAKE) -C $$project_name $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done

.PHONY: test
test:
	for project_name in ${PROJECT_NAMES} ; do \
		$(MAKE) -C $$project_name $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done

.PHONY: ci-build
ci-build: 
	for project_name in ${PROJECT_NAMES} ; do \
		$(MAKE) -C $$project_name $@ ; \
		make_result=$$? ; \
		if [ $$make_result -ne 0 ] ; then \
			exit $$make_result ; \
		fi ; \
	done