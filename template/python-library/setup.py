import os
import re
from setuptools import setup, find_packages

__version__ = None
with open('pl/__init__.py') as f:
    exec(f.read())

if "VERSION" in os.environ:
    if os.environ["VERSION"]:
        __version__ =  os.environ["VERSION"]

# Convert version from Semantic Version into PEP 440
pattern = re.compile(r"""(?P<major_minor_patch>[0-9]*\.[0-9]*\.[0-9]*)(-.*\.(?P<increment>[0-9]*))?""", re.VERBOSE)
match = pattern.match(__version__)
if match:
    __version__ = match.group("major_minor_patch")
    if match.group("increment") is not None:
        __version__ += ".dev" + match.group("increment")

with open("README.md", "r") as f:
    long_description = f.read()

setup(
    name="pl",
    version=__version__,
    description="Python library",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="https://github.com/gbesancon/projects",
    author="Gilles Besançon",
    author_email="gilles.besancon@gmail.com",
    packages=find_packages(exclude=['tests', 'tests.*']),
    keywords=[],
    classifiers=[
        "Programming Language :: Python :: 3",
        "License :: OSI Approved :: MIT License",
        "Operating System :: OS Independent",
    ],
    python_requires='>=3.6',
)