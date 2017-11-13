TESTS=$(addsuffix run,$(wildcard tests/*.test))

.PHONY: all test clean

all: test

clean:
	rm -f *~ tests/*~

test: $(TESTS)

tests/%.testrun: tests/%.test
	./run_test.sh $<
