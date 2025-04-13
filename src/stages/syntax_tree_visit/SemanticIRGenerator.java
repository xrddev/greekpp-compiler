package stages.syntax_tree_visit;

import errors.SymbolTableErrors;
import stages.syntax_tree_buildup.ASTNode;
import stages.syntax_tree_buildup.scope.ScopeManager;
import stages.syntax_tree_buildup.scope.symbol.Symbol;
import stages.syntax_tree_visit.intermediate.QuadManager;

public class SemanticIRGenerator {
    private final ScopeManager scopeManager;
    private final QuadManager quadManager;

    public SemanticIRGenerator(ASTNode absRoot, ScopeManager scopeManager) {
        this.scopeManager = scopeManager;
        this.quadManager = new QuadManager();
        this.visitNode(absRoot);
    }

    private void visitNode(ASTNode node) {
        if (node == null) return;

        //Symbol Table actions. Only terminal are processed here.
        this.resolverSymbol(node);

        //Intermediate code actions
        this.generateIRCode_ON_ENTER(node);
        node.getChildren().forEach(this::visitNode);
        this.generateIRCode_ON_EXIT(node);
    }


    private void resolverSymbol(ASTNode node) {
        if (node == null) return;
        Symbol symbol;
        boolean isDefaultCaseNode = false;

        symbol = switch (node.getNodeType()) {
            case VARIABLE_USAGE, PARAMETER_USAGE, PARAMETER_INOUT_DECLARATION ->
                    this.scopeManager.lookUpIdentifier(node.getId(), node.getAttribute());
            case FUNCTION_CALL_IN_ASSIGMENT, SUBROUTINE_USAGE ->
                    this.scopeManager.lookUpSubroutine(node.getId(), node.getAttribute());
            default -> {
                isDefaultCaseNode = true;
                yield null;
            }
        };

        if (!isDefaultCaseNode && symbol == null)
            SymbolTableErrors.identifierNotDeclared(node.getAttribute(), node.getLine(), node.getColumn());
    }

    private void generateIRCode_ON_ENTER(ASTNode node) {
        switch (node.getNodeType()) {
            case PROGRAM_BLOCK -> this.enterProgramBlock(node);
        }
    }

    private void generateIRCode_ON_EXIT(ASTNode node) {
        switch (node.getNodeType()) {
            case PROGRAM_BLOCK -> this.exitProgramBlock(node);
            case PROGRAM_END_KEYWORD -> this.exitProgramEnd(node);
            case EXPRESSION -> this.exitExpression(node);
            case OPTIONAL_SIGN -> this.exitOptionalSign(node);
            case FACTOR -> this.exitFactor(node);
            case TERM -> this.exitTerm(node);
            case CONDITION -> this.exitCondition(node);
        }
    }

    public QuadManager getQuadManager() {
        return quadManager;
    }


    //toDo
    private void exitFactor(ASTNode factorNode) {
        ASTNode firstChild = factorNode.getChildren().getFirst();

        factorNode.setAttribute(switch (firstChild.getNodeType()) {
            case NUMBER -> firstChild.getAttribute();
            case VARIABLE_USAGE, FUNCTION_CALL_IN_ASSIGMENT -> "PREPEI NA TO KANW";
            default -> factorNode.getChildren().get(1).getAttribute();
        });
    }


    private void exitCondition(ASTNode conditionNode) {
        String T1, T2, operator;

        System.out.println(conditionNode);
    }


    /// etoimoi
    ///


    private void enterProgramBlock(ASTNode node) {
        this.quadManager.generateQuad("BEGIN_BLOCK", node.getAttribute(), null, null);
    }


    private void exitProgramBlock(ASTNode node) {
        this.quadManager.generateQuad("END_BLOCK", node.getAttribute(), null, null);
    }

    private void exitProgramEnd(ASTNode node) {
        this.quadManager.generateQuad("HALT_", null, null, null);
    }


    private void exitExpression(ASTNode expressionNode) {
        String T1, T2, addOperator;

        T1 = expressionNode.getChildren().get(0).getAttribute()         //OptionalSign
                + expressionNode.getChildren().get(1).getAttribute();   //Term

        for (int i = 2; i < expressionNode.getChildren().size(); i += 2) {
            addOperator = expressionNode.getChildren().get(i).getAttribute();
            T2 = expressionNode.getChildren().get(i + 1).getAttribute();

            String temp = this.quadManager.newTemp();
            this.quadManager.generateQuad(addOperator, T1, T2, temp);
            T1 = temp;
        }

        expressionNode.setAttribute(T1);
    }

    private void exitTerm(ASTNode termNode) {
        String T1, T2, mulOperator;

        T1 = termNode.getChildren().getFirst().getAttribute();
        for (int i = 1; i < termNode.getChildren().size(); i += 2) {
            mulOperator = termNode.getChildren().get(i).getAttribute();
            T2 = termNode.getChildren().get(i + 1).getAttribute();

            String temp = this.quadManager.newTemp();
            this.quadManager.generateQuad(mulOperator, T1, T2, temp);
            T1 = temp;
        }
        termNode.setAttribute(T1);
    }

    private void exitOptionalSign(ASTNode node) {
        if (node.getChildren().isEmpty())
            node.setAttribute("");
        else
            node.setAttribute(node.getChildren().getFirst().getAttribute());
    }

}