package errors;

import stages.semantic.symbol.FunctionSymbol;
import stages.semantic.symbol.Identifier;
import stages.semantic.symbol.ProcedureSymbol;


public class SemanticErrors {
    private SemanticErrors(){
        throw new UnsupportedOperationException("This is a static class only for error messages. No objects allowed");
    }

    public static void identifierAlreadyDeclared(Identifier identifier, int line , int column){
        System.err.println("Semantic Error || Line : " + line + " Column : " + column + " ||");
        System.err.println("Variable <" + identifier.getName() + "> of type <" + identifier.getDataType() +"> is already defined at current scope.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void functionAlreadyDefinedAtScope(FunctionSymbol function, int line , int column){
        System.err.println("Semantic Error || Line : " + line + " Column : " + column + " ||");
        System.err.println("Function <" + function.getName() + "> is already defined at current scope.");
        System.err.println("Aborting compilation -");
        System.exit(-1);

    }

    public static void procedureAlreadyDefinedAtScope(ProcedureSymbol procedure, int line , int column){
        System.err.println("Semantic Error || Line : " + line + " Column : " + column + " ||");
        System.err.println("Procedure <" + procedure.getName() + "> is already defined at current scope.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void undeclaredVariable(String variable, int line , int column){
        System.err.println("Semantic Error || Line : " + line + " Column : " + column + " ||");
        System.err.println("Variable <" + variable + "> of type <Integer> is used but not declared at current scope !");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }
}
