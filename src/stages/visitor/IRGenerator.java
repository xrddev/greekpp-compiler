package stages.visitor;

import errors.SemanticErrors;
import stages.astree.parser.ASTNode;
import stages.visitor.intermediate.QuadManager;
import stages.visitor.semantic.ScopeManager;
import stages.visitor.semantic.symbol.*;

import java.util.ArrayList;
import java.util.List;

public class IRGenerator extends Visitor {
    private final QuadManager quadManager;
    private final ScopeManager scopeManager;
    private Procedure parameterOwnerPointer;

    public IRGenerator() {
        this.scopeManager = new ScopeManager();
        this.quadManager = new QuadManager();
        this.parameterOwnerPointer = null;
    }

    public ScopeManager getScopeManager() {
        return scopeManager;
    }
    public QuadManager getQuadManager() {
        return quadManager;
    }
    private void registerVariable(ASTNode IDNode){
        Variable variable = new Variable(IDNode.getPlace(), DataType.Integer,this.scopeManager.getCurrentScopeOffset());
        this.scopeManager.setCurrentScopeOffset(this.scopeManager.getCurrentScopeOffset() + 4);
        if(!this.scopeManager.addVariable(variable))
            SemanticErrors.alreadyDeclaredVariable(IDNode.getPlace(), IDNode.getLine(), IDNode.getColumn());
    }
    private void registerParameter(ASTNode IDNode){
        Parameter parameter = new Parameter(IDNode.getPlace(),DataType.Integer,this.scopeManager.getCurrentScopeOffset());
        this.scopeManager.setCurrentScopeOffset(this.scopeManager.getCurrentScopeOffset() + 4);
        this.parameterOwnerPointer.getFormalParameters().add(parameter);
        if(!this.scopeManager.addVariable(parameter))
            SemanticErrors.alreadyDeclaredParameter(IDNode.getPlace(), IDNode.getLine(), IDNode.getColumn());

    }



    @Override
    public void visitProgramBlock(ASTNode programBlockNode) {
        this.quadManager.generateQuad("BEGIN_BLOCK", programBlockNode.getPlace(), null, null);
        programBlockNode.getChildren().forEach(this::visit);
        this.quadManager.generateQuad("END_BLOCK", programBlockNode.getPlace(), null, null);
        this.scopeManager.closeScope();
    }

    @Override
    public void visitProgramEndKeyword(ASTNode programEndNode) {
        this.quadManager.generateQuad("HALT_", null, null, null);
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
        this.quadManager.generateQuad("in", null, null, IDNode.getPlace());
    }

    @Override
    public void visitExpression(ASTNode expressionNode) {
        ASTNode sign = expressionNode.getChildren().getFirst();
        this.visit(sign);

        ASTNode T1 = expressionNode.getChildren().get(1);
        this.visit(T1);

        if(sign.getPlace().equals("-")){
            String temp = this.quadManager.newTemp();
            this.quadManager.generateQuad("-", "0", T1.getPlace(), temp);
            T1.setPlace(temp);
        }

        for (int i = 2; i < expressionNode.getChildren().size(); i += 2) {
            ASTNode operator = expressionNode.getChildren().get(i);
            this.visit(operator);

            ASTNode T2 = expressionNode.getChildren().get(i + 1);
            this.visit(T2);

            String temp = this.quadManager.newTemp();
            this.quadManager.generateQuad(operator.getPlace(), T1.getPlace(), T2.getPlace(), temp);
            T1.setPlace(temp);
        }
        expressionNode.setPlace(T1.getPlace());
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
            this.quadManager.generateQuad(operator.getPlace(), F1.getPlace(), F2.getPlace(), temp);
            F1.setPlace(temp);
        }
        termNode.setPlace(F1.getPlace());
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
            case KEYWORD -> {
                ASTNode conditionNode = boolFactorNode.getChildren().get(2);
                this.visit(conditionNode);

                boolFactorNode.setTrueList(conditionNode.getFalseList());
                boolFactorNode.setFalseList(conditionNode.getTrueList());
            }
            case GROUP_SYMBOL -> {
                ASTNode conditionNode = boolFactorNode.getChildren().get(1);
                this.visit(conditionNode);

                boolFactorNode.setTrueList(conditionNode.getTrueList());
                boolFactorNode.setFalseList(conditionNode.getFalseList());
            }
            default -> {
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
        }
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

        ASTNode factorID = expressionNode.getChildren().get(1).getChildren().getFirst().getChildren().getFirst();
        if(factorID.getNodeType() == ASTNode.NodeType.FUNCTION_CALL_IN_ASSIGMENT)
            this.quadManager.flashOutOfOrderQuads();

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

        this.scopeManager.openScope();

        this.visit(ID);
        this.visit(expression1);
        this.quadManager.generateQuad(":=", ID.getPlace(), null, expression1.getPlace());

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
        this.quadManager.generateQuad("+", ID.getPlace(), stepNode.getPlace(), temp);
        this.quadManager.generateQuad(":=", ID.getPlace(), null, temp);

        this.quadManager.generateQuad(">=", stepNode.getPlace(), "0", positiveStepCheckLabel);
        this.quadManager.generateQuad("jump", null, null, negativeCheckStepLabel);
        ///

        //exit
        this.quadManager.backPatch(checkFalseList, this.quadManager.nextQuad());

        this.scopeManager.closeScope();
    }

    @Override
    public void visitStep(ASTNode stepNode) {
        if (stepNode.getChildren().isEmpty())
            stepNode.setPlace("0");
        else {
            ASTNode expressionNode = stepNode.getChildren().get(1);
            this.visit(expressionNode);
            stepNode.setPlace(expressionNode.getPlace());
        }
    }

    @Override
    public void visitID(ASTNode IDNode) {
        switch (IDNode.getNodeType()){
            case VARIABLE_IDENTIFIER -> this.registerVariable(IDNode);
            case PARAMETER_IDENTIFIER -> this.registerParameter(IDNode);
        }
    }


    @Override
    public void visitProcedure(ASTNode procedureNode) {
        String procedureName = procedureNode.getChildren().get(1).getPlace();
        Procedure procedure = new Procedure(procedureName);
        this.parameterOwnerPointer = procedure;
        if(!this.scopeManager.addSubroutine(procedure))
            SemanticErrors.alreadyDeclaredProcedure(procedureName, procedureNode.getLine(), procedureNode.getColumn());

        int procedureTemps = this.quadManager.nextQuad();
        this.scopeManager.openScope();
        ASTNode formalParametersListNode =  procedureNode.getChildren().get(3);
        this.visit(formalParametersListNode);

        this.quadManager.generateQuad("BEGIN_BLOCK", procedureName, null, null);
        procedure.setStartingQuad(this.quadManager.nextQuad());

        ASTNode procedureBlock = procedureNode.getChildren().get(5);
        this.visit(procedureBlock);

        this.quadManager.generateQuad("END_BLOCK", procedureName, null, null);

        procedureTemps = this.quadManager.nextQuad() - procedureTemps;
        procedure.setFrameLength(this.scopeManager.getCurrentScopeOffset() + procedureTemps);
        this.scopeManager.closeScope();
    }

    @Override
    public void visitFunction(ASTNode functionNode) {
        String functionName = functionNode.getChildren().get(1).getPlace();
        Function function = new Function(functionName, DataType.Integer);
        this.parameterOwnerPointer = function;
        if(!this.scopeManager.addSubroutine(function))
            SemanticErrors.alreadyDeclaredFunction(functionName, functionNode.getLine(), functionNode.getColumn());

        int functionTemps = this.quadManager.nextQuad();
        this.scopeManager.openScope();

        ASTNode formalParametersListNode =  functionNode.getChildren().get(3);
        this.visit(formalParametersListNode);

        this.quadManager.generateQuad("BEGIN_BLOCK", functionName, null, null);
        ASTNode functionBlock = functionNode.getChildren().get(5);
        function.setStartingQuad(this.quadManager.nextQuad());
        this.visit(functionBlock);
        this.quadManager.generateQuad("END_BLOCK", functionName, null, null);

        functionTemps = this.quadManager.nextQuad() - functionTemps;
        function.setFrameLength(this.scopeManager.getCurrentScopeOffset() + functionTemps);
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
                    Parameter parameter = (Parameter) this.scopeManager.resolveVariable(parameterName);
                    if(parameter == null)
                        SemanticErrors.undeclaredInOutParameter(parameterName, node.getLine(), node.getColumn());
                    else
                        parameter.setMode(Parameter.Mode.in);
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
                    Parameter parameter = (Parameter) this.scopeManager.resolveVariable(parameterName);
                    if(parameter == null)
                        SemanticErrors.undeclaredInOutParameter(parameterName, node.getLine(), node.getColumn());
                    else
                        parameter.setMode(Parameter.Mode.out);
                });
    }

    @Override
    public void visitCallStatement(ASTNode callStatementNode) {
        String procedureName = callStatementNode.getChildren().get(1).getPlace();
        ASTNode idTailNode = callStatementNode.getChildren().get(2);

        this.visit(idTailNode);
        this.quadManager.generateDelayedQuad("call", procedureName, null, null);
        this.quadManager.flashOutOfOrderQuads();
    }

    @Override
    public void visitActualParameterItem(ASTNode actualParameterItemNode) {
        switch (actualParameterItemNode.getChildren().getFirst().getNodeType()){
            case REFERENCE_OPERATOR -> {
                ASTNode IDNode = actualParameterItemNode.getChildren().get(1);
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
    public void visitFactor(ASTNode factorNode) {
        switch (factorNode.getChildren().getFirst().getNodeType()) {
            case NUMBER, VARIABLE_USAGE -> {
                ASTNode numberORVarNode = factorNode.getChildren().getFirst();
                this.visit(numberORVarNode);
                factorNode.setPlace(numberORVarNode.getPlace());
            }
            case FUNCTION_CALL_IN_ASSIGMENT -> {
                ASTNode IDNode = factorNode.getChildren().getFirst();
                ASTNode idTailNode = factorNode.getChildren().get(1);

                this.quadManager.openDelayedQuadsLevel();
                this.visit(idTailNode);
                String newTemp = this.quadManager.newTemp();
                this.quadManager.generateDelayedQuad("par", newTemp, "ret", null);
                this.quadManager.generateDelayedQuad("call", IDNode.getPlace(), null, null);
                this.quadManager.closeDelayedQuadsLevel();
                factorNode.setPlace(newTemp);
            }
            default -> {
                ASTNode expressionNode = factorNode.getChildren().get(1);
                this.visit(expressionNode);
                factorNode.setPlace(expressionNode.getPlace());
            }
        }
    }
}