package stages.backend;

import errors.SemanticErrors;
import stages.backend.final_code.RiscVAssemblyGenerator;
import stages.backend.quads.Quad;
import stages.backend.quads.QuadManager;
import stages.frontend.parser.ASTNode;
import stages.backend.semantic.ScopeManager;
import stages.backend.symbol.*;

import java.util.ArrayList;
import java.util.List;

public class IntermediateGenerator extends Visitor {
    private final QuadManager quadManager;
    private final ScopeManager scopeManager;
    private final RiscVAssemblyGenerator asmManager;

    private Procedure currentBlockOwner;

    public IntermediateGenerator() {
        this.quadManager = new QuadManager();
        this.asmManager = new RiscVAssemblyGenerator(this.scopeManager = new ScopeManager());
        this.currentBlockOwner = new Procedure("$$$_Main_$$$", this.scopeManager.getDepth());
        this.scopeManager.declareProcedure(this.currentBlockOwner,0,0);
    }

    public ScopeManager getScopeManager() {
        return scopeManager;
    }
    public QuadManager getQuadManager() {
        return quadManager;
    }
    public RiscVAssemblyGenerator getAsmManager() {
        return this.asmManager;
    }


    //Visitors
    @Override
    public void visitProgramBlock(ASTNode programBlockNode) {
        ASTNode declarationsNode = programBlockNode.getChildren().getFirst();
        ASTNode subprogramsNode = programBlockNode.getChildren().get(1);
        ASTNode sequenceNode = programBlockNode.getChildren().get(3);
        ASTNode programEndNode = programBlockNode.getChildren().get(4);

        Procedure mainMethod = this.currentBlockOwner;
        this.visit(declarationsNode);
        this.visit(subprogramsNode);

        this.quadManager.generateQuad("begin_block", "$$$_Main_$$$", null, null);
        mainMethod.getActivationRecord().setStartingQuadAddress(this.quadManager.nextQuad());
        this.visit(sequenceNode);
        this.visit(programEndNode);
        this.quadManager.generateQuad("end_block", "$$$_Main_$$$", null, null);


        mainMethod.getActivationRecord().addAllTemporaryVariables(this.scopeManager.getCurrentScopeTemporaryVariables().values());
        this.asmManager.generateAsmForCurrentScope(this.quadManager.getQuads());


        this.scopeManager.closeScope(); //Scope Manager constructor automatically opens the base scope.
    }


    @Override
    public void visitProgramEndKeyword(ASTNode programEndNode) {
        this.quadManager.generateQuad("halt", null, null, null);
    }

    @Override
    public void visitPrintStatement(ASTNode printStatementNode) {
        ASTNode expressionNode = printStatementNode.getChildren().get(1);
        this.visit(expressionNode);
        this.quadManager.generateQuad("out", null, null, expressionNode.getPlace());
    }


    @Override
    public void visitInputStatement(ASTNode inputStatementNode) {
        ASTNode IDNode = inputStatementNode.getChildren().get(1);
        this.visit(IDNode);
        this.quadManager.generateQuad("in", null, null, IDNode.getPlace());
    }


    @Override
    public void visitExpression(ASTNode expressionNode) {
        ASTNode sign = expressionNode.getChildren().getFirst();
        this.visit(sign);

        ASTNode T1 = expressionNode.getChildren().get(1);
        this.visit(T1);

        this.negativeNumberMapping(T1,sign);

        for (int i = 2; i < expressionNode.getChildren().size(); i += 2) {
            ASTNode operator = expressionNode.getChildren().get(i);
            this.visit(operator);

            ASTNode T2 = expressionNode.getChildren().get(i + 1);
            this.visit(T2);

            String temp = this.quadManager.newTemp();
            this.scopeManager.addTemporaryVariable(new TemporaryVariable(temp,DataType.Integer, this.scopeManager.getDepth()));
            this.quadManager.generateQuad(operator.getPlace(), T1.getPlace(), T2.getPlace(), temp);
            T1.setPlace(temp);
        }
        expressionNode.setPlace(T1.getPlace());
    }
    private void negativeNumberMapping(ASTNode T1 , ASTNode sign){
        if(sign.getPlace().equals("-")){
            String temp = this.quadManager.newTemp();
            this.scopeManager.getCurrentScopeTemporaryVariables().put(temp,new TemporaryVariable(temp,DataType.Integer, this.scopeManager.getDepth()));
            this.quadManager.generateQuad("-", "0", T1.getPlace(), temp);
            T1.setPlace(temp);
        }
    }


    @Override
    public void visitOptionalSign(ASTNode optionalSignNode) {
        if (optionalSignNode.getChildren().isEmpty())
            optionalSignNode.setPlace("");
        else
            switch (optionalSignNode.getChildren().getFirst().getPlace()) {
                case "-" -> optionalSignNode.setPlace("-");
                case "+" -> optionalSignNode.setPlace("");
            }
    }


    @Override
    public void visitTerm(ASTNode termNode) {
        ASTNode F1 = termNode.getChildren().getFirst();
        this.visit(F1);

        for (int i = 1; i < termNode.getChildren().size(); i += 2) {
            ASTNode operator = termNode.getChildren().get(i);
            this.visit(operator);

            ASTNode F2 = termNode.getChildren().get(i + 1);
            this.visit(F2);

            String temp = this.quadManager.newTemp();
            this.scopeManager.addTemporaryVariable(new TemporaryVariable(temp,DataType.Integer, this.scopeManager.getDepth()));
            this.quadManager.generateQuad(operator.getPlace(), F1.getPlace(), F2.getPlace(), temp);
            F1.setPlace(temp);
        }
        termNode.setPlace(F1.getPlace());
    }


    @Override
    public void visitFactor(ASTNode factorNode) {
        switch (factorNode.getChildren().getFirst().getNodeType()) {
            case NUMBER, VARIABLE_USAGE -> this.numberOrVariableFactor(factorNode);
            case FUNCTION_CALL_IN_ASSIGMENT -> this.functionCallInAssigmentFactor(factorNode);
            default -> this.expressionFactor(factorNode);
        }
    }
    private void numberOrVariableFactor(ASTNode factorNode){
        ASTNode numberORVarNode = factorNode.getChildren().getFirst();
        this.visit(numberORVarNode);
        factorNode.setPlace(numberORVarNode.getPlace());
    }
    private void functionCallInAssigmentFactor(ASTNode factorNode){
        ASTNode IDNode = factorNode.getChildren().getFirst();
        ASTNode idTailNode = factorNode.getChildren().get(1);
        ASTNode actualParameterListNode = idTailNode.getChildren().getFirst().getChildren().get(1);

        this.quadManager.openDelayedQuadsLevel();

        this.visit(IDNode);
        this.legalNumberOfParametersCheck(IDNode,actualParameterListNode);
        this.visit(idTailNode);

        String temp = this.quadManager.newTemp();
        this.scopeManager.addTemporaryVariable(new TemporaryVariable(temp,DataType.Integer, this.scopeManager.getDepth()));
        this.quadManager.generateDelayedQuad("par", temp, "ret", null);
        this.quadManager.generateDelayedQuad("call", null, null, IDNode.getPlace());

        this.quadManager.flashDelayedQuads();
        this.quadManager.closeDelayedQuadsLevel();
        factorNode.setPlace(temp);
    }
    private void expressionFactor(ASTNode factorNode){
        ASTNode expressionNode = factorNode.getChildren().get(1);
        this.visit(expressionNode);
        factorNode.setPlace(expressionNode.getPlace());
    }


    @Override
    public void visitCondition(ASTNode conditionNode) {
        ASTNode Q1 = conditionNode.getChildren().getFirst();
        this.visit(Q1);

        conditionNode.setTrueList(Q1.getTrueList());
        conditionNode.setFalseList(Q1.getFalseList());

        for (int i = 1; i < conditionNode.getChildren().size(); i += 2) {
            this.quadManager.backPatch(conditionNode.getFalseList(), this.quadManager.nextQuad());

            ASTNode Q2 = conditionNode.getChildren().get(i + 1);
            this.visit(Q2);

            conditionNode.getTrueList().addAll(Q2.getTrueList());
            conditionNode.setFalseList(Q2.getFalseList());
        }
    }


    @Override
    public void visitBoolTerm(ASTNode boolTermNode) {
        ASTNode R1 = boolTermNode.getChildren().getFirst();
        this.visit(R1);

        boolTermNode.setTrueList(R1.getTrueList());
        boolTermNode.setFalseList(R1.getFalseList());

        for (int i = 1; i < boolTermNode.getChildren().size(); i += 2) {
            this.quadManager.backPatch(boolTermNode.getTrueList(), this.quadManager.nextQuad());

            ASTNode R2 = boolTermNode.getChildren().get(i + 1);
            this.visit(R2);

            boolTermNode.getFalseList().addAll(R2.getFalseList());
            boolTermNode.setTrueList(R2.getTrueList());
        }
    }


    @Override
    public void visitBoolFactor(ASTNode boolFactorNode) {
        switch (boolFactorNode.getChildren().getFirst().getNodeType()) {
            case KEYWORD -> this.boolFactorNotCondition(boolFactorNode);
            case GROUP_SYMBOL -> this.boolFactorCondition(boolFactorNode);
            default -> this.boolFactorRelationalOperator(boolFactorNode);
        }
    }
    private void boolFactorNotCondition(ASTNode boolFactorNode){
        ASTNode conditionNode = boolFactorNode.getChildren().get(2);
        this.visit(conditionNode);

        boolFactorNode.setTrueList(conditionNode.getFalseList());
        boolFactorNode.setFalseList(conditionNode.getTrueList());
    }
    private void boolFactorCondition(ASTNode boolFactorNode){
        ASTNode conditionNode = boolFactorNode.getChildren().get(1);
        this.visit(conditionNode);

        boolFactorNode.setTrueList(conditionNode.getTrueList());
        boolFactorNode.setFalseList(conditionNode.getFalseList());
    }
    private void boolFactorRelationalOperator(ASTNode boolFactorNode){
        ASTNode E1 = boolFactorNode.getChildren().getFirst();
        ASTNode relOp = boolFactorNode.getChildren().get(1);
        ASTNode E2 = boolFactorNode.getChildren().get(2);

        this.visit(E1);
        this.visit(relOp);
        this.visit(E2);

        boolFactorNode.setTrueList(new ArrayList<>(List.of(this.quadManager.nextQuad())));
        this.quadManager.generateQuad(relOp.getPlace(), E1.getPlace(), E2.getPlace(), null);
        boolFactorNode.setFalseList(new ArrayList<>(List.of(this.quadManager.nextQuad())));
        this.quadManager.generateQuad("jump", null, null, null);
    }


    @Override
    public void visitWhileStatement(ASTNode whileStatementNode) {
        String conditionQuadLabel = String.valueOf(this.quadManager.nextQuad());

        ASTNode conditionNode = whileStatementNode.getChildren().get(1);
        this.visit(conditionNode);

        this.quadManager.backPatch(conditionNode.getTrueList(), this.quadManager.nextQuad());

        ASTNode sequenceNode = whileStatementNode.getChildren().get(3);
        this.visit(sequenceNode);

        this.quadManager.generateQuad("jump", null, null, conditionQuadLabel);
        this.quadManager.backPatch(conditionNode.getFalseList(), this.quadManager.nextQuad());
    }


    @Override
    public void visitIfStatement(ASTNode ifStatementNode) {
        ASTNode conditionNode = ifStatementNode.getChildren().get(1);
        this.visit(conditionNode);

        this.quadManager.backPatch(conditionNode.getTrueList(), this.quadManager.nextQuad());

        ASTNode sequenceNode = ifStatementNode.getChildren().get(3);
        this.visit(sequenceNode);

        List<Integer> ifList = new ArrayList<>(List.of(this.quadManager.nextQuad()));
        this.quadManager.generateQuad("jump", null, null, null);
        this.quadManager.backPatch(conditionNode.getFalseList(), this.quadManager.nextQuad());


        ASTNode elseStatementNode = ifStatementNode.getChildren().get(4);
        this.visit(elseStatementNode);
        this.quadManager.backPatch(ifList, this.quadManager.nextQuad());
    }


    @Override
    public void visitElseStatement(ASTNode elseStatementNode) {
        if (elseStatementNode.getChildren().isEmpty()) return;

        ASTNode sequenceNode = elseStatementNode.getChildren().get(1);
        this.visit(sequenceNode);
    }


    @Override
    public void visitAssignmentStatement(ASTNode assignmentStatementNode) {
        ASTNode variableUsageNode = assignmentStatementNode.getChildren().getFirst();
        this.visit(variableUsageNode);

        ASTNode expressionNode = assignmentStatementNode.getChildren().get(3);

        this.visit(expressionNode);
        this.quadManager.generateQuad(":=",expressionNode.getPlace() , null, variableUsageNode.getPlace());
    }


    @Override
    public void visitDoStatement(ASTNode doStatementNode) {
        int sequenceQuadLabel = this.quadManager.nextQuad();
        ASTNode sequenceNode = doStatementNode.getChildren().get(1);
        this.visit(sequenceNode);

        ASTNode conditionNode = doStatementNode.getChildren().get(3);
        this.visit(conditionNode);

        this.quadManager.backPatch(conditionNode.getFalseList(), sequenceQuadLabel);
        this.quadManager.backPatch(conditionNode.getTrueList(), this.quadManager.nextQuad());
    }


    @Override
    public void visitForStatement(ASTNode forStatementNode) {
        ASTNode ID = forStatementNode.getChildren().get(1);
        ASTNode expression1 = forStatementNode.getChildren().get(4);
        ASTNode expression2 = forStatementNode.getChildren().get(6);
        ASTNode stepNode = forStatementNode.getChildren().get(7);
        ASTNode sequenceNode = forStatementNode.getChildren().get(9);


        this.visit(expression1);
        this.quadManager.generateQuad(":=", expression1.getPlace(), null, ID.getPlace());

        this.visit(expression2);
        this.visit(stepNode);


        List<Integer> stepIsPositive = new ArrayList<>(List.of(this.quadManager.nextQuad()));
        this.quadManager.generateQuad(">=", stepNode.getPlace(), "0", null);
        List<Integer> stepIsNegative = new ArrayList<>(List.of(this.quadManager.nextQuad()));
        this.quadManager.generateQuad("jump", null, null, null);


        List<Integer> checkTrueList = new ArrayList<>();
        List<Integer> checkFalseList = new ArrayList<>();


        //Step is positive
        this.quadManager.backPatch(stepIsPositive, this.quadManager.nextQuad());

        String positiveStepCheckLabel = String.valueOf(this.quadManager.nextQuad());
        checkTrueList.add(this.quadManager.nextQuad());
        this.quadManager.generateQuad("<=", ID.getPlace(), expression2.getPlace(), null);

        checkFalseList.add(this.quadManager.nextQuad());
        this.quadManager.generateQuad("jump", null, null, null);

        //Step is negative
        /////////
        this.quadManager.backPatch(stepIsNegative, this.quadManager.nextQuad());

        String negativeCheckStepLabel = String.valueOf(this.quadManager.nextQuad());
        checkTrueList.add(this.quadManager.nextQuad());
        this.quadManager.generateQuad(">=", ID.getPlace(), expression2.getPlace(), null);

        checkFalseList.add(this.quadManager.nextQuad());
        this.quadManager.generateQuad("jump", null, null, null);


        ///
        this.quadManager.backPatch(checkTrueList, this.quadManager.nextQuad());
        this.visit(sequenceNode);
        String temp = this.quadManager.newTemp();
        this.scopeManager.addTemporaryVariable(new TemporaryVariable(temp,DataType.Integer, this.scopeManager.getDepth()));
        this.quadManager.generateQuad("+", ID.getPlace(), stepNode.getPlace(), temp);
        this.quadManager.generateQuad(":=", temp , null, ID.getPlace());

        this.quadManager.generateQuad(">=", stepNode.getPlace(), "0", positiveStepCheckLabel);
        this.quadManager.generateQuad("jump", null, null, negativeCheckStepLabel);
        ///

        //exit
        this.quadManager.backPatch(checkFalseList, this.quadManager.nextQuad());

    }


    @Override
    public void visitStep(ASTNode stepNode) {
        if (stepNode.getChildren().isEmpty())
            stepNode.setPlace("1");
        else {
            ASTNode expressionNode = stepNode.getChildren().get(1);
            this.visit(expressionNode);
            stepNode.setPlace(expressionNode.getPlace());
        }
    }


    @Override
    public void visitCallStatement(ASTNode callStatementNode) {
        ASTNode IDNode = callStatementNode.getChildren().get(1);
        ASTNode idTailNode = callStatementNode.getChildren().get(2);
        ASTNode actualParameterListNode = idTailNode.getChildren().getFirst().getChildren().get(1);

        this.quadManager.openDelayedQuadsLevel();

        this.visit(IDNode);
        this.legalNumberOfParametersCheck(IDNode,actualParameterListNode);
        this.visit(idTailNode);

        this.quadManager.generateDelayedQuad("call", null, null, IDNode.getPlace());

        this.quadManager.flashDelayedQuads();
        this.quadManager.closeDelayedQuadsLevel();
    }

    @Override
    public void visitActualParameterItem(ASTNode actualParameterItemNode) {
        switch (actualParameterItemNode.getChildren().getFirst().getNodeType()){
            case REFERENCE_OPERATOR -> {
                ASTNode IDNode = actualParameterItemNode.getChildren().get(1);
                this.visit(IDNode);
                this.quadManager.generateDelayedQuad("par",IDNode.getPlace(), "ref", null);
            }
            case EXPRESSION -> {
                ASTNode expressionNode = actualParameterItemNode.getChildren().getFirst();
                this.visit(expressionNode);
                this.quadManager.generateDelayedQuad("par",expressionNode.getPlace(), "cv", null);
            }
        }
    }


    @Override
    public void visitProcedure(ASTNode procedureNode) {
        String procedureName = procedureNode.getChildren().get(1).getPlace();
        Procedure procedure = new Procedure(procedureName, this.scopeManager.getDepth() + 1);
        this.scopeManager.declareProcedure(procedure
                ,procedureNode.getChildren().get(1).getLine()
                ,procedureNode.getChildren().get(1).getColumn());

        this.currentBlockOwner = procedure; //We need this pointer to bind the parameters to the procedure entity.
        this.scopeManager.openScope();
        ASTNode formalParametersListNode =  procedureNode.getChildren().get(3);
        this.visit(formalParametersListNode);

        ASTNode procedureBlock = procedureNode.getChildren().get(5);
        ASTNode functionInput = procedureBlock.getChildren().get(1);
        ASTNode functionOutput = procedureBlock.getChildren().get(2);
        ASTNode declarations = procedureBlock.getChildren().get(3);
        ASTNode subprograms = procedureBlock.getChildren().get(4);
        ASTNode sequence = procedureBlock.getChildren().get(6);

        this.visit(functionInput);
        this.visit(functionOutput);
        this.visit(declarations);
        this.visit(subprograms);
        this.currentBlockOwner = procedure;
        this.quadManager.generateQuad("begin_block", procedureName, null, null);
        procedure.getActivationRecord().setStartingQuadAddress(this.quadManager.nextQuad());
        this.visit(sequence);

        this.quadManager.generateQuad("end_block", procedureName, null, null);
        procedure.getActivationRecord().addAllTemporaryVariables(this.scopeManager.getCurrentScopeTemporaryVariables().values());

        this.asmManager.generateAsmForCurrentScope(this.quadManager.getQuads());
        this.scopeManager.closeScope();
    }


    @Override
    public void visitFunction(ASTNode functionNode) {
        String functionName = functionNode.getChildren().get(1).getPlace();
        Function function = new Function(functionName, DataType.Integer, this.scopeManager.getDepth() + 1);
        this.scopeManager.declareFunction(function
                ,functionNode.getChildren().get(1).getLine()
                ,functionNode.getChildren().get(1).getColumn());

        this.currentBlockOwner = function; //We need this pointer to bind the parameters to the function entity.
        this.scopeManager.openScope();


        ASTNode formalParametersListNode =  functionNode.getChildren().get(3);
        this.visit(formalParametersListNode);

        this.scopeManager.declareReturnVariable(functionName, currentBlockOwner);

        ASTNode functionBlock = functionNode.getChildren().get(5);
        ASTNode functionInput = functionBlock.getChildren().get(1);
        ASTNode functionOutput = functionBlock.getChildren().get(2);
        ASTNode declarations = functionBlock.getChildren().get(3);
        ASTNode subprograms = functionBlock.getChildren().get(4);
        ASTNode sequence = functionBlock.getChildren().get(6);

        this.visit(functionInput);
        this.visit(functionOutput);
        this.visit(declarations);
        this.visit(subprograms);
        this.currentBlockOwner = function;
        this.quadManager.generateQuad("begin_block", functionName, null, null);
        function.getActivationRecord().setStartingQuadAddress(this.quadManager.nextQuad());
        this.visit(sequence);

        this.getQuadManager().generateQuad("retv",null, null, functionName);
        this.quadManager.generateQuad("end_block", functionName, null, null);
        function.getActivationRecord().addAllTemporaryVariables(this.scopeManager.getCurrentScopeTemporaryVariables().values());

        this.asmManager.generateAsmForCurrentScope(this.quadManager.getQuads());
        this.scopeManager.closeScope();
    }



    @Override
    public void visitFunctionInput(ASTNode functionInputNode) {
        if(functionInputNode.getChildren().isEmpty()) return;

        ASTNode varList = functionInputNode.getChildren().get(1);
        varList.getChildren()
                .stream()
                .filter(node -> node.getNodeType() == ASTNode.NodeType.PARAMETER_INOUT_DECLARATION)
                .forEach(node ->{
                    String parameterName = node.getPlace();
                    Entity parameter = this.scopeManager.resolveVariable(parameterName);
                    if(parameter == null)
                        SemanticErrors.undeclaredInOutParameter(parameterName, node.getLine(), node.getColumn());
                    else if (!(parameter instanceof Parameter)) {
                        SemanticErrors.localParameterAsINOUT(parameterName, node.getLine(), node.getColumn());
                    }else{
                        ((Parameter) parameter).setMode(Parameter.Mode.input);
                    }
                });
    }


    @Override
    public void visitFunctionOutput(ASTNode functionOutputNode) {
        if(functionOutputNode.getChildren().isEmpty()) return;

        ASTNode varList = functionOutputNode.getChildren().get(1);
        varList.getChildren()
                .stream()
                .filter(node -> node.getNodeType() == ASTNode.NodeType.PARAMETER_INOUT_DECLARATION)
                .forEach(node ->{
                    String parameterName = node.getPlace();
                    Entity parameter = this.scopeManager.resolveVariable(parameterName);
                    if(parameter == null)
                        SemanticErrors.undeclaredInOutParameter(parameterName, node.getLine(), node.getColumn());
                    else if (!(parameter instanceof Parameter)) {
                        SemanticErrors.localParameterAsINOUT(parameterName, node.getLine(), node.getColumn());
                    }else{
                        ((Parameter) parameter).setMode(Parameter.Mode.reference_input);
                    }
                });
    }


    private void legalNumberOfParametersCheck(ASTNode IDNode, ASTNode actualParameterListNode){
        Procedure subroutine = this.scopeManager.resolveSubroutine(IDNode.getPlace());

        if(subroutine == null)
            SemanticErrors.undeclaredSubroutine(IDNode.getPlace(), IDNode.getLine(), IDNode.getColumn());

        int subroutineParametersNumberOnCall = Math.toIntExact(
                actualParameterListNode.getChildren()
                        .stream()
                        .filter(node -> node.getNodeType() == ASTNode.NodeType.ACTUAL_PARAMETER_ITEM)
                        .count());

        int subroutineParametersNumberOnDeclaration = subroutine.getActivationRecord().countFormalParameters();

        if(subroutineParametersNumberOnCall != subroutineParametersNumberOnDeclaration)
            SemanticErrors.subroutineParametersOnCallError(IDNode.getPlace(),
                    subroutineParametersNumberOnCall, subroutineParametersNumberOnDeclaration,
                    IDNode.getLine(), IDNode.getColumn());

    }


    @Override
    public void visitID(ASTNode IDNode) {
        switch (IDNode.getNodeType()){
            case VARIABLE_IDENTIFIER -> this.scopeManager.declareVariable(IDNode,this.currentBlockOwner);
            case PARAMETER_IDENTIFIER -> this.scopeManager.declareParameter(IDNode,this.currentBlockOwner);
            case VARIABLE_USAGE -> {}
            case FUNCTION_CALL_IN_ASSIGMENT -> this.scopeManager.resolveFunctionInAssigment(IDNode);
        }
    }
}