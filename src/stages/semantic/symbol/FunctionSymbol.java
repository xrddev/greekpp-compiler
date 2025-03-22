package stages.semantic.symbol;

import java.util.List;

public class FunctionSymbol extends ProcedureSymbol {
    public enum FunctionReturnType {
        INTEGER
    }

    private final FunctionReturnType returnType;

    public FunctionSymbol(String name, List<Identifier> parameters){
        super(name,parameters);
        this.returnType = FunctionReturnType.INTEGER;
    }

    public FunctionReturnType getReturnType() {
        return returnType;
    }

    @Override
    public String toString() {
        return "FunctionSymbol{" +
                "name='" + name + '\'' +
                ", isUsed=" + isUsed +
                ", parameters=" + parameters +
                ", returnType=" + returnType +
                '}';
    }
}