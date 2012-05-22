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
-export([preloop/1, loop/1, newWolf/2]).

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

findNewSquare(Wolf, MapList) ->
	{Length, PossibleSquares} = maxGrass(parseList(Wolf, MapList, 1, []),[],0,0),
	if(Length =/= 0)->
		Dir = random:uniform(Length),
		{_,X2,Y2} = lists:nth(Dir, PossibleSquares),
		randw:move({wolf, Wolf, {X2, Y2}});
	  true ->
		  loop(Wolf)
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
foodAvailible(MapLis, Wolf)->
	tbi.


eat(Wolf, Map) ->
	
	
	
	
	{Grass, _, List} = getMap(Wolf),
%% 	Grass = 5,
%% 	{X, Y} = {Rabbit#rabbit.x, Rabbit#rabbit.y},
	case randw:isHungry({wolf, Wolf}) and (Grass > 0) of
		true ->
			{Wolf#wolf{hunger = Wolf#wolf.hunger - 1}, gotFood, List};
		false ->
			{Wolf#wolf{hunger = Wolf#wolf.hunger + 1}, noFood, List}
	end.
	

%% 
%% 
%% 

getMap(Wolf) ->
	Wolf#wolf.spid ! {wolfGet, self(), Wolf#wolf.x, Wolf#wolf.y},
	receive
		{map, List} ->
			List
	end.
	
%% 	PID = Rabbit#rabbit.spid,
%% 	PID ! {get, self()},
%% 	receive
%% 		{map, Array} ->
%% 			Array
%% 	end.


%%  1  2  3  4  5
%%  6  7  8  9 10
%% 11 12 13 14 15
%% 16 17 18 19 20
%% 21 22 23 24 25

listFolderHelper(Type, {AccEat, AccMove, X, Y, Wolf}, NextX, NextY) 
  when Type =:= rabbit->
	{[{Type,Wolf#wolf.x+X,Wolf#wolf.y+Y}]++AccEat, 
			 AccMove,NextX, NextY,Wolf};
listFolderHelper(Type, {AccEat, AccMove, X, Y, Wolf}, NextX, NextY) 
  when Type =:= none->
	{AccEat, 
			 [{Type,Wolf#wolf.x+X,Wolf#wolf.y+Y}]++AccMove,
			 NextX, NextY,Wolf};
listFolderHelper(Type, {AccEat, AccMove, X, Y, Wolf}, NextX, NextY)->
	{AccEat, AccMove, X, Y, Wolf}.

listFolder({_, Type}, {AccEat, AccMove, X, Y, Wolf}) 
  when X =:= 2->
	NextX = 0,
	NextY = Y+1,
	case Type of
		rabbit ->
			
		none ->
			
	end.


parseList(_,[],_,Acc)->
	Acc;
parseList(Wolf, [{_, Type}|T], ListIndex, AccEat, AccMove) ->
	case ListIndex of
		1->
			{NewAccEat, NewAccMove} = parseListHelper(
									{Type,Wolf#wolf.x-2,Wolf#wolf.y-2},
									AccEat, AccMove, Type),
			parseList(
			  Wolf,T,ListIndex+1,NewAccEat, NewAccMove,  Type);
		2->
			{NewAccEat, NewAccMove} = parseListHelper(
									{Type,Wolf#wolf.x-1,Wolf#wolf.y-2},
									AccEat, AccMove, Type),
		   	parseList(
			 Wolf,T,ListIndex+1,NewAccEat, NewAccMove,  Type);
		3->
			{NewAccEat, NewAccMove} = parseListHelper(
									{Type,Wolf#wolf.x,Wolf#wolf.y-2},
									AccEat, AccMove, Type),
		   	parseList(
			 Wolf,T,ListIndex+1,NewAccEat, NewAccMove,  Type);
		4->
			{NewAccEat, NewAccMove} = parseListHelper(
									{Type,Wolf#wolf.x+1,Wolf#wolf.y-2},
									AccEat, AccMove, Type),
		   	parseList(
			 Wolf,T,ListIndex+1,NewAccEat, NewAccMove,  Type);
		5->
			{NewAccEat, NewAccMove} = parseListHelper(
									{Type,Wolf#wolf.x+2,Wolf#wolf.y-2},
									AccEat, AccMove, Type),
		   	parseList(
			 Wolf,T,ListIndex+1,NewAccEat, NewAccMove,  Type);
		6->
			{NewAccEat, NewAccMove} = parseListHelper(
									{Type,Wolf#wolf.x-2,Wolf#wolf.y-1},
									AccEat, AccMove, Type),
		   	parseList(
			 Wolf,T,ListIndex+1,NewAccEat, NewAccMove,  Type);
		7->
			{NewAccEat, NewAccMove} = parseListHelper(
									{Type,Wolf#wolf.x-1,Wolf#wolf.y-1},
									AccEat, AccMove, Type),
		   	parseList(
			 Wolf,T,ListIndex+1,NewAccEat, NewAccMove,  Type);
		8->
			{NewAccEat, NewAccMove} = parseListHelper(
									{Type,Wolf#wolf.x,Wolf#wolf.y-1},
									AccEat, AccMove, Type),
		   	parseList(
			 Wolf,T,ListIndex+1,NewAccEat, NewAccMove,  Type);
		9->
			{NewAccEat, NewAccMove} = parseListHelper(
									{Type,Wolf#wolf.x+1,Wolf#wolf.y-1},
									AccEat, AccMove, Type),
		   	parseList(
			 Wolf,T,ListIndex+1,NewAccEat, NewAccMove,  Type);
		10->
			{NewAccEat, NewAccMove} = parseListHelper(
									{Type,Wolf#wolf.x+2,Wolf#wolf.y-1},
									AccEat, AccMove, Type),
		   	parseList(
			 Wolf,T,ListIndex+1,NewAccEat, NewAccMove,  Type);
		11->
			{NewAccEat, NewAccMove} = parseListHelper(
									{Type,Wolf#wolf.x-2,Wolf#wolf.y},
									AccEat, AccMove, Type),
		   	parseList(
			 Wolf,T,ListIndex+1,NewAccEat, NewAccMove,  Type);
			

	 
	   end.
doTick(Wolf) ->
	Wolf2 = randw:increaseAge({wolf, Wolf}),
 	PID = Wolf#wolf.spid,
	Map = getMap(Wolf),
	{Wolf3, Ate, List} = eat(Wolf2, Map),
	if
		Ate == gotFood ->
 			PID ! {wolfEat, self(), Wolf3#wolf.age, Wolf3#wolf.hunger, Wolf3#wolf.x, Wolf3#wolf.y},
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
 			case randw:checkToDie({rabbit, Wolf}) of
 				true ->
					PID = Wolf#wolf.spid,
					PID ! {death, self(), Wolf#wolf.x, Wolf#wolf.y},
 					exit(died);
 				false ->
 					Wolf2 = doTick(Wolf),
					loop(Wolf2)
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




%% out som atom för vargarna när de ska äta om ruta är utanför kartan, tex hörn och kanter
%% istället för tick, när jag väntar matchning mot death osv, så kör en after och då börjar jag loopa igen så ny "doTick" startar