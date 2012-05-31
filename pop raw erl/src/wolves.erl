%% Author: Linnea
%% Created: 3 maj 2012
%% Description: TODO: Add description to rabbits
-module(wolves).

%%
%% Include files
%%
-include_lib("eunit/include/eunit.hrl").
%%
%% Exported Functions
%%
-export([preloop/1, newWolf/2]).

%% 
%% Defining a Wolf record
%% 
-record(wolf, {age=0, hunger=0, x=none, y=none, spid=none}).

%% ----------------------------------------------------------------------------
%% @doc Spawns a new wolf process. When the wolf is initialised it sends a 
%%message to the java server.
%% @spec newWolf(Touple::{X::integer(),Y::integer()}, 
%%					SenderPid::pid()) -> {newWolf, PID, X, Y}
%% @end
%% ----------------------------------------------------------------------------
newWolf({X, Y}, SenderPID) ->
	PID = spawn(wolves, preloop, 
				[#wolf{age = 0, hunger = 10, x = X, y = Y, spid = SenderPID}]),
	SenderPID ! {newWolf, PID, X, Y}.
		



%% ----------------------------------------------------------------------------
%% @doc Finds a new random square for the Wolf. 
%% <br/>If there is no possible move, this funktion does not return.
%% @spec findNewSquare(Wolf::wolf(), 
%% 		MapList::list(),
%% 		Length::integer()) -> NewWolf::wolf()
%% @end
%% ----------------------------------------------------------------------------
findNewSquare(Wolf, MapList, Length) ->
	if(Length =/= 0)->
		  Dir = random:uniform(Length),
		  {_, X,Y}= lists:nth(Dir, MapList),
		  randw:move({wolf, Wolf, {X, Y}});
	  true ->
		  Wolf
	end.


%% ----------------------------------------------------------------------------
%% @doc Function handling the wolf eating.
%% @spec eat(Wolf::wolf(), Map::list(), Length::integer())->UpdatedWolf::wolf()
%% @end
%% ----------------------------------------------------------------------------
eat(Wolf, Map, Length) ->
	
	Dir = random:uniform(Length),
	{_, X, Y} = lists:nth(Dir, Map),
	PID = Wolf#wolf.spid,
	PID ! {wolfEat, self(),Wolf#wolf.age, Wolf#wolf.hunger, Wolf#wolf.x, 
		   Wolf#wolf.y, X, Y},
	receive
		{yes} ->
			Wolf#wolf{x = X, y = Y, hunger = Wolf#wolf.hunger - 5};
		{eatMove} ->
			Wolf#wolf{x = X, y = Y, hunger = Wolf#wolf.hunger+1};
		{no} ->
			Wolf#wolf{hunger = Wolf#wolf.hunger+1}
	end.
%% ----------------------------------------------------------------------------
%% @doc requests the map from the server.
%% @spec getMap(Wolf::wolf())-> Map::list()
%% @end
%% ----------------------------------------------------------------------------
getMap(Wolf) ->
	Wolf#wolf.spid ! {wolfMap, self(), Wolf#wolf.x, Wolf#wolf.y},
	receive
		{death}->
			exit(killed);
		{wolfMap, List} ->
			List
	end.
	


%% ----------------------------------------------------------------------------
%% @doc Searches the Map and finds the best moves 
%% @spec parseList(Wolf::wolf(), MapList::list(), Acc::list(), 
%%Index::integer()) -> PossibleMovesList::list()
%% @end
%% ----------------------------------------------------------------------------
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
%% ----------------------------------------------------------------------------
%% @doc Executes one tick for the wolf
%% @spec doTick(Wolf::wolf())->NewWolf::wolf()
%% @end
%% ----------------------------------------------------------------------------
doTick(Wolf) ->	
	{Eatable, Movable} = lists:splitwith(fun({A,_,_})-> A =:= rabbit end, parseList(Wolf, getMap(Wolf), [], 1)),
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

%% ----------------------------------------------------------------------------
%% @doc 
%% @hidden
%% @end
%% ----------------------------------------------------------------------------
preloop(Wolf) ->
	receive
		{start} ->
			
			init(),
			loop(Wolf)
	end.
%% ----------------------------------------------------------------------------
%% @doc 
%% @hidden
%% @end
%% ----------------------------------------------------------------------------
loop(Wolf) ->
	Wolf2 = randw:increaseAge({wolf, Wolf}),
	receive
		{Sender, getInfo} ->
			Sender ! {self(), Wolf2},
			loop(Wolf2);
		{Sender, getCoords} ->
			Sender ! {self(), {Wolf2#wolf.x, Wolf2#wolf.y}},
			loop(Wolf2);
		{death} ->
			exit(killed)
		after 400 ->
 			case randw:checkToDie({wolf, Wolf2}) of
 				true ->
					PID = Wolf2#wolf.spid,
					PID ! {death, self(), Wolf2#wolf.x, Wolf2#wolf.y},
 					exit(died);
 				false ->
					
 					Wolf3 = doTick(Wolf2),
					
					loop(doTick(Wolf3))
 			end
	end.
%% ----------------------------------------------------------------------------
%% @doc 
%% @hidden
%% @end
%% ----------------------------------------------------------------------------
init() ->
	{A1, A2, A3} = now(),
	Mega = lists:nth(2, pid_to_list(self())),
	random:seed(A1 + Mega, A2 + Mega, A3 + Mega).



%% @type wolf(). A record describing a wolf.
%% @end
%%      ___________________________________________________________
%%     /,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-\
%%    /'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'\
%%   /-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,\
%%  /-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'\
%% |'*-.,.-*'^'*-.,.-*'^'*-.,.-*'TEST CASES ^'*-.,.-*'^'*-.,.-*'^'*-.,.|
%%  \-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'/
%%   \-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,/
%%    \'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'/
%%     \,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-.,.-*'^'*-/
%%      \_________________________________________________________/
%%       \XXXXXXXXXXXX/\XXXXXXXXXXXX/ \XXXXXXXXXXXX/\XXXXXXXXXXXX/
%%        \XXXXXXXXXX/  \XXXXXXXXXX/   \XXXXXXXXXX/  \XXXXXXXXXX/
%%         \XXXXXXXX/    \XXXXXXXX/     \XXXXXXXX/    \XXXXXXXX/
%%          \XXXXXX/      \XXXXXX/       \XXXXXX/      \XXXXXX/
%%           \XXXX/        \XXXX/         \XXXX/        \XXXX/
%%            \XX/          \XX/           \XX/          \XX/
%%             \/____________\/_____________\/____________\/
%%              \XXXXXXXXXXXX/\XXXXXXXXXXXXX/\XXXXXXXXXXXX/
%%               \XXXXXXXXXX/  \XXXXXXXXXXX/  \XXXXXXXXXX/
%%                \XXXXXXXX/    \XXXXXXXXX/    \XXXXXXXX/
%%                 \XXXXXX/      \XXXXXXX/      \XXXXXX/
%%                  \XXXX/        \XXXXX/        \XXXX/
%%                   \XX/          \XXX/          \XX/
%%                    \/____________\X/____________\/
%%                     \XXXXXXXXXXXX/ \XXXXXXXXXXXX/
%%                      \XXXXXXXXXX/   \XXXXXXXXXX/
%%                       \XXXXXXXX/     \XXXXXXXX/
%%                        \XXXXXX/       \XXXXXX/
%%                         \XXXX/         \XXXX/
%%                          \XX/           \XX/
%%                           \/_____________\/
%%                            \XXXXXXXXXXXXX/
%%                             \XXXXXXXXXXX/
%%                              \XXXXXXXXX/
%%                               \XXXXXXX/
%%                                \XXXXX/
%%                                 \XXX/
%%                                  \X/
%%                                  /X\
%%                                 / X \
%%                                /  X  \
%%                               /  X    \
%%                              /     X   \
%%                             /     X     \
%%                            /____ __X_____\
%%                           /\      X      /\
%%                          /  \    X      /  \
%%                         /    \    X    /    \
%%                        /      \    X  /      \
%%                       /        \  X  /        \
%%                      /          \  X/          \
%%                     /____________\X/____________\
%%                    /\            /X\            /\
%%                   /  \          /  X\          /  \
%%                  /    \        /  X  \        /    \
%%                 /      \      /  X    \      /      \
%%                /        \    /    X    \    /        \
%%               /          \  /     X     \  /          \
%%              /____________\/_______X_____\/____________\
%%             /\            /\      X      /\            /\
%%            /  \          /  \   XXXXX   /  \          /  \
%%           /    \        /    \XXXXXXXXX/    \        /    \
%%          /      \      /     X\XXXXXXX/XX    \      /      \
%%         /        \    /   XXXXX\XXXXX/XXXXXXXX\    /        \
%%        /XXXXXXXXXX\XX/XXXXXXXXXX\XXX/XXXXXXXXXX\XX/XXXXXXXXXX\
%%       /XXXXXXXXXXXX\/XXXXXXXXXXXX\X/XXXXXXXXXXXX\/XXXXXXXXXXXX\
%%      /#########################################################\
%%     /###########################################################\
%%    /¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤\
%%   /¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤\
%%  /XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\
%% |XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX|
%%  \YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY/
%%   \YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY/
%%    \|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||/
%%     \|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||/ 
%%      ¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨ 
newWolf_test()->
	newWolf({1,1},self()),
	receive
		{newWolf, _PID, 1,1}->
			?assert(true)
	after 50 ->
		?assert(false)
	end.

findNewSquare_test()->
	Wolf = #wolf{age = 0, hunger = 10, x = 2, y = 2, spid = self()},
	NewWolf = findNewSquare(Wolf, [], 0),
	List = [
			{none, 0,0},{none, 1,0},{none, 2,0},{none, 3,0},{none, 4,0},
		    {none, 0,1},{none, 1,1},{none, 2,1},{none, 3,1},{none, 4,1},
			{none, 0,2},{none, 1,2},{none, 2,2},{none, 3,2},{none, 4,2},
			{none, 0,3},{none, 1,3},{none, 2,3},{none, 3,3},{none, 4,3},
			{none, 0,4},{none, 1,4},{none, 2,4},{none, 3,4},{none, 4,4}],
	self() ! {yes},
	NewWolf2 = findNewSquare(Wolf,List,25),
	receive
		{wolfMove, _,_,_,_,_,X,Y}->
			nothing
	end,
	self() ! {no},
	NewWolf3 = findNewSquare(Wolf,List,25),
	receive
		MSG -> nothing
	end,
	
	
	?assert(Wolf == NewWolf),
	?assert(NewWolf2#wolf.x == X),
	?assert(NewWolf2#wolf.y == Y),
	?assert(NewWolf3 == Wolf).
	

eat_test()->
	Wolf = #wolf{age = 0, hunger = 10, x = 2, y = 2, spid = self()},
	List = [
		{rabbit, 0,0},{rabbit, 1,0},{rabbit, 2,0},{rabbit, 3,0},{rabbit, 4,0},
	    {rabbit, 0,1},{rabbit, 1,1},{rabbit, 2,1},{rabbit, 3,1},{rabbit, 4,1},
		{rabbit, 0,2},{rabbit, 1,2},{rabbit, 2,2},{rabbit, 3,2},{rabbit, 4,2},
		{rabbit, 0,3},{rabbit, 1,3},{rabbit, 2,3},{rabbit, 3,3},{rabbit, 4,3},
		{rabbit, 0,4},{rabbit, 1,4},{rabbit, 2,4},{rabbit, 3,4},{rabbit, 4,4}],
	self() ! {yes},
	Wolf2 = eat(Wolf, List, 25),
	receive
		{wolfEat, _,_, _, _, _, X, Y} ->
			nothing
	end,
	
	self() ! {no},
	Wolf3 = eat(Wolf, List, 25),
	receive
		{wolfEat, _,_, _, _, _, _, _} ->
			nothing
	end,
	
	self() ! {eatMove},
	Wolf4 = eat(Wolf, List, 25),
	receive
		{wolfEat, _,_, _, _, _, X3, Y3} ->
			nothing
	end,
	?assert(Wolf2#wolf.x == X),
	?assert(Wolf2#wolf.y == Y),
	?assert(Wolf2#wolf.hunger == Wolf#wolf.hunger -5),
	?assert(Wolf3#wolf.x == Wolf#wolf.x),
	?assert(Wolf3#wolf.y == Wolf#wolf.y),
	?assert(Wolf3#wolf.hunger == Wolf#wolf.hunger +1),
	?assert(Wolf4#wolf.x == X3),
	?assert(Wolf4#wolf.y == Y3),
	?assert(Wolf4#wolf.hunger == Wolf#wolf.hunger +1).
getMap_test()->
	Wolf = #wolf{age = 0, hunger = 10, x = 2, y = 2, spid = self()},
	List = [
		{rabbit, 0,0},{rabbit, 1,0},{rabbit, 2,0},{rabbit, 3,0},{rabbit, 4,0},
	    {rabbit, 0,1},{rabbit, 1,1},{rabbit, 2,1},{rabbit, 3,1},{rabbit, 4,1},
		{rabbit, 0,2},{rabbit, 1,2},{rabbit, 2,2},{rabbit, 3,2},{rabbit, 4,2},
		{rabbit, 0,3},{rabbit, 1,3},{rabbit, 2,3},{rabbit, 3,3},{rabbit, 4,3},
		{rabbit, 0,4},{rabbit, 1,4},{rabbit, 2,4},{rabbit, 3,4},{rabbit, 4,4}],
	self() ! {wolfMap, List},
	Map = getMap(Wolf),
	?assert(Map == List).



















parseList_test()->
	Wolf = #wolf{age = 0, hunger = 10, x = 2, y = 2, spid = self()},
	List = [
		{5,wolf},{5,wolf},{5,wolf},{5,wolf},{5,wolf},
		{5,wolf},{5,wolf},{5,wolf},{5,wolf},{5,wolf},
		{5,wolf},{5,wolf},{5,wolf},{5,wolf},{5,wolf},
		{5,wolf},{5,wolf},{5,wolf},{5,wolf},{5,wolf},
		{5,wolf},{5,wolf},{5,wolf},{5,wolf},{5,wolf}],
	NewMap = parseList(Wolf, List, [], 0),
	List2 = [
		{5,wolf},{5,rabbit},{5,none},{5,wolf},{5,rabbit},
		{5,none},{5,wolf},{5,rabbit},{5,none},{5,wolf},
		{5,rabbit},{5,none},{5,wolf},{5,rabbit},{5,none},
		{5,wolf},{5,rabbit},{5,none},{5,wolf},{5,rabbit},
		{5,none},{5,rabbit},{5,wolf},{5,wolf},{5,rabbit}],
	NewMap2 = parseList(Wolf, List2, [], 0),
	AuxList = 
		[{rabbit, 1,0},{none, 2,0},{rabbit, 4,0},
	    {none, 0,1},{rabbit, 2,1},{none, 3,1},
		{rabbit, 0,2},{none, 1,2},{rabbit, 3,2},{none, 4,2},
		{rabbit, 1,3},{none, 2,3},{rabbit, 4,3},
		{none, 0,4},{rabbit, 1,4},{rabbit, 4,4}],
	
	?assert(NewMap == []),
	?assert(NewMap2 == AuxList).




