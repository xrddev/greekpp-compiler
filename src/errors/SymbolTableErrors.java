package errors;

import stages.syntax_tree_buildup.scope.symbol.Symbol;

public class SymbolTableErrors {

    public static void identifierAlreadyDeclared(Symbol identifier, int line , int column){
        System.err.println("Syntax error || Line : " + line + " Column : " + column + " ||");
        System.err.println("Identifier <" + identifier + "> already declared.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void identifierNotDeclared(String symbolName , int line , int column){
        System.err.println("Syntax error || Line : " + line + " Column : " + column + " ||");
        System.err.println("Identifier <" + symbolName + "> not declared.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }
}
