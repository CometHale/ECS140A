/* Program to solve the Missionary Cannibal Puzzle */

/* AXIOMS */
/* 
safe configuration predicate: checks if a given configuration
is 'safe', i.e., no one is converted.
safe_config(NumCannibalsOrigin,NumMissionariesOrigin,NumCannibalsDestination,NumMissionariesDestination,BoatPosition)
BoatPosition is o if at the origin shore and d if at the destination shore.
*/
safe_config([3,_,0,_,_])./* All Cannibals at Origin*/
safe_config([0,_,3,_,_])./* All Cannibals at Destination */
safe_config([Z,Z,_,_,_])./* NumCannibalsOrigin == NumMissionariesOrigin */
safe_config([_,_,Z,Z,_])./* NumCannibalsDestination == NumMissionariesDestination */

%% config(NumCannibals,NumMissionaries,BoatPosition)
mission_acquired([3,3,0,0,o],X).
mission_complete([0,0,3,3,d],X).

/*
	COB: Cannibals Origin Before Move
	MOB: Missionaries Origin Before Move
	CDB: Cannibals Destination Before Move
	MDB: Missionaries Destination Before Move
	COA: Cannibals Origin After Move
	MOA: Missionaries Origin After Move
	CDA: Cannibals Destination After Move
	MDA: Missionaries Destination After Move
	[CB,MB,S2]: Cannibals on Boat, Missionaries on Boat, Shore 2

*/

%% move([COB,MOB,CDB,MDB,_],[COA,MOA,CDA,MDA,_]) :- CB is COB - COA, MB is MOB - MOA,CB > 1,safe_config().

%% Two Cannibals on Boat
move([CO,MO,CD,MD,S1],[CB,MB,S2],X) :- CB > 1, COA is CO - CB,CDA is CD + CB, safe_config([COA,MO,CDA,MD,S2]).

%% Two Missionaries on Boat
move([CO,MO,CD,MD,S1],[CB,MB,S2],X) :- MB > 1, MOA is MO - MB,MDA is MD + MB, safe_config([MOA,MO,MDA,MD,S2]).

%% One Missionary and One Cannibal On Boat
move([CO,MO,CD,MD,S1],[CB,MB,S2],X) :- MB < 2, CB < 2, COA is CO - CB,CDA is CD + CB, MOA is MO - MB,MDA is MD + MB, safe_config([COA,MOA,CDA,MDA,S2]).

%% One Missionary On Boat
move([CO,MO,CD,MD,S1],[CB,MB,S2],X) :- MB < 2, MOA is MO - MB, MDA is MD + MB, safe_config([CO,MOA,CD,MDA,S2]).

%% One Cannibal On Boat
move([CO,MO,CD,MD,S1],[CB,MB,S2],X) :- CB < 2, COA is CO - CB,CDA is CD + CB, safe_config([COA,MO,CDA,MD,S2]).



mission_solve(X) :- mission_acquired([3,3,0,0,o],X), write("starting...").