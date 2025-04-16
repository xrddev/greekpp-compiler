package stages.visitor.semantic.symbol;

public class Parameter extends Variable{
    public enum Mode{
        in , out , ref ,ret
    }
    Mode mode;

    public Parameter(String name, DataType dataType,int offset) {
        super(name, dataType, offset);
    }

    public Mode getMode() {
        return mode;
    }
    public void setMode(Mode mode) {
        this.mode = mode;
    }
    @Override
    public String toString() {
        return "Parameter{" +
                "name='" + name + '\'' +
                ", dataType=" + dataType +
                ", mode=" + mode +
                ", offset=" + offset +
                '}';
    }
}
