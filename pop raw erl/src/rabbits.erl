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
-export([newRabbit/2, parseList/4, maxGrass/4, findNewSquare/2, eat/1, getMap/1, doTick/1, preloop/1, loop/1, test/0, sendTick/1, getInfo/1, getCoords/1, execute/1, init/0]).
%% -compile(exportall).

%% 
%% Defining a rabbit record
%% 
-record(rabbit, {age=0, hunger=0, x=none, y=none, spid=none}).

%% 
%% @doc Spwans a new rabbit process.
%% 

newRabbit({X, Y}, SenderPID) ->
	PID = spawn(rabbits, preloop, [#rabbit{age = 0, hunger = 0, x = X, y = Y, spid = SenderPID}]),
	SenderPID ! {newRabbit, PID, X, Y}.
		


%% 
%% 
%% 

parseList(_,[],_,Acc)->
	Acc;
parseList(Rabbit, [{Grass, Type}|T], ListIndex, Acc) ->
	if Type =:= none ->
		   case ListIndex of
			   1->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x-1,Rabbit#rabbit.y-1}]++Acc);
			   2->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x,Rabbit#rabbit.y-1}]++Acc);
			   3->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x+1,Rabbit#rabbit.y-1}]++Acc);
			   4->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x-1,Rabbit#rabbit.y}]++Acc);
			   5->
				   parseList(Rabbit,T,ListIndex+1, Acc);
			   6->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x+1,Rabbit#rabbit.y}]++Acc);
			   7->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x-1,Rabbit#rabbit.y+1}]++Acc);
			   8->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x,Rabbit#rabbit.y+1}]++Acc);
			   9->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x+1,Rabbit#rabbit.y+1}]++Acc)
		   end;
	   Type =/= none ->
		   parseList(Rabbit, T, ListIndex+1, Acc)
	end.
	
%% 
%% 
%% 

maxGrass([], Acc, _CurrMax, Length) ->
	{Length, Acc};
maxGrass([{Grass, X, Y}|T], Acc, CurrMax, Length) ->
	if (Grass > CurrMax) ->
		   maxGrass(T, [{Grass,X,Y}], Grass, 1);
	   Grass == CurrMax ->
		   maxGrass(T, [{Grass,X,Y}]++Acc, CurrMax, Length+1);
	   Grass < CurrMax ->
		   maxGrass(T, Acc, CurrMax, Length)
	end.


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
	Rabbit#rabbit.spid ! {rabbitMap, self(), Rabbit#rabbit.x, Rabbit#rabbit.y},
	receive
		{rabbitMap, List} ->
			{Grass, Type} = lists:nth(5, List),
			{Grass, Type, List}
	end.
	
%% 	PID = Rabbit#rabbit.spid,
%% 	PID ! {get, self()},
%% 	receive
%% 		{map, Array} ->
%% 			Array
%% 	end.




doTick(Rabbit) ->
	Rabbit2 = randw:increaseAge({rabbit, Rabbit}),
 	PID = Rabbit#rabbit.spid,
	{Rabbit3, Ate, List} = eat(Rabbit2),
	if
		Ate == gotFood ->
 			PID ! {rabbitEat, self(), Rabbit3#rabbit.age, Rabbit3#rabbit.hunger, Rabbit3#rabbit.x, Rabbit3#rabbit.y},
			Rabbit3;
		true ->
			findNewSquare(Rabbit3, List)
	end.

%% 
%% 
%% 

preloop(Rabbit) ->
	receive	
		start ->
			io:format("Starting rabbit~n"),
			init(),
			loop(Rabbit)
	end.

loop(Rabbit) ->
	receive
		{Sender, getInfo} ->
			Sender ! {self(), Rabbit},
			loop(Rabbit);
		{Sender, getCoords} ->
			Sender ! {self(), {Rabbit#rabbit.x, Rabbit#rabbit.y}},
			loop(Rabbit);
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