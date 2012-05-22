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
-export([increaseAge/1, move/1, mate/0, checkMate/1, isHungry/1, isTooOld/1, isTooHungry/1, checkToDie/1]).
%% -compile(exportall).

-record(rabbit, {age=0, hunger=0, x=none, y=none, spid=none}).
-record(wolf, {age=0, hunger=0, x=none, y=none, spid=none}).

%% 
%% @doc Increases Rabbit's/Wolf's age by 1.
%% 

increaseAge({Atom, Creature}) ->
	case Atom of
		rabbit ->
			Creature#rabbit{age = Creature#rabbit.age + 1};
		wolf ->
			Creature#wolf{age = Creature#wolf.age + 1, hunger = Creature#wolf.hunger-1}
	end.

%% 
%% @doc Updates Rabbit's/Wolf's coordinates to {X, Y}.
%% 

move({Atom, Creature, {X, Y}}) ->
	case Atom of
		rabbit ->
			PID = Creature#rabbit.spid,
			PID ! {move, self(), Creature#rabbit.age, Creature#rabbit.hunger, Creature#rabbit.x, Creature#rabbit.y, X, Y },
			receive
				{death}->
					exit(killed);
				{yes} ->
					NewRabbit = Creature#rabbit{x = X, y = Y};
				{no} ->
					Creature
			end;
		wolf ->
			PID = Creature#wolf.spid,
			PID ! {wolfMove, self(), Creature#wolf.age, Creature#wolf.hunger, Creature#wolf.x, Creature#wolf.y, X, Y },
			receive
				{death}->
					exit(killed);
				{yes} ->
					
					NewWolf = Creature#wolf{x = X, y = Y};
				{no} ->
					Creature
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

isHungry({Atom, Creature}) ->
	case Atom of
		rabbit ->
			Creature#rabbit.hunger > 0;
		wolf ->
			Creature#wolf.hunger > 0
	end.

%% 
%% 
%% 

isTooOld({Atom, Creature}) ->
	case Atom of
		rabbit ->
			Creature#rabbit.age >= 70;
		wolf ->
			Creature#wolf.age >= 100
	end.

%% 
%% 
%% 

isTooHungry({Atom, Creature}) ->
	case Atom of 
		rabbit ->
			Creature#rabbit.hunger >= 5;
		wolf ->
			Creature#wolf.hunger >= 20
	end.

%% 
%% 
%% 

checkToDie({Atom, Creature}) ->
	case isTooOld({Atom, Creature}) or isTooHungry({Atom, Creature}) of
		true ->
			true;
		false ->
		 	false
	end.
