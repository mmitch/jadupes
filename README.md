jadupes – Java duplicate file finder and deduper
================================================

[![Build Status](https://travis-ci.org/mmitch/jadupes.svg?branch=master)](https://travis-ci.org/mmitch/jadupes)
[![Coverage Status](https://codecov.io/github/mmitch/jadupes/coverage.svg?branch=master)](https://codecov.io/github/mmitch/jadupes?branch=master)

abstract
--------

jadupes is a duplicate file detector that is inspired by
[jdupes](https://github.com/jbruchon/jdupes), while having
different design goals:

* jdupes has many configuration options and operation modes.
  jadupes is (currently) designed to do exactly one thing:
  dedupe [rsnapshot](http://rsnapshot.org/) backup archives.

* jdupes is designed to be extremely fast while using little memory.
  jadupes favors a small and simple codebase and more expensive higher
  language constructs over resource usage.

* jadupes functionality should be verified by automated tests.

It also also a direct descendant of my own, unfinished Perl variant of
jdupes called [pdupes](https://github.com/mmitch/pdupes): It turns
out that currently I like Java and Eclipse refactoring tools more
than cool classic Perl ;-)

Because Java is platform independent, some things I was planning 
to use for deduping (like keeping the file with the higher link count
of two duplicate files and deleting the other) will not be possible.


usage
-----

`java jadupes.jar <directory> [<directory> ...]`

jadupes will scan all files in the given directories and will then
hardlink duplicate files to each other to consume disk space.


#TODO
-----

* fresd project: todo ALL the things!