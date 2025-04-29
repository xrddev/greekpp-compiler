package stages.semantic;

import stages.semantic.symbol.LocalVariable;
import stages.semantic.symbol.Procedure;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
                    variables.values().forEach(v -> sb.append("  ").append(v.toString()).append("\n"));
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


    public Scope getCurrentScope() {
        return this.currentScope;
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

    public void printScopesLog(String programName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(programName + ".sym"))) {
            writer.write(scopesLog.toString());
            System.out.println("Scope logs have been successfully written to the file " + programName + ".sym" );
        } catch (IOException e) {
            System.err.println("Error while writing scope logs to the file: " + e.getMessage());
        }
    }

}