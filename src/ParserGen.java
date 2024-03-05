import org.antlr.v4.runtime.misc.Pair;
import rules.GrammarRule;
import rules.NonTerminal;
import rules.Terminal;

import java.util.*;

public class ParserGen {
    private final MyGrammar gr;

    public ParserGen(MyGrammar gr) {
        this.gr = gr;
    }

    public void generateParser() {
        String parserName = gr.getName() + "Parser";
        String lexerName = gr.getName() + "Lexer";

        String javaParserTemplate = """
                package lab4gen.%s;
                                
                import java.text.ParseException;
                                
                public class %s {
                    private %s lex;
                    
                    public Tree parse(String str) throws ParseException {
                        lex = new %s(str);
                        lex.nextToken();
                        return e(0);
                    }
                    
                    %s
                }
                """;

        new MyUtils().writeCode(parserName, String.format(javaParserTemplate
                , gr.getName()
                , parserName
                , lexerName
                , lexerName
                , genTrees()), gr.getName());
    }

    private String genTrees() {
        Set<String> nonTermNames = gr.getNonTermNames();

        List<String> nodes = new ArrayList<>();

        for (String nonTermName : nonTermNames) {
            nodes.add(genTree(nonTermName));
        }

        return String.join("\n\n\n", nodes);

    }

    private String genTree(String nonTermName) {
        List<NonTerminal> nonTerminals = gr.getNonTerminals();
        NonTerminal rule = nonTerminals.stream().filter(nt -> Objects.equals(nt.getName(), nonTermName)).toList().get(0);

        Set<String> nonTermNames = gr.getNonTermNames();

        String defCode = "";
        boolean flag = false;
        for (NonTerminal nonTerminal : nonTerminals) {
            if (flag) {
                break;
            }
            if (!Objects.equals(nonTerminal.getName(), nonTermName)) {
                continue;
            }

            for (Pair<String, String> prod : nonTerminal.getProds()) {
                if (Objects.equals(prod.a, "EPSILON")) {
                    defCode = nonTerminal.getCode();
                    flag = true;
                    break;
                }
            }
        }

        List<GrammarRule> a = gr.getRules().stream().filter(r -> Objects.equals(r.getName(), nonTermName)).toList();

        Map<String, GrammarRule> aFirst = new HashMap<>();

        a.forEach(r -> {
            if (r instanceof Terminal) {
                aFirst.get(r.getName());
            } else if (r instanceof NonTerminal nonTerminal) {
                gr.getFirst().getCases()
                        .get(String.join(" ", nonTerminal.getProds().stream().map(prod -> prod.a).toList()))
                        .stream().filter(x -> !Objects.equals(x, "EPSILON"))
                        .forEach(y -> aFirst.put(y, nonTerminal));
            }
        });

        Set<String> follow = gr.getFollow().getEntity().get(nonTermName);

        //follow.retainAll(aFirst.keySet());

        String ans = """
                private Tree %s(%s) throws ParseException {
                    Tree res = new Tree("%s");
                    Token token = lex.getCurToken().a;
                    String str = lex.getCurToken().b;
                    switch (token) {
                        %s
                        
                        %s
                     
                        default -> throw new AssertionError();
                    }
                    
                    return res;
                }
                """;

        String arg = Objects.equals(rule.getArg().a, "0") ? "Integer x" : rule.getArg().b.name() + " " + rule.getArg().a;

        return String.format(ans
                , nonTermName
                , arg
                , nonTermName
                , genFirst(aFirst, nonTermNames)
                , genFollow(follow, defCode));
    }

    private String genFirst(Map<String, GrammarRule> aFirst, Set<String> nonTermNames) {
        List<String> cases = new ArrayList<>();

        for (Map.Entry<String, GrammarRule> entry : aFirst.entrySet()) {
            cases.add(String.format(
                    """
                            case %s -> {
                                %s
                            }
                            """
                    , entry.getKey()
                    , genCaseEntity((NonTerminal) entry.getValue(), nonTermNames)));
        }


        return String.join("\n", cases);
    }

    private String genCaseEntity(NonTerminal rule, Set<String> nonTermNames) {
        return String.join("\n", rule.getProds().stream().map(prod -> {
            if (nonTermNames.contains(prod.a)) {
                return String.format(
                        """
                                Tree %s = %s(%s);
                                res.children.add(%s);
                                                        
                                """
                        , prod.a
                        , prod.a
                        , prod.b
                        , prod.a);
            } else {
                return String.format(
                        """
                                Tree %s = new Tree("%s");
                                res.children.add(%s);
                                                        
                                lex.nextToken();
                                                        
                                """
                        , prod.a
                        , prod.a
                        , prod.a);
            }
        }).filter(str -> !str.isBlank()).toList()) + String.format("\n  %s;", rule.getCode());

    }

    private String genFollow(Set<String> follow, String defCode) {
        if (defCode.length() > 0) {
            return String.format(
                    """
                            case %s -> %s;
                            """
                    , String.join(", ", follow)
                    , defCode);
        } else if (!follow.isEmpty()) {
            return String.format(
                    """
                            case %s -> {return res;}
                            """
                    , String.join(", ", follow));
        }

        return "";
    }

}
