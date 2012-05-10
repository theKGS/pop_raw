%% Author: Linnea
%% Created: 3 maj 2012
%% Description: TODO: Add description to rabbits
-module(rabbits).

%%
%% Include files
%%

%%
%% Exported Functions
%%
-export([preloop/1, loop/1, test/0, mate/0, move/2, checkMate/1, new/2]).

%% 
%% Defining a rabbit record
%% 
-record(rabbit, {age=0, hunger=0, x=none, y=none, spid=none}).

%% 
%% @doc Spwans a new rabbit process.
%% 

new({X, Y}, SenderPID) ->
	SenderPID ! {new, self(), X, Y},
	receive
		start ->
			spawn(rabbits, preloop, [#rabbit{age = 0, hunger = 0, x = X, y = Y, spid = SenderPID}])
	end.
		
%% 
%% @doc Increases Rabbit's age by 1.
%% 

 increaseAge(Rabbit) ->
	Rabbit#rabbit{age = Rabbit#rabbit.age + 1}.

%% 
%% @doc Updates Rabbit's coordinates to {X, Y}.
%% 

move(Rabbit, {X, Y}) ->
	Rabbit#rabbit{x = X, y = Y}.

%% 
%% @doc Finds a new random square for Rabbit, left/right/up/down compared to Rabbit's current coordinate.  
%% 

findNewSquare(Rabbit) ->
	Dir = random:uniform(4),
	{X, Y} = {Rabbit#rabbit.x, Rabbit#rabbit.y},
	case Dir of
		1 ->
			{X2, Y2} = {X - 1, Y};
		2 ->
			{X2, Y2} = {X, Y - 1};
		3 ->
			{X2, Y2} = {X + 1, Y};
		4 ->
			{X2, Y2} = {X, Y + 1}
	end,
	move(Rabbit, {X2, Y2}).

%% 	PID = Rabbit#rabbit.spid,
%% 	PID ! {move, self(), Rabbit#rabbit.age, Rabbit#rabbit.hunger, X, Y, X2, Y2},
%% 	receive
%% 		yes ->
%% 			move(Rabbit, {X2, Y2});
%% 		no ->
%% 			findNewSquare(Rabbit)
%% 	end.

							
%% 
%% 
%% 

eat(Rabbit) ->
	{Grass, _} = getMap(),
%% 	Grass = 5,
%% 	{X, Y} = {Rabbit#rabbit.x, Rabbit#rabbit.y},
	case isHungry(Rabbit) and (Grass > 0) of
		true ->
			{Rabbit#rabbit{hunger = Rabbit#rabbit.hunger - 1}, gotFood};
		false ->
			{Rabbit#rabbit{hunger = Rabbit#rabbit.hunger + 1}, noFood}
	end.
	

%% 
%% 
%% 

mate() -> 
	P = random:uniform(10),
	if
		P >= 3 ->
			true;
		P < 3 ->
			false
	end.

%% 
%% 
%% 

checkMate(Rabbit) ->
	Hunger = Rabbit#rabbit.hunger,
	Age = Rabbit#rabbit.age,
	if
		(Age >= 7) and (Hunger =< 3) ->
			true;
		true ->
			false
	end.

%% 
%% 
%% 

isHungry(Rabbit) ->
	Rabbit#rabbit.hunger > 0.

%% 
%% 
%% 

isTooOld(Rabbit) ->
	Rabbit#rabbit.age >= 70.

%% 
%% 
%% 

isTooHungry(Rabbit) ->
	Rabbit#rabbit.hunger >= 5.

%% 
%% 
%% 

getMap() ->
	receive
		{map, [H|L]} ->
			{Grass, Type} = lists:nth(5, [H|L]),
			{Grass, Type}
	end.
	
%% 	PID = Rabbit#rabbit.spid,
%% 	PID ! {get, self()},
%% 	receive
%% 		{map, Array} ->
%% 			Array
%% 	end.


checkToDie(Rabbit) ->
	case isTooOld(Rabbit) or isTooHungry(Rabbit) of
		true ->
			true;
		false ->
		 	false
	end.

%% 
%% 
%% 

doTick(Rabbit) ->
	Rabbit2 = increaseAge(Rabbit),
 	PID = Rabbit#rabbit.spid,
	{Rabbit3, Ate} = eat(Rabbit2),
	if
		Ate == gotFood ->
 			PID ! {eat, self(), Rabbit3#rabbit.age, Rabbit3#rabbit.hunger, Rabbit3#rabbit.x, Rabbit3#rabbit.y},
			Rabbit3;
		true ->
			findNewSquare(Rabbit3)
	end.

%% 
%% 
%% 

preloop(Rabbit) ->
	init(),
	loop(Rabbit).

loop(Rabbit) ->
	receive
		{_, tick} ->
			Rabbit2 = doTick(Rabbit),
			case checkToDie(Rabbit2) of
				true ->
					exit(died);
				false ->
					loop(Rabbit2)
			end;
		{Sender, getInfo} ->
			Sender ! {self(), Rabbit},
			loop(Rabbit);
		{Sender, getCoords} ->
			Sender ! {self(), {Rabbit#rabbit.x, Rabbit#rabbit.y}},
			loop(Rabbit);
		{_, die} ->
			exit(killed)
	end.

%% 
%% 
%% 

sendTick(Pid) ->
	Pid ! {self(), tick}.

getInfo(Pid) ->
	Pid ! {self(), getInfo},
	receive
		{Pid, Rabbit} ->
			Rabbit
	end.

getCoords(Pid) ->
	Pid ! {self(), getCoords},
	receive
		{Pid, Coords} ->
			Coords
	end.
	
execute(Pid) ->
	Pid ! {self(), die}.


init() ->
	{A1, A2, A3} = now(),
	Mega = lists:nth(2, pid_to_list(self())),
	random:seed(A1 + Mega, A2 + Mega, A3 + Mega).

test() ->
	init(),
	KaninPid = new({5, 5}, self()),
	sendTick(KaninPid),
	sendTick(KaninPid),
	sendTick(KaninPid),
	sendTick(KaninPid),
	sendTick(KaninPid),
	sendTick(KaninPid),
	sendTick(KaninPid),
	sendTick(KaninPid),
	sendTick(KaninPid),
	sendTick(KaninPid),
	sendTick(KaninPid),
	sendTick(KaninPid),
	sendTick(KaninPid),
	sendTick(KaninPid),
	{X, Y} = getCoords(KaninPid),
 	RabbitInfo = getInfo(KaninPid),
	io:format("Rabbit (~w)~nAge: ~w~nHunger: ~w~n", [KaninPid, RabbitInfo#rabbit.age, RabbitInfo#rabbit.hunger]),
	io:format("x: ~w~ny: ~w~n", [X, Y]),
	execute(KaninPid),
	test_finished.


%% out som atom för vargarna när de ska äta om ruta är utanför kartan, tex hörn och kanter
%% istället för tick, när jag väntar matchning mot death osv, så kör en after och då börjar jag loopa igen så ny "doTick" startar