-module(jint_send).

-export([send/0]).

send() ->
	{sparta, athens@laptop} ! {test, 56}.