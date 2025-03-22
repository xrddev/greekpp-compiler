package stages.semantic;

import errors.SemanticErrors;
import stages.semantic.symbol.FunctionSymbol;
import stages.semantic.symbol.Identifier;
import stages.semantic.symbol.ProcedureSymbol;
import stages.semantic.symbol.Symbol;

import java.util.*;

public class SymbolTable {
    private static class Scope  {
        Map<String, Identifier> identifiers = new HashMap<>();
        Map<String, FunctionSymbol> functions = new HashMap<>();
        Map<String, ProcedureSymbol> procedures = new HashMap<>();
    }

    private final Deque<Scope> scopes;

    public SymbolTable(){
        this.scopes = new ArrayDeque<>();
    }

    public void openScope(){
        this.scopes.push(new Scope());
    }
    public void closeScope() {
        if (scopes.isEmpty()) {
            throw new IllegalStateException("Cannot close scope: No open scopes available.");
        }
        scopes.pop();
    }


    public void defineIdentifier(Identifier identifier, int line , int column){
        Scope currentScope = this.scopes.peek();
        if(currentScope.identifiers.containsKey(identifier.getName()))
            SemanticErrors.identifierAlreadyDeclared(identifier,line ,column);

        currentScope.identifiers.put(identifier.getName(), identifier);
    }

    public void defineFunction(FunctionSymbol function, int line , int column){
        Iterator<Scope> iterator = this.scopes.iterator();
        iterator.next();
        Scope parentScope = iterator.next();

        if(parentScope.functions.containsKey(function.getName()))
            SemanticErrors.functionAlreadyDefinedAtScope(function, line, column);

        parentScope.functions.put(function.getName(), function);
    }

    public void defineProcedure(ProcedureSymbol procedure, int line , int column){
        Iterator<Scope> iterator = this.scopes.iterator();
        iterator.next();
        Scope parentScope = iterator.next();

        if(parentScope.procedures.containsKey(procedure.getName()))
            SemanticErrors.procedureAlreadyDefinedAtScope(procedure, line , column);

        parentScope.procedures.put(procedure.getName(), procedure);
    }


    public Symbol lookUp(String name, Class<? extends Symbol> symbolClass) {

        for (Scope scope : scopes) {
            if (symbolClass == Identifier.class)
                return scope.identifiers.get(name);
            else if (symbolClass == FunctionSymbol.class)
                return scope.functions.get(name);
            else if (symbolClass == ProcedureSymbol.class)
                return scope.procedures.get(name);
        }
        return null;
    }




    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int scopeLevel = this.scopes.size();
        for(Scope scope : scopes){
            stringBuilder.append("Scope Level ").append(scopeLevel--).append(" : \n");

            for(Identifier identifierSymbol : scope.identifiers.values())
                stringBuilder.append(identifierSymbol).append("\n");

            for(FunctionSymbol functionSymbol : scope.functions.values())
                stringBuilder.append(functionSymbol).append("\n");

            for(ProcedureSymbol procedureSymbol : scope.procedures.values())
                stringBuilder.append(procedureSymbol).append("\n");

            stringBuilder.append("---------\n");
        }
        return stringBuilder.toString();
    }

}

