!check(slots).

+!check(slots) : not garbage(sensor, A)
   <- next(slot);
      !check(slots).

+!check(slots) : garbage(sensor, A)
   <-
   ?pos(self,X,Y);
   .send(controller,achieve,garbage(X,Y,A));
   next(slot);
   !check(slots).
