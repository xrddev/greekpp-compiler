package errors;

import stages.frontend.parser.DFAState;

public final class LexerErrors {
    private LexerErrors(){
        throw new UnsupportedOperationException("This is a static class only for error messages. No objects allowed");
    }

    public static void IllegalStateTransition(Character character, DFAState errorState, int line, int column){
        System.err.println("Lexical Error ! || Line : " + line +" , Column : " + column + " ||");
        System.err.println("Character <" + character + "> at illegal position.");
        System.err.println(switch (errorState){
            case LETTER_AFTER_DIGIT_ERROR_STATE -> "A digit cannot be followed directly from a letter.";
            case CLOSING_BRACKET_OUTSIDE_COMMENT_ERROR_STATE -> "Closing curly bracket symbol exists in greek++ only as a comment separator.";
            default -> "Character does not belong to greek++ grammar.";
        });
        System.err.println("Aborting compilation -");
        System.exit(-1);
    }
}
