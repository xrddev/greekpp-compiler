package stages.backend.semantic;

import errors.SemanticErrors;
import stages.backend.symbol.*;
import stages.frontend.parser.ASTNode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ScopeManager {
    private static class Scope {
        private static class SymbolTable {
            private final Map<String, LocalVariable> variables;
            private final Map<String, Procedure> subroutines;

            private SymbolTable() {
                this.variables = new HashMap<>();
                this.subroutines = new HashMap<>();
            }

            public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("Variables:\n");
                if (variables.isEmpty()) {
                    sb.append("  (none)\n");
                } else {
                    variables.values()
                            .stream()
                            .sorted(Comparator.comparingInt(LocalVariable::getOffset).reversed())
                            .forEach(v -> sb.append("  ").append(v.toString()).append("\n"));
                }
                sb.append("Subroutines:\n");
                if (subroutines.isEmpty()) {
                    sb.append("  (none)\n");
                } else {
                    subroutines.values().forEach(s -> {
                        String[] lines = s.toString().split("\n");
                        for (String line : lines) {
                            sb.append("  ").append(line).append("\n");
                        }
                    });
                }

                sb.append("---\n");
                return sb.toString();
            }
        }

        private final Scope parent;
        private final SymbolTable symbolTable;

        public Scope(Scope parent) {
            this.symbolTable = new SymbolTable();
            this.parent = parent;
        }

        public String toString(){
            return symbolTable.toString();
        }

    }


    private final Map<String, TemporaryVariable> currentScopeTemporaryVariables;
    private final StringBuilder scopesLog;
    private Scope currentScope;
    int depth;

    public ScopeManager() {
        this.currentScope = new Scope(null);
        this.scopesLog = new StringBuilder();
        this.currentScopeTemporaryVariables = new HashMap<>();
        this.depth = 0;
    }

    public int getDepth(){
        return this.depth;
    }

    public void openScope() {
        this.currentScope = new Scope(this.currentScope);
        this.depth++;
    }
    public void closeScope() {
        this.logScope();
        this.currentScope = this.currentScope.parent;
        this.depth--;
        this.currentScopeTemporaryVariables.clear();
    }

    public boolean addVariable(LocalVariable localVariable) {
        if(this.currentScope.symbolTable.variables.containsKey(localVariable.getName())){
            return false;
        }
        this.currentScope.symbolTable.variables.put(localVariable.getName(), localVariable);
        return true;
    }
    public boolean addSubroutine(Procedure subroutine) {
        if(this.currentScope.symbolTable.subroutines.containsKey(subroutine.getName())){
            return false;
        }
        this.currentScope.symbolTable.subroutines.put(subroutine.getName(), subroutine);
        return true;
    }

    private void logScope(){
        this.scopesLog.append("Closing scope    || Depth: ").append(this.depth).append("\n");
        this.scopesLog.append(this.currentScope.symbolTable).append("\n");
    }


    public LocalVariable resolveVariable(String name){
        Scope scope = this.currentScope;
        while(scope != null){
            if(scope.symbolTable.variables.containsKey(name)){
                return scope.symbolTable.variables.get(name);
            }
            scope = scope.parent;
        }

        return null;
    }

    public Procedure resolveSubroutine(String name){
        Scope scope = this.currentScope;
        while(scope != null){
            if(scope.symbolTable.subroutines.containsKey(name)){
                return scope.symbolTable.subroutines.get(name);
            }
            scope = scope.parent;
        }
        return null;
    }

    public TemporaryVariable resolveTemporaryVariable(String name){
        return this.currentScopeTemporaryVariables.get(name);
    }



















    public void printScopesLog(String programName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(programName + ".sym"))) {
            writer.write(scopesLog.toString());
            System.out.println("Scope logs have been successfully written to the file " + programName + ".sym" );
        } catch (IOException e) {
            System.err.println("Error while writing scope logs to the file: " + e.getMessage());
        }
    }


    public void declareVariable(ASTNode IDNode, Procedure currentScopeOwner){
        LocalVariable localVariable = new LocalVariable(IDNode.getPlace(), DataType.Integer, this.getDepth());
        if(!this.addVariable(localVariable))
            SemanticErrors.alreadyDeclaredVariable(localVariable.getName(), IDNode.getLine(), IDNode.getColumn());
        currentScopeOwner.getActivationRecord().addLocalVariable(localVariable);
    }

    public void declareReturnVariable(String functionName, Procedure currentScopeOwner){
        LocalVariable localVariable = new LocalVariable(functionName, DataType.Integer, this.getDepth());
        if(!this.addVariable(localVariable))
            SemanticErrors.alreadyDeclaredVariable(localVariable.getName(), 0, 0);
        currentScopeOwner.getActivationRecord().addLocalVariable(localVariable);
    }


    public void declareParameter(ASTNode IDNode, Procedure currentScopeOwner){
        Parameter parameter =  new Parameter(IDNode.getPlace(),DataType.Integer, this.getDepth());
        currentScopeOwner.getActivationRecord().addFormalParameter(parameter);
        if(!this.addVariable(parameter))
            SemanticErrors.alreadyDeclaredParameter(parameter.getName(), IDNode.getLine(), IDNode.getColumn());
    }


    public void declareFunction(Function function, int line , int column){
        if(!this.addSubroutine(function))
            SemanticErrors.alreadyDeclaredFunction(function.getName(), line, column);
    }


    public void declareProcedure(Procedure procedure, int line , int column){
        if(!this.addSubroutine(procedure))
            SemanticErrors.alreadyDeclaredProcedure(procedure.getName(), line, column);
    }




    public void addTemporaryVariable(TemporaryVariable temporaryVariable){
        this.currentScopeTemporaryVariables.put(temporaryVariable.getName(), temporaryVariable);
    }


    public Map<String, TemporaryVariable> getCurrentScopeTemporaryVariables(){
        return this.currentScopeTemporaryVariables;
    }


    public void resolveFunctionInAssigment(ASTNode IDNode){
        Procedure subroutine;

        if((subroutine = this.resolveSubroutine(IDNode.getPlace())) == null)
            SemanticErrors.undeclaredSubroutine(IDNode.getPlace(),IDNode.getLine(), IDNode.getColumn());

        if(!(subroutine instanceof Function))
            SemanticErrors.procedureCallInAssigment(IDNode.getPlace(), IDNode.getLine(), IDNode.getColumn());
    }
}