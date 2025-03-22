package stages.semantic.intermediate.instructions;

public abstract class IRInstruction {
    public enum InstructionType {
        ADD, SUB, MUL, DIV , CONTROL , JUMP, BEGIN_BLOCK, END_BLOCK, IN, OUT, HALT
    }
    final String scope;
    final InstructionType instructionType;


    public IRInstruction(String scope, InstructionType instructionType) {
        this.scope = scope;
        this.instructionType = instructionType;
    }

    public String getScope() {
        return scope;
    }

    public InstructionType getInstructionType() {
        return instructionType;
    }

}