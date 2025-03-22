package stages.semantic.symbol;

import java.util.List;

public class ProcedureSymbol extends Symbol {

    final List<Identifier> parameters;

    public ProcedureSymbol(String name , List<Identifier> parameters){
        super(name, false);
        this.parameters = parameters;
    }

    public List<Identifier> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "ProcedureSymbol{" +
                "name='" + name + '\'' +
                ", isUsed=" + isUsed +
                ", parameters=" + parameters +
                '}';
    }
}