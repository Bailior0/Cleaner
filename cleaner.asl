checking_cells.

at(P) :- pos(P,X,Y) & pos(cleaner,X,Y).

+!ensure_pick(S)
   <- pick(garb);
      +checking_cells;
      !ensure_pick(S).
		   
+!move_towards(X,Y) : not pos(cleaner,X,Y)
   <- move_towards(X,Y);
      -checking_cells;
      !move_towards(X,Y).

+!move_towards(X,Y) : pos(cleaner,X,Y)
   <- 
      -busy.
	  
+garbage(cleaner) : true
	<- !ensure_pick(S).



+!goto(X,Y)[source(controller)] : not busy 
   <- 
   +busy;
   !move_towards(X,Y).
								
								

