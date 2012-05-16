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
	PID = spawn(rabbits, preloop, [#rabbit{age = 0, hunger = 0, x = X, y = Y, spid = SenderPID}]),
	SenderPID ! {new, PID, X, Y}.
		
%% 
%% @doc Increases Rabbit's age by 1.
%% 

 increaseAge(Rabbit) ->
	Rabbit#rabbit{age = Rabbit#rabbit.age + 1}.

%% 
%% @doc Updates Rabbit's coordinates to {X, Y}.
%% 

move(Rabbit, {X, Y}) ->
	PID = Rabbit#rabbit.spid,
	PID ! {move, self(), Rabbit#rabbit.age, Rabbit#rabbit.hunger, Rabbit#rabbit.x, Rabbit#rabbit.y, X, Y },
	receive
		yes ->
			NewRabbit = Rabbit#rabbit{x = X, y = Y},
			loop(NewRabbit);
		no ->
			loop(Rabbit)
	end.
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
		move(Rabbit, {X2, Y2});
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
	case isHungry(Rabbit) and (Grass > 0) of
		true ->
			{Rabbit#rabbit{hunger = Rabbit#rabbit.hunger - 1}, gotFood, List};
		false ->
			{Rabbit#rabbit{hunger = Rabbit#rabbit.hunger + 1}, noFood, List}
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
	{Rabbit3, Ate, List} = eat(Rabbit2),
	if
		Ate == gotFood ->
 			PID ! {eat, self(), Rabbit3#rabbit.age, Rabbit3#rabbit.hunger, Rabbit3#rabbit.x, Rabbit3#rabbit.y},
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
 			case checkToDie(Rabbit) of
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