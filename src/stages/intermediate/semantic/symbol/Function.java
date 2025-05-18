package stages.intermediate.semantic.symbol;

public class Function extends Procedure {
    DataType returnType;


    public Function(String name, DataType returnType) {
        super(name);
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("── Function: ").append(name).append(" ──\n");
        sb.append("  Return Type: ").append(returnType).append("\n");
        sb.append(indent(activationRecord.toString(), "    "));
        return sb.toString();
    }

    private String indent(String text, String prefix) {
        StringBuilder result = new StringBuilder();
        for (String line : text.split("\n")) {
            result.append(prefix).append(line).append("\n");
        }
        return result.toString();
    }

}
