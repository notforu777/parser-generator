package lab4gen.PySubset;

import org.antlr.v4.runtime.misc.Pair;

import java.text.ParseException;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PySubsetLexer {
    private final String s;
    private int curPos;
    private Pair<Token, String> curToken;
    private final Pattern skipPattern = Pattern.compile("[ \n\r\t]+");
    private final Matcher matcher;

    private final Map<Token, String> nameReg = new HashMap<>(Map.of(
    Token.LPAREN, "\\(",
Token.RPAREN, "\\)",
Token.OR, "or",
Token.AND, "and",
Token.XOR, "xor",
Token.NOT, "not",
Token.VAR, "[a-z]"
    ));

    public PySubsetLexer(String s) {
        this.s = s;
        matcher = Pattern.compile("").matcher(s);
        curToken = new Pair<>(Token.START, "");
    }

    public void nextToken() throws ParseException {
            matcher.usePattern(skipPattern);
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
