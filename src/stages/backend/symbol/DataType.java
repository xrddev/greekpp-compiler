package stages.backend.symbol;

public enum DataType {
    Integer(4),
    Float(8),
    Char(1);

    private final int byteSize;

    DataType(int byteSize) {
        this.byteSize = byteSize;
    }

    public int getByteSize() {
        return byteSize;
    }
}
