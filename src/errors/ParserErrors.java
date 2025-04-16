package errors;


import stages.astree.lexer.Token;

public class ParserErrors {

    private ParserErrors() {
        throw new UnsupportedOperationException("This is a static class only for error messages. No objects allowed");
    }


    public static void programEndKeywordIsMissing(Token token){
        System.err.println("Syntax error || Line : " + token.getLine() + " Column : " + token.getColumn() + " ||");
        System.err.println("Every program should end with keyword  <τέλος_προγράμματος>");
        System.err.println("Instead <" + token.getRecognizedString() + "> was found.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void wrongTokenFamily(Token token, Token.TokenFamily expectedFamily){
        System.err.println("Syntax error || Line : " + token.getLine() + " Column : " + token.getColumn() + " ||");
        System.err.println("Illegal String : <" + token.getRecognizedString() + ">");
        System.err.println("<" + expectedFamily + "> was expected at this point but instead a <"
                + token.getFamily() + "> was found.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void wrongTokenFamilyDetailed(Token token , String expectedString, Token.TokenFamily expectedFamily){

        System.err.println("Syntax error || Line : " + token.getLine() + " Column : " + token.getColumn() + " ||");
        System.err.print("<" + token.getRecognizedString() + "> of family <" + token.getFamily() + "> was found ");
        System.err.println("but <" + expectedString + "> of family <" + expectedFamily + "> is expected !");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void EOFMissingAfterProgramEndReached(Token token){
        System.err.println("Syntax error || Line : " + token.getLine() + " Column : " + token.getColumn() + " ||");
        System.err.println("Keyword <τέλος_προγράμματος> has to be the last statement of the code file.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void wrongTokenString(Token token, String exprectedString){
        if(token.getRecognizedString().isEmpty()) EOF(token.getLine(), token.getColumn());

        System.err.println("Syntax error || Line : " + token.getLine() + " Column : " + token.getColumn() + " ||");
        System.err.println("String <" + exprectedString + "> expected but instead <" + token.getRecognizedString() +"> was found.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }

    public static void EOF(int line , int column ){
        System.err.println("Syntax error || Line : " + line + " Column : " + column + " ||");
        System.err.println("ERROR ! || End of file reached while parsing");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }


    public static void integerNumberTooLarge(String number , int line , int column){
        System.err.println("Syntax error || Line : " + line + " Column : " + column + " ||");
        System.err.println("Number <" + number + "> is to large !");
        System.err.println("Numbers range : [-32767 , 32767]");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }


    public static void identifierMaxCharLimitReached(Token token){
        System.err.println("Syntax error || Line : " + token.getLine() + " Column : " + token.getColumn() + " ||");
        System.err.println("Identifier exdev characters limit. <" + token.getRecognizedString() + ">");
        System.err.println("Identifiers can be 30 characters or less.");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }


    public static void assigmentOperatorExpected(String identifier, int line , int column){
        System.err.println("Syntax error || Line : " + line + " Column : " + column + " ||");
        System.err.println("Illegal identifier <" + identifier + ">");
        System.err.println("Assigment operator <:=> expected at this point. Instead character <" + identifier + "> was found");
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }
}
