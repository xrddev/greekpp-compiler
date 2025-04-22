package stages.semantic.symbol;

public class Parameter extends LocalVariable {
    public enum Mode{
        input, output, returnValue , input_by_default
    }
    Mode mode;

    public Parameter(String name, DataType dataType) {
        super(name, dataType);
        this.mode = Mode.input_by_default;
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
