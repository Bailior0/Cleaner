import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.logging.Logger;

public class CleanerEnv extends Environment {

    public static final int GSize = 7; // grid size
    public static final int GARB  = 16; // garbage code in grid model
	public static int num  = 0;

    public static final Term    ns = Literal.parseLiteral("next(slot)");
    public static final Term    pg = Literal.parseLiteral("pick(garb)");
    public static final Literal s1 = Literal.parseLiteral("garbage(sensor)");
	public static final Literal c1 = Literal.parseLiteral("garbage(controller)");
	public static final Literal r1 = Literal.parseLiteral("garbage(cleaner)");

    static Logger logger = Logger.getLogger(CleanerEnv.class.getName());

    private CleanerModel model;
    private CleanerView  view;

    @Override
    public void init(String[] args) {
        model = new CleanerModel();
        view  = new CleanerView(model);
        model.setView(view);
        updatePercepts();
    }

    @Override
    public boolean executeAction(String ag, Structure action) {
        logger.info(ag+" doing: "+ action);
        try {
            if (action.equals(ns)) {
                model.nextSlot();
            } else if (action.getFunctor().equals("move_towards")) {
                int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                model.moveTowards(x,y);
            } else if (action.equals(pg)) {
                model.pickGarb();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        updatePercepts();

        try {
            Thread.sleep(200);
        } catch (Exception e) {}
        informAgsEnvironmentChanged();
        return true;
    }

    /** creates the agents perception based on the CleanerModel */
    void updatePercepts() {
        clearPercepts();

        Location s1Loc = model.getAgPos(0);
        Location r1Loc = model.getAgPos(1);

        Literal pos1 = Literal.parseLiteral("pos(self," + s1Loc.x + "," + s1Loc.y + ")");
        Literal pos2 = Literal.parseLiteral("pos(cleaner," + r1Loc.x + "," + r1Loc.y + ")");

        addPercept(pos1);
        addPercept(pos2);

        //Literal pos1 = Literal.parseLiteral("pos(senzor," + s1Loc.x + "," + s1Loc.y + ")");
        //removePerceptsByUnif("senzor",pos1);
        //addPercept(pos1);

        if (model.hasObject(GARB, s1Loc)) {
            addPercept("sensor",s1);
        }
        else{
            removePercept("sensor",s1);
        }
        if (model.hasObject(GARB, r1Loc)) {
            //addPercept(r1);
        }
    }

    class CleanerModel extends GridWorldModel {

        public static final int MErr = 2; // max error in pick garb
        int nerr; // number of tries of pick garb
        boolean r1HasGarb = false; // whether r1 is carrying garbage or not

        Random random = new Random(System.currentTimeMillis());

        private CleanerModel() {
            super(GSize, GSize, 2);

            // initial location of agents
            try {
                setAgPos(0, 0, 0);
				
				Location r1Loc = new Location(GSize/2, GSize/2);
                setAgPos(1, r1Loc);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // initial location of garbage
            add(GARB, 3, 0);
            add(GARB, GSize-1, 0);
            add(GARB, 1, 2);
            add(GARB, 0, GSize-2);
            add(GARB, GSize-1, GSize-1);
        }

        void nextSlot() throws Exception {
            Location senzor = getAgPos(0);
            senzor.x++;
            if (senzor.x == getWidth()) {
                senzor.x = 0;
                senzor.y++;
            }
            // finished searching the whole grid
            if (senzor.y == getHeight()) {
				setAgPos(0, 0, 0);
				senzor = getAgPos(0);
            }
			setAgPos(1, getAgPos(1));
            setAgPos(0, senzor);
        }

        void moveTowards(int x, int y) throws Exception {
            Location cleener = getAgPos(1);
            if (cleener.x < x)
                cleener.x++;
            else if (cleener.x > x)
                cleener.x--;
            if (cleener.y < y)
                cleener.y++;
            else if (cleener.y > y)
                cleener.y--;
            setAgPos(1, cleener);
            setAgPos(0, getAgPos(0));
        }

        void pickGarb() {
            if (model.hasObject(GARB, getAgPos(1))) {
                if (random.nextBoolean() || nerr == MErr) {
                    remove(GARB, getAgPos(1));
                    nerr = 0;
                    r1HasGarb = true;
					num++;
                } else {
                    nerr++;
                }
            }
        }
    }

    class CleanerView extends GridWorldView {

        public CleanerView(CleanerModel model) {
            super(model, "Cleaner World", 600);
            defaultFont = new Font("Arial", Font.BOLD, 18); // change default font
            setVisible(true);
            repaint();
        }

        
        @Override
        public void draw(Graphics g, int x, int y, int object) {
            switch (object) {
            case CleanerEnv.GARB:
                drawGarb(g, x, y);
                break;
            }
        }

        @Override
        public void drawAgent(Graphics g, int x, int y, Color c, int id) {
            String label = "R"+(id+1) + " G:" + num;
            c = Color.blue;
			if (((CleanerModel)model).r1HasGarb) {
                    c = Color.orange;
            }
            if (id == 0) {
				label = "S"+(id+1);
                c = Color.white;
                
            }
			if (id!= -1) {
				super.drawAgent(g, x, y, c, -1);
			}
            if (id == 0) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.white);
            }
			if (id != -1) {
				super.drawString(g, x, y, defaultFont, label);
			}
            repaint();
        }

        public void drawGarb(Graphics g, int x, int y) {
            super.drawObstacle(g, x, y);
            g.setColor(Color.white);
            drawString(g, x, y, defaultFont, "G");
        }

    }
}
