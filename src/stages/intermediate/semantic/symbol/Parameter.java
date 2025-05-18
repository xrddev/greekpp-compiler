package stages.intermediate.semantic.symbol;

public class Parameter extends LocalVariable {
    public enum Mode{
        input, reference_input, returnValue , input_by_default
    }
    Mode mode;

    public Parameter(String name, DataType dataType, int scopeDepth) {
        super(name, dataType, scopeDepth);
        this.mode = Mode.input_by_default;
    }

    public Parameter(String name, DataType dataType, Mode mode, int scopeDepth) {
        super(name, dataType, scopeDepth);
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
                ", scopeDepth=" + scopeDepth +
                '}';
    }

    public Mode getMode(){
        return this.mode;
    }
}
