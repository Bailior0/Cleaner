+!garbage(X,Y)[source(sensor)] : true
  <- 
  .print("Garbage at:" , X , ", " , Y);
  .broadcast(tell,auction(X,Y)).
 
  
@pb1[atomic]
+place_bid(X,Y,_)     // receives bids and checks for new winner
   :  .findall(b(V,A),place_bid(X,Y,V)[source(A)],L) &
      .length(L,4)  // all 4 expected bids was received
   <- .max(L,b(V,W));
      .print("Winner is ",W," with ", V);
      //show_winner(N,W); // show it in the GUI
      .broadcast(tell, winner(W,X,Y));
      .abolish(place_bid(X,Y,_)).