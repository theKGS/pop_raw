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
-export([init/2,outerMapCheck/1,foo/2,makeMove/6]).

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
		   array:set(Y1, {Food, 0, none}, array:get(X1, Map)),
		   array:set(Y2, {NewFood, Type, PID}, array:get(X2,Map));
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


%%
%% TEST CASES
%%
init_test()->
	?assert(outerMapCheck(init(10,10)) =:= true).



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
						



						
