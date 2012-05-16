%% Author: Linnea
%% Created: 3 maj 2012
%% Description: TODO: Add description to rabbits
-module(wolves).

%%
%% Include files
%%

%%
%% Exported Functions
%%
-export([preloop/1, loop/1, test/0, newRabbit/2]).

%% 
%% Defining a rabbit record
%% 
-record(wolf, {age=0, hunger=0, x=none, y=none, spid=none}).

%% 
%% @doc Spwans a new rabbit process.
%% 

newWolf({X, Y}, SenderPID) ->
	PID = spawn(wolves, preloop, [#wolf{age = 0, hunger = 0, x = X, y = Y, spid = SenderPID}]),
	SenderPID ! {new, PID, X, Y}.
		



%% 
%% @doc Finds a new random square for Rabbit, left/right/up/down compared to Rabbit's current coordinate.  
%% 

findNewSquare(Rabbit, MapList) ->
	{Length, PossibleSquares} = maxGrass(parseList(Rabbit, MapList, 1, []),[],0,0),
	if(Length =/= 0)->
		Dir = random:uniform(Length),
		{_,X2,Y2} = lists:nth(Dir, PossibleSquares),
		randw:move({rabbit, Rabbit, {X2, Y2}});
	  true ->
		  loop(Rabbit)
	end.

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
	{Grass, _, List} = getMap(Rabbit),
%% 	Grass = 5,
%% 	{X, Y} = {Rabbit#rabbit.x, Rabbit#rabbit.y},
	case randw:isHungry({rabbit, Rabbit}) and (Grass > 0) of
		true ->
			{Rabbit#rabbit{hunger = Rabbit#rabbit.hunger - 1}, gotFood, List};
		false ->
			{Rabbit#rabbit{hunger = Rabbit#rabbit.hunger + 1}, noFood, List}
	end.
	

%% 
%% 
%% 

getMap(Rabbit) ->
	Rabbit#rabbit.spid ! {get, self(), Rabbit#rabbit.x, Rabbit#rabbit.y},
	receive
		{map, List} ->
			{Grass, Type} = lists:nth(5, List),
			{Grass, Type, List}
	end.
	
%% 	PID = Rabbit#rabbit.spid,
%% 	PID ! {get, self()},
%% 	receive
%% 		{map, Array} ->
%% 			Array
%% 	end.




doTick(Wolf) ->
	Wolf2 = randw:increaseAge({wolf, Wolf}),
 	PID = Wolf#wolf.spid,
	{Wolf3, Ate, List} = eat(Wolf2),
	if
		Ate == gotFood ->
 			PID ! {eat, self(), Wolf3#wolf.age, Wolf3#wolf.hunger, Wolf3#wolf.x, Wolf3#wolf.y},
			Wolf3;
		true ->
			findNewSquare(Wolf3, List)
	end.

%% 
%% 
%% 

preloop(Wolf) ->
	receive
		start ->
			init(),
			loop(Wolf)
	end.

loop(Wolf) ->
	receive
		{Sender, getInfo} ->
			Sender ! {self(), Wolf},
			loop(Wolf);
		{Sender, getCoords} ->
			Sender ! {self(), {Wolf#wolf.x, Wolf#wolf.y}},
			loop(Wolf);
		{_, die} ->
			exit(killed)
		after 200 ->
 			case randw:checkToDie({rabbit, Rabbit}) of
 				true ->
					PID = Rabbit#rabbit.spid,
					PID ! {death, self(), Rabbit#rabbit.x, Rabbit#rabbit.y},
 					exit(died);
 				false ->
 					Rabbit2 = doTick(Rabbit),
					loop(Rabbit2)
 			end
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
	KaninPid = newRabbit({5, 5}, self()),
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