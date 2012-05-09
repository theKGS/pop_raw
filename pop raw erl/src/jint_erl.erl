-module(jint_erl).

-export([start/0, server/0]).

start() ->
	Pid = spawn(jint_erl, server, []),
	register(rawMap, Pid),
	set_cookie

server() ->
	receive
		{abba, _} ->
			io:fwrite("Hello, world!\n"),
			server()
	end.