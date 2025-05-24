from setuptools import setup

setup(
    name='AnimalShelter',
    version='1.0',
    description='A CRUD commands package for Animals collection in MongoDB',
    author='<Daniel Gorelkin>',
    author_email='<Goralkin@Gmail.com>',
    packages=['AnimalShelter'],
    install_requires=['pymongo']
)