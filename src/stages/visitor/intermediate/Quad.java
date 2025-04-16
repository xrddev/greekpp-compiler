package stages.visitor.intermediate;

public class Quad {
    private final String operator;
    private final String operand1;
    private final String operand2;
    private String result;

    public Quad(String operator, String operand1, String operand2, String result) {
        this.operator = operator;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
    }

    public void setResult(String result){
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s", operator
                , operand1 == null ? "_" : operand1
                , operand2 == null ? "_" : operand2
                , result == null ? "_" : result);
    }
}
