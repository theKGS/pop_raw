-module(jint_rec).

-export([setup/1, server/1]).

setup(SendPid) ->
	server(SendPid).

server(SendPid) ->
	receive
		{newRabbit, _Pid, X, Y} ->
			rabbits:new({X, Y}, SendPid),
			server(SendPid);
		{newWolf, _Pid, _X, _Y} ->
			%% TODO New wolves
			server(SendPid);
		{A,Pid, B} ->
			Pid ! {A, B},
			server(SendPid);
		{A, Pid} ->
			Pid ! A,
			server(SendPid)
	end.