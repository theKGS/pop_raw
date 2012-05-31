%% For testing purposes only.

-module(test).

-export([test/0]).

test() ->
	rabbits:test(),
	randw:test(),
	wolves:test().