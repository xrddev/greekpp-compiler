package errors;

public class SemanticErrors {
    private SemanticErrors(){
        throw new UnsupportedOperationException("This is a static class only for error messages. No objects allowed");
    }

    public static void alreadyDeclaredVariable(String name, int line, int column){
        System.err.println("Semantic Error ! || Line : " + line +" , Column : " + column + " ||");
        System.err.println("Variable <" + name + "> already declared.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void alreadyDeclaredParameter(String name , int line , int column){
        System.err.println("Semantic Error ! || Line : " + line +" , Column : " + column + " ||");
        System.err.println("Parameter <" + name + "> already declared.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void undeclaredInOutParameter(String name , int line , int column){
        System.err.println("Semantic Error ! || Line : " + line +" , Column : " + column + " ||");
        System.err.println("Undeclared inout formal parameter <" + name + ">");
        System.err.println("Inout mode declaration must have declared formal parameter.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void alreadyDeclaredFunction(String name , int line , int column){
        System.err.println("Semantic Error ! || Line : " + line +" , Column : " + column + " ||");
        System.err.println("Function <" + name + "> already declared.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void alreadyDeclaredProcedure(String name , int line , int column){
        System.err.println("Semantic Error ! || Line : " + line +" , Column : " + column + " ||");
        System.err.println("Procedure <" + name + "> already declared.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }
}
