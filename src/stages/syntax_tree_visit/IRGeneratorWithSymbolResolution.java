package stages.syntax_tree_visit;

import errors.SymbolTableErrors;
import stages.syntax_tree_buildup.ASTNode;
import stages.syntax_tree_buildup.scope.ScopeManager;
import stages.syntax_tree_buildup.scope.symbol.Symbol;

public class IRGeneratorWithSymbolResolution {
    private final ScopeManager scopeManager;

    public IRGeneratorWithSymbolResolution(ASTNode absRoot, ScopeManager scopeManager) {
        this.scopeManager = scopeManager;
        this.visitNode(absRoot);
    }

   private void visitNode(ASTNode node){
        if(node == null) return;

        this.processNodeSymbolTableResolution(node);
        this.processNodeIntermediateRepresentationGeneration(node);

        node.getChildren().forEach(this::visitNode);
   }


   private void processNodeSymbolTableResolution(ASTNode node){
       if(node == null) return;

       Symbol symbol;
       boolean isDefaultCaseNode = false;

       symbol = switch (node.getNodeType()){
           case VARIABLE_USAGE, PARAMETER_USAGE, PARAMETER_INOUT_DECLARATION
                   -> this.scopeManager.lookUpIdentifier(node.getId(), node.getValue());
           case FUNCTION_CALL_IN_ASSIGMENT , SUBROUTINE_USAGE ->
                   this.scopeManager.lookUpSubroutine(node.getId(), node.getValue());
           default -> {
               isDefaultCaseNode = true;
               yield null;
           }
       };

       if(!isDefaultCaseNode && symbol == null)
           SymbolTableErrors.identifierNotDeclared(node.getValue(), node.getLine(), node.getColumn());
   }

   private void processNodeIntermediateRepresentationGeneration(ASTNode node){
   }





}
