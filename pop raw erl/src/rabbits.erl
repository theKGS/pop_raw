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
-export([loop/1, test/0, mate/0, move/2, checkMate/1, new/2]).


%% 
%% Defining a rabbit record
%% 
%% -record(rabbit, {age=0, hunger=0, x=none, y=none}).
-record(rabbit2, {age=0, hunger=0, x=none, y=none, spid=none}).


%% 
%% Spwans a new rabbit process with info in record
%% 

%% new({X, Y}) ->
%% 	spawn(rabbits, loop, [#rabbit{age = 0, hunger = 0, x = X, y = Y}]).


new({X, Y}, SenderPID) ->
	spawn(rabbits, loop, [#rabbit2{age = 0, hunger = 0, x = X, y = Y, spid = SenderPID}]).


		
%% 
%% 
%% 

%% increaseAge(Rabbit) ->
%% 	Rabbit#rabbit{age = Rabbit#rabbit.age + 1}.

%% 
%% 
%% 

move(Rabbit, {X, Y}) ->
	Rabbit#rabbit2{x = X, y = Y}.


findNewSquare(Rabbit) ->
	Dir = random:uniform(4),
	{X, Y} = {X, Y} = {Rabbit#rabbit2.x, Rabbit#rabbit2.y},
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
	Map = getMap(Rabbit),
	PID = Rabbit#rabbit2.spid,
	PID ! {move, self(), X, Y, X2, Y2},
	receive
		yes ->
			move(Rabbit, {X2, Y2});
		no ->
			findNewSquare(Rabbit)
	end.

							
%% 
%% 
%% 

eat(Rabbit) ->
	Map = getMap(Rabbit),
	{X, Y} = {Rabbit#rabbit2.x, Rabbit#rabbit2.y},
	{Grass, _, _} = array:get(Y,array:get(X, Map)),
	case isHungry(Rabbit) and (Grass > 0) of
		true ->
			Rabbit#rabbit2{hunger = Rabbit#rabbit2.hunger - 1},
			{X, Y, food};
		false ->
			{X, Y, noFood}
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
	Hunger = Rabbit#rabbit2.hunger,
	Age = Rabbit#rabbit2.age,
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
	Rabbit#rabbit2.hunger /= 0.

%% 
%% 
%% 

isTooOld(Rabbit) ->
	Rabbit#rabbit2.age >= 70.

%% 
%% 
%% 

isTooHungry(Rabbit) ->
	Rabbit#rabbit2.hunger >= 5.

%% 
%% 
%% 


getMap(Rabbit) ->
	PID = Rabbit#rabbit2.spid,
	PID ! {get, self()},
	receive
		{map, Array} ->
			Array
	end.

%% 
%% 
%% 

doTick(Rabbit) ->
	NewAge = Rabbit#rabbit2.age + 1,
	case isHungry(Rabbit) of
		true ->
			{X, Y, Z} = eat(Rabbit),
			if
				Z == food ->
					tbi;
					%% send {X, Y} to map, map will decrease grass level
				true ->
					findNewSquare(Rabbit)							
			end,
			% Simple temporary workaround (starve)
			NewHunger = Rabbit#rabbit2.hunger - 1;
		false ->
			NewHunger = Rabbit#rabbit2.hunger + 1,
			findNewSquare(Rabbit)
 			
	end,
	case isTooOld(Rabbit) or isTooHungry(Rabbit) of
		true ->
			exit(killed);
		false ->
		 	Rabbit#rabbit2{age = NewAge, hunger = NewHunger}
	end.

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
			Sender ! {self(), {Rabbit#rabbit2.x, Rabbit#rabbit2.y}},
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
	KaninPid = new({1, 1}, self()),
	sendTick(KaninPid),
	sendTick(KaninPid),
	sendTick(KaninPid),
	{X, Y} = getCoords(KaninPid),
	RabbitInfo = getInfo(KaninPid),
	io:format("Rabbit (~w)~nAge: ~w~nHunger: ~w~n", [KaninPid, RabbitInfo#rabbit2.age, RabbitInfo#rabbit2.hunger]),
	io:format("x: ~w~ny: ~w~n", [X, Y]),
	execute(KaninPid),
	test_finished.
