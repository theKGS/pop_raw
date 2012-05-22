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

findNewSquare(Wolf, MapList, Length) ->
	Dir = random:uniform(Length),
	{_, X,Y}= lists:nth(Dir, MapList),
	randw:move({wolf, Wolf, {X, Y}}).

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



eat(Wolf, Map, Length) ->
	Dir = random:uniform(Length),
	{_, X, Y} = lists:nth(Dir, Map),
	PID = Wolf#wolf.spid,
	PID ! {wolfEat, self(),Wolf#wolf.age, Wolf#wolf.hunger, Wolf#wolf.x, Wolf#wolf.y, X,Y},
	receive
		yes ->
			Wolf2 = Wolf#wolf{x = X, y = Y},
			loop(Wolf2);
		no ->
			loop(Wolf)
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
listFolderHelper(_, {AccEat, AccMove, _, _, Wolf}, NextX, NextY)->
	{AccEat, AccMove, NextX, NextY, Wolf}.

listFolder({_, Type}, {AccEat, AccMove, X, Y, Wolf}) 
  when X =:= 2->
	NextX = 0,
	NextY = Y+1,
	listFolderHelper(Type,{AccEat, AccMove, X, Y, Wolf}, NextX, NextY).


parseList(Wolf, Map)->
	lists:foldl(fun(Next,AccIn) -> listFolder(Next,AccIn) end, 
				{[],[],-2,-2,Wolf}, Map).
doTick(Wolf) ->
	Wolf2 = randw:increaseAge({wolf, Wolf}),
	{Eatable, Movable} = parseList(Wolf, getMap(Wolf)),
	ELength = lists:foldl(fun(_,AccIn)-> AccIn+1 end, 0, Eatable),
	MLength = lists:foldl(fun(next, AccIn)->AccIn+1 end, 0, Movable),
	Hunger = randw:isHungry(Wolf2),
	if Hunger > 0,  ELength > 0 ->
		   eat(Wolf, Eatable, ELength);
	   MLength > 0 ->
		   findNewSquare(Wolf, Movable, MLength);
	   true->
		   loop(Wolf2)
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