checking_cells.

at(P) :- pos(P,X,Y) & pos(self,X,Y).

@lbid
+auction(X,Y)[source(S)] : not busy
   <- .send(S, tell, place_bid(X,Y,6)).

+auction(X,Y)[source(S)] : busy
   <- .send(S, tell, place_bid(X,Y,-1)).

+winner(M,X,Y)[source(S)] : .my_name(M)
   <-.abolish(winner(W,X,Y));
     .abolish(auction(X,Y));
     !goto(X,Y).

+winner(W,X,Y) : not .my_name(W)
    <- .abolish(winner(W,X,Y));
       .abolish(auction(X,Y)).

+!move_towards(X,Y) : not pos(self,X,Y)
   <- move_towards(X,Y);
      -checking_cells;
      !move_towards(X,Y).

+!move_towards(X,Y) : pos(self,X,Y)
   <- pick(garb);
      -busy.



+!goto(X,Y) : not busy 
   <- 
   +busy;
   !move_towards(X,Y).

+!goto(X,Y) : busy .
