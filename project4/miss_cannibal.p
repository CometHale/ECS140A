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
safe_config(CO,MO,CD,MD) :- CO >= 0, MO >= 0, CO >= MO.
safe_config(CO,MO,CD,MD) :- CD >= 0, MD >= 0, CD >= MD.

/* 
	boat: possible boats.
	[CB,MB,Shore]
*/
boat(2,0,o,Boat) :- Boat = [2,0,o].
boat(2,0,d,Boat) :- Boat = [2,0,d].
boat(0,2,o,Boat) :- Boat = [0,2,o].
boat(0,2,d,Boat) :- Boat = [0,2,d].
boat(1,1,o,Boat) :- Boat = [1,1,o].
boat(1,1,d,Boat) :- Boat = [1,1,d].
boat(1,0,o,Boat) :- Boat = [1,0,o].
boat(1,0,d,Boat) :- Boat = [1,0,d].
boat(0,1,o,Boat) :- Boat = [0,1,o].
boat(0,1,d,Boat) :- Boat = [0,1,d].

%% 	COB: Cannibals Origin Before Move
%% 	MOB: Missionaries Origin Before Move

%% 	CDB: Cannibals Destination Before Move
%% 	MDB: Missionaries Destination Before Move

%% 	COA: Cannibals Origin After Move
%% 	MOA: Missionaries Origin After Move

%% 	CDA: Cannibals Destination After Move
%% 	MDA: Missionaries Destination After Move

%% 	[CB,MB,S2]: Cannibals on Boat, Missionaries on Boat, Shore 2

%% Two Cannibals cross from origin to destination
move([CO,MO,CD,MD,o],[CB,MB,d], NextConfig) :- 
	CB > 1,
	COA = CO - CB,
	CDA = CD + CB, 
	safe_config(COA,MO,CDA,MD), 
	NextConfig = [COA,MO,CDA,MD,d].

%% Two Cannibals cross from destination to origin
move([CO,MO,CD,MD,d],[CB,MB,o], NextConfig) :- 
	CB > 1, 
	COA = CO + CB,
	CDA = CD - CB, 
	safe_config(COA,MO,CDA,MD), 
	NextConfig = [COA,MO,CDA,MD,o].

%% Two Missionaries cross from origin to destination
move([CO,MO,CD,MD,o],[CB,MB,d], NextConfig) :- 
	MB > 1, 
	MOA = MO - MB,
	MDA = MD + MB, 
	safe_config(CO,MOA,CD,MDA), 
	NextConfig = [CO,MOA,CD,MDA,o].

%% Two Missionaries cross from destination to origin
move([CO,MO,CD,MD,d],[CB,MB,o], NextConfig) :- 
	MB > 1, 
	MOA = MO + MB,
	MDA = MD - MB, 
	safe_config(CO,MOA,CD,MDA), 
	NextConfig = [CO,MOA,CD,MDA,d].

%% %% One Missionary and One Cannibal  cross from origin to destination
move([CO,MO,CD,MD,o],[CB,MB,d], NextConfig) :- 
	MB < 2, 
	CB < 2, 
	COA = CO - CB,
	CDA = CD + CB, 
	MOA = MO - MB,
	MDA = MD + MB, 
	safe_config(COA,MOA,CDA,MDA),
	NextConfig = [COA,MOA,CDA,MDA,d].

%% %% One Missionary and One Cannibal  cross from destination to origin
move([CO,MO,CD,MD,d],[CB,MB,o], NextConfig) :- 
	MB < 2, 
	CB < 2, 
	COA = CO + CB,
	CDA = CD - CB, 
	MOA = MO + MB,
	MDA = MD - MB, 
	safe_config(COA,MOA,CDA,MDA),
	NextConfig = [COA,MOA,CDA,MDA,o].

%% One Missionary cross from origin to destination
move([CO,MO,CD,MD,o],[CB,MB,d], NextConfig) :- 
	MB < 2, 
	CB == 0, 
	MOA = MO - MB, 
	MDA = MD + MB, 
	safe_config(CO,MOA,CD,MDA),
	NextConfig = [CO,MOA,CD,MDA,d].

%% One Missionary cross from destination to origin
move([CO,MO,CD,MD,d],[CB,MB,o], NextConfig) :- 
	MB < 2, 
	CB == 0, 
	MOA = MO + MB, 
	MDA = MD - MB, 
	safe_config(CO,MOA,CD,MDA),
	NextConfig = [CO,MOA,CD,MDA,o].

%% One Cannibal cross from origin to destination
move([CO,MO,CD,MD,o],[CB,MB,d], NextConfig) :- 
	CB < 2, 
	MB == 0, 
	COA = CO - CB,
	CDA = CD + CB, 
	safe_config(COA,MO,CDA,MD),
	NextConfig = [COA,MO,CDA,MD,d].

%% One Cannibal cross from destination to origin
move([CO,MO,CD,MD,d],[CB,MB,o], NextConfig) :- 
	CB < 2, 
	MB == 0, 
	COA = CO + CB,
	CDA = CD - CB, 
	safe_config(COA,MO,CDA,MD),
	NextConfig = [COA,MO,CDA,MD,o].

%% recursion, used https://www.cpp.edu/~jrfisher/www/prolog_tutorial/2_16.html
dfs(Config,_,_,_) :- end_state(Config),write('ending...'), !.

dfs([CO,MO,CD,MD,B1],[CB,MB,B2],PreviousConfigs,MoveList) :-
	move([CO,MO,CD,MD,B1],[CB,MB,B2],NextConfig),
	\+member(NextConfig,PreviousConfigs),
	append([[CB,MB,B2]],MoveList,Temp),
	append([NextConfig],PreviousConfigs,Temp2),
	boat(_,_,_,NextBoat),
	dfs(NextConfig,NextBoat,Temp2,Temp).

%% print_moves(MoveList) :- .
miss_cannibal(X) :- 
	Start = [3,3,0,0,o], 
	boat(_,_,_,Boat),
	dfs(Start,Boat,[Start],[]), 
	X = MoveList.
