.PHONY: clean build-clean build-requirements tools-requirements lint build docs-clean docs-requirements docs package-clean package package-zip-clean package-zip

clean: build-clean docs-clean package-clean package-zip-clean

build-clean:

build-requirements:
	pip install -r requirements.txt

tools-requirements:
	pip install -r requirements-tools.txt

format-check: tools-requirements

format: tools-requirements

lint: tools-requirements
	pylint pa -r n --msg-template="{path}:{line}: [{msg_id}({symbol}), {obj}] {msg}" --rcfile=./pylint.rc | tee pylint-pa.output
	cat pylint-pa.output | grep "10.00/10"

build: build-requirements format-check lint

docs-clean:
	rm -rf docs/build

docs-requirements:
	pip install -r docs/requirements.txt

docs: docs-requirements docs-clean
	cd docs && $(MAKE) html
	
package-clean:
	rm -f PythonApplication.spec
	rm -rf build
	rm -rf dist
	rm -rf package

package: build
	pyinstaller --onefile -w PythonApplication.py --add-data ./resources:resources

package-zip-clean: package-clean docs-clean
	rm -rf package

package-zip: package docs
	mkdir -p package
	mkdir -p package/tmp && cp -rf dist/PythonApplication docs/build/html package/tmp
	cd package/tmp && zip -r2T ../PythonApplication_$(VERSION).zip *
	rm -rf package/tmp

