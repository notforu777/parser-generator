package types;

public class BooleanType extends AttributeType{
    @Override
    public String defaultVal() {
        return "false";
    }

    @Override
    public String name() {
        return "Boolean";
    }
}
