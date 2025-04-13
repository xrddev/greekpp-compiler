package stages.syntax_tree_buildup.scope.symbol;

import java.util.ArrayList;
import java.util.List;

public abstract class Subroutine extends Symbol{
    List<Identifier> parameters;

    public Subroutine(String name) {
        super(name);
    }

    public void addParameter(Identifier parameter){
        if(this.parameters == null) this.parameters = new ArrayList<>();
        this.parameters.add(parameter);
    }

    public List<Identifier> getParameters() {
        return parameters;
    }

}

