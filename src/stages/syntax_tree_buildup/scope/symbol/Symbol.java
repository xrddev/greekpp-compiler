package stages.syntax_tree_buildup.scope.symbol;

public abstract class Symbol {

    final String name;
    boolean isUsed;

    public Symbol(String name,boolean isUsed) {
        this.name = name;
        this.isUsed = isUsed;
    }

    public Symbol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public Symbol markAsUsed() {
        this.isUsed = true;
        return this;
    }

}