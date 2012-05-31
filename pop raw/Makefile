###############################################################################
## This Makefile must be placed in the directory pop_raw to have access to both
## "pop raw" and "pop raw erl", it is currently not due to the stupidity of 
## github
###############################################################################

DESTINATION = -o ./"pop raw"/Compiled
SDEST = ./"pop raw erl"/src/
SOURCES = $(SDEST)jint_rec.erl $(SDEST)jint_send.erl $(SDEST)rabbits.erl \
			$(SDEST)wolves.erl $(SDEST)randw.erl
NAME = -sname foo -setcookie thisissparta

all:
	erlc $(DESTINATION) $(SOURCES)

clean:
	del .\"pop raw"\Compiled\*.beam