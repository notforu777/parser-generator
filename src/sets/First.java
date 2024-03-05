package sets;

import org.antlr.v4.runtime.misc.Pair;
import rules.GrammarRule;
import rules.NonTerminal;
import rules.Terminal;

import java.util.Map;
import java.util.Set;

public class First {
    private Map<String, Set<String>> entity;
    private Map<String, Set<String>> cases;

    public First(Map<String, Set<String>> nonTerminals, Map<String, Set<String>> alternatives) {
        this.entity = nonTerminals;
        this.cases = alternatives;
    }

    public Map<String, Set<String>> getEntity() {
        return entity;
    }

    public Map<String, Set<String>> getCases() {
        return cases;
    }

    public void setEntity(Map<String, Set<String>> nonTerminals) {
        this.entity = nonTerminals;
    }

    public void setCases(Map<String, Set<String>> alternatives) {
        this.cases = alternatives;
    }

    public void addRule(GrammarRule rule) {
        if (rule instanceof Terminal) {
            entity.get(rule.getName()).add(rule.getName());
            cases.get(rule.getName()).add(rule.getName());
        } else if (rule instanceof NonTerminal nonTerminal) {
            String rightPart = String.join(" ", nonTerminal.getProds().stream().map(prod -> prod.a).toList());

            Pair<String, String> prod = nonTerminal.getProds().get(0);

            entity.get(nonTerminal.getName()).addAll(entity.get(prod.a));
            cases.get(rightPart).addAll(entity.get(prod.a));
        }
    }
}