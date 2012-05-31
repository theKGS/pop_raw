-module(idiot_rabbit).


-export([new/1, loop/1]).

%% OBS THIS IS PURELY FOR TESTING PURPOSES AND 
%% NOTHING SHOULD EVER BE DONE TO THIS

new(Pid) ->
	ReportPid = spawn(idiot_rabbit, loop, [Pid]),
	Pid ! {newRabbit, ReportPid, 1, 2}.

loop(Pid) ->
	receive
		{death} ->
			Pid ! {death, self(), 1, 2},
			loop(Pid);
		{rabbitMap, _A} ->
			Pid ! {move, self(), 1, 2, 3, 4, 5, 6},
			loop(Pid);
		{wolfMap, _B} ->
			Pid ! {wolfEat, self(), 1, 2, 3, 4, 5, 6},
			loop(Pid)
	end.