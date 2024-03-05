package rules;

import org.antlr.v4.runtime.misc.Pair;
import types.Attribute;

import java.util.List;

public class NonTerminal extends GrammarRule {
    private Pair<String, Attribute> arg;
    private List<Pair<String, String>> prods;

    public NonTerminal(String name, String code, Pair<String, Attribute> arg, List<Pair<String, String>> prod) {
        super(name, code);
        this.arg = arg;
        this.prods = prod;
    }

    public Pair<String, Attribute> getArg() {
        return arg;
    }

    public List<Pair<String, String>> getProds() {
        return prods;
    }

    public void setArg(Pair<String, Attribute> arg) {
        this.arg = arg;
    }

    public void setProd(List<Pair<String, String>> prods) {
        this.prods = prods;
    }

}
