
+register(.my_name)[source(R)] :true
 <- .concat(robots,R,L);
    robots = L.

robotsCount(C) :-
    .findall(X,register(X),L) &
    .length(L,C).


+!garbage(X,Y,A)[source(sensor)] : true
  <- 
  .print("Garbage at:" , X , ", " , Y);
  .broadcast(tell,auction(X,Y,A)).

valid(N) :- N > 0.


@pb1[atomic]
+place_bid(X,Y,_)     // receives bids and checks for new winner
   :  .findall(b(V,A),place_bid(X,Y,V)[source(A)],L) &
      robotsCount(C) &
      .length(L,C) &  // all expected bids was received
      .max(L,b(V,W)) &
      valid(V)
   <- .print("Winner is ",W," with ", V);
      //show_winner(N,W); // show it in the GUI
      .broadcast(tell, winner(W,X,Y));
      .abolish(place_bid(X,Y,_)).

@pb2[atomic]
+place_bid(X,Y,_)     // receives bids and checks for new winner
   :  .findall(b(V,A),place_bid(X,Y,V)[source(A)],L) &
      .length(L,4) &  // all 4 expected bids was received
      .max(L,b(V,W)) &
      not valid(V)
   <- .print("Winner is ",W," with ", V);
      //show_winner(N,W); // show it in the GUI
      .broadcast(tell, winner(none,X,Y));
      .abolish(place_bid(X,Y,_)).