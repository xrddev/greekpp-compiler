package stages.intermediate.final_code;

import stages.intermediate.IntConst;
import stages.intermediate.Operand;
import stages.intermediate.VariableOperand;
import stages.intermediate.semantic.symbol.LocalVariable;
import stages.intermediate.semantic.symbol.Parameter;

import java.util.ArrayList;
import java.util.List;

public class RiscVAssemblyGenerator {
    private static final String TEMP_0 = "t0";
    private static final String TEMP_1 = "t1";
    private static final String TEMP_2 = "t2";

    private int labelCounter = 0;

    private final List<String> instructions;

    public RiscVAssemblyGenerator() {
        this.instructions = new ArrayList<>();
    }

    public void emit(String instruction){
        this.instructions.add(instruction);
    }


    public void emitAssignment(Operand value, LocalVariable result, int currentScopeDepth) {
        this.emitQuadNumberLabel();
        loadOperand(TEMP_0, value, currentScopeDepth);
        storeVar(TEMP_0, result, currentScopeDepth);
    }

    public void emitArithmeticOperation(String operator, Object operand1, Object operand2, LocalVariable result, int currentScopeDepth) {
        this.emitQuadNumberLabel();
        loadOperand(TEMP_1, wrapOperand(operand1), currentScopeDepth);
        loadOperand(TEMP_2, wrapOperand(operand2), currentScopeDepth);

        String instr = switch (operator) {
            case "+" -> "add";
            case "-" -> "sub";
            case "*" -> "mul";
            case "/" -> "div";
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };

        this.emit(instr + " " + TEMP_1 + ", " + TEMP_1 + ", " + TEMP_2);
        storeVar(TEMP_1, result, currentScopeDepth);
    }

    public void emitJump(String result){
        this.emitQuadNumberLabel();
        this.emit("j " + result);
    }

    public void emitConditionalJump(String operator, Object operand1, Object operand2, String label, int currentScopeDepth) {
        this.emitQuadNumberLabel();

        loadOperand(TEMP_1, wrapOperand(operand1), currentScopeDepth);
        loadOperand(TEMP_2, wrapOperand(operand2), currentScopeDepth);

        String branch = switch (operator) {
            case "=" -> "beq";
            case "<>" -> "bne";
            case "<"  -> "blt";
            case ">"  -> "bgt";
            case "<=" -> "ble";
            case ">=" -> "bge";
            default -> throw new IllegalArgumentException("Invalid conditional operator: " + operator);
        };

        this.emit(branch + " " + TEMP_1 + ", " + TEMP_2 + ", " + label);
    }

    public void emitProgramHalt() {
        this.emitQuadNumberLabel();
        this.emit("li a0, 0");
        this.emit("li a7, 93");
        this.emit("ecall");
    }

    public void emitProgramStartJump(){
        this.emitQuadNumberLabel();
        this.emit("j Main");
    }

    public void emitMainEntry(int activationRecordLength){
        this.emit("Main:");
        this.emit("addi sp, sp, -" + activationRecordLength);
    }









    private void loadOperand(String targetRegister, Operand operand, int currentScopeDepth) {
        switch (operand) {
            case IntConst integerConstant -> loadIntegerConst(targetRegister, integerConstant.value());
            case VariableOperand localVariable -> loadVar(targetRegister, localVariable.value(), currentScopeDepth);
        }
    }

    private Operand wrapOperand(Object value) {
        if (value instanceof Integer integerConst) return new IntConst(integerConst);
        if (value instanceof LocalVariable localVariable) return new VariableOperand(localVariable);
        throw new IllegalArgumentException("Unsupported operand type: " + value);
    }











    private void emitQuadNumberLabel(){
        this.instructions.add("\nL" + labelCounter++ + ":");
    }

    public void loadIntegerConst(String targetRegister, int value){
        this.emit("li " + targetRegister + ", " + value);
    }

    public void gnlvcode(int levelDifference, int variableOffset){
        emit("lw t0, -4(sp)");
        for (int i = 1; i < levelDifference; i++) {
            emit("lw t0, -4(t0)");
        }
        emit("addi t0, t0, -" + variableOffset);
    }


    private boolean isInputWithReferenceParameter(LocalVariable variable){
        return variable instanceof Parameter && ((Parameter) variable).getMode() == Parameter.Mode.reference_input;
    }

    public void loadVar(String targetRegister, LocalVariable variable, int currentScopeDepth){
        int scopeLevelDifference = currentScopeDepth - variable.getScopeDepth();

        if(scopeLevelDifference > 0)
            this.loadFromDifferentLevelScope(targetRegister, variable, scopeLevelDifference);
        else
            this.loadFromSameLevelScope(targetRegister, variable);
    }
    private void loadFromSameLevelScope(String targetRegister, LocalVariable variable ){
        if(isInputWithReferenceParameter(variable)){
            this.emit("lw " + TEMP_0 + ", -" + variable.getOffset() + "(sp)");
            this.emit("lw " + targetRegister + ", 0(" + TEMP_0 + ")");
        }else{
            this.emit("lw " + targetRegister + ", -" + variable.getOffset() + "(sp)");
        }
    }
    private void loadFromDifferentLevelScope(String targetRegister, LocalVariable variable, int levelDifference){
        if(isInputWithReferenceParameter(variable)){
            this.gnlvcode(levelDifference, variable.getOffset());
            this.emit("lw " + TEMP_0 + ", 0(" + TEMP_0 + ")");
            this.emit("lw " + targetRegister + ", 0(" + TEMP_0 + ")");
        }else{
            this.gnlvcode(levelDifference, variable.getOffset());
            this.emit("lw " + targetRegister + ", 0(" + TEMP_0 + ")");
        }
    }


    public void storeVar(String sourceRegister, LocalVariable variable, int currentScopeDepth){
        int scopeLevelDifference = currentScopeDepth - variable.getScopeDepth();

        if(scopeLevelDifference > 0)
            this.storeToDifferentLevelScope(sourceRegister, variable, scopeLevelDifference);
        else
            this.storeToSameLevelScope(sourceRegister, variable);
    }
    private void storeToSameLevelScope(String sourceRegister, LocalVariable variable){
        if(isInputWithReferenceParameter(variable)){
            this.emit("lw " + TEMP_0 + ", -" + variable.getOffset() + "(sp)");
            this.emit("sw " + sourceRegister + ", 0(" + TEMP_0 + ")");
        }else{
            this.emit("sw " + sourceRegister + ", -" + variable.getOffset() + "(sp)");
        }
    }
    private void storeToDifferentLevelScope(String sourceRegister, LocalVariable variable, int levelDifference){
        if(isInputWithReferenceParameter(variable)){
            this.gnlvcode(levelDifference, variable.getOffset());
            this.emit("lw " + TEMP_0 + ", 0(" + TEMP_0 + ")");
            this.emit("sw " + sourceRegister + ", 0(" + TEMP_0 + ")");
        }else{
            this.gnlvcode(levelDifference, variable.getOffset());
            this.emit("sw " + sourceRegister + ", 0(" + TEMP_0 + ")");
        }
    }
}

