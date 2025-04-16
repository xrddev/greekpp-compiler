package stages.visitor.semantic.symbol;

public class Variable extends Entity {
    final DataType dataType;
    int offset;

    public Variable(String name, DataType dataType, int offset){
        super(name);
        this.dataType = dataType;
        this.offset = offset;
    }

    public DataType getDataType(){
        return this.dataType;
    }
    public int getOffset(){
        return this.offset;
    }

    public String toString(){
        return "Variable{" +
                "name='" + name + '\'' +
                ", dataType=" + dataType +
                ", offset=" + offset +
                '}';
    }
}
