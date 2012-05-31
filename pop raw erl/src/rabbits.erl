%% Author: Linnea
%% Created: 3 maj 2012
%% Description: TODO: Add description to rabbits
-module(rabbits).

%%
%% Include files
%%
-include_lib("eunit/include/eunit.hrl").

%%
%% Exported Functions
%%
-export([newRabbit/2, parseList/4, maxGrass/4, findNewSquare/2, eat/1, 
		 getMap/1, doTick/1, preloop/1, loop/1, init/0]).

%% ----------------------------------------------------------------------------
%% @edoc A rabbit record
%% @type rabbit() = #rabbit{ age::integer(), hunger::integer(),
%%                           x::integer(), y::integer(), spid::integer()}.
%% @end
%% ----------------------------------------------------------------------------
-record(rabbit, {age=0, hunger=0, x=none, y=none, spid=none}).

%% ----------------------------------------------------------------------------
%% @doc Spawns a new rabbit process. When the rabbit is initialized it sends a 
%% message to the java server.
%% @spec newRabbit(Touple::{X::integer(),Y::integer()}, 
%%					SenderPid::pid()) -> {newRabbit::atom(), PID::pid(), 
%% 											X::integer(), Y::integer()}
%% @end
%% ---------------------------------------------------------------------------- 

newRabbit({X, Y}, SenderPID) ->
	PID = spawn(rabbits, preloop, [#rabbit{age = 0, hunger = 0, x = X, y = Y, 
										   spid = SenderPID}]),
	SenderPID ! {newRabbit, PID, X, Y}.
		


%% ----------------------------------------------------------------------------
%% @doc Searches the Map and finds the best moves 
%% @spec parseList(Rabbit::rabbit(), MapList::list(), Index::integer(), 
%% 					Acc::list()) -> PossibleMovesList::list()
%% @end
%% ----------------------------------------------------------------------------

parseList(_,[],_,Acc)->
	Acc;
parseList(Rabbit, [{Grass, Type}|T], ListIndex, Acc) ->
	if Type =:= none ->
		   case ListIndex of
			   1->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x-1,
													Rabbit#rabbit.y-1}]++Acc);
			   2->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x,
													Rabbit#rabbit.y-1}]++Acc);
			   3->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x+1,
													Rabbit#rabbit.y-1}]++Acc);
			   4->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x-1,
													Rabbit#rabbit.y}]++Acc);
			   5->
				   parseList(Rabbit,T,ListIndex+1, Acc);
			   6->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x+1,
													Rabbit#rabbit.y}]++Acc);
			   7->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x-1,
													Rabbit#rabbit.y+1}]++Acc);
			   8->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x,
													Rabbit#rabbit.y+1}]++Acc);
			   9->
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x+1,
													Rabbit#rabbit.y+1}]++Acc);
			   _Def->
				   parseList(Rabbit,T,ListIndex+1,Acc)
		   end;
	   Type =/= none ->
		   parseList(Rabbit, T, ListIndex+1, Acc)
	end.
	
%% ----------------------------------------------------------------------------
%% @doc Returns a list of squares with the highest grasslevel, gets the entire 
%% list with squares if the grasslevel = 0 in all squares
%% @spec maxGrass([{Grass::integer(), X::integer(), Y::integer()}|T]::list(), 
%% 					Acc::integer(), _CurrMax::integer(), Length::integer()) ->
%%					maxGrass(T::list(), Acc::integer(), CurrMax::integer(), 
%% 					Length::integer())
%% @end
%% ---------------------------------------------------------------------------- 

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

%% ----------------------------------------------------------------------------
%% @doc Finds a new square for Rabbit, left/right/up/down/diagonal compared to 
%% Rabbit's current coordinate. If there are no possible moves, the function 
%% returns to loop(Rabbit::rabbit()).
%% @spec findNewSquare(Rabbit::rabbit(), MapList::list()) -> Rabbit:rabbit() 
%% @end
%% ---------------------------------------------------------------------------- 

findNewSquare(Rabbit, MapList) ->
	{Length, PossibleSquares} = maxGrass(parseList(Rabbit, MapList, 1, []),
										 [],0,0),
	
	if(Length =/= 0)->
		Dir = random:uniform(Length),
		{_,X2,Y2} = lists:nth(Dir, PossibleSquares),
		randw:move({rabbit, Rabbit, {X2, Y2}});
	  true ->
		  Rabbit
	end.
						
%% ----------------------------------------------------------------------------
%% @doc Makes a rabbit who is hungry eat if it there is enough grass 
%% @spec eat(Rabbit::rabbit()) -> {Rabbit::rabbit(), gotFood/noFood::atom(), 
%%   								List::list()}
%% @end
%% ---------------------------------------------------------------------------- 

eat(Rabbit) ->
	{Grass, _, List} = getMap(Rabbit),
	case randw:isHungry({rabbit, Rabbit}) and (Grass > 0) of
		true ->
			{Rabbit#rabbit{hunger = Rabbit#rabbit.hunger - 1}, gotFood, List};
		false ->

			{Rabbit#rabbit{hunger = Rabbit#rabbit.hunger + 1}, noFood, List}
	end.
	

%% ----------------------------------------------------------------------------
%% @doc Ask the map process for the map and its current layout 
%% @spec getMap(Rabbit::rabbit()) ->  {Grass::integer(), Type::atom(), 
%% 										List::list()}
%% @end
%% ---------------------------------------------------------------------------- 

getMap(Rabbit) ->
	Rabbit#rabbit.spid ! {rabbitMap, self(), Rabbit#rabbit.x, Rabbit#rabbit.y},
	receive
		{death}->
			exit(killed);
		{rabbitMap, List} ->
			{Grass, Type} = lists:nth(5, List),
			{Grass, Type, List}
		
	end.

%% ----------------------------------------------------------------------------
%% @doc Initiates all things required to happen during a time tick for a rabbit
%% @spec doTick(Rabbit::rabbit()) -> Rabbit:rabbit()
%% @end
%% ---------------------------------------------------------------------------- 

doTick(Rabbit) ->
	Rabbit2 = randw:increaseAge({rabbit, Rabbit}),
 	PID = Rabbit#rabbit.spid,
	{Rabbit3, Ate, List} = eat(Rabbit2),
	
	if
		Ate == gotFood ->
 			PID ! {rabbitEat, self(), Rabbit3#rabbit.age, 
				   Rabbit3#rabbit.hunger, Rabbit3#rabbit.x, Rabbit3#rabbit.y},
			Rabbit3;
		true ->
			findNewSquare(Rabbit3, List)
	end.

%% ----------------------------------------------------------------------------
%% @doc Calls init() and loop(Rabbit:rabbit()) or kills the rabbit process 
%% when that command is recieved
%% @spec preloop(Rabbit::rabbit()) -> loop(Rabbit::rabbit()) 
%% @end
%% ---------------------------------------------------------------------------- 

preloop(Rabbit) ->
	receive	
		{death}->
			exit(killed);
		{start} ->

			init(),
			loop(Rabbit)
	end.

%% ----------------------------------------------------------------------------
%% @doc Upon starting the program, loop(Rabbit::rabbit()) is started and will
%% call other functions in the program, will kill the rabbit process if that
%% command is recieved. 
%% @spec loop(Rabbit::rabbit()) -> loop(NewRabbit::rabbit()) 
%% @end
%% ---------------------------------------------------------------------------- 

loop(Rabbit) ->
	receive
		{Sender, getInfo} ->
			Sender ! {self(), Rabbit},
			loop(Rabbit);
		{Sender, getCoords} ->
			Sender ! {self(), {Rabbit#rabbit.x, Rabbit#rabbit.y}},
			loop(Rabbit);
		{death} ->
			exit(killed)
		after 400 ->
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

%% ----------------------------------------------------------------------------
%% @doc Sets the random seed.
%% @spec init() -> random:seed::integer()
%% @end
%% ---------------------------------------------------------------------------- 

init() ->
	{A1, A2, A3} = now(),
	Mega = lists:nth(2, pid_to_list(self())),
	random:seed(A1 + Mega, A2 + Mega, A3 + Mega).


%% ----------------------------------------------------------------------------
%% FUNCTIONS FOR TESTING
%% ----------------------------------------------------------------------------
%%  
%% sendTick(Pid) ->
%% 	Pid ! {self(), tick}.
%% 
%% getInfo(Pid) ->
%% 	Pid ! {self(), getInfo},
%% 	receive
%% 		{Pid, Rabbit} ->
%% 			Rabbit
%% 	end.
%% 
%% getCoords(Pid) ->
%% 	Pid ! {self(), getCoords},
%% 	receive
%% 		{Pid, Coords} ->
%% 			Coords
%% 	end.
%% 
%% execute(Pid) ->
%% 	Pid ! {self(), die}.
%% 
%% 
%% test() ->
%% 	init(),
%% 	KaninPid = newRabbit({5, 5}, self()),
%% 	sendTick(KaninPid),
%% 	sendTick(KaninPid),
%% 	sendTick(KaninPid),
%% 	sendTick(KaninPid),
%% 	sendTick(KaninPid),
%% 	sendTick(KaninPid),
%% 	sendTick(KaninPid),
%% 	sendTick(KaninPid),
%% 	sendTick(KaninPid),
%% 	sendTick(KaninPid),
%% 	sendTick(KaninPid),
%% 	sendTick(KaninPid),
%% 	sendTick(KaninPid),
%% 	sendTick(KaninPid),
%% 	{X, Y} = getCoords(KaninPid),
%%  	RabbitInfo = getInfo(KaninPid),
%% 	io:format("Rabbit (~w)~nAge: ~w~nHunger: ~w~n", [KaninPid, RabbitInfo#rabbit.age, RabbitInfo#rabbit.hunger]),
%% 	io:format("x: ~w~ny: ~w~n", [X, Y]),
%% 	execute(KaninPid),
%% 	test_finished.

%% ----------------------------------------------------------------------------
%% TESTS
%% ----------------------------------------------------------------------------

newRabbit_test() ->
	newRabbit({1,2}, self()),
	receive
		{newRabbit, _PID, 1, 2} ->
			?assert(true)
	after 50 ->
			?assert(false)
	end.
	
parseList_test()->
	Rabbit = #rabbit{age = 0, hunger = 2, x = 2, y = 2, spid = self()},
	List = [{5,rabbit},{5,rabbit},{5,rabbit},{5,rabbit},{5,rabbit},
		{5,rabbit},{5,rabbit},{5,rabbit},{5,rabbit}],
 	NewMap = parseList(Rabbit, List, 1, []),
 	List2 = [{5,none},{5,none},{5,none},{5,none},{5,rabbit},
 		{5,none},{5,none},{5,none},{5,none}],
	NewMap2 = parseList(Rabbit, List2, 1, []),
 	AuxList = lists:reverse([{5,1,1},{5,2,1},{5,3,1},{5,1,2},{5,3,2},
 		{5,1,3},{5,2,3},{5,3,3}]),
 	?assert(NewMap == []),
 	?assert(NewMap2 == AuxList).
 
maxGrass_test() ->
	List = [{4, 1,1}, {2,1,2}, {3,1,3}, {2,2,1}, {2,2,3}, {5,3,1}, {4,3,2}, {0,3,3}],
	List2 = maxGrass(List, [], 0, 8),
	?assert(List2 == {1,[{5,3,1}]}).

findNewSquare_test() ->
	Rabbit = #rabbit{age = 0, hunger = 10, x = 2, y = 2, spid = self()},
 	NewRabbit = findNewSquare(Rabbit, []),
	List = [{5,none},{5,none},{5,none},{5,none},{5,none},
 		{5,none},{5,none},{5,none},{5,none}],
 	self() ! {yes},
 	NewRabbit2 = findNewSquare(Rabbit,List),
	receive
		{move, _,_,_,_,_,X,Y}->
			nothing
	end,
	self() ! {no},
	NewRabbit3 = findNewSquare(Rabbit,List),
	receive
		MSG -> nothing
	end,
	
  	?assert(Rabbit == NewRabbit),
 	?assert(NewRabbit2#rabbit.x == X),
 	?assert(NewRabbit2#rabbit.y == Y),
	?assert(NewRabbit3 == Rabbit).

eat_test()->
	Rabbit = #rabbit{age = 0, hunger = 4, x = 2, y = 2, spid = self()},
 	List = [{2, rabbit}, {0, rabbit}, {5, rabbit}, {1, rabbit}, {5, rabbit}, {2, rabbit}, {0, rabbit},
 			{0, rabbit}, {1, rabbit}],
	self() ! {rabbitMap, List},
	{Rabbit2, _, _} = eat(Rabbit),
	?assert(Rabbit2#rabbit.hunger =:= 3).
	
getMap_test()->
	Rabbit = #rabbit{age = 0, hunger = 10, x = 2, y = 2, spid = self()},
	List = [{2, rabbit}, {0, rabbit}, {5, rabbit}, {1, rabbit}, {2, rabbit}, {0, rabbit},
			{0, rabbit}, {1, rabbit}],
	self() ! {rabbitMap, List},
	{_Grass, _Type, Map} = getMap(Rabbit),
	?assert(Map == List).


