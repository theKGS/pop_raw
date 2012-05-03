-module(jint_erl).

-export([start/0]).

start() ->
	Pid = spawn(jint_erl, server, []),
	register(rawmap, Pid).

server() ->
	receive
		{From, message...} ->
			From ! answer,
			%% TODO call function
			server();
		{From, message...} ->
			From ! answer,
			%% TODO call function
			server();
		{From, stop} ->
			From ! stop
	end.