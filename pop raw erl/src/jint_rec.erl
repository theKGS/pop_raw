-module(jint_rec).

-export([setup/1, server/1]).

setup(SendPid) ->
	server(SendPid).

server(SendPid) ->
	receive
		{_A, _, _, _} ->
			io:fwrite("Hello world!~n", []),
			server(SendPid)
%% 		{new, _Pid, X, Y} ->
%% 			rabbits:new({X, Y}, SendPid),
%% 			server(SendPid);
%% 		{A,Pid, B} ->
%% 			Pid ! {A, B},
%% 			server(SendPid);
%% 		{A, Pid} ->
%% 			Pid ! A,
%% 			server(SendPid)
	end.