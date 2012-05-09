-module(jint_erl).

-export([setup/0, server/0]).

setup() ->
	spawn(jint_erl, server, []).

server() ->
	receive
		{_} ->
			io:fwrite("Hello, world!\n"),
			server()
	end.