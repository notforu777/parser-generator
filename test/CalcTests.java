import lab4gen.Calc.CalcLexer;
import lab4gen.Calc.CalcParser;
import lab4gen.Calc.Token;
import lab4gen.Calc.Tree;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

public class CalcTests {
    private static final CalcParser parser = new CalcParser();
    private static final String DOUBLE = "11+22";
    private static final String MINUS = "1-2-3";
    private static final String NUM = "42424242";
    private static final String MUL = "2*3*4";

    private static int cur = 0;

    private static void dfs(Tree tree, int prId, int curId, StringBuilder sb) {
        if (!tree.children.isEmpty()) {
            //System.out.print(tree.node);
            //System.out.println(tree.children.size());

            sb.append(String.format("\t%d [label = \"%s\"]\n", curId, tree.node));

            if (prId != -1) {
                sb.append(String.format("\t%d -> %d\n", prId, curId));
            }

            tree.children.forEach(child -> dfs(child, curId, ++cur, sb));
        } else {
            sb.append(String.format("\t%d [label = \"%s\"]\n", curId, tree.node))
                    .append(String.format("\t%s -> %d\n", prId, curId));
            ++cur;
        }
    }


    @Test
    public void dbl() throws ParseException {
        printMe(parser.parse(DOUBLE));
        assertEquals(33, eval(DOUBLE));
    }

    @Test
    public void minus() throws ParseException {
        printMe(parser.parse(MINUS));
        assertEquals(-4, eval(MINUS));
    }

    @Test
    public void num() throws ParseException {
        printMe(parser.parse(NUM));
        assertEquals(42424242, eval(NUM));
    }

    @Test
    public void mul() throws ParseException {
        printMe(parser.parse(MUL));
        assertEquals(24, eval(MUL));
    }

    private void printMe(Tree ans) {
        String FILE = "graph4.txt";

        try (final Writer writer = new FileWriter(FILE)) {
            StringBuilder sb = new StringBuilder("digraph {\n");
            dfs(ans, -1, 0, sb);
            sb.append("}");

            writer.write(String.valueOf(sb));
        } catch (final IOException e) {
            System.err.println("Error in writing to graph");
        }
    }

    private int eval(String s) throws ParseException {
        System.out.println("EVAL: ");
        CalcLexer calcLexer = new CalcLexer(s);
        while (calcLexer.getCurToken().a != Token.END) {
            calcLexer.nextToken();

            Pair<Token, String> token = calcLexer.getCurToken();

            System.out.println(token.a + " " + token.b);
        }

        CalcParser calcParser = new CalcParser();
        return calcParser.parse(s).getV();
    }
}
