import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.*;
import gen.*;
import rules.GrammarRule;
import rules.NonTerminal;
import rules.Terminal;
import types.*;
import sets.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class MyGrammar {

    private final String name;

    private final List<GrammarRule> rules;

    private final Set<Pair<String, Attribute>> attrs;

    private final List<Terminal> terminals;

    private final List<NonTerminal> nonTerminals;

    private final First first;

    private final Follow follow;

    private final Set<String> nonTermNames;

    public MyGrammar(String name, List<GrammarRule> rules, Set<Pair<String, Attribute>> attrs) {
        this.name = name;
        this.rules = rules;
        this.attrs = attrs;

        List<Terminal> tempTerm = new ArrayList<>();

        for (GrammarRule x : rules) {
            if (x instanceof Terminal) {
                tempTerm.add((Terminal) x);
            }
        }

        this.terminals = tempTerm;

        List<NonTerminal> tempNonTerm = new ArrayList<>();

        for (GrammarRule x : rules) {
            if (x instanceof NonTerminal) {
                tempNonTerm.add((NonTerminal) x);
            }
        }

        this.nonTerminals = tempNonTerm;

        this.nonTermNames = nonTerminals
                .stream()
                .map(GrammarRule::getName)
                .collect(Collectors.toSet());

        this.first = buildFirst(emptyFirst(this));

        this.follow = buildFollow(emptyFollow(this));
    }

    public boolean checkLL1() {
        boolean flag1 = true;
        boolean flag2 = true;

        for (GrammarRule rule1 : rules) {
            for (GrammarRule rule2 : rules) {
                if (!Objects.equals(rule1, rule2)
                        && rule1 instanceof NonTerminal ntm1
                        && rule2 instanceof NonTerminal ntm2
                        && Objects.equals(rule1.getName(), rule2.getName())) {
                    String ntmName = rule1.getName();

                    String rightPart1 = String.join(" ", ntm1.getProds().stream().map(prod -> prod.a).toList());
                    String rightPart2 = String.join(" ", ntm2.getProds().stream().map(prod -> prod.a).toList());

                    Set<String> first1 = getFirst().getCases().get(rightPart1);
                    Set<String> first2 = getFirst().getCases().get(rightPart2);

                    System.out.println("Name: " + ntm1.getName() + " first1: " + first1.toString() + " first2: " + first2.toString());

                    if (first1.contains("EPSILON")) {
                        Set<String> follow = getFollow().getEntity().get(ntmName);

                        System.out.println("Follow: " + follow.toString() + " first2: " + first2);

                        follow.retainAll(first2);
                        if (!follow.isEmpty()) {
                            flag2 = false;
                        }
                    }

                    if (first2.contains("EPSILON")) {
                        Set<String> follow = getFollow().getEntity().get(ntmName);

                        System.out.println("Follow: " + follow.toString() + " first1: " + first1);

                        follow.retainAll(first1);
                        if (!follow.isEmpty()) {
                            flag2 = false;
                        }
                    }


                    first1.retainAll(first2);
                    if (!first1.isEmpty()) {
                        flag1 = false;
                    }
                }
            }
        }

        return flag1 && flag2;
    }

    private First emptyFirst(MyGrammar gr) {
        Map<String, Set<String>> entity = new HashMap<>();
        Map<String, Set<String>> alts = new HashMap<>();

        for (GrammarRule rule : gr.getRules()) {
            entity.put(rule.getName(), new HashSet<>());

            String key = "";

            if (rule instanceof Terminal) {
                key = rule.getName();
            } else if (rule instanceof NonTerminal nonTerminal) {
                key = String.join(" ", nonTerminal.getProds().stream().map(prod -> prod.a).toList());
            }

            alts.put(key, new HashSet<>());
        }

        return new First(entity, alts);
    }

    private First buildFirst(First first) {
        boolean changed = true;

        while (changed) {
            changed = false;

            for (GrammarRule rule : getRules()) {
                Set<String> now = new HashSet<>(first.getEntity().get(rule.getName()));
                first.addRule(rule);

                if (!Objects.equals(first.getEntity().get(rule.getName()), now)) {
                    changed = true;
                }
            }
        }

        return first;
    }

    private Follow emptyFollow(MyGrammar gr) {
        Follow follow = new Follow(new HashMap<>());

        for (GrammarRule rule : gr.rules) {
            follow.getEntity().put(rule.getName(), new HashSet<>());
        }

        follow.getEntity().get(gr.rules.get(0).getName()).add("END");

        return follow;
    }

    private Follow buildFollow(Follow follow) {
        boolean changed = true;

        while (changed) {
            changed = false;

            for (NonTerminal nonTerminal : nonTerminals) {
                List<Pair<String, String>> prods = nonTerminal.getProds();

                for (int i = 0; i < prods.size(); ++i) {

                    String prod_name = prods.get(i).a;

                    if (!nonTermNames.contains(prod_name)) {
                        continue;
                    }

                    String g = (i >= nonTerminal.getProds().size() - 1) ?
                            "EPSILON" :
                            nonTerminal.getProds().get(i + 1).a;

                    Set<String> now = new HashSet<>(follow.getEntity().get(prod_name));

                    follow.getEntity().get(prod_name).addAll(first.getEntity().get(g)
                            .stream()
                            .filter(term -> !Objects.equals(term, "EPSILON"))
                            .collect(Collectors.toSet()));

                    if (first.getEntity().get(g).contains("EPSILON")) {
                        follow.getEntity().get(prod_name).addAll(follow.getEntity().get(nonTerminal.getName()));
                    }

                    if (!Objects.equals(follow.getEntity().get(prod_name), now)) {
                        changed = true;
                    }
                }
            }
        }

        return follow;
    }

    public MyGrammar(Path grPath) throws IOException {
        MyGrammar temp = new GrammarListener().build(grPath);

        this.name = temp.name;
        this.rules = temp.rules;
        this.attrs = temp.attrs;
        this.terminals = temp.terminals;
        this.nonTerminals = temp.nonTerminals;
        this.nonTermNames = temp.nonTermNames;
        this.first = temp.first;
        this.follow = temp.follow;
    }

    public String getName() {
        return name;
    }

    public List<GrammarRule> getRules() {
        return rules;
    }

    public Set<Pair<String, Attribute>> getAttrs() {
        return attrs;
    }

    public List<Terminal> getTerminals() {
        return terminals;
    }

    public List<NonTerminal> getNonTerminals() {
        return nonTerminals;
    }

    public First getFirst() {
        return first;
    }

    public Follow getFollow() {
        return follow;
    }

    public Set<String> getNonTermNames() {
        return nonTermNames;
    }

    private static class GrammarListener extends TemplateBaseListener {

        private List<GrammarRule> rules;
        private Set<Pair<String, Attribute>> attrs;

        @Override
        public void exitTermRule(TemplateParser.TermRuleContext ctx) {
            rules.add(new Terminal(
                    ctx.TERM().getText(),
                    ctx.REGEX().getText()));
        }

        @Override
        public void exitNonTermRule(TemplateParser.NonTermRuleContext ctx) {
            String name = ctx.ID().getText();
            Pair<String, Attribute> arg = createField(ctx.attr());

            rules.addAll(ctx.case_().stream().map(altCtx -> createRule(altCtx, name, arg)).toList());
        }

        @Override
        public void exitAttrs(TemplateParser.AttrsContext ctx) {
            attrs.addAll(ctx == null ?
                    new ArrayList<>() :
                    ctx.attr().stream().map(this::createField).toList());
        }

        private Pair<String, Attribute> createField(TemplateParser.AttrContext ctx) {
            if (ctx == null)
                return new Pair<>("0", new StringType());
            else
                return new Pair<>(ctx.ID(0).getText(),
                        atrValueOf(ctx.ID(1).getText()));
        }

        private GrammarRule createRule(TemplateParser.CaseContext altCtx,
                                       String name,
                                       Pair<String, Attribute> arg) {
            String code = altCtx.code() == null ? "" : String.join("", Arrays.stream(altCtx.code()
                    .getText()
                    .chars()
                    .filter(c -> c != '{' && c != '}')
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString()
                    .split(System.lineSeparator())).map(String::strip).toList());

            List<TemplateParser.ProdContext> names = altCtx.prod();

            List<Pair<String, String>> prod = names.stream().map(n -> {
                String passedArg;
                if (n.inherAttr() == null) {
                    passedArg = "0";
                } else {
                    passedArg = n.inherAttr()
                            .TRIANGLE_CODE()
                            .getText()
                            .chars()
                            .filter(c -> c != '<' && c != '>')
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();
                }

                String id = n.ID() == null ?
                        n.TERM().getText() :
                        n.ID().getText();

                return new Pair<>(id, passedArg);
            }).toList();

            return new NonTerminal(name, code, arg, prod);
        }

        private Attribute atrValueOf(String str) {
            switch (str) {
                case "Integer" -> {
                    return new IntType();
                }
                case "Boolean" -> {
                    return new BooleanType();
                }
                case "String" -> {
                    return new StringType();
                }
                default -> {
                    return null;
                }
            }
        }

        public MyGrammar build(Path path) throws IOException {
            TemplateLexer lexer = new TemplateLexer(CharStreams.fromPath(path));
            TemplateParser parser = new TemplateParser(new CommonTokenStream(lexer));

            TemplateParser.TemplateContext context = parser.template();

            GrammarListener listener = new GrammarListener();
            listener.attrs = new HashSet<>();
            listener.rules = new ArrayList<>();
            ParseTreeWalker walker = new ParseTreeWalker();

            walker.walk(listener, context);

            listener.rules.add(new Terminal("EPSILON", ""));

            return new MyGrammar(
                    context.grammarName().ID().getText(),
                    listener.rules,
                    listener.attrs
            );
        }
    }
}
