-module(jint_send).

-export([setup/0, send/1]).

%% ----------------------------------------------------------------------------
%% @doc The sending part of the send/receive pair. This spawns the receive part
%%		and sends the ping to {sparta, Address} where Address is the atom
%%		athens@Computername where Computername is the name of the computer.
%% @specc setup() -> none()
%% @end
%% ----------------------------------------------------------------------------
setup() ->
	Node = erlang:atom_to_list(athens@),
	Host = net_adm:localhost(),
	Address = erlang:list_to_atom(lists:append(Node, Host)),
	PidSelf = spawn(jint_send, send, [Address]),
	Pid = spawn(jint_rec, setup, [PidSelf]),
	sendPid(net_adm:ping(Address), Address, Pid).

%% ----------------------------------------------------------------------------
%% @doc Funktion to send the pid Java will communicate with. This function will
%%		ping the Java node and will continuing doing this untill it succeeds.
%%		When it's successfull it will send 1 message containing the pid Java 
%%		should communicate with.
%% @specc sendPid(atom(), Address::atom(), Pid::pid())
%% @end
%% ----------------------------------------------------------------------------
sendPid(pong, Address, Pid) ->
	{sparta, Address} ! {Pid};
sendPid(pang, Address, Pid) ->
	timer:sleep(10),
	sendPid(net_adm:ping(Address), Address, Pid).

%% ----------------------------------------------------------------------------
%% @doc Receives a message from the Erlang side and translates it accordingly
%%		and send is to the Java side.
%% @specc send() -> none()
%% @end
%% ----------------------------------------------------------------------------
send(Address) ->
	receive
		%% rabbitMap = 0
		{rabbitMap, A, B, C} ->
			{sparta, Address} ! {0, A, B, C};
		%% rabbitEat = 1
		{rabbitEat,A,B,C,D,E} ->
			{sparta, Address} ! {1, A, B, C, D, E};
		%% move = 2
		{move, A, B, C, D, E, F, G} ->
			{sparta, Address} ! {2, A, B, C, D, E, F, G};
		%% newRabbit = 4
		{newRabbit, A, B, C} ->
			{sparta, Address} ! {4, A, B, C};
		%% death = 5
		{death, A, B, C} ->
			{sparta, Address} ! {5, A, B, C};
		%% wolfEat = 6
		{wolfEat, A, B, C, D, E, F, G} ->
			{sparta, Address} ! {6, A, B, C, D, E, F, G};
		%% wolfMap = 7
		{wolfMap, A, B, C} ->
			{sparta, Address} ! {7, A, B, C};
		%% newWolf = 8
		{newWolf, A, B, C} ->
			{sparta, Address} ! {8, A, B, C};
		%% wolfMove = 13
		{wolfMove, A, B, C, D, E, F, G} ->
			{sparta, Address} ! {13, A, B, C, D, E, F, G}
	end,
	send(Address).

%% @type none(). Does not return anything.