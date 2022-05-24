package cleanerSim;

import jason.asSemantics.Agent;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.NumberTermImpl;
import jason.asSyntax.VarTerm;
import jason.environment.grid.Location;

public class Entity extends Agent {

    private Literal pos = Literal.parseLiteral("capacity(self, X)");

    @Override
    public void initAg() {
        super.initAg();

        var b = getTS().getSettings().getUserParameter("size");
        System.out.println(b);
        var cap = Integer.parseInt(b);

        this.addInitialBel(Literal.parseLiteral("capacity(self,"+cap+")"));
    }

}
