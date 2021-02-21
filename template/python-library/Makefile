.PHONY: clean build-clean build-requirements tools-requirements format lint build test-clean test-requirements test-lint test cover docs-clean docs-requirements docs examples package-clean package publish

clean: build-clean test-clean docs-clean package-clean

build-clean:
	rm -rf build
	find . -name \*.pyc -delete

build-requirements:
	pip install -r requirements.txt

tools-requirements:
	pip install -r requirements-tools.txt

format-check: tools-requirements

format: tools-requirements

lint: tools-requirements
	pylint pl -r n --msg-template="{path}:{line}: [{msg_id}({symbol}), {obj}] {msg}" --rcfile=./pylint.rc | tee pylint-pl.output
	cat pylint-pl.output | grep "10.00/10"

build: build-requirements format-check lint
	pip install .

test-clean:

test-requirements:
	pip install -r tests/requirements.txt

test-lint: tools-requirements
	pylint tests -r n --msg-template="{path}:{line}: [{msg_id}({symbol}), {obj}] {msg}" --rcfile=./pylint.rc | tee pylint-tests.output
	cat pylint-tests.output | grep "10.00/10"

test: test-requirements test-lint
	find tests -type d | xargs nosetests

cover: test-requirements test-lint
	find tests -type d | xargs nosetests --with-coverage --cover-inclusive --cover-erase --cover-package=damodel

docs-clean:
	rm -rf docs/build

docs-requirements:
	pip install -r docs/requirements.txt

docs: docs-requirements docs-clean
	cd docs && $(MAKE) html

package-clean:
	rm -rf dist

package: docs
	python setup.py sdist bdist_wheel

publish: package
	twine upload --verbose --repository testpypi dist/*
