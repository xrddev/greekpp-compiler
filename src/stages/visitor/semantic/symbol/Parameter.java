package stages.visitor.semantic.symbol;

public class Parameter extends Variable{
    public enum Mode{
        input, output, returnValue;
    }
    Mode mode;

    public Parameter(String name, DataType dataType) {
        super(name, dataType);
    }

    public Parameter(String name, DataType dataType, Mode mode) {
        super(name, dataType);
        this.mode = mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", dataType=" + dataType +
                ", offset=" + offset +
                ", mode=" + mode +
                '}';
    }
}
