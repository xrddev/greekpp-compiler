package stages.visitor;

import stages.astree.parser.ASTNode;

// Abstract Visitor class with default (empty) implementations
public abstract class Visitor {

    public void visit(ASTNode node) {
        switch (node.getNodeType()) {
            case PROGRAM_BLOCK -> this.visitProgramBlock(node);
            case PROGRAM_END_KEYWORD -> this.visitProgramEndKeyword(node);
            case INPUT_STATEMENT -> this.visitInputStatement(node);
            case PRINT_STATEMENT -> this.visitPrintStatement(node);
            case EXPRESSION -> this.visitExpression(node);
            case OPTIONAL_SIGN -> this.visitOptionalSign(node);
            case TERM -> this.visitTerm(node);
            case FACTOR -> this.visitFactor(node);
            case CONDITION -> this.visitCondition(node);
            case BOOL_TERM -> this.visitBoolTerm(node);
            case BOOL_FACTOR -> this.visitBoolFactor(node);
            case WHILE_STATEMENT -> this.visitWhileStatement(node);
            case IF_STATEMENT -> this.visitIfStatement(node);
            case ELSE_STATEMENT -> this.visitElseStatement(node);
            case ASSIGMENT_STATEMENT -> this.visitAssignmentStatement(node);
            case DO_STATEMENT -> this.visitDoStatement(node);
            case FOR_STATEMENT -> this.visitForStatement(node);
            case STEP -> this.visitStep(node);
            case PROCEDURE -> this.visitProcedure(node);
            case FUNCTION -> this.visitFunction(node);
            case FUNCTION_INPUT -> this.visitFunctionInput(node);
            case FUNCTION_OUTPUT -> this.visitFunctionOutput(node);
            case CALL_STATEMENT -> this.visitCallStatement(node);
            case ACTUAL_PARAMETER_ITEM -> this.visitActualParameterItem(node);
            case PROGRAM_NAME_IDENTIFIER,
                 VARIABLE_IDENTIFIER,
                 PARAMETER_IDENTIFIER,
                 VARIABLE_USAGE,
                 SUBROUTINE_USAGE,
                 FUNCTION_CALL_IN_ASSIGMENT,
                 FUNCTION_IDENTIFIER,
                 PROCEDURE_IDENTIFIER,
                 PARAMETER_USAGE -> this.visitID(node);
            default -> this.defaultVisit(node);
        }
    }


    public void defaultVisit(ASTNode node) {
        node.getChildren().forEach(this::visit);
    }


    public abstract void visitProgramBlock(ASTNode node);
    public abstract void visitProgramEndKeyword(ASTNode node);
    public abstract void visitInputStatement(ASTNode node);
    public abstract void visitPrintStatement(ASTNode node);
    public abstract void visitExpression(ASTNode node);
    public abstract void visitOptionalSign(ASTNode node);
    public abstract void visitTerm(ASTNode node);
    public abstract void visitFactor(ASTNode node);
    public abstract void visitCondition(ASTNode node);
    public abstract void visitBoolTerm(ASTNode node);
    public abstract void visitBoolFactor(ASTNode node);
    public abstract void visitWhileStatement(ASTNode node);
    public abstract void visitIfStatement(ASTNode node);
    public abstract void visitElseStatement(ASTNode node);
    public abstract void visitAssignmentStatement(ASTNode node);
    public abstract void visitDoStatement(ASTNode node);
    public abstract void visitForStatement(ASTNode node);
    public abstract void visitStep(ASTNode node);
    public abstract void visitProcedure(ASTNode node);
    public abstract void visitFunction(ASTNode node);
    public abstract void visitID(ASTNode node);
    public abstract void visitFunctionInput(ASTNode node);
    public abstract void visitFunctionOutput(ASTNode node);
    public abstract void visitCallStatement(ASTNode node);
    public abstract void visitActualParameterItem(ASTNode node);

}

