package stages.syntax_tree_buildup.scope;

import errors.SymbolTableErrors;
import stages.syntax_tree_buildup.ASTNode;
import stages.syntax_tree_buildup.scope.symbol.*;

import java.util.HashMap;
import java.util.Map;

public class ScopeManager {
    private static class Scope {
        private static class SymbolTable {
            private final Map<String, Identifier> identifiers;
            private final Map<String, Subroutine> subroutines;

            private SymbolTable(){
                this.identifiers = new HashMap<>();
                this.subroutines = new HashMap<>();
            }

            private boolean addIdentifier(Identifier identifier){
                if(this.identifiers.containsKey(identifier.getName()))
                    return false;

                this.identifiers.put(identifier.getName(),identifier);
                return true;
            }
            private boolean addFunction(FunctionSymbol function){
                if(this.subroutines.containsKey(function.getName()))
                    return false;

                this.subroutines.put(function.getName(),function);
                return true;
            }

            private boolean addProcedure(ProcedureSymbol procedure){
                if(this.subroutines.containsKey(procedure.getName()))
                    return false;

                this.subroutines.put(procedure.getName(),procedure);
                return true;
            }

            private String print(int depth){
                StringBuilder stringBuilder = new StringBuilder();
                this.identifiers.values().forEach(x -> stringBuilder.append("\t".repeat(depth)).append(x).append("\n"));
                this.subroutines.values().forEach(x -> stringBuilder.append("\t".repeat(depth)).append(x).append("\n"));
                return stringBuilder.toString();
            }
        }

        /// /////
        private final Scope parent;
        private final SymbolTable symbolTable;

        public Scope(Scope parent) {
            this.parent = parent;
            this.symbolTable = new SymbolTable();
        }

        public String toString(){
            return this.symbolTable.toString();
        }
    }

    /// ///////////
    private final Map<Long,Scope> nodeIdToScopeMap;
    private final StringBuilder logs;

    private final Scope globalScope;
    private Scope currentScope;
    private Subroutine parameterOwnerPointer;
    private int depth;

    public ScopeManager() {
        this.nodeIdToScopeMap = new HashMap<>();
        this.logs = new StringBuilder("Symbol Table Logs :\n");

        this.depth = 0;
        this.globalScope = new Scope(null);
        this.logs.append("Opening new Scope. Now depth is : 0").append("\n");
        this.currentScope = this.globalScope;

        this.parameterOwnerPointer = null;
    }

    public void openScope(){
        this.currentScope = new Scope(this.currentScope);
        this.depth++;
        this.logScopeOpen();
    }

    public void closeScope(){
        this.logScopeCloseMessage1();
        this.currentScope = this.currentScope.parent;
        this.depth--;
        this.logScopeCloseMessage2();
    }

    public void bindNodeWithCurrentScope(boolean symbolTableEnabled, long nodeId){
        if(!symbolTableEnabled) return;
        this.nodeIdToScopeMap.put(nodeId, this.currentScope);
    }

    public void registerSymbol(boolean symbolTableEnabled, ASTNode node){
        if(!symbolTableEnabled) return;

        boolean success;
        Symbol symbol = null;

        success = switch (node.getNodeType()){
            case VARIABLE_IDENTIFIER ->{
                Identifier variable = new Identifier(node.getValue(), Identifier.IdentifierKind.VARIABLE, Identifier.IdentifierDataType.INTEGER);
                symbol = variable;
                yield this.currentScope.symbolTable.addIdentifier(variable);
            }
            case PROGRAM_NAME -> {
                Identifier programName = new Identifier(node.getValue(), Identifier.IdentifierKind.PROGRAM_NAME, null);
                symbol = programName;
                yield this.currentScope.symbolTable.addIdentifier(programName);
            }
            case FUNCTION_IDENTIFIER -> {
                FunctionSymbol function = new FunctionSymbol(node.getValue());
                Identifier functionReturnIdentifier = new Identifier(node.getValue(), Identifier.IdentifierKind.FUNCTION_RETURN, Identifier.IdentifierDataType.INTEGER);

                symbol = function;
                this.parameterOwnerPointer = function;
                this.currentScope.symbolTable.addIdentifier(functionReturnIdentifier);
                yield this.globalScope.symbolTable.addFunction(function);
            }
            case PARAMETER_IDENTIFIER -> {
                Identifier parameter = new Identifier(node.getValue(), Identifier.IdentifierKind.FUNCTION_PARAMETER , Identifier.IdentifierDataType.INTEGER);
                symbol = parameter;
                this.parameterOwnerPointer.addParameter(parameter);
                yield this.currentScope.symbolTable.addIdentifier(parameter);
            }
            case PROCEDURE_IDENTIFIER -> {
                ProcedureSymbol procedure = new ProcedureSymbol(node.getValue());
                symbol = procedure;
                this.parameterOwnerPointer = procedure;
                yield this.globalScope.symbolTable.addProcedure(procedure);
            }
            case PARAMETER_INOUT_DECLARATION, VARIABLE_USAGE, PARAMETER_USAGE, SUBROUTINE_USAGE -> {
                //SECOND PARSING CHECK
                yield true;
            }
            default -> false;
        };

        if(!success)
            SymbolTableErrors.identifierAlreadyDeclared(symbol, node.getLine(), node.getColumn());
    }


    public Symbol lookUpIdentifier(Long nodeId, String identifierSignature){
        for(Scope nodeScope = this.nodeIdToScopeMap.get(nodeId); nodeScope != null; nodeScope = nodeScope.parent){
            if(nodeScope.symbolTable.identifiers.containsKey(identifierSignature))
                return nodeScope.symbolTable.identifiers.get(identifierSignature).markAsUsed();
        }
        return null;
    }

    public Symbol lookUpSubroutine(Long nodeId, String functionSignature){
        for(Scope nodeScope = this.nodeIdToScopeMap.get(nodeId); nodeScope != null; nodeScope = nodeScope.parent){
            if(nodeScope.symbolTable.subroutines.containsKey(functionSignature))
                return nodeScope.symbolTable.subroutines.get(functionSignature).markAsUsed();
        }
        return null;
    }

    public Map<Long, Scope> getNodeIdToScopeMap() {
        return nodeIdToScopeMap;
    }

    public Scope getGlobalScope() {
        return globalScope;
    }


    public void printLogsToSymFile(){
        System.out.println(this.logs);
    }

    private void logScopeOpen(){
        this.logs.append("\t".repeat(this.depth))
                .append("Opening new Scope. Now depth is : ").append(this.depth).append("\n");
    }

    private void logScopeCloseMessage1(){
        this.logs.append("\n").append("\t".repeat(this.depth)).append("|Snapshot of scopes current structure|");

        int tempDepth = this.depth;
        for(Scope nodeScope = this.currentScope; nodeScope != null; nodeScope = nodeScope.parent){
            this.logs.append("\n").append("\t".repeat(this.depth)).append("Scope with depth :").append(tempDepth--).append("\n");
            this.logs.append(nodeScope.symbolTable.print(this.depth));
        }

        this.logs.append("\t".repeat(this.depth));
        this.logs.append("\n\n").append("\t".repeat(this.depth)).append("Closing Scope with depth : ").append(this.depth).append("\n");

    }

    private void logScopeCloseMessage2(){
        this.logs.append("\t".repeat(depth)).append("Now depth is : ").append(depth).append("\n");
    }
}
