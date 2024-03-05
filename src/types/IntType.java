package types;

public class IntType extends AttributeType {
    @Override
    public String defaultVal() {
        return "0";
    }

    @Override
    public String name() {
        return "Integer";
    }
}
