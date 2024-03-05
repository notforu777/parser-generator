import lab4gen.Calc.CalcLexer;
import lab4gen.Calc.CalcParser;
import lab4gen.Calc.Token;
import lab4gen.PySubset.PySubsetLexer;
import lab4gen.PySubset.PySubsetParser;
import org.antlr.v4.runtime.misc.Pair;
import rules.GrammarRule;
import rules.NonTerminal;
import rules.Terminal;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        String first = "Calc.txt";
        String second = "PySubset.txt";

        Path grPath = Path.of("").resolve("grammars/" + first);

        try {
            /*MyGrammar gr = new MyGrammar(grPath);

            LexGen lexGen = new LexGen(gr);
            ParserGen parserGen = new ParserGen(gr);

            lexGen.generateLexer();
            parserGen.generateParser();

            grDump(gr);

            System.out.println(gr.checkLL1());*/

            String s = "--1";

            CalcLexer calcLexer = new CalcLexer(s);
            while (calcLexer.getCurToken().a != Token.END) {
                calcLexer.nextToken();

                Pair<Token, String> token = calcLexer.getCurToken();

                System.out.println(token.a + " " + token.b);
            }

            CalcParser calcParser = new CalcParser();
            int val = calcParser.parse("--1-2-3+(--6)").getV();

            System.out.println("Ans: " + val);

            /*String s = "(a and b) or not (c xor (a or not b))";

            PySubsetLexer pySubsetLexer = new PySubsetLexer(s);
            while (pySubsetLexer.getCurToken().a != Token.END) {
                pySubsetLexer.nextToken();

                Pair<Token, String> token = pySubsetLexer.getCurToken();

                System.out.println(token.a + " " + token.b);
            }

            System.out.println(s);

            PySubsetParser pySubsetParser = new PySubsetParser();
            pySubsetParser.parse(s);*/

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static void grDump(MyGrammar gr) {
        for (GrammarRule rule : gr.getRules()) {
            if (rule instanceof Terminal) {
                System.out.println("name: " + rule.getName() +
                        " code: " + rule.getCode() +
                        " pattern: " + ((Terminal) rule).getPattern());
            }
        }

        for (GrammarRule rule : gr.getRules()) {
            if (rule instanceof NonTerminal) {
                System.out.println();
                System.out.println("name: " + rule.getName() +
                        " code: " + rule.getCode());
                System.out.println("arg: " + ((NonTerminal) rule).getArg().a + " " + ((NonTerminal) rule).getArg().b.name());
                System.out.println("prods: " + ((NonTerminal) rule).getProds());
            }
        }

        System.out.println("First: " + gr.getFirst().getEntity().toString() + "\n");
        System.out.println("First alt: " + gr.getFirst().getCases().toString() + "\n");
        System.out.println("Follow: " + gr.getFollow().getEntity().toString());
    }
}