package cleanerSim;

import jason.environment.grid.Location;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class HouseWorldModel {

    protected int width, height;

    int[][] dirtMap;
    int[][] dirtTypeMap;

    Map<String, Cleaner> agLocation = new Hashtable<>();
    public Location dumpsterLocation = new Location(0,0);
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

    public List<Map.Entry<String, Cleaner>> getAgs(){
        return agLocation.entrySet().stream().toList();
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
        Location senzor = getCleaner(ag).loc;
        senzor.x++;
        if (senzor.x == getWidth()) {
            senzor.x = 0;
            senzor.y++;
        }
        // finished searching the whole grid
        if (senzor.y == getHeight()) {
            getCleaner(ag).loc = new Location(0,0);
            senzor = getCleaner(ag).loc;
        }

        getCleaner(ag).loc = senzor;
    }

    void moveTowards(String ag, int x, int y) throws Exception {
        Location cleaner = getAgPos(ag);
        if (cleaner.x < x)
            cleaner.x++;
        else if (cleaner.x > x)
            cleaner.x--;
        if (cleaner.y < y)
            cleaner.y++;
        else if (cleaner.y > y)
            cleaner.y--;
        getCleaner(ag).loc = cleaner;
    }

    void pickGarb(String agName,Location loc){
        pickGarb(agName,loc.x,loc.y);
    }

    void pickGarb(String agName, int x, int y) {
        int amount = dirtMap[x][y];
        dirtMap[x][y] = 0;

        getCleaner(agName).currentAmount += amount;

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
        return getCleaner(agName).loc;
    }

    public Cleaner getCleaner(String ag){
        if(agLocation.containsKey(ag)){
            return agLocation.get(ag);
        }
        var a = new Cleaner();
        agLocation.put(ag,a);
        return a;
    }

    public void putGarb(String ag) {
        getCleaner(ag).currentAmount = 0;
    }
}
