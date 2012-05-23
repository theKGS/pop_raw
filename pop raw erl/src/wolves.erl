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
	SenderPID ! {newWolf, PID, X, Y}.
		



%% 
%% @doc Finds a new random square for Rabbit, left/right/up/down compared to Rabbit's current coordinate.  
%% 

findNewSquare(Wolf, MapList, Length) ->
	if(Length =/= 0)->
		  Dir = random:uniform(Length),
		  {_, X,Y}= lists:nth(Dir, MapList),
		  randw:move({wolf, Wolf, {X, Y}});
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



eat(Wolf, Map, Length) ->
	Dir = random:uniform(Length),
	{_, X, Y} = lists:nth(Dir, Map),
	PID = Wolf#wolf.spid,
	PID ! {wolfEat, self(),Wolf#wolf.age, Wolf#wolf.hunger, Wolf#wolf.x, Wolf#wolf.y, X, Y},
	receive
		{yes} ->
			Wolf#wolf{x = X, y = Y, hunger = Wolf#wolf.hunger - 20};
%% 			loop(Wolf2);
		{eatMove} ->
			Wolf#wolf{x = X, y = Y, hunger = Wolf#wolf.hunger+1};
%% 			loop(Wolf2);
		{no} ->
			Wolf#wolf{hunger = Wolf#wolf.hunger+1}
%% 			loop(Wolf)
	end.
	

%% 
%% 
%% 

getMap(Wolf) ->
	Wolf#wolf.spid ! {wolfMap, self(), Wolf#wolf.x, Wolf#wolf.y},
	receive
		{death}->
			exit(killed);
		{wolfMap, List} ->
			List
	end.
	


%%  1  2  3  4  5
%%  6  7  8  9 10
%% 11 12 13 14 15
%% 16 17 18 19 20
%% 21 22 23 24 25
parseList(_, [], Acc, _)->
	Acc;
parseList(Wolf, [{_,Type}|Map], Acc, Index)->
	if Type =/= wolf ->
		   case Index of
			   1->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x - 2, Wolf#wolf.y-2}]++Acc, Index+1);
			   2->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x - 1, Wolf#wolf.y-2}]++Acc, Index+1);
			   3->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x, Wolf#wolf.y-2}]++Acc, Index+1);
			   4->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x +1, Wolf#wolf.y-2}]++Acc, Index+1);
			   5->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x +2, Wolf#wolf.y-2}]++Acc, Index+1);
			   6->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x-2, Wolf#wolf.y-1}]++Acc, Index+1);
			   7->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x -1, Wolf#wolf.y-1}]++Acc, Index+1);
			   8->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x , Wolf#wolf.y-1}]++Acc, Index+1);
			   9->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x +1, Wolf#wolf.y-1}]++Acc, Index+1);
			   10->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x + 2, Wolf#wolf.y-1}]++Acc, Index+1);
			   11->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x-2, Wolf#wolf.y}]++Acc, Index+1);
			   12->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x-1, Wolf#wolf.y}]++Acc, Index+1);
			   13->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x, Wolf#wolf.y}]++Acc, Index+1);
			   14->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x+1, Wolf#wolf.y}]++Acc, Index+1);
			   15->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x+2, Wolf#wolf.y}]++Acc, Index+1);
			   16->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x-2, Wolf#wolf.y+1}]++Acc, Index+1);
			   17->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x-1, Wolf#wolf.y+1}]++Acc, Index+1);
			   18->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x, Wolf#wolf.y+1}]++Acc, Index+1);
			   19->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x+1, Wolf#wolf.y+1}]++Acc, Index+1);
			   20->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x+2, Wolf#wolf.y+1}]++Acc, Index+1);
			   21->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x-2, Wolf#wolf.y+2}]++Acc, Index+1);
			   22->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x-1, Wolf#wolf.y+2}]++Acc, Index+1);
			   23->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x, Wolf#wolf.y+2}]++Acc, Index+1);
			   24->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x+1, Wolf#wolf.y+2}]++Acc, Index+1);
			   25->
				  parseList(Wolf,Map,[{Type, Wolf#wolf.x+2, Wolf#wolf.y+2}]++Acc, Index+1)
		   end;
	   true->
		   parseList(Wolf, Map, Acc, Index+1)
	end.

doTick(Wolf) ->	
	{Eatable, Movable} = lists:splitwith(fun({A,_,_})-> A =:= rabbit end, parseList(Wolf, getMap(Wolf), [], 1)),
	
	%%lists:foldl(fun(Next, Acc)->io:format("~w~n", [Next]),Acc end, 0, Movable),
	ELength = lists:foldl(fun(_,AccIn)-> AccIn+1 end, 0, Eatable),
	MLength = lists:foldl(fun(_, AccIn)->AccIn+1 end, 0, Movable),
	Hunger = randw:isHungry({wolf,Wolf}),
	
	if Hunger > 0,  ELength > 0 ->
		   
		   eat(Wolf, Eatable, ELength);
	   MLength > 0 ->
		   Wolf2 =  Wolf#wolf{hunger = Wolf#wolf.hunger+1},
		   findNewSquare(Wolf2, Movable, MLength);
	   true->
		   Wolf
	end.

%% 
%% 
%% 

preloop(Wolf) ->
	receive
		{start} ->
			
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
		{death} ->
			exit(killed)
		after 200 ->
 			case randw:checkToDie({wolf, Wolf}) of
 				true ->
					PID = Wolf#wolf.spid,
					PID ! {death, self(), Wolf#wolf.x, Wolf#wolf.y},
 					exit(died);
 				false ->
					
 					Wolf2 = doTick(Wolf),
					randw:increaseAge({wolf, Wolf}),
					loop(doTick(Wolf2))
 			end
	end.

%% 
%% 
%% 


init() ->
	{A1, A2, A3} = now(),
	Mega = lists:nth(2, pid_to_list(self())),
	random:seed(A1 + Mega, A2 + Mega, A3 + Mega).




%% out som atom för vargarna när de ska äta om ruta är utanför kartan, tex hörn och kanter
%% istället för tick, när jag väntar matchning mot death osv, så kör en after och då börjar jag loopa igen så ny "doTick" startar