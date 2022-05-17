!check(slots).

+!check(slots) : not garbage(senzor)
   <- next(slot);
      !check(slots).

+!check(slots) : garbage(senzor)
   <- 
   ?pos(senzor,X,Y)
   .send(controller,achieve,garbage(X,Y));
   next(slot);
   !check(slots).
