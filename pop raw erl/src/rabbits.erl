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
-export([loop/1, test/0, new/1, mate/0, move/2, checkMate/1]).


%% 
%% Defining a rabbit record
%% 
-record(rabbit, {age=0, hunger=0, x=none, y=none}).

%% 
%% Spwans a new rabbit process with info in record
%% 

new({X, Y}) ->
	spawn(rabbits, loop, [#rabbit{age = 0, hunger = 0, x = X, y = Y}]).
	
%% 
%% 
%% 

increaseAge(Rabbit) ->
	Rabbit#rabbit{age = Rabbit#rabbit.age + 1}.

%% 
%% 
%% 

move(Rabbit, {X, Y}) ->
	Rabbit#rabbit{x = X, y = Y}.

%% 
%% 
%% 

%% eat(Rabbit, Value) ->
%% 	kolla att hunger och ålder inte är för stora/små?
%% 	case canEat() of
%% 	true ->
%% 		NewHunger = Rabbit#rabbit.hunger - 1;
%% 	false ->
%% 		NewHunger = Rabbit#rabbit.hunger + 1
%% 	end,
%% 	NewHunger = Rabbit#rabbit.hunger - 1,
%% 	Value - 1,
%% 	Rabbit.

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
	Rabbit#rabbit.hunger /= 0.

%% 
%% 
%% 

isOld(Rabbit) ->
	Rabbit#rabbit.age > 70.

%% 
%% 
%% 

doTick(Rabbit) ->
	NewAge = Rabbit#rabbit.age + 1,
	case isHungry(Rabbit) of
		true ->
			% Call some EAT-function here :)
			% Simple temporary workaround (starve)
			NewHunger = Rabbit#rabbit.hunger - 1;
		false ->
			NewHunger = Rabbit#rabbit.hunger + 1
	end,
	Rabbit#rabbit{age = NewAge, hunger = NewHunger}.

%% 
%% 
%% 

loop(Rabbit) ->
	receive
		{_, tick} ->
			loop(doTick(Rabbit));
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

test() ->
	KaninPid = new({1, 1}),
	sendTick(KaninPid),
	sendTick(KaninPid),
	sendTick(KaninPid),
	{X, Y} = getCoords(KaninPid),
	RabbitInfo = getInfo(KaninPid),
	io:format("Rabbit (~w)~nAge: ~w~nHunger: ~w~n", [KaninPid, RabbitInfo#rabbit.age, RabbitInfo#rabbit.hunger]),
	io:format("x: ~w~ny: ~w~n", [X, Y]),
	execute(KaninPid),
	test_finished.