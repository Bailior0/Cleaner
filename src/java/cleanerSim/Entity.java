package cleanerSim;

import jason.asSemantics.Agent;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.VarTerm;
import jason.environment.grid.Location;

public class Entity extends Agent {

    private Literal pos = Literal.parseLiteral("pos(self,X,Y)");

    public Location location;

    @Override
    public boolean believes(LogicalFormula bel, Unifier un) {

        if(bel.equals(pos)) {
            System.out.println("asd");
            var a = Literal.parseLiteral("pos(self,"+1+","+ 2 +")");
            //un.compose(a);
            un.bind(new VarTerm("X"), new NumberTermImpl(location.x));
            un.bind(new VarTerm("Y"), new NumberTermImpl(location.y));
            return true;
        }

        return super.believes(bel, un);
    }
}
