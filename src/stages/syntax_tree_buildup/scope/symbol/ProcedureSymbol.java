package stages.syntax_tree_buildup.scope.symbol;

public class ProcedureSymbol extends Subroutine{
    public ProcedureSymbol(String name) {
        super(name);
    }
    public String toString(){
        return "ProcedureSymbol{" +
                "name='" + name + '\'' +
                ", isUsed=" + isUsed +
                ", parameters=" + parameters +
                '}';
    }
}
