%% Author: Linnea
%% Created: 15 maj 2012
%% Description: TODO: Add description to randw
-module(randw).

%%
%% Include files
%%
-include_lib("eunit/include/eunit.hrl").
%%
%% Exported Functions
%%
-export([increaseAge/1, move/1, isHungry/1, isTooOld/1, isTooHungry/1, checkToDie/1]).
%% -compile(exportall).

-record(rabbit, {age=0, hunger=0, x=none, y=none, spid=none}).
-record(wolf, {age=0, hunger=0, x=none, y=none, spid=none}).
%% @type wolf(). A record representing a wolf.
%% @type rabbit(). A record representing a rabbit.
%% @type creature(). A record for either a wolf() or rabbit()

%% 
%% @doc Increases Rabbit's/Wolf's age by 1. The "Atom" must be either rabbor or wolf
%% @spec increaseAge({Atom::atom(), Creature::creature()})->Creature::creature()

increaseAge({Atom, Creature}) ->
	case Atom of
		rabbit ->
			Creature#rabbit{age = Creature#rabbit.age + 1};
		wolf ->
			Creature#wolf{age = Creature#wolf.age + 1}
	end.

%% 
%% @doc Updates Rabbit's/Wolf's coordinates to {X, Y}.
%% @spec move({Atom::atom(), Creature::creature(), {X::integer(), Y::integer()}})->Creature::creature()

move({Atom, Creature, {X, Y}}) ->
	case Atom of
		rabbit ->
			PID = Creature#rabbit.spid,
			PID ! {move, self(), Creature#rabbit.age, Creature#rabbit.hunger, Creature#rabbit.x, Creature#rabbit.y, X, Y },
			receive
				{death}->
					exit(killed);
				{yes} ->
					Creature#rabbit{x = X, y = Y};
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
					
					Creature#wolf{x = X, y = Y};
				{no} ->
					Creature
			end
	end.


%% 
%% @doc checks if the creature is hungry
%% @spec isHungry({Atom::atom(), Creature::creature()})->boolean()

isHungry({Atom, Creature}) ->
	case Atom of
		rabbit ->
			Creature#rabbit.hunger > 0;
		wolf ->
			Creature#wolf.hunger > 0
	end.

%% 
%% @doc checks if the creature is to old
%% @spec isTooOld({Atom::atom(), Creature::creature()})->boolean()

isTooOld({Atom, Creature}) ->
	case Atom of
		rabbit ->
			Creature#rabbit.age >= 70;
		wolf ->
			Creature#wolf.age >= 70
	end.

%% 
%% @doc checks if the creature is to hungry
%% @spec isTooHungry({Atom::atom(), Creature::creature()})->boolean()

isTooHungry({Atom, Creature}) ->
	case Atom of 
		rabbit ->
			Creature#rabbit.hunger >= 5;
		wolf ->
			Creature#wolf.hunger >= 30
	end.

%% 
%% @doc checks if the creature is too old or to hungry
%% @spec checkToDie({Atom::atom(),Creature::creature()})->boolean()

checkToDie({Atom, Creature}) ->
	case isTooOld({Atom, Creature}) or isTooHungry({Atom, Creature}) of
		true ->
			true;
		false ->
		 	false
	end.
%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% TESTS %%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%
%%@doc
%%@hidden
isTooOld_test()->
	Wolf = #wolf{age = 0, hunger = 0, x = 2, y = 2, spid = self()},
	Wolf2 = #wolf{age = 69, hunger = 0, x = 2, y = 2, spid = self()},
	Wolf3 = #wolf{age = 70, hunger = 0, x = 2, y = 2, spid = self()},
	Wolf4 = #wolf{age = 71, hunger = 0, x = 2, y = 2, spid = self()},
	
	Rabbit = #rabbit{age = 0, hunger = 0, x = 2, y = 2, spid = self()},
	Rabbit2 = #rabbit{age = 69, hunger = 0, x = 2, y = 2, spid = self()},
	Rabbit3 = #rabbit{age = 70, hunger = 0, x = 2, y = 2, spid = self()},
	Rabbit4 = #rabbit{age = 71, hunger = 0, x = 2, y = 2, spid = self()},
	
	WResponse = isTooOld({wolf, Wolf}),
	WResponse2 = isTooOld({wolf, Wolf2}),
	WResponse3 = isTooOld({wolf, Wolf3}),
	WResponse4 = isTooOld({wolf, Wolf4}),
	
	RResponse = isTooOld({rabbit, Rabbit}),
	RResponse2 = isTooOld({rabbit, Rabbit2}),
	RResponse3 = isTooOld({rabbit, Rabbit3}),
	RResponse4 = isTooOld({rabbit, Rabbit4}),
	
	?assert(WResponse == false),
	?assert(WResponse2 == false),
	?assert(WResponse3 == true),
	?assert(WResponse4 == true),
	
	?assert(RResponse == false),
	?assert(RResponse2 == false),
	?assert(RResponse3 == true),
	?assert(RResponse4 == true).
%%@doc
%%@hidden
isTooHungry_test()->
	Wolf = #wolf{age = 0, hunger = 0, x = 2, y = 2, spid = self()},
	Wolf2 = #wolf{age = 69, hunger = -10, x = 2, y = 2, spid = self()},
	Wolf3 = #wolf{age = 70, hunger = 30, x = 2, y = 2, spid = self()},
	Wolf4 = #wolf{age = 71, hunger = 100, x = 2, y = 2, spid = self()},
	
	Rabbit = #rabbit{age = 0, hunger = -10, x = 2, y = 2, spid = self()},
	Rabbit2 = #rabbit{age = 69, hunger = 0, x = 2, y = 2, spid = self()},
	Rabbit3 = #rabbit{age = 70, hunger = 5, x = 2, y = 2, spid = self()},
	Rabbit4 = #rabbit{age = 71, hunger = 125, x = 2, y = 2, spid = self()},
	
	WResponse = isTooHungry({wolf, Wolf}),
	WResponse2 = isTooHungry({wolf, Wolf2}),
	WResponse3 = isTooHungry({wolf, Wolf3}),
	WResponse4 = isTooHungry({wolf, Wolf4}),
	
	RResponse = isTooHungry({rabbit, Rabbit}),
	RResponse2 = isTooHungry({rabbit, Rabbit2}),
	RResponse3 = isTooHungry({rabbit, Rabbit3}),
	RResponse4 = isTooHungry({rabbit, Rabbit4}),

	?assert(WResponse == false),
	?assert(WResponse2 == false),
	?assert(WResponse3 == true),
	?assert(WResponse4 == true),
	
	?assert(RResponse == false),
	?assert(RResponse2 == false),
	?assert(RResponse3 == true),
	?assert(RResponse4 == true).
%%@doc
%%@hidden
increaseAge_test()->
	Wolf = #wolf{age = 0, hunger = 0, x = 2, y = 2, spid = self()},
	Wolf2 = #wolf{age = 69, hunger = -10, x = 2, y = 2, spid = self()},
	Wolf3 = #wolf{age = 70, hunger = 30, x = 2, y = 2, spid = self()},
	Wolf4 = #wolf{age = 71, hunger = 100, x = 2, y = 2, spid = self()},
	
	Rabbit = #rabbit{age = 0, hunger = -10, x = 2, y = 2, spid = self()},
	Rabbit2 = #rabbit{age = 69, hunger = 0, x = 2, y = 2, spid = self()},
	Rabbit3 = #rabbit{age = 70, hunger = 5, x = 2, y = 2, spid = self()},
	Rabbit4 = #rabbit{age = 71, hunger = 125, x = 2, y = 2, spid = self()},
	
	WResponse = increaseAge({wolf, Wolf}),
	WResponse2 = increaseAge({wolf, Wolf2}),
	WResponse3 = increaseAge({wolf, Wolf3}),
	WResponse4 = increaseAge({wolf, Wolf4}),
	
	RResponse = increaseAge({rabbit, Rabbit}),
	RResponse2 = increaseAge({rabbit, Rabbit2}),
	RResponse3 = increaseAge({rabbit, Rabbit3}),
	RResponse4 = increaseAge({rabbit, Rabbit4}),

	?assert(WResponse#wolf.age == 1),
	?assert(WResponse2#wolf.age == 70),
	?assert(WResponse3#wolf.age == 71),
	?assert(WResponse4#wolf.age == 72),
	
	?assert(RResponse#rabbit.age == 1),
	?assert(RResponse2#rabbit.age == 70),
	?assert(RResponse3#rabbit.age == 71),
	?assert(RResponse4#rabbit.age == 72).
%%@doc
%%@hidden
checkToDie_test()->
	Wolf = #wolf{age = 0, hunger = 0, x = 2, y = 2, spid = self()},
	Wolf2 = #wolf{age = 70, hunger = -10, x = 2, y = 2, spid = self()},
	Wolf3 = #wolf{age = 69, hunger = 30, x = 2, y = 2, spid = self()},
	Wolf4 = #wolf{age = 71, hunger = 100, x = 2, y = 2, spid = self()},
	
	Rabbit = #rabbit{age = 0, hunger = -10, x = 2, y = 2, spid = self()},
	Rabbit2 = #rabbit{age = 70, hunger = 0, x = 2, y = 2, spid = self()},
	Rabbit3 = #rabbit{age = 69, hunger = 5, x = 2, y = 2, spid = self()},
	Rabbit4 = #rabbit{age = 71, hunger = 125, x = 2, y = 2, spid = self()},
	
	WResponse = checkToDie({wolf, Wolf}),
	WResponse2 = checkToDie({wolf, Wolf2}),
	WResponse3 = checkToDie({wolf, Wolf3}),
	WResponse4 = checkToDie({wolf, Wolf4}),
	
	RResponse = checkToDie({rabbit, Rabbit}),
	RResponse2 = checkToDie({rabbit, Rabbit2}),
	RResponse3 = checkToDie({rabbit, Rabbit3}),
	RResponse4 = checkToDie({rabbit, Rabbit4}),

	?assert(WResponse == false),
	?assert(WResponse2 == true),
	?assert(WResponse3 == true),
	?assert(WResponse4 == true),
	
	?assert(RResponse == false),
	?assert(RResponse2 == true),
	?assert(RResponse3 == true),
	?assert(RResponse4 == true).
%%@doc
%%@hidden
isHungry_test()->
	Wolf = #wolf{age = 0, hunger = 0, x = 2, y = 2, spid = self()},
	Wolf2 = #wolf{age = 69, hunger = -10, x = 2, y = 2, spid = self()},
	Wolf3 = #wolf{age = 70, hunger = 30, x = 2, y = 2, spid = self()},
	Wolf4 = #wolf{age = 71, hunger = 100, x = 2, y = 2, spid = self()},
	
	Rabbit = #rabbit{age = 0, hunger = -10, x = 2, y = 2, spid = self()},
	Rabbit2 = #rabbit{age = 69, hunger = 0, x = 2, y = 2, spid = self()},
	Rabbit3 = #rabbit{age = 70, hunger = 5, x = 2, y = 2, spid = self()},
	Rabbit4 = #rabbit{age = 71, hunger = 125, x = 2, y = 2, spid = self()},
	
	WResponse = isHungry({wolf, Wolf}),
	WResponse2 = isHungry({wolf, Wolf2}),
	WResponse3 = isHungry({wolf, Wolf3}),
	WResponse4 = isHungry({wolf, Wolf4}),
	
	RResponse = isHungry({rabbit, Rabbit}),
	RResponse2 = isHungry({rabbit, Rabbit2}),
	RResponse3 = isHungry({rabbit, Rabbit3}),
	RResponse4 = isHungry({rabbit, Rabbit4}),

	?assert(WResponse == false),
	?assert(WResponse2 == false),
	?assert(WResponse3 == true),
	?assert(WResponse4 == true),
	
	?assert(RResponse == false),
	?assert(RResponse2 == false),
	?assert(RResponse3 == true),
	?assert(RResponse4 == true).

%%@doc
%%@hidden
move_test()->
	Wolf = #wolf{age = 0, hunger = 0, x = 2, y = 2, spid = self()},
	
	
	Rabbit = #rabbit{age = 0, hunger = -10, x = 2, y = 2, spid = self()},
	
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	
	self() ! {no},
	
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	
	self() ! {yes},
	self() ! {yes},
	self() ! {yes},
	
	self() ! {no},
	
	WResp = move({wolf, Wolf, {0,0}}),
	WResp2 = move({wolf, Wolf, {1,0}}),
	WResp3 = move({wolf, Wolf, {2,0}}),
	WResp4 = move({wolf, Wolf, {3,0}}),
	WResp5 = move({wolf, Wolf, {4,0}}),
	WResp6 = move({wolf, Wolf, {0,1}}),
	WResp7 = move({wolf, Wolf, {1,1}}),
	WResp8 = move({wolf, Wolf, {2,1}}),
	WResp9 = move({wolf, Wolf, {3,1}}),
	WResp10 = move({wolf, Wolf, {4,1}}),
	WResp11 = move({wolf, Wolf, {0,2}}),
	WResp12 = move({wolf, Wolf, {1,2}}),
	WResp13 = move({wolf, Wolf, {2,2}}),
	WResp14 = move({wolf, Wolf, {3,2}}),
	WResp15 = move({wolf, Wolf, {4,2}}),
	WResp16 = move({wolf, Wolf, {0,3}}),
	WResp17 = move({wolf, Wolf, {1,3}}),
	WResp18 = move({wolf, Wolf, {2,3}}),
	WResp19 = move({wolf, Wolf, {3,3}}),
	WResp20 = move({wolf, Wolf, {4,3}}),
	WResp21 = move({wolf, Wolf, {0,4}}),
	WResp22 = move({wolf, Wolf, {1,4}}),
	WResp23 = move({wolf, Wolf, {2,4}}),
	WResp24 = move({wolf, Wolf, {3,4}}),
	WResp25 = move({wolf, Wolf, {4,4}}),
	WResp26 = move({wolf, Wolf, {3,3}}),
	

	RResp1 = move({rabbit, Rabbit, {1,1}}),
	RResp2 = move({rabbit, Rabbit, {2,1}}),
	RResp3 = move({rabbit, Rabbit, {3,1}}),

	RResp4 = move({rabbit, Rabbit, {1,2}}),
	RResp5 = move({rabbit, Rabbit, {2,2}}),
	RResp6 = move({rabbit, Rabbit, {3,2}}),

	RResp7 = move({rabbit, Rabbit, {1,3}}),
	RResp8 = move({rabbit, Rabbit, {2,3}}),
	RResp9 = move({rabbit, Rabbit, {3,3}}),
	
	RResp10 = move({rabbit, Rabbit, {3,3}}),

	?assert({WResp#wolf.x, WResp#wolf.y} == {0,0}),
	?assert({WResp2#wolf.x, WResp2#wolf.y} == {1,0}),
	?assert({WResp3#wolf.x, WResp3#wolf.y} == {2,0}),
	?assert({WResp4#wolf.x, WResp4#wolf.y} == {3,0}),
	?assert({WResp5#wolf.x, WResp5#wolf.y} == {4,0}),
	
	?assert({WResp6#wolf.x, WResp6#wolf.y} == {0,1}),
	?assert({WResp7#wolf.x, WResp7#wolf.y} == {1,1}),
	?assert({WResp8#wolf.x, WResp8#wolf.y} == {2,1}),
	?assert({WResp9#wolf.x, WResp9#wolf.y} == {3,1}),
	?assert({WResp10#wolf.x, WResp10#wolf.y} == {4,1}),
	
	?assert({WResp11#wolf.x, WResp11#wolf.y} == {0,2}),
	?assert({WResp12#wolf.x, WResp12#wolf.y} == {1,2}),
	?assert({WResp13#wolf.x, WResp13#wolf.y} == {2,2}),
	?assert({WResp14#wolf.x, WResp14#wolf.y} == {3,2}),
	?assert({WResp15#wolf.x, WResp15#wolf.y} == {4,2}),
	
	?assert({WResp16#wolf.x, WResp16#wolf.y} == {0,3}),
	?assert({WResp17#wolf.x, WResp17#wolf.y} == {1,3}),
	?assert({WResp18#wolf.x, WResp18#wolf.y} == {2,3}),
	?assert({WResp19#wolf.x, WResp19#wolf.y} == {3,3}),
	?assert({WResp20#wolf.x, WResp20#wolf.y} == {4,3}),
	
	?assert({WResp21#wolf.x, WResp21#wolf.y} == {0,4}),
	?assert({WResp22#wolf.x, WResp22#wolf.y} == {1,4}),
	?assert({WResp23#wolf.x, WResp23#wolf.y} == {2,4}),
	?assert({WResp24#wolf.x, WResp24#wolf.y} == {3,4}),
	?assert({WResp25#wolf.x, WResp25#wolf.y} == {4,4}),
	
	?assert(WResp26 == Wolf),

	?assert({RResp1#rabbit.x, RResp1#rabbit.y} == {1,1}),
	?assert({RResp2#rabbit.x, RResp2#rabbit.y} == {2,1}),
	?assert({RResp3#rabbit.x, RResp3#rabbit.y} == {3,1}),

	?assert({RResp4#rabbit.x, RResp4#rabbit.y} == {1,2}),
	?assert({RResp5#rabbit.x, RResp5#rabbit.y} == {2,2}),
	?assert({RResp6#rabbit.x, RResp6#rabbit.y} == {3,2}),

	?assert({RResp7#rabbit.x, RResp7#rabbit.y} == {1,3}),
	?assert({RResp8#rabbit.x, RResp8#rabbit.y} == {2,3}),
	?assert({RResp9#rabbit.x, RResp9#rabbit.y} == {3,3}),
	
	?assert(RResp10 == Rabbit).
	