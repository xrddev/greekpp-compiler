package stages.visitor.semantic;

import stages.visitor.semantic.symbol.Procedure;
import stages.visitor.semantic.symbol.Variable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScopeManager {
    private static class Scope {
        private static class SymbolTable {
            private final Map<String, Variable> variables;
            private final Map<String, Procedure> subroutines;

            private SymbolTable() {
                this.variables = new HashMap<>();
                this.subroutines = new HashMap<>();
            }

            public String toString(){
                StringBuilder sb = new StringBuilder();
                sb.append("Variables:\n");
                this.variables.forEach((name, variable) -> sb.append("    ").append(variable).append("\n"));
                sb.append("Subroutines:\n");
                this.subroutines.forEach((name, subroutine) -> sb.append("    ").append(subroutine).append("\n"));
                sb.append("---\n---");
                return sb.toString();
            }
        }

        private final Scope parent;
        private final SymbolTable symbolTable;
        private int offset;

        public Scope(Scope parent) {
            this.symbolTable = new SymbolTable();
            this.parent = parent;
            this.offset = 12;
        }

        public String toString(){
            return symbolTable.toString();
        }

    }


    private final StringBuilder scopesLog;
    private Scope currentScope;
    int depth;

    public ScopeManager() {
        this.currentScope = new Scope(null);
        this.scopesLog = new StringBuilder();
        this.depth = 0;
    }

    public void openScope() {
        this.currentScope = new Scope(this.currentScope);
        this.depth++;
    }
    public void closeScope() {
        this.logScope();
        this.currentScope = this.currentScope.parent;
        this.depth--;
    }
    public boolean addVariable(Variable variable) {
        if(this.currentScope.symbolTable.variables.containsKey(variable.getName())){
            return false;
        }
        this.currentScope.symbolTable.variables.put(variable.getName(), variable);
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

    public int getCurrentScopeOffset(){
        return this.currentScope.offset;
    }

    public void setCurrentScopeOffset(int offset){
        this.currentScope.offset = offset;
    }

    public Variable resolveVariable(String name){
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

    public void printScopesLog() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("greekpp.sym"))) {
            writer.write(scopesLog.toString()); // Γράφει τα περιεχόμενα του scopesLog στο αρχείο
            System.out.println("Scope logs have been successfully written to the file greekpp.sym");
        } catch (IOException e) {
            System.err.println("Error while writing scope logs to the file: " + e.getMessage());
        }
    }

}