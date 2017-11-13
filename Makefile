TESTDIR=acceptance

.PHONY: all build clean test

all: build

build:
	gradle build
	gradle test
	
clean:
	rm -f *~
	$(MAKE) -C $(TESTDIR) clean

test:
	$(MAKE) -C $(TESTDIR) test
	