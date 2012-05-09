-module(jint_send).

-export([setup/0, send/0]).

setup() ->
	Pid = spawn(jint_erl, start, []),
	{sparta, athens@laptop} ! {Pid},
	send().

send() ->
	{sparta, athens@laptop} ! {test, 56}.