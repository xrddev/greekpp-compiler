package stages.semantic.intermediate.instructions;

public class JumpInstruction extends IRInstruction {
    private String destination;
    private final String condition;

    public JumpInstruction(String scope, InstructionType operation, String condition, String destination) {
        super(scope, operation);
        this.condition = condition;
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return "JumpInstruction{" +
                "operation=" + instructionType +
                ", scope='" + scope + '\'' +
                ", condition='" + condition + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
