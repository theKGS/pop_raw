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

%% ----------------------------------------------------------------------------
%% @edoc A rabbit record
%% @type rabbit() = #rabbit{ age::integer(), hunger::integer(),
%%                           x::integer(), y::integer(), spid::integer()}. CAN THIS BE DOCUMENTED!?!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
%% @end
%% ----------------------------------------------------------------------------
-record(rabbit, {age=0, hunger=0, x=none, y=none, spid=none}).

%% ----------------------------------------------------------------------------
%% @doc Spawns a new rabbit process. When the rabbit is initialized it sends a 
%% message to the java server.
%% @spec newRabbit(Touple::{X::integer(),Y::integer()}, 
%%					SenderPid::pid()) -> {newRabbit, PID, X, Y}
%% @end
%% ---------------------------------------------------------------------------- 

newRabbit({X, Y}, SenderPID) ->
	PID = spawn(rabbits, preloop, [#rabbit{age = 0, hunger = 0, x = X, y = Y, spid = SenderPID}]),
	SenderPID ! {newRabbit, PID, X, Y}.
		


%% ----------------------------------------------------------------------------
%% @doc 
%% @spec
%% @end
%% ---------------------------------------------------------------------------- 

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
				   parseList(Rabbit,T,ListIndex+1,[{Grass,Rabbit#rabbit.x+1,Rabbit#rabbit.y+1}]++Acc);
			   Def->
				   parseList(Rabbit,T,ListIndex+1,Acc)
		   end;
	   Type =/= none ->
		   parseList(Rabbit, T, ListIndex+1, Acc)
	end.
	
%% ----------------------------------------------------------------------------
%% @doc 
%% @spec
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
%% Rabbit's current coordinate.
%% @spec findNewSquare(Rabbit::record(), MapList::list()) -> !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
%% @end
%% ---------------------------------------------------------------------------- 

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

							
%% ----------------------------------------------------------------------------
%% @doc Makes a rabbit who is hungry eat if it there is enough grass 
%% @spec eat(Rabbit::record()) -> {Rabbit, gotFood/noFood, List}
%% @end
%% ---------------------------------------------------------------------------- 

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
	

%% ----------------------------------------------------------------------------
%% @doc Ask the map process for the map and its current layout 
%% @spec getMap(Rabbit::record()) ->  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
	
%% 	PID = Rabbit#rabbit.spid,
%% 	PID ! {get, self()},
%% 	receive
%% 		{map, Array} ->
%% 			Array
%% 	end.



%% ----------------------------------------------------------------------------
%% @doc Initiates all things required to happen during a time tick for a rabbit
%% @spec doTick(Rabbit::record()) -> !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
%% @end
%% ---------------------------------------------------------------------------- 

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

%% ----------------------------------------------------------------------------
%% @doc Calls init() and loop() or kills the rabbits process when that command 
%% is recieved
%% @spec preloop(Rabbit::record()) -> !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
%% @doc 
%% @spec loop(Rabbit::record())
%% @end
%% ---------------------------------------------------------------------------- '

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

%% ----------------------------------------------------------------------------
%% @doc 
%% @spec
%% @end
%% ---------------------------------------------------------------------------- 

sendTick(Pid) ->
	Pid ! {self(), tick}.

%% ----------------------------------------------------------------------------
%% @doc 
%% @spec
%% @end
%% ---------------------------------------------------------------------------- 

getInfo(Pid) ->
	Pid ! {self(), getInfo},
	receive
		{Pid, Rabbit} ->
			Rabbit
	end.

%% ----------------------------------------------------------------------------
%% @doc 
%% @spec
%% @end
%% ---------------------------------------------------------------------------- 

getCoords(Pid) ->
	Pid ! {self(), getCoords},
	receive
		{Pid, Coords} ->
			Coords
	end.

%% ----------------------------------------------------------------------------
%% @doc 
%% @spec
%% @end
%% ---------------------------------------------------------------------------- 

execute(Pid) ->
	Pid ! {self(), die}.

%% ----------------------------------------------------------------------------
%% @doc 
%% @spec
%% @end
%% ---------------------------------------------------------------------------- 

init() ->
	{A1, A2, A3} = now(),
	Mega = lists:nth(2, pid_to_list(self())),
	random:seed(A1 + Mega, A2 + Mega, A3 + Mega).

%% ----------------------------------------------------------------------------
%% @doc 
%% @spec
%% @end
%% ---------------------------------------------------------------------------- 

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