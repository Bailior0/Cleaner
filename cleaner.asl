cleaning.

hasSpaceFor(A) :-
    stored(cleaner, C) &
    capacity(self, X) &
    L = X - C &
    L > A.

bid_value(X,Y,S,B) :-
    pos(self,U,V) &
    cleanerSim.ManhattanDistance(X,Y,U,V,D) &
    B = 1/D.

//Aukció stuff

@lbid
+auction(X,Y,A)[source(S)] : not busy & hasSpaceFor(A)
   <- ?bid_value(X,Y,A,B);
      .send(S, tell, place_bid(X,Y,B)).

+auction(X,Y,A)[source(S)] : busy
   <- .send(S, tell, place_bid(X,Y,-1)).

+auction(X,Y,A)[source(S)] : not hasSpaceFor(A)
   <- .send(S, tell, place_bid(X,Y,-1));
      -cleaning;
      +dispose;
      !goto(0,0).

+winner(M,X,Y)[source(S)] : .my_name(M)
   <-.abolish(winner(W,X,Y));
     .abolish(auction(X,Y,_));
     !goto(X,Y).

+winner(W,X,Y) : not .my_name(W)
    <- .abolish(winner(W,X,Y));
       .abolish(auction(X,Y,_)).

//Full detection

+full(cleaner) : true
 <- -cleaning;
    +dispose;
    !goto(0,0).

//Moving

+!move_towards(X,Y) : not pos(self,X,Y)
   <- move_towards(X,Y);
      -checking_cells;
      !move_towards(X,Y).

+!move_towards(X,Y) : pos(self,X,Y) & cleaning
   <- -busy;
      pick(garb).

+!move_towards(X,Y) : pos(self,X,Y) & dispose
 <- -busy;
    put(garb);
    -dispose;
    +cleaning.

+!goto(X,Y) : not busy 
   <- 
   +busy;
   !move_towards(X,Y).

+!goto(X,Y) : busy .
