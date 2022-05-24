package cleanerSim;

import jason.environment.grid.Location;

public class Cleaner {
    Location loc;
    int size = 5;
    int currentAmount = 0;

    public Cleaner() {
        loc = new Location(0,0);
    }

    public Cleaner(Location loc, int size, int currentAmount) {
        this.loc = loc;
        this.size = size;
        this.currentAmount = currentAmount;
    }

    int remainingSpace() {
        return  size-currentAmount;
    }

    boolean getIsFull() {
        return  size <= currentAmount;
    }
}
