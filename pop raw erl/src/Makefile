
SOURCES = jint_rec.erl jint_send.erl rabbits.erl
NAME = -sname foo -setcookie thisissparta

all:
	erlc $(SOURCES)

start:	all
	erl $(NAME) -run jint_send setup

clean:
	del *.beam