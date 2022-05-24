package cleanerSim;

import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.TimeSteppedEnvironment;
import jason.environment.grid.Location;

import java.util.Collection;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HouseEnv extends Environment {

    public static final int HouseSize = 20;


    public static final Term ns = Literal.parseLiteral("next(slot)");
    public static final Term pg = Literal.parseLiteral("pick(garb)");
    public static final Term putGarb = Literal.parseLiteral("put(garb)");

    private Random rnd= new Random(11);

    private HouseWorldModel model;
    private HouseWorldView view;

    @Override
    public void init(String[] args) {
        super.init(new String[] {  } ); // set step timeout
        //setOverActionsPolicy(OverActionsPolicy.ignoreSecond);

        model = new HouseWorldModel(HouseSize,HouseSize);
        view  = new HouseWorldView(model);
        model.setView(view);

        model.dirtMap[3][0] = 1;

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                stepFinished(0,0,false);
            }
        };
        var timer = new Timer();
        timer.schedule(timerTask, 0,200);
    }

    @Override
    public Collection<Literal> getPercepts(String agName) {
        updateAgPercept(agName);

        return super.getPercepts(agName);
    }


    protected void stepFinished(int step, long time, boolean timeout) {

        if( rnd.nextInt(100) < 50) {
            int x = rnd.nextInt(HouseSize);
            int y = rnd.nextInt(HouseSize);
            model.addDirt(x,y,rnd.nextInt(3)+1);
        }

        if (step % 5 == 0) {

            if (view != null) {
                view.update();
            }
        }

    }


    void updateAgPercept(String agName) {
        clearPercepts(agName);


        Location l = model.getAgPos(agName);
        Literal lpos = ASSyntax.createLiteral("pos",
                ASSyntax.createAtom("self"),
                ASSyntax.createNumber(l.x),
                ASSyntax.createNumber(l.y));
        addPercept(agName, lpos);

        if(agName.contains("sensor")){
            if(model.dirtMap[l.x][l.y] > 0) {
                Literal lgarbage = ASSyntax.createLiteral("garbage",
                        ASSyntax.createAtom("sensor"),ASSyntax.createNumber(model.dirtMap[l.x][l.y]));
                addPercept(agName, lgarbage);
            }
        }
        if(agName.contains("cleaner")) {
            Cleaner c =  model.getCleaner(agName);

            Literal literal = ASSyntax.createLiteral("stored",
                    ASSyntax.createAtom("cleaner"),
                    ASSyntax.createNumber(c.currentAmount));
            addPercept(agName,literal);

        }

    }

    @Override
    public boolean executeAction(String ag, Structure action) {
        //logger.info(ag+" doing: "+ action);
        try {
            if (action.equals(ns)) {
                model.nextSlot(ag);
            } else if (action.getFunctor().equals("move_towards")) {
                int x = (int)((NumberTerm)action.getTerm(0)).solve();
                int y = (int)((NumberTerm)action.getTerm(1)).solve();
                model.moveTowards(ag,x,y);
            } else if (action.equals(pg)) {
                model.pickGarb(ag,model.getAgPos(ag));
            } else if (action.equals(putGarb)) {
                model.putGarb(ag);
            }
            else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //updatePercepts();


        try {
            Thread.sleep(200);
        } catch (Exception e) {}
        informAgsEnvironmentChanged();
        return true;
    }

}
