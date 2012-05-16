%% Author: Linnea
%% Created: 15 maj 2012
%% Description: TODO: Add description to randw
-module(randw).

%%
%% Include files
%%

%%
%% Exported Functions
%%
%% -export([preloop/1, loop/1, test/0, mate/0, move/2, checkMate/1, new/2]).
-compile(exportall).

-record(rabbit, {age=0, hunger=0, x=none, y=none, spid=none}).
-record(wolf, {age=0, hunger=0, x=none, y=none, spid=none}).

%% 
%% @doc Increases Rabbit's/Wolf's age by 1.
%% 

increaseAge({atom, Creature}) ->
	case atom of
		rabbit ->
			Creature#rabbit{age = Creature#rabbit.age + 1};
		wolf ->
			Creature#wolf{age = Creature#wolf.age + 1}
	end.

%% 
%% @doc Updates Rabbit's/Wolf's coordinates to {X, Y}.
%% 

move({atom, Creature, {X, Y}}) ->
	case atom of
		rabbit ->
			PID = Creature#rabbit.spid,
			PID ! {move, self(), Creature#rabbit.age, Creature#rabbit.hunger, Creature#rabbit.x, Creature#rabbit.y, X, Y },
			receive
				yes ->
					NewRabbit = Creature#rabbit{x = X, y = Y},
					rabbits:loop(NewRabbit);
				no ->
					rabbits:loop(Creature)
			end;
		wolf ->
			PID = Creature#wolf.spid,
			PID ! {move, self(), Creature#wolf.age, Creature#wolf.hunger, Creature#wolf.x, Creature#wolf.y, X, Y },
			receive
				yes ->
					NewWolf = Creature#wolf{x = X, y = Y},
					wolvess:loop(NewWolf);
				no ->
					wolves:loop(Creature)
			end
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

isHungry({atom, Creature}) ->
	case atom of
		rabbit ->
			Creature#rabbit.hunger > 0;
		wolf ->
			Creature#wolf.hunger > 0
	end.

%% 
%% 
%% 

isTooOld({atom, Creature}) ->
	case atom of
		rabbit ->
			Creature#rabbit.age >= 70;
		wolf ->
			Creature#wolf.age >= 40
	end.

%% 
%% 
%% 

isTooHungry({atom, Creature}) ->
	case atom of 
		rabbit ->
			Creature#rabbit.hunger >= 5;
		wolf ->
			Creature#wolf.hunger >= 10
	end.

%% 
%% 
%% 

checkToDie({atom, Creature}) ->
	case isTooOld({atom, Creature}) or isTooHungry({atom, Creature}) of
		true ->
			true;
		false ->
		 	false
	end.
