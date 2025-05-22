package stages.backend.final_code;

import errors.SemanticErrors;
import stages.backend.quads.Quad;
import stages.backend.semantic.ScopeManager;
import stages.backend.symbol.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RiscVAssemblyGenerator {
    private static final String TEMP_0 = "t0";
    private static final String TEMP_1 = "t1";
    private static final String TEMP_2 = "t2";
    private static final String SP_TEMP = "t3";
    private static final String A0 = "a0";
    private static final String A7 = "a7";

    private final ScopeManager scopeManager;

    private int generatedQuadsCounter;
    private int labelCounter;

    private final List<String> instructions;

    public RiscVAssemblyGenerator(ScopeManager scopeManager){
        this.generatedQuadsCounter = 0;
        this.scopeManager = scopeManager;
        this.labelCounter = 0;
        this.instructions = new ArrayList<>();
        this.emitProgramStart();
    }

    public void emit(String instruction){
        this.instructions.add("\t" + instruction);
    }

    private Procedure loadFromStuckAndResolveSubroutine(String subroutineName){
        Procedure subroutine;
        if((subroutine = this.scopeManager.resolveSubroutine(subroutineName)) == null) {
            SemanticErrors.undeclaredSubroutine(subroutineName, 0, 0);
        }
        return subroutine;
    }

    private Operand loadFromStuckAndResolveVariable(String variableName){
        LocalVariable variable;
        boolean isIntegerConst = this.isInteger(variableName);

        if(((variable = this.scopeManager.resolveTemporaryVariable(variableName)) == null)
                && ((variable = this.scopeManager.resolveVariable(variableName)) == null)
                && !isIntegerConst) {
            SemanticErrors.undeclaredVariable(variableName, 0, 0);
        }

        return isIntegerConst ? wrapOperand(Integer.parseInt(variableName)) : wrapOperand(variable);
    }
    private LocalVariable loadFromStuckAndResolveVariableNoIntegerConstantAllowed(String variableName){
        LocalVariable variable;

        if(((variable = this.scopeManager.resolveTemporaryVariable(variableName)) == null)
                && ((variable = this.scopeManager.resolveVariable(variableName)) == null)) {
            SemanticErrors.undeclaredVariable(variableName, 0, 0);
        }
        return variable;
    }
    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private void generateAsmForArithmeticOperation(Quad quad){
       Operand operand1 = this.loadFromStuckAndResolveVariable(quad.getOperand1());
       Operand operand2 = this.loadFromStuckAndResolveVariable(quad.getOperand2());
       LocalVariable result = this.loadFromStuckAndResolveVariableNoIntegerConstantAllowed(quad.getResult());
       this.emitArithmeticOperation(quad.getOperator(), operand1, operand2, result);
    }
    private void emitArithmeticOperation(String operator, Operand operand1, Operand operand2, LocalVariable result) {
        loadOperand(TEMP_1, operand1, this.scopeManager.getDepth());
        loadOperand(TEMP_2, operand2, this.scopeManager.getDepth());

        String instr = switch (operator) {
            case "+" -> "add";
            case "-" -> "sub";
            case "*" -> "mul";
            case "/" -> "div";
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
        };

        this.emit(instr + " " + TEMP_1 + ", " + TEMP_1 + ", " + TEMP_2);
        storeVar(TEMP_1, result, this.scopeManager.getDepth());
    }


    private void generateAsmForAssigment(Quad quad){
        LocalVariable result = this.loadFromStuckAndResolveVariableNoIntegerConstantAllowed(quad.getResult());
        Operand operand = this.loadFromStuckAndResolveVariable(quad.getOperand1());
        this.emitAssignment(operand, result);
    }

    private void emitAssignment(Operand value, LocalVariable result) {
        loadOperand(TEMP_1, value, this.scopeManager.getDepth());
        storeVar(TEMP_1, result, this.scopeManager.getDepth());
    }


    private void generateAsmForReturnOnCallee(Quad quad){
        Operand result = this.loadFromStuckAndResolveVariable(quad.getResult());
        this.emitReturnOnCallee(result);
    }
    private void emitReturnOnCallee(Operand operand){
        LocalVariable result = ((VariableOperand) operand).value();
        this.emit("lw " + TEMP_0 + ", 8(sp)");
        this.emit("lw " + TEMP_1 + ", " + result.getOffset() + "(sp)");
        this.emit("sw " + TEMP_1 + ", 0(" + TEMP_0 + ")");
    }

    private void generateAsmForPrint(Quad quad) {
        Operand result = this.loadFromStuckAndResolveVariable(quad.getResult());
        this.loadOperand(TEMP_0, result, this.scopeManager.getDepth());


        this.emit("mv " + A0 + ", " + TEMP_0);
        this.emit("li " + A7 + ", 1");
        this.emit("ecall");

        this.emit("la " + A0 + ", str_nl");
        this.emit("li " + A7 + ", 4");
        this.emit("ecall");
    }


    private void generateAsmForBeginBlock(Quad quad){
        Procedure main = this.scopeManager.resolveSubroutine("$$$_Main_$$$");
        switch (quad.getOperand1()){
            case "$$$_Main_$$$" -> {
                this.instructions.add("LMain:");
                this.emit("addi sp, sp, -" + main.getActivationRecord().getRecordLength());
            }
            default -> this.emit("sw ra, 0(sp)");
        }
    }

    private void generateAsmForEndBlock(Quad quad){
        switch (quad.getOperand1()){
            case "$$$_Main_$$$" -> {} //Nothing to do. Halt doing what needs to be done in the end.
            default -> {
                this.emit("lw ra, 0(sp)");
                this.emit("jr ra");
            }
        }
    }


    private void generateAsmForSubroutineBlock(int callQuadIndex, List<Quad> scopeQuads){
        var subroutine= this.loadFromStuckAndResolveSubroutine(scopeQuads.get(callQuadIndex).getResult());
        int calleeNumberOfParameters = subroutine.getActivationRecord().countFormalParameters();
        boolean subroutineIsFunction = subroutine instanceof Function;
        int formalParametersOffesetCounter = 12;

        //Create a stack for callee.
        this.emit("mv " + SP_TEMP + ", sp");
        this.emit("addi sp, sp, -" + subroutine.getActivationRecord().getRecordLength());

        //Handle dynamic link load for recursive or sibling calling.
        int callerDepth = this.scopeManager.getDepth();
        int calleeDepth = subroutine.getScopeDepth();
        if(callerDepth == calleeDepth)
            this.emit("lw " + SP_TEMP + ", 4(" + SP_TEMP +")");


        //Save dynamic link before parsing parameters so they can use it.
        this.emit("sw " + SP_TEMP + ", 4(sp)");
        this.emit("# Allocate callee stack and handle dynamic link ↑↑↑\n");


        //Store callee parameters to callee stack.
        for(int i = callQuadIndex - calleeNumberOfParameters - (subroutineIsFunction ? 1 : 0); i < callQuadIndex - (subroutineIsFunction ? 1 : 0); i++){
            this.generateSubroutineParameters(scopeQuads.get(i), formalParametersOffesetCounter);
            this.emit("# parameter "+ scopeQuads.get(i).getOperand1() + " ↑↑↑\n");
            formalParametersOffesetCounter += 4;
        }


        //Store return if is a function
        if(subroutineIsFunction)
            this.generateAsmForFunctionReturnParameter(scopeQuads.get(callQuadIndex - 1));


        //save return address to callee
        this.emit("sw ra, 0(sp)");
        //Jump to begin block quad. Always will be just before the function starting quad.
        this.emit("jal L" + (subroutine.getActivationRecord().getStartingQuadAddress() -1));


        this.emit("# call ↑↑↑\n");


        //Free callee stack.
        this.emit("addi sp, sp, " + subroutine.getActivationRecord().getRecordLength());
        this.emit("# Free callee stack ↑↑↑");
    }

    private void generateAsmForFunctionReturnParameter(Quad quad){
        TemporaryVariable returnTempVariable = this.scopeManager.resolveTemporaryVariable(quad.getOperand1());

        this.emit("addi " + TEMP_0 + "," + SP_TEMP +  ", " + returnTempVariable.getOffset());
        this.emit("sw " + TEMP_0 + ", 8(sp)");

        this.emit("# ret par ↑↑↑\n");
    }


    private void generateSubroutineParameters(Quad quad, int formalParameterOffset){
        switch (quad.getOperand2()){
            case "cv" -> this.emitParameterByValue(quad, formalParameterOffset);
            case "ref" -> this.emitParameterByReference(quad, formalParameterOffset);
            default -> throw new IllegalArgumentException("Unsupported operand: " + quad.getOperand2());
        }
    }

    private void emitParameterByValue(Quad quad, int formalParameterOffset){
        Operand parameter = this.loadFromStuckAndResolveVariable(quad.getOperand1());
        //Parameters are parsed while still being on the caller scope, but the callee stack is already allocated.
        int calleeScopeDepth = this.scopeManager.getDepth() + 1;

        this.loadOperand(TEMP_0, parameter, calleeScopeDepth);
        this.emit("sw " + TEMP_0 + ", " + formalParameterOffset + "(sp)");
    }



    private void emitParameterByReference(Quad quad,int formalParameterOffset){
        LocalVariable variable = this.loadFromStuckAndResolveVariableNoIntegerConstantAllowed(quad.getOperand1());
        int calleeScopeDepth = this.scopeManager.getDepth() + 1;
        //Parameters are parsed while still being on the caller scope, but the callee stack is already allocated.
        int levelDifference = calleeScopeDepth - variable.getScopeDepth();


        boolean referenceByReference =
                variable instanceof Parameter && ((Parameter) variable).getMode() == Parameter.Mode.reference_input;



        if (referenceByReference) {
            //Parameter is by reference at caller scope.
            if (levelDifference > 0) {
                this.gnlvcode(levelDifference, variable.getOffset());
                this.emit("lw " + TEMP_0 + ", 0(" + TEMP_0 + ")");
            } else {
                this.emit("lw " + TEMP_0 + ", " + variable.getOffset() + "(sp)");
            }
        }
        else {
            //Parameter is by value at caller scope.
            if (levelDifference > 0) {
                this.gnlvcode(levelDifference, variable.getOffset());
            } else {
                this.emit("addi " + TEMP_0 + ", sp, " + variable.getOffset());
            }
        }
        this.emit("sw " + TEMP_0 + ", " + formalParameterOffset + "(sp)");
    }













    public void emitJump(Quad quad){
        this.emit("j L" + quad.getResult());
    }
    public void emitConditionalJump(Quad quad) {
        Operand operand1 = this.loadFromStuckAndResolveVariable(quad.getOperand1());
        Operand operand2 = this.loadFromStuckAndResolveVariable(quad.getOperand2());

        loadOperand(TEMP_1, operand1, this.scopeManager.getDepth());
        loadOperand(TEMP_2, operand2, this.scopeManager.getDepth());

        String branch = switch (quad.getOperator()) {
            case "=" -> "beq";
            case "<>" -> "bne";
            case "<"  -> "blt";
            case ">"  -> "bgt";
            case "<=" -> "ble";
            case ">=" -> "bge";
            default -> throw new IllegalArgumentException("Invalid conditional operator: " + quad.getOperator());
        };

        this.emit(branch + " " + TEMP_1 + ", " + TEMP_2 + ", L" + quad.getResult());
    }


    public void emitProgramHalt() {
        this.emit("li a0, 0");
        this.emit("li a7, 93");
        this.emit("ecall");
    }



    public void emitProgramStart() {
        emit(".data");
        emit("str_nl: .asciz \"\\n\"");
        emit(".text");
        this.emmitNewLine();
        emit("j LMain");
    }
    public void writeToFile(String asmFileName) {
        try {
            String fileName = asmFileName + ".asm";

            try (FileWriter writer = new FileWriter(fileName)) {
                for (String instr : instructions) {
                    writer.write(instr + "\n");
                }
            }

            System.out.println("✅ Assembly written to: " + fileName);
        } catch (IOException e) {
            System.err.println("❌ Error writing assembly file: " + e.getMessage());
        }
    }


    public void generateAsmForCurrentScope(List<Quad> scopeQuads) {
        int batchCount = 0;
        for(int i = this.generatedQuadsCounter; i < scopeQuads.size(); i++ , batchCount++){
            this.emmitNewLine();
            this.emitComment(scopeQuads.get(i).toString());
            this.emitLabel();

            switch(scopeQuads.get(i).getOperator()){
                case ":=" -> this.generateAsmForAssigment(scopeQuads.get(i));
                case "begin_block" -> this.generateAsmForBeginBlock(scopeQuads.get(i));
                case "+", "-", "*", "/" -> this.generateAsmForArithmeticOperation(scopeQuads.get(i));
                case "retv" -> this.generateAsmForReturnOnCallee(scopeQuads.get(i));
                case "end_block" -> this.generateAsmForEndBlock(scopeQuads.get(i));
                case "out" -> this.generateAsmForPrint(scopeQuads.get(i));
                case "par" -> this.emit("# Ignored. Call quad will handle it.");
                case "call" -> this.generateAsmForSubroutineBlock(i ,scopeQuads);
                case "jump" -> this.emitJump(scopeQuads.get(i));
                case "=", "<>", "<", ">", "<=", ">=" -> this.emitConditionalJump(scopeQuads.get(i));
                case "halt" -> this.emitProgramHalt();
                case "in" -> this.generateAsmForInput(scopeQuads.get(i));
                default -> throw new IllegalArgumentException("Unsupported operator: " + scopeQuads.get(i).getOperator());
            }
        }


        this.generatedQuadsCounter += batchCount;
        this.emmitNewLine();
        this.emitComment("↑↑↑ Exiting current scope ↑↑↑ (Depth: " + this.scopeManager.getDepth() +
                ")   || Assembly batch for this scope generated and flushed successfully ||");
    }



    private void generateAsmForInput(Quad quad) {
        LocalVariable result = this.loadFromStuckAndResolveVariableNoIntegerConstantAllowed(quad.getResult());

        this.emit("li " + A7 + ", 5");
        this.emit("ecall");

        this.storeVar(A0, result, this.scopeManager.getDepth());
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
    private void emitLabel(){
        this.instructions.add("L" + labelCounter++ + ":");
    }
    private void emitComment(String comment){
        this.instructions.add("# " + comment);
    }
    private void emmitNewLine(){
        this.emit("\n".repeat(1));
    }
    private void loadIntegerConst(String targetRegister, int value){
        this.emit("li " + targetRegister + ", " + value);
    }
    private boolean isInputWithReferenceParameter(LocalVariable variable){
        return variable instanceof Parameter && ((Parameter) variable).getMode() == Parameter.Mode.reference_input;
    }
    private void loadVar(String targetRegister, LocalVariable variable, int currentScopeDepth){
        int scopeLevelDifference = currentScopeDepth - variable.getScopeDepth();

        if(scopeLevelDifference > 0)
            this.loadFromDifferentLevelScope(targetRegister, variable, scopeLevelDifference);
        else
            this.loadFromSameLevelScope(targetRegister, variable);
    }
    private void loadFromSameLevelScope(String targetRegister, LocalVariable variable ){
        if(isInputWithReferenceParameter(variable)){
            this.emit("lw " + TEMP_0 + ", " + variable.getOffset() + "(sp)");
            this.emit("lw " + targetRegister + ", 0(" + TEMP_0 + ")");
        }else{
            this.emit("lw " + targetRegister + ", " + variable.getOffset() + "(sp)");
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
    private void storeVar(String sourceRegister, LocalVariable variable, int currentScopeDepth){
        int scopeLevelDifference = currentScopeDepth - variable.getScopeDepth();

        if(scopeLevelDifference > 0)
            this.storeToDifferentLevelScope(sourceRegister, variable, scopeLevelDifference);
        else
            this.storeToSameLevelScope(sourceRegister, variable);
    }
    private void storeToSameLevelScope(String sourceRegister, LocalVariable variable){
        if(isInputWithReferenceParameter(variable)){
            this.emit("lw " + TEMP_0 + ", " + variable.getOffset() + "(sp)");
            this.emit("sw " + sourceRegister + ", 0(" + TEMP_0 + ")");
        }else{
            this.emit("sw " + sourceRegister + ", " + variable.getOffset() + "(sp)");
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
    private void gnlvcode(int levelDifference, int variableOffset){
        emit("lw " + TEMP_0 + ", 4(sp)");
        for (int i = 1; i < levelDifference; i++) {
            emit("lw " + TEMP_0 + ", 4(" + TEMP_0 + ")");
        }
        emit("addi " + TEMP_0 + " , " + TEMP_0 + ", " + variableOffset);
    }
}

