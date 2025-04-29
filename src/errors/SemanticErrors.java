package errors;

public class SemanticErrors {
    private SemanticErrors(){
        throw new UnsupportedOperationException("This is a static class only for error messages. No objects allowed");
    }

    public static void alreadyDeclaredVariable(String name, int line, int column){
        System.err.println("Semantic Error ! || Line : " + line +" , Column : " + column + " ||");
        System.err.println("LocalVariable <" + name + "> already declared.");
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

    public static void undeclaredVariable(String name , int line , int column){
        System.err.println("Semantic Error ! || Line : " + line +" , Column : " + column + " ||");
        System.err.println("Undeclared variable <" + name + ">");
        System.err.println("LocalVariable must be declared before use either as function parameter or as a global variable.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void undeclaredSubroutine(String name , int line , int column){
        System.err.println("Semantic Error ! || Line : " + line +" , Column : " + column + " ||");
        System.err.println("Undeclared subroutine [Function or Procedure ] call <" + name + ">");
        System.err.println("Subroutine must be declared before use.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void procedureCallInAssigment(String name, int line, int column){
        System.err.println("Semantic Error ! || Line : " + line +" , Column : " + column + " ||");
        System.err.println("Illegal call of subroutine  <" + name + ">");
        System.err.println("Subroutines cannot be called inside an assigment. They have no return value");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void subroutineParametersOnCallError(String name , int declaredParameters, int onCallParameters, int line , int column){
        System.err.println("Semantic Error ! || Line : " + line +" , Column : " + column + " ||");
        System.err.println("Wrong number of parameters in the call statement of the subroutine [Function or Procedure] <" + name + ">");
        System.err.println("Number of declared Parameters : " + declaredParameters);
        System.err.println("Number of parameters found on the actually call : "  + onCallParameters);
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void localParameterAsINOUT(String name ,int line , int column){
        System.err.println("Semantic Error ! || Line : " + line +" , Column : " + column + " ||");
        System.err.println("Parameter <" + name + "> is not in function argument list.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }
}
