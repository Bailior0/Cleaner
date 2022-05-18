+!garbage(X,Y)[source(sensor)] : true
  <- 
  .print("Garbage at:" , X , ", " , Y);
  .send(cleaner,achieve,goto(X,Y)).
  
