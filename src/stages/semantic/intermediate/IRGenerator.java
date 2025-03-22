package stages.semantic.intermediate;

import stages.semantic.intermediate.instructions.ArithmeticInstruction;
import stages.semantic.intermediate.instructions.IRInstruction;
import stages.semantic.intermediate.instructions.JumpInstruction;
import stages.semantic.intermediate.instructions.RegionMark;

import java.util.ArrayList;
import java.util.List;

public class IRGenerator {
    private final List<IRInstruction> instructions;
    private int tempCounter;

    public IRGenerator() {
        this.instructions = new ArrayList<>();
        this.tempCounter = 0;
    }

    public void generateRegionMarkInstruction(String scope , IRInstruction.InstructionType instructionType, String regionOwner){
        this.instructions.add(new RegionMark(scope, instructionType, regionOwner));
    }

    public void generateJumpInstruction(String scope, String condition, String destination){
        this.instructions.add(new JumpInstruction(scope, IRInstruction.InstructionType.JUMP, condition, destination));
    }

    public void generateArithmeticOperation(String scope, IRInstruction.InstructionType instructionType, String destination, String operand1, String operand2){
        this.instructions.add(new ArithmeticInstruction(scope, instructionType, destination, operand1, operand2));
    }

    public String generateTemp(){
        return "$_" + this.tempCounter++;
    }

    public void backPatch(List<Integer> instructionsIDs , int targetID){
        instructionsIDs.forEach(id -> ((JumpInstruction) this.instructions.get(id)).setDestination(String.valueOf(targetID)));
    }



}
