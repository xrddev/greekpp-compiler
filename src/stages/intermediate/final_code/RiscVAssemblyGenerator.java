package stages.intermediate.final_code;

import stages.intermediate.semantic.symbol.LocalVariable;
import stages.intermediate.semantic.symbol.Parameter;

import java.util.ArrayList;
import java.util.List;

public class RiscVAssemblyGenerator {
    private final List<String> instructions;

    public RiscVAssemblyGenerator() {
        this.instructions = new ArrayList<>();
    }

    public void emit(String instruction){
        this.instructions.add(instruction);
    }










    public void emitComment(String comment){
        this.instructions.add("# " + comment);
    }


    public void loadIntegerConst(int value, String targetRegister){
        this.emit("li " + targetRegister + ", " + value);
    }


    public void storeIntegerConst(int value, LocalVariable variable, int currentScopeDepth) {
        this.emit("li t0, " + value);
        storeVar(variable, "t0", currentScopeDepth);
    }


    public void gnlvcode(int levelDifference, int variableOffset){
        emit("lw t0, -4(sp)");
        for (int i = 1; i < levelDifference; i++) {
            emit("lw t0, -4(t0)");
        }
        emit("addi t0, t0, -" + variableOffset);
    }

    public void loadVar(LocalVariable variable, String targetRegister, int currentScopeDepth){
        int scopeLevelDifference = currentScopeDepth - variable.getScopeDepth();

        if(scopeLevelDifference > 0)
            this.loadFromDifferentLevelScope(variable, targetRegister, scopeLevelDifference);
        else
            this.loadFromSameLevelScope(variable, targetRegister);
    }

    private boolean isInputWithReferenceParameter(LocalVariable variable){
        return variable instanceof Parameter && ((Parameter) variable).getMode() == Parameter.Mode.reference_input;
    }
    private void loadFromSameLevelScope(LocalVariable variable, String targetRegister){
        if(isInputWithReferenceParameter(variable)){
            this.emit("lw t0, -" + variable.getOffset() + "(sp)");
            this.emit("lw " + targetRegister + ", 0(t0)");
        }else{
            this.emit("lw " + targetRegister + ", -" + variable.getOffset() + "(sp)");
        }
    }
    private void loadFromDifferentLevelScope(LocalVariable variable, String targetRegister, int levelDifference){
        if(isInputWithReferenceParameter(variable)){
            this.gnlvcode(levelDifference, variable.getOffset());
            this.emit("lw t0, 0(t0)");
            this.emit("lw " + targetRegister + ", 0(t0)");
        }else{
            this.gnlvcode(levelDifference, variable.getOffset());
            this.emit("lw " + targetRegister + ", 0(t0)");
        }
    }


    public void storeVar(LocalVariable variable, String sourceRegister, int currentScopeDepth){
        int scopeLevelDifference = currentScopeDepth - variable.getScopeDepth();

        if(scopeLevelDifference > 0)
            this.storeToDifferentLevelScope(variable, sourceRegister, scopeLevelDifference);
        else
            this.storeToSameLevelScope(variable, sourceRegister);
    }


    private void storeToDifferentLevelScope(LocalVariable variable, String sourceRegister, int levelDifference){
        if(isInputWithReferenceParameter(variable)){
            this.gnlvcode(levelDifference, variable.getOffset());
            this.emit("lw t0, 0(t0)");
            this.emit("sw " + sourceRegister + ", 0(t0)");
        }else{
            this.gnlvcode(levelDifference, variable.getOffset());
            this.emit("sw " + sourceRegister + ", 0(t0)");
        }
    }
    private void storeToSameLevelScope(LocalVariable variable, String sourceRegister){
        if(isInputWithReferenceParameter(variable)){
            this.emit("lw t0, -" + variable.getOffset() + "(sp)");
            this.emit("sw " + sourceRegister + ", 0(t0)");
        }else{
            this.emit("sw " + sourceRegister + ", -" + variable.getOffset() + "(sp)");
        }
    }


    public List<String> getInstructions() {
        return instructions;
    }
}

