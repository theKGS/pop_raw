-module(jint_send).

-export([setup/0, send/0]).

setup() ->
	PidSelf = spawn(jint_send, send, []),
	Pid = spawn(jint_rec, setup, [PidSelf]),
	Node = erlang:atom_to_list(athens@),
	Host = net_adm:localhost(),
	Address = erlang:list_to_atom(lists:append(Node, Host)),
	{sparta, Address} ! {Pid}.

send() ->
	Node = erlang:atom_to_list(athens@),
	Host = net_adm:localhost(),
	Address = erlang:list_to_atom(lists:append(Node, Host)),
	receive
		%% rabbitMap = 0
		{rabbitMap, A, B, C} ->
			{sparta, Address} ! {0, A, B, C};
		%% rabbitEat = 1
		{rabbitEat, A, B, C} ->
			{sparta, Address} ! {1, A, B, C};
		%% move = 2
		{move, A, B, C, D, E} ->
			{sparta, Address} ! {2, A, B, C, D, E};
		%% death = 5
		{death, A, B, C} ->
			{sparta, Address} ! {5, A, B, C};
		%% wolfEat = 6
		{wolfEat, A, B, C} ->
			{sparta, Address} ! {6, A, B, C};
		%% wolfMap = 7
		{wolfMap, A, B, C} ->
			{sparta, Address} ! {7, A, B, C}
	end,
	send().