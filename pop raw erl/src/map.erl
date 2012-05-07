%% Author: Johan
%% Created: 2 maj 2012
%% Description: TODO: Add description to map
-module(map).

%%
%% Include files
%%
-include_lib("eunit/include/eunit.hrl").
%%
%% Exported Functions
%%
-export([init/2,outerMapCheck/1,foo/2,makeMove/6, move/6]).

%%
%% API Functions
%%
%%-----------------------------------------------------------------------------
%%@doc
%%	Iinitialization funktion for map module.
%%@end
%%-----------------------------------------------------------------------------
init(_Seed, Size)->
	MapArr = array:new(Size, {default,array:new(Size,{default, {5,free,none}})}),
	PMatePID = spawn(fun()->pmate:start() end),
	spawn(fun()->receiveLoop(MapArr, PMatePID) end).
	
	
receiveLoop(Map, PMatePID)->
	receive
		{get,PID}->
			PID ! {Map, none, none, none, none},
			receiveLoop(Map, PMatePID);
		{move, PID,X1,Y1, X2,Y2}->
			NewMap = makeMove(Map,PID, X1,Y1,X2,Y2),
			
			if(NewMap =:= false) ->
				  PID ! no,
				  %%Check for mating
				  %%PMatePID ! {mate, self(), Map, PID, X1,Y1},
				  receiveLoop(Map, PMatePID);
			  true ->
				  PID ! {moveok, X2,Y2},
				  %%PMatePID ! {mate, self(), NewMap, PID, X1,Y1},
				  receiveLoop(NewMap, PMatePID)
			end;
		{eat, PID, X, Y}->
			PID ! ok,
			%%PMatePID ! {mate, self(), Map, PID, X,Y},
			receiveLoop(Map, PMatePID);
		{spawn, _X, _Y}->
			io:format("Mate successfull"),
			tbi		 
	end.
foo(PID, MSG)->
	PID ! MSG,
	receive
		R -> R
	end.				


%%
%% Local Functions
%%




makeMove(Map,PID,X1,Y1,X2,Y2)->
	{Food, Type, _} = array:get(Y1, array:get(X1, Map)),
	{NewFood, NewType, NewPid} = array:get(Y2, array:get(X2, Map)),
	if NewPid =:= none ->
		   Map2 = array:set(X1, array:set(Y1, {Food, 0, none}, array:get(X1, Map)), Map),
		   Map3 = array:set(X1,array:set(Y2, {NewFood, Type, PID}, array:get(X2,Map)), Map2),
		   Map3;
	(NewType =:= wolf) ->
		if Type =:= rabbit->
			PID ! death,
			false;
		true -> false
		end;
	(NewType =:= rabbit) ->
		false;
	true -> false
	end.

move(MapPID, Self, X1,Y1,X2,Y2)->
	MapPID ! {move, Self, X1,Y1,X2,Y2},
	receive
		no -> false;
		{moveok,_,_} ->
			MapPID ! {get, Self},
			receive
				{Map,_,_,_,_} -> Map
			end
	end.
	
			   
%%
%% TEST CASES
%%

move_test()->
	PID = init(10,10),
	Self = self(),
	NewMap = move(PID, Self, 0,0,0,1),
	{Grass,Type, SPID} = array:get(1,array:get(0, NewMap)),
	?assertMatch({5, free, Self}, {Grass,Type,SPID}).
%%	?assertMatch(cat, cat).

%%
%% TEST HELPER FUNKTIONS
%%
outerMapCheck([])->
	true;
outerMapCheck([H|T])->
	innerMapCheck(H, T).

innerMapCheck([], T)->
	outerMapCheck(T);
innerMapCheck([{5,0,none}|Tail], T)->
	innerMapCheck(Tail, T);
innerMapCheck(_H, _T)->
	false.
						



						
