package stages.backend.symbol;

public class TemporaryVariable  extends LocalVariable {
    public TemporaryVariable(String name, DataType dataType, int scopeDepth) {
        super(name, dataType, scopeDepth);
    }
    @Override
    public String toString() {
        return "TemporaryVariable{" +
                "name='" + name + '\'' +
                ", dataType=" + dataType +
                ", offset=" + offset +
                ", scopeDepth=" + scopeDepth +
                '}';
    }
}
