!check(slots).

+!check(slots) : not garbage(sensor)
   <- next(slot);
      !check(slots).

+!check(slots) : garbage(sensor)
   <- 
   ?pos(self,X,Y);
   .send(controller,achieve,garbage(X,Y));
   next(slot);
   !check(slots).
