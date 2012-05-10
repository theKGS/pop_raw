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
		Msg ->
			{sparta, Address} ! Msg
	end,
	send().