
SOURCES = jint_rec.erl jint_send.erl rabbits.erl wolves.erl randw.erl
NAME = -sname foo -setcookie thisissparta

all:
	erlc $(SOURCES)

start:
	erl $(NAME) -run jint_send setup

clean:
	del *.beam
	
testAux:
	erlc test.erl

test: clean testAux all
	erl $(Name) -run test test