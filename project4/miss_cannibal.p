%% /* Program to solve the Missionary Cannibal Puzzle */

/* 
[NumCannibalsOrigin,NumMissionariesOrigin,NumCannibalsDestination,
	NumMissionariesDestination,BoatPosition]
BoatPosition is o if at the origin shore and d if at the destination shore.
*/

start_state([3,3,0,0,o]).
end_state([0,0,3,3,d]).

/* 
	safe_config: Is the given configuration a safe one?
	
	Safe:
		NumCannibals >= NumMissionaries on either shore.
*/

safe_config([CO,MO,CD,MD,_]) :- 
	CO >= 0, 
	MO >= 0,
	CD >= 0, 
	MD >= 0,
	CO < 4, 
	MO < 4,
	CD < 4, 
	MD < 4,
	(CO >= MO;CO is 0), 
	(CD >= MD;CD is 0).

%% Two Cannibals cross from origin to destination
move([CO,MO,CD,MD,o],[CO2,MO,CD2,MD,d],Move) :- 
	CO2 is CO - 2,
	CD2 is CD + 2,
	safe_config([CO2,MO,CD2,MD,d]),
	Move = 'cannibal_cannibal ---> destination'.

%% Two Cannibals cross from destination to origin
move([CO,MO,CD,MD,d],[CO2,MO,CD2,MD,o],Move) :- 
	CO2 is CO + 2,
	CD2 is CD - 2,
	safe_config([CO2,MO,CD2,MD,o]),
	Move = 'cannibal_cannibal ---> origin'.

%% Two Missionaries cross from origin to destination
move([CO,MO,CD,MD,o],[CO,MO2,CD,MD2,d],Move) :- 
	MO2 is MO - 2,
	MD2 is MD + 2,
	safe_config([CO,MO2,CD,MD2,d]),
	Move = 'missionary_missionary ---> destination'.

%% Two Missionaries cross from destination to origin
move([CO,MO,CD,MD,d],[CO,MO2,CD,MD2,o],Move) :-
	MO2 is MO + 2,
	MD2 is MD - 2,
	safe_config([CO,MO2,CD,MD2,o]),
	Move = 'missionary_missionary ---> origin'.

%% %% One Missionary and One Cannibal  cross from origin to destination
move([CO,MO,CD,MD,o],[CO2,MO2,CD2,MD2,d],Move) :- 
	CO2 is CO - 1,
	MO2 is MO - 1,
	CD2 is CD + 1,
	MD2 is MD + 1,
	safe_config([CO2,MO2,CD2,MD2,d]),
	Move = 'cannibal_missionary ---> destination'.

%% %% One Missionary and One Cannibal  cross from destination to origin
move([CO,MO,CD,MD,d],[CO2,MO2,CD2,MD2,o],Move) :-
	CO2 is CO + 1,
	MO2 is MO + 1,
	CD2 is CD - 1,
	MD2 is MD - 1,
	safe_config([CO2,MO2,CD2,MD2,o]),
	Move = 'cannibal_missionary ---> origin'.

%% One Missionary cross from origin to destination
move([CO,MO,CD,MD,o],[CO,MO2,CD,MD2,d],Move) :-
	MO2 is MO - 1,
	MD2 is MD + 1,
	safe_config([CO,MO2,CD,MD2,d]),
	Move = 'missionary ---> destination'.

%% One Missionary cross from destination to origin
move([CO,MO,CD,MD,d],[CO,MO2,CD,MD2,o],Move) :-
	MO2 is MO + 1,
	MD2 is MD - 1,
	safe_config([CO,MO2,CD,MD2,o]),
	Move = 'missionary ---> origin'.

%% One Cannibal cross from origin to destination
move([CO,MO,CD,MD,o],[CO2,MO,CD2,MD,d],Move) :-
	CO2 is CO - 1,
	CD2 is CD + 1,
	safe_config([CO2,MO,CD2,MD,d]),
	Move = 'cannibal ---> destination'.

%% One Cannibal cross from destination to origin
move([CO,MO,CD,MD,d],[CO2,MO,CD2,MD,o],Move) :-
	CO2 is CO + 1,
	CD2 is CD - 1,
	safe_config([CO2,MO,CD2,MD,o]),
	Move = 'cannibal ---> origin'.

%% recursion, used https://www.cpp.edu/~jrfisher/www/prolog_tutorial/2_16.html
dfs([CO,MO,CD,MD,B1],_,_,MoveList,Result) :- 
	end_state([CO,MO,CD,MD,B1]),
	Result = MoveList.

dfs([CO,MO,CD,MD,B1],B2,PreviousConfigs,MoveList,Result) :-
	move([CO,MO,CD,MD,B1],[CO2,MO2,CD2,MD2,B2],Temp),
	\+member([CO2,MO2,CD2,MD2,B2],PreviousConfigs),
	dfs([CO2,MO2,CD2,MD2,B2],B1,[[CO2,MO2,CD2,MD2,B2]|PreviousConfigs],[Temp|MoveList],Result).

miss_cannibal(X) :- 
	Start = [3,3,0,0,o], 
	dfs(Start,d,[Start],[],X),!.
