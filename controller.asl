+!garbage(X,Y)[source(senzor)] : true
  <- 
  .print("Garbage at:" , X , ", " , Y)
  .send(cleaner,achieve,goto(X,Y)).
  
