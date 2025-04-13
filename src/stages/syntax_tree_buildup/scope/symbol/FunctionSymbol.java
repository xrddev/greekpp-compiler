package stages.syntax_tree_buildup.scope.symbol;

public class FunctionSymbol extends Subroutine {
    public enum FunctionReturnType {
        INTEGER
    }

    private final FunctionReturnType returnType;

    public FunctionSymbol(String name){
        super(name);
        this.returnType = FunctionReturnType.INTEGER;
    }

    public FunctionReturnType getReturnType() {
        return returnType;
    }

    public String toString(){
        return "FunctionSymbol{" +
                "name='" + name + '\'' +
                ", isUsed=" + isUsed +
                ", parameters=" + parameters +
                ", returnType=" + returnType +
                '}';
    }
}
