package stages.intermediate.semantic.symbol;

public class LocalVariable extends Entity {
    final DataType dataType;
    int offset;
    int scopeDepth;

    public LocalVariable(String name, DataType dataType, int scopeDepth){
        super(name);
        this.dataType = dataType;
        this.scopeDepth = scopeDepth;
    }

    public DataType getDataType(){
        return this.dataType;
    }
    public int getOffset(){
        return this.offset;
    }
    public void setOffset(int offset){
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "LocalVariable{" +
                "name='" + name + '\'' +
                ", dataType=" + dataType +
                ", offset=" + offset +
                ", scopeDepth=" + scopeDepth +
                '}';
    }

    public int getScopeDepth() {
        return scopeDepth;
    }


    public ActivationRecord getActivationRecordPointer(){
        return null;
    }

}
