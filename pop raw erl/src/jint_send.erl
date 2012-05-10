-module(jint_send).

-export([setup/0, send/0]).

setup() ->
	PidSelf = spawn(jint_send, send, []),
	Pid = spawn(jint_rec, setup, [PidSelf]),
	{sparta, athens@laptop} ! {Pid}.

send() ->
	Node = erlang:atom_to_list(athens@),
	Host = net_adm:localhost(),
	Address = erlang:list_to_atom(lists:append(Node, Host)),
	receive
		Msg ->
			%% TODO Fixa dynamiskt laptop argument
			{sparta, Address} ! Msg
	end,
	send().