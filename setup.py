import os
from setuptools import setup, find_packages

def read(fname):
    return open(os.path.join(os.path.dirname(__file__), fname)).read()

setup(
    name='Chi',
    version='2.0.0',
    author='kingkingyyk',
    author_email='king8793yyk@hotmail.com',
    description=('An Internet of Things automation system'),
    license='-',
    keywords='Chi',
    url='https://github.com/kingkingyyk/Chi',
    packages=find_packages(),
    install_requires=read('requirements.txt').splitlines(),
    long_description=read('README.md'),
    classifiers=['Development Status :: Development',
                 'Topic :: Utilities'],
)