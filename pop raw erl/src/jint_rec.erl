-module(jint_rec).

-export([setup/1, server/1]).

setup(SendPid) ->
	server(SendPid).

server(SendPid) ->
	receive
		%% 4 = newRabbit
		{4, _Pid, X, Y} ->
			rabbits:newRabbit({X, Y}, SendPid),
			server(SendPid);
		%% 8 = newWolf
		{8, _Pid, X, Y} ->
			wolves:newWolf({X, Y}, SendPid),
			server(SendPid);
		%% 0 = rabbitMap
		{0,Pid, B} ->
			Pid ! {rabbitMap, B},
			server(SendPid);
		%% 5 = death
		{5,Pid} ->
			Pid ! {death},
			server(SendPid);
		%% 0 = wolfMap
		{7,Pid, B} ->
			Pid ! {wolfMap, B},
			server(SendPid);
		%% 9 = yes
		{9, Pid} ->
			Pid ! {yes},
			server(SendPid);
		%% 10 = no
		{10, Pid} ->
			Pid ! {no},
			server(SendPid);
		%% 11 = start
		{11, Pid} ->
			Pid ! {start},
			server(SendPid);
		{12, Pid} ->
			Pid ! {eatMove},
			server(SendPid)
	end.