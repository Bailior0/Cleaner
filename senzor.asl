!check(slots).

+!check(slots) : not garbage(senzor)
   <- next(slot);
      !check(slots).
+!check(slots).

+garbage(senzor) : true <- .send(controller,achieve,found(garb));
	next(slot);
	!check(slots).
