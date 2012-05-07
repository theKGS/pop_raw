%% Author: Johan
%% Created: 7 maj 2012
%% Description: TODO: Add description to mate
-module(mate).

%%
%% Include files
%%

%%
%% Exported Functions
%%
-export([checkMate/3]).

%%
%% API Functions
%%
%%@doc
%% 
%%@end
checkMate({Map, LeftMapPID, TopMapPID, RightMapPID, BottomMapPID}, X,Y)->
	{_,CurrentType,PID} = array:get(Y, array:get(X, Map)),
	MapSize = array:size(Map),
	MapDirs = [up,left,right,down],
	io:format("Getting mate list  ~n"),
	MateList = [compare(Dir,{Map, LeftMapPID, TopMapPID, RightMapPID, BottomMapPID},
						X,
						Y,
						CurrentType,MapSize) || Dir <- MapDirs],
	io:format("got mate list: ~s~n",[MateList]),
	MateList.
	


%%
%% Local Functions
%%
compare(up,{Map, _, TopMapPID, _, _},X,Y, CurrentType, _)->
	if Y < 1, TopMapPID =/= none ->
		   tbi;
	   Y > 0 ->
		   {_, MateType, PID} = array:get(Y,array:get(X, Map)),
		   compareType(CurrentType, MateType, PID,X,Y);
	   true -> {none, notPossible}
	end;
compare(left,{Map, LeftMapPID, _, _, _},X,Y, CurrentType, MapSize)->
	if MapSize - X < 1, LeftMapPID =/= none ->
		   tbi;
	   MapSize - X > 0 ->
		   {_, MateType, PID} = array:get(Y,array:get(X, Map)),
		   compareType(CurrentType, MateType, PID,X,Y);
	   true -> {none, notPossible}
	end;
compare(down,{Map, _, _, _, BottomMapPID},X,Y, CurrentType, MapSize)->
	if MapSize - Y < 1, BottomMapPID =/= none ->
		   tbi;
	   MapSize - Y > 0 ->
		   {_, MateType, PID} = array:get(Y,array:get(X, Map)),
		   compareType(CurrentType, MateType, PID,X,Y);
	   true -> {none, notPossible}
	end;
compare(right,{Map, _, _, RightMapPID, _},X,Y, CurrentType, _)->
	if X < 1, RightMapPID =/= none ->
		   tbi;
	   X > 0 ->
		   {_, MateType, PID} = array:get(Y,array:get(X, Map)),
		   compareType(CurrentType, MateType, PID,X,Y);
	   true -> {none, notPossible}
	end.


compareType(Type1, Type2, PID,X,Y) ->
	if Type2 =:= none ->
		   {none, free,X,Y};
	   Type2 =/= Type1 ->
		   {none, enemy,X,Y};
	   true -> 
		   {PID, possible,X,Y}
	end.