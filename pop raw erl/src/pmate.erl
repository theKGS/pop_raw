%% Author: Johan
%% Created: 7 maj 2012
%% Description: TODO: Add description to pmate
-module(pmate).

%%
%% Exported Functions
%%
-export([start/0]).
%%
%% API Functions
%%
start()->
	receiveLoop().

	

%%
%% Local Functions
%%
receiveLoop()->
	receive
		{mate, MapPID, MapStruct, MatePID, X,Y} ->
			io:format("Got mate message~n"),
			MateList = mate:checkMate(MapStruct, X, Y),
			FreeSlotAvailible = lists:keyfind(free, 2, MateList),
			NearbyEnemies = lsits:keyfind(enemy, 2, MateList),
			if  FreeSlotAvailible =:= false ->
					receiveLoop();
				NearbyEnemies =/= false ->
					receiveLoop();
				true -> 
				   {_, _ ,X,Y} = FreeSlotAvailible,
				   {PID, possible, X,Y} = lists:keyfind(possible, 2, MateList),
				   OkToMate = askPID([MatePID, PID]),
				   if OkToMate =:= true ->
						  MapPID ! {spawn, X, Y},
						  receiveLoop();
					  true -> 
						  notPossible,
						  receiveLoop()
				   end
			end;
		MSG ->
			io:format("Got bad message: ~s ~n", [MSG]),
			receiveLoop()
	end.
askPID([])->
	true;
askPID([H|T])->
	H ! mate,
	receive
		true->
			askPID(T);
		false -> true%%@TODO set back to false when rabbits are working
		after 100 -> true
	end.
