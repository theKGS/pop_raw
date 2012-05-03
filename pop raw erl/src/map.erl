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
-export([init/2]).

%%
%% API Functions
%%
init(Seed, Size)->
	MapArray = array:new(Size, {default, {5,none}}),
	array:map(fun({Next, _W})->io:fwrite("~s\n", Next) end, MapArray).

receiveLoop(MapArray)->
	tbi.


%%
%% Local Functions
%%

foo({Next, _Bar}, Acc) when Next =/= 0->
	Acc+1;
foo(_,Acc)->
	Acc.

%%
%% TEST CASES
%%
