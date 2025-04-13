package stages.syntax_tree_visit.intermediate;

public class Quad {
    public enum Mode {
        CV("cv"),       // Πέρασμα με τιμή
        REF("ref"),     // Πέρασμα με αναφορά
        RET("ret");     // Επιστροφή τιμής συνάρτησης

        private final String symbol;

        Mode(String symbol) {
            this.symbol = symbol;
        }

        @Override
        public String toString() {
            return this.symbol;
        }
    }




    private final String operator;
    private final String operand1;
    private final String operand2;
    private final String result;

    public Quad(String operator, String operand1, String operand2, String result) {
        this.operator = operator;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
    }

    public String getOperator() {
        return operator;
    }

    public String getOperand1() {
        return operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Quad{" +
                "operator=" + operator +
                ", operand1='" + operand1 + '\'' +
                ", operand2='" + operand2 + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
