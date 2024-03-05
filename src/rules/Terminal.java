package rules;

public class Terminal extends GrammarRule {
    private String pattern;

    public Terminal(String name, String pattern) {
        super(name, "");
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }


    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

}