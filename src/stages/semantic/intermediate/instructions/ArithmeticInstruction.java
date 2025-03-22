package stages.semantic.intermediate.instructions;

public class ArithmeticInstruction extends IRInstruction {
    private final String destination;
    private final String operand1;
    private final String operand2;


    public ArithmeticInstruction(String scope, InstructionType operation, String destination, String operand1, String operand2) {
        super(scope, operation);
        this.destination = destination;
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    public String getDestination() {
        return destination;
    }

    public String getOperand1() {
        return operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    @Override
    public String toString() {
        return "ArithmeticInstruction{" +
                "scope='" + scope + '\'' +
                ", operation=" + instructionType +
                ", destination='" + destination + '\'' +
                ", operand1='" + operand1 + '\'' +
                ", operand2='" + operand2 + '\'' +
                '}';
    }
}
