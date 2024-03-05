package lab4gen.Calc;

import org.antlr.v4.runtime.misc.Pair;

import java.text.ParseException;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcLexer {
    private final String s;
    private int curPos;
    private Pair<Token, String> curToken;
    private final Matcher matcher;

    private final Map<Token, String> nameReg = new HashMap<>(Map.of(
    Token.LPAREN, "\\(",
Token.RPAREN, "\\)",
Token.PLUS, "\\+",
Token.MUL, "\\*",
Token.POW, "\\^",
Token.UNARY_MINUS, "--",
Token.MINUS, "-",
Token.NUM, "(0|[1-9][0-9]*)"
    ));

    public CalcLexer(String s) {
        this.s = s;
        matcher = Pattern.compile("").matcher(s);
        curToken = new Pair<>(Token.START, "");
    }

    public void nextToken() throws ParseException {
            matcher.usePattern(Pattern.compile("[ \n\r\t]+"));
            matcher.region(curPos, s.length());
            if (matcher.lookingAt()) {
                curPos += matcher.end() - matcher.start();
            }

            if (curPos == s.length()) {
                curToken = new Pair<>(Token.END, "");
            }

            boolean hasNext = false;

            for (Token token : Token.values()) {
                    if (token == Token.END || token == Token.START || token == Token.EPSILON) {
                        continue;
            }

            matcher.usePattern(Pattern.compile(nameReg.get(token)));
            matcher.region(curPos, s.length());
            if (matcher.lookingAt()) {
                        curToken = new Pair<>(token, s.substring(curPos, curPos + matcher.end() - matcher.start()));
                        curPos += matcher.end() - matcher.start();
                        hasNext = true;
                        break;
                    }
            }

            if (!hasNext && curPos != s.length()) {
                    throw new ParseException("Bad string at: ", curPos);
            }
        }

    public Pair<Token, String> getCurToken() {
        return curToken;
    }

    public int getCurPos() {
        return curPos;
    }

}
