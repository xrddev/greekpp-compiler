package stages.visitor.semantic.symbol;

public class Variable extends Entity {
    final DataType dataType;
    int offset;

    public Variable(String name, DataType dataType){
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
        return "Variable{" +
                "name='" + name + '\'' +
                ", dataType=" + dataType +
                ", offset=" + offset +
                '}';
    }
}
