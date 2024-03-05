import rules.GrammarRule;

import java.util.*;

public class LexGen {
    private final MyGrammar gr;

    public LexGen(MyGrammar gr) {
        this.gr = gr;
    }

    public void generateLexer() {
        genEnum();

        String entry = String.join(",\n", gr.getTerminals().stream()
                .filter(a -> !Objects.equals(a.getName(), "EPSILON"))
                .map(token -> String.format("%s, %s", "Token." + token.getName(), esc(token.getPattern()))).toList());

        String lexerName = gr.getName() + "Lexer";

        String javaLexerTemplate = """
                package lab4gen.%s;
                                
                import org.antlr.v4.runtime.misc.Pair;
                                
                import java.text.ParseException;
                import java.util.Map;
                import java.util.HashMap;
                import java.util.regex.Matcher;
                import java.util.regex.Pattern;
                                
                public class %s {
                    private final String s;
                    private int curPos;
                    private Pair<Token, String> curToken;
                    private final Matcher matcher;
                    
                    private final Map<Token, String> nameReg = new HashMap<>(Map.of(
                    %s
                    ));
                                
                    public %s(String s) {
                        this.s = s;
                        matcher = Pattern.compile("").matcher(s);
                        curToken = new Pair<>(Token.START, "");
                    }
                    
                    public void nextToken() throws ParseException {
                            matcher.usePattern(Pattern.compile("[ \\n\\r\\t]+"));
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
                """;
        new MyUtils().writeCode(lexerName, String.format(javaLexerTemplate, gr.getName(), lexerName, entry, lexerName), gr.getName());
    }

    private void genEnum() {
        List<String> terminals = gr.getTerminals().stream().map(GrammarRule::getName).filter(name -> !Objects.equals(name, "WHITESPACES")).toList();

        String concatTerminals = String.join(", ", terminals);

        String javaEnumTemplate = """
                package lab4gen.%s;
                                
                public enum Token {
                                
                    START, %s, END
                    
                }
                """;

        String enumName = "Token";

        new MyUtils().writeCode(enumName, String.format(javaEnumTemplate, gr.getName(), concatTerminals), gr.getName());
    }

    private String esc(String s) {
        return s.replace("\\", "\\\\");
    }
}
