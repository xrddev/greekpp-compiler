package stages.visitor.semantic.symbol;

public class Function extends Procedure {
    DataType returnType;


    public Function(String name, DataType returnType) {
        super(name);
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        return "Function{" +
                "name='" + name + '\'' +
                ", returnType=" + returnType +
                ", activationRecord=" + activationRecord +
                '}';
    }
}
