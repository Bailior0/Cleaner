checking_cells.

at(P) :- pos(P,X,Y) & pos(cleener,X,Y).

+!ensure_pick(S)
   <- pick(garb);
      +checking_cells;
      !ensure_pick(S).
		   
+!move_towards(X,Y) 
   <- move_towards(X,Y);
      -checking_cells;
      !move_towards(X,Y).
	  
+garbage(cleaner) : true
	<- !ensure_pick(S).
		   
+!found(garb)[source(controller)] : checking_cells 
   <- ?pos(L,X,Y);
      !move_towards(X,Y).
								
								

