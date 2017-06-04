color(red).
color(white).
color(blue).
color(gold).

/* checkList: recursive function that checks if our list is valid / no adj country has the same color ; returns true if color is valid*/
checkList(_, Color, AdjCs, [LiH | _]) :- nth0(0, LiH, AdjC), member(AdjC, AdjCs), nth0(1, LiH, AdjClr), AdjClr = Color, !.
checkList(Country, Color, AdjCs, [_ | LiT]) :- checkList(Country, Color, AdjCs, LiT).

/* colorMap: recursive function */
colorMap([], OldLi, NewLi) :- NewLi = OldLi, !.
colorMap([CtrH | CtrT], OldLi, NewLi) :- 
  findall(X, is_adjacent(CtrH, X), AdjCs), 
  color(Color), 
  \+checkList(CtrH, Color, AdjCs, OldLi), 
  append(OldLi, [[CtrH, Color]], Li), 
  colorMap(CtrT, Li, NewLi).

mapcolor(List) :- findall(X, is_adjacent(X, _), Countries), sort(Countries), colorMap(Countries, [], List), !.