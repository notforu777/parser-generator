package types;

public class StringType extends AttributeType {
    @Override
    public String defaultVal() {
        return "\"\"";
    }

    @Override
    public String name() {
        return "String";
    }
}
