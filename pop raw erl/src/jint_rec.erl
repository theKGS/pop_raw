-module(jint_rec).

-export([setup/1, server/1]).

setup(SendPid) ->
	server(SendPid).

server(SendPid) ->
	receive
		{new, _Pid, X, Y} ->
			io:fwrite("new~n", []),
			rabbits:new({X, Y}, SendPid),
			server(SendPid);
		{A,Pid, B} ->
			io:format("Type ~s~n", [A]),
			Pid ! {A, B},
			server(SendPid);
		{A, Pid} ->
			io:format("Type ~s~n", [A]),
			Pid ! A,
			server(SendPid)
	end.