#!/bin/bash
set -e

TEST="$1"
DIR=`mktemp -d --tmpdir gbdt-unittest-XXXXXXXX`

# setup colors
if tput sgr0 >/dev/null 2>&1; then
    RED=$(tput setaf 1)
    GREEN=$(tput setaf 2)
    YELLOW=$(tput setaf 3)
    WHITE=$(tput setaf 7)
    BOLD=$(tput bold)
    RESET=$(tput sgr0)
else
    RED=
    GREEN=
    YELLOW=
    WHITE=
    BOLD=
    RESET=
fi

status()
{
    echo "${BOLD}${YELLOW}>> ${@}${RESET}"
}

error_out()
{
    status "${RED}test script \`$TEST' was interrupted"
    status "${RED}temporary directory \`$DIR' was not cleaned"
    status "${RED}investigate and delete at your leisure"
    trap '' ERR
    exit 1
}

trap error_out ERR

do_assertion()
{
    local TEXT="$1" STATE="$2"

    if [ "$STATE" = OK ]; then
	printf "${BOLD}${GREEN}%s${WHITE} : %s${RESET}\n" 'OK' "$TEXT"
    else
	printf "${BOLD}${RED}%s${WHITE} : %s : ${RED}%s${RESET}\n" '!!' "$TEXT" "$STATE"
	error_out
    fi

}

assert_dir()
{
    local TESTDIR STATE
    for TESTDIR in "$@"; do
	if [ -d "$TESTDIR" ]; then
	    STATE="OK"
	else
	    if [ -e "$TESTDIR" ]; then
		STATE="file exists, but is no directory"
	    else
		STATE="missing directory"
	    fi
	fi
	do_assertion "checking directory \`…${TESTDIR/$DIR}'" "$STATE"
    done
}

assert_nofile()
{
    local TESTFILE STATE
    for TESTFILE in "$@"; do
	if [ ! -e "$TESTFILE" ]; then
	    STATE='OK'
	else
	    if [ -d "$TESTDIR" ]; then
		STATE='unwanted file exists (directory)'
	    else
		STATE='unwanted file exists'
	    fi
	fi
	do_assertion "checking missing file \`…${TESTFILE/$DIR}'" "$STATE"
    done
}

assert_content()
{
    local TESTFILE="$1" EXPECTED="$2"

    local STATE
    if [ -e "$TESTFILE" ]; then
	ACTUAL="$(cat "$TESTFILE")"
	if [ "$ACTUAL" = "$EXPECTED" ]; then
	    STATE='OK'
	else
	    printf -v STATE "expected content = \`%s', actual content = \`%s'" "$EXPECTED" "$ACTUAL"
	fi
    else
	STATE='missing file'
    fi
    
    do_assertion "checking file content \`…${TESTFILE/$DIR}'" "$STATE"
}

assert_equals()
{
    local EXPECTED="$1" ACTUAL="$2"

    local STATE
    if [ "$ACTUAL" = "$EXPECTED" ]; then
	STATE='OK'
    else
	printf -v STATE "expected value = \`%s', actual value = \`%s'" "$EXPECTED" "$ACTUAL"
    fi
    
    do_assertion "assert_equals" "$STATE"
}

#################################################################

source "$TEST"

#################################################################

rm -rf "$DIR"

status "${GREEN}test \`$TEST' OK${RESET}"
exit 0
