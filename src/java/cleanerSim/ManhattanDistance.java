package cleanerSim;

import jason.*;
import jason.asSyntax.*;
import jason.asSemantics.*;

public class ManhattanDistance extends DefaultInternalAction {
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args ) throws Exception {
        try {
            NumberTerm p1x = (NumberTerm)args[0];
            NumberTerm p1y = (NumberTerm)args[1];
            NumberTerm p2x = (NumberTerm)args[2];
            NumberTerm p2y = (NumberTerm)args[3];
            double r = Math.abs(p1x.solve()-p2x.solve()) + Math.abs(p1y.solve()-p2y.solve());
            if(r == 0)
                r = 0.1;
            NumberTerm result = new NumberTermImpl(r);
            return un.unifies(result,args[4]);

        } catch (Exception e) {
            throw e;
        }
    }
}
