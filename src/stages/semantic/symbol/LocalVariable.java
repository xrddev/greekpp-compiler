package stages.semantic.symbol;

public class LocalVariable extends Entity {
    final DataType dataType;
    int offset;

    public LocalVariable(String name, DataType dataType){
        super(name);
        this.dataType = dataType;
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
                '}';
    }
}
