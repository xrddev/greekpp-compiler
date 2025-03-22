package stages.semantic;

import common.ASTNode;
import stages.semantic.intermediate.IRGenerator;
import stages.semantic.symbol.Identifier;

import java.util.List;

public class SemanticAnalyzer {
    private final SymbolTable symbolTable;
    private final IRGenerator irGenerator;


    public SemanticAnalyzer(ASTNode ASTRoot){
        this.symbolTable = new SymbolTable();
        this.irGenerator = new IRGenerator();
        this.analyze(ASTRoot);

    }

    private void analyze(ASTNode node){
        if(node.getNodeType().isToken()){
            this.processToken(node);
            return;
        }
        this.openScopeIfNeeded(node.getNodeType());
        this.dispatchAnalysis(node);
        this.analyzeChildren(node.getChildren());
        this.closeScopeIfNeeded(node.getNodeType());
    }

    private void openScopeIfNeeded(ASTNode.NodeType nodeType){
        if(isScopeBoundaryNode(nodeType)) this.symbolTable.openScope();
    }
    private void closeScopeIfNeeded(ASTNode.NodeType nodeType){
        if(isScopeBoundaryNode(nodeType)) {
            System.out.println(this.symbolTable);
            this.symbolTable.closeScope();
        }
    }
    private void analyzeChildren(List<ASTNode> children){
        children.forEach(this::analyze);
    }
    private boolean isScopeBoundaryNode(ASTNode.NodeType nodeType){
        return switch (nodeType){
            case PROGRAM_BLOCK -> true;
            default -> false;
        };
    }

    private void dispatchAnalysis(ASTNode node){
        switch (node.getNodeType()){
            case DECLARATIONS -> this.analyzeDeclarations(node);
        }
    }

    private void analyzeDeclarations(ASTNode declarations){
        //Declarations are optional.
        if(declarations.getChildren().isEmpty()) return;

        ASTNode varList = declarations.getChildren().get(1);
        varList.getChildren()
                .stream()
                .filter(node -> node.getNodeType() == ASTNode.NodeType.VARIABLE_IDENTIFIER)
                .forEach(node -> {
                    Identifier variable = new Identifier(
                            node.getValue(),
                            Identifier.IdentifierKind.VARIABLE,
                            Identifier.IdentifierDataType.INTEGER);

                    this.symbolTable.defineIdentifier(variable,node.getLine(),node.getColumn());});
    }


    private void processToken(ASTNode node){

    }

}
