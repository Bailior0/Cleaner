package cleanerSim;

import jason.environment.grid.Location;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class HouseWorldModel {

    protected int width, height;

    int[][] dirtMap;
    int[][] dirtTypeMap;

    Map<String, Location> agLocation = new Hashtable<>();

    protected HouseWorldView view;

    public HouseWorldView getView() {
        return view;
    }

    public void setView(HouseWorldView view) {
        this.view = view;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public HouseWorldModel(int x, int y) {
        this.width = x;
        this.height = y;

        dirtMap = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                dirtMap[i][j] = 0;
            }
        }
    }

    void nextSlot(String ag) throws Exception {
        Location senzor = agLocation.get(ag);
        senzor.x++;
        if (senzor.x == getWidth()) {
            senzor.x = 0;
            senzor.y++;
        }
        // finished searching the whole grid
        if (senzor.y == getHeight()) {
            //setAgPos(0, 0, 0);
            agLocation.put(ag, new Location(0,0));
            senzor = agLocation.get(ag);
        }
        //setAgPos(1, getAgPos(1));
        //setAgPos(0, senzor);
        agLocation.put(ag, senzor);
    }

    void moveTowards(String ag, int x, int y) throws Exception {
        Location cleaner = agLocation.get(ag);
        if (cleaner.x < x)
            cleaner.x++;
        else if (cleaner.x > x)
            cleaner.x--;
        if (cleaner.y < y)
            cleaner.y++;
        else if (cleaner.y > y)
            cleaner.y--;
        agLocation.put(ag, cleaner);
    }

    void pickGarb(Location loc){
        pickGarb(loc.x,loc.y);
    }

    void pickGarb(int x, int y) {
        dirtMap[x][y] = 0;
        /*if (model.hasObject(GARB, getAgPos(1))) {
            if (random.nextBoolean() || nerr == MErr) {
                remove(GARB, getAgPos(1));
                nerr = 0;
                r1HasGarb = true;
                num++;
            } else {
                nerr++;
            }
        }*/
    }


    void addDirt(int x,int y, int amount){
        this.dirtMap[x][y] = amount;
    }

    public Location getAgPos(String agName) {
        if(!agLocation.containsKey(agName)){
            agLocation.put(agName, new Location(0,0));
        }
        return agLocation.get(agName);
    }
}
