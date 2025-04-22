package stages.semantic.symbol;

public class TemporaryVariable  extends LocalVariable {
    public TemporaryVariable(String name, DataType dataType) {
        super(name, dataType);
    }
    @Override
    public String toString() {
        return "TemporaryVariable{" +
                "name='" + name + '\'' +
                ", dataType=" + dataType +
                ", offset=" + offset +
                '}';
    }
}
