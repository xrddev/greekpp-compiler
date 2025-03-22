package stages.semantic.symbol;

public class Identifier extends Symbol {
    public enum IdentifierKind{
        VARIABLE,
        FUNCTION_PARAMETER,
        PROCEDURE_PARAMETER
    }

    public enum IdentifierDataType{
        INTEGER
    }

    private IdentifierKind kind;
    private IdentifierDataType dataType;

    public Identifier(String name , IdentifierKind kind, IdentifierDataType dataType){
        super(name, false);
        this.kind = kind;
        this.dataType = dataType;
    }

    public IdentifierKind getKind() {
        return kind;
    }

    public IdentifierDataType getDataType() {
        return dataType;
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "name='" + name + '\'' +
                ", isUsed=" + isUsed +
                ", kind=" + kind +
                ", dataType=" + dataType +
                '}';
    }
}
