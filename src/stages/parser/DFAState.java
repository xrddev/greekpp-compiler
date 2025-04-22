package stages.parser;

import stages.lexer.CharacterType;

import java.util.EnumMap;
import java.util.Map;

public enum DFAState {
    START,
    WHITESPACE, WHITESPACE_FINAL,
    DIGIT, DIGIT_FINAL,
    IDENTIFIER_OR_KEYWORD, IDENTIFIER_OR_KEYWORD_FINAL,
    ADD_OPERATOR , ADD_OPERATOR_FINAL,
    MULL_OPERATOR, MULL_OPERATOR_FINAL,
    GROUP_SYMBOL , GROUP_SYMBOL_FINAL,
    COMMENT, COMMENT_BRACKET_CONSUME, COMMENT_FINAL,
    EQUAL , EQUAL_FINAL,
    LESS_THAN , LESS_THAN_FINAL ,
    LESS_THAN_EQUAL , LESS_THAN_EQUAL_FINAL ,
    NOT_EQUAL, NOT_EQUAL_FINAL,
    GREATER_THAN, GREATER_THAN_FINAL,
    GREATER_THAN_EQUAL, GREATER_THAN_EQUAL_FINAL,
    DELIMITER, DELIMITER_FINAL,
    MODULO, MODULO_FINAL, EOF_FINAL,
    LETTER_AFTER_DIGIT_ERROR_STATE,
    ILLEGAL_CHARACTER_ERROR_STATE,
    CLOSING_BRACKET_OUTSIDE_COMMENT_ERROR_STATE
    ;

    private static final Map<DFAState, Map<CharacterType, DFAState>> transitionTable = new EnumMap<>(DFAState.class);

    static {
        initializeTransitionTable();
    }

    private static void initializeTransitionTable(){
        //WHITESPACE
        addTransition(DFAState.START, CharacterType.WHITESPACE, DFAState.WHITESPACE);
        addTransition(DFAState.WHITESPACE, CharacterType.WHITESPACE, DFAState.WHITESPACE);
        for(CharacterType chrType : CharacterType.values())
            if(chrType != CharacterType.WHITESPACE)
                addTransition(DFAState.WHITESPACE, chrType, DFAState.WHITESPACE_FINAL);


        //DIGIT
        addTransition(DFAState.START, CharacterType.DIGIT, DFAState.DIGIT);
        addTransition(DFAState.DIGIT, CharacterType.DIGIT, DFAState.DIGIT);
        addTransition(DFAState.DIGIT, CharacterType.LETTER, DFAState.LETTER_AFTER_DIGIT_ERROR_STATE);
        for(CharacterType characterType : CharacterType.values())
            if(characterType != CharacterType.DIGIT && characterType != CharacterType.LETTER)
                addTransition(DFAState.DIGIT , characterType , DFAState.DIGIT_FINAL);



        //IDENTIFIER_OR_KEYWORD
        addTransition(DFAState.START, CharacterType.LETTER, DFAState.IDENTIFIER_OR_KEYWORD);
        addTransition(DFAState.IDENTIFIER_OR_KEYWORD , CharacterType.LETTER , DFAState.IDENTIFIER_OR_KEYWORD);
        addTransition(DFAState.IDENTIFIER_OR_KEYWORD, CharacterType.DIGIT, DFAState.IDENTIFIER_OR_KEYWORD);
        addTransition(DFAState.IDENTIFIER_OR_KEYWORD, CharacterType.UNDERSCORE, DFAState.IDENTIFIER_OR_KEYWORD);
        for(CharacterType characterType : CharacterType.values())
            if(characterType != CharacterType.LETTER
                    && characterType != CharacterType.DIGIT
                    && characterType != CharacterType.UNDERSCORE)
                addTransition(DFAState.IDENTIFIER_OR_KEYWORD, characterType , DFAState.IDENTIFIER_OR_KEYWORD_FINAL);



        //ADD_OPERATOR
        addTransition(DFAState.START , CharacterType.ADD , DFAState.ADD_OPERATOR);
        addTransition(DFAState.START, CharacterType.SUBTRACT, DFAState.ADD_OPERATOR);
        for(CharacterType characterType : CharacterType.values())
            addTransition(DFAState.ADD_OPERATOR, characterType , DFAState.ADD_OPERATOR_FINAL);


        //MULL_OPERATOR
        addTransition(DFAState.START, CharacterType.MULTIPLY , DFAState.MULL_OPERATOR);
        addTransition(DFAState.START, CharacterType.DIVIDE , DFAState.MULL_OPERATOR);
        for(CharacterType characterType : CharacterType.values())
            addTransition(DFAState.MULL_OPERATOR , characterType , DFAState.MULL_OPERATOR_FINAL);


        //EQUAL
        addTransition(DFAState.START, CharacterType.EQUAL, DFAState.EQUAL);
        for(CharacterType characterType : CharacterType.values())
            addTransition(DFAState.EQUAL, characterType, DFAState.EQUAL_FINAL);


        //LESS_THAN - LESS_THAN_EQUAL - NOT_EQUAL
        addTransition(DFAState.START, CharacterType.LESS_THAN, DFAState.LESS_THAN);
        addTransition(DFAState.LESS_THAN, CharacterType.EQUAL, DFAState.LESS_THAN_EQUAL);
        addTransition(DFAState.LESS_THAN, CharacterType.GREATER_THAN, DFAState.NOT_EQUAL);
        for(CharacterType characterType : CharacterType.values()){
            if(characterType != CharacterType.EQUAL && characterType != CharacterType.GREATER_THAN)
                addTransition(DFAState.LESS_THAN, characterType, DFAState.LESS_THAN_FINAL);

            addTransition(DFAState.LESS_THAN_EQUAL, characterType , DFAState.LESS_THAN_EQUAL_FINAL);
            addTransition(DFAState.NOT_EQUAL, characterType, DFAState.NOT_EQUAL_FINAL);
        }

        //GREATER_THAN - GREATER_THAN_EQUAL
        addTransition(DFAState.START, CharacterType.GREATER_THAN, DFAState.GREATER_THAN);
        addTransition(DFAState.GREATER_THAN, CharacterType.EQUAL, DFAState.GREATER_THAN_EQUAL);
        for(CharacterType characterType : CharacterType.values()){
            if(characterType != CharacterType.EQUAL)
                addTransition(DFAState.GREATER_THAN, characterType, DFAState.GREATER_THAN_FINAL);

            addTransition(DFAState.GREATER_THAN_EQUAL, characterType , DFAState.GREATER_THAN_EQUAL_FINAL);
        }


        //MODULO
        addTransition(DFAState.START, CharacterType.MODULO, DFAState.MODULO);
        for(CharacterType characterType : CharacterType.values())
            addTransition(DFAState.MODULO, characterType , DFAState.MODULO_FINAL);


        //GROUP_SYMBOL
        addTransition(DFAState.START, CharacterType.SQUARE_BRACKET, DFAState.GROUP_SYMBOL);
        addTransition(DFAState.START, CharacterType.PARENTHESIS, DFAState.GROUP_SYMBOL);
        for(CharacterType characterType : CharacterType.values())
            addTransition(DFAState.GROUP_SYMBOL, characterType , DFAState.GROUP_SYMBOL_FINAL);


        //COMMENT
        addTransition(DFAState.START, CharacterType.CURLY_BRACKET_OPEN , DFAState.COMMENT);
        addTransition(DFAState.COMMENT, CharacterType.CURLY_BRACKET_CLOSE, DFAState.COMMENT_BRACKET_CONSUME);
        for(CharacterType chrType : CharacterType.values()) {
            if (chrType != CharacterType.CURLY_BRACKET_CLOSE)
                addTransition(DFAState.COMMENT, chrType, DFAState.COMMENT);

            addTransition(DFAState.COMMENT_BRACKET_CONSUME, chrType , DFAState.COMMENT_FINAL);
        }

        //DELIMITERS
        addTransition(DFAState.START, CharacterType.COMMA, DFAState.DELIMITER);
        addTransition(DFAState.START, CharacterType.SEMICOLON, DFAState.DELIMITER);
        addTransition(DFAState.START, CharacterType.COLON, DFAState.DELIMITER);
        for(CharacterType characterType : CharacterType.values())
            addTransition(DFAState.DELIMITER, characterType ,DFAState.DELIMITER_FINAL);


        //CLOSING CURLY BRACKET
        for(DFAState dfaState : DFAState.values()){
            if(dfaState != COMMENT)
                addTransition(dfaState, CharacterType.CURLY_BRACKET_CLOSE, DFAState.CLOSING_BRACKET_OUTSIDE_COMMENT_ERROR_STATE);
        }


        //Here we override edge cases on above assigns also

        //EOF
        for(DFAState dfaState : DFAState.values())
            if(dfaState != DFAState.WHITESPACE)
                addTransition(dfaState, CharacterType.EOF, DFAState.EOF_FINAL);


        //Illegal Character
        for(DFAState dfaState : DFAState.values())
            if(dfaState != DFAState.COMMENT)
                addTransition(dfaState, CharacterType.ILLEGAL_CHARACTER, DFAState.ILLEGAL_CHARACTER_ERROR_STATE);

    }
    private static void addTransition(DFAState from, CharacterType inputType, DFAState to) {
        transitionTable.computeIfAbsent(from, k -> new EnumMap<>(CharacterType.class)).put(inputType, to);
    }


    public boolean isErrorState() {
        return switch (this){
            case ILLEGAL_CHARACTER_ERROR_STATE,
                 LETTER_AFTER_DIGIT_ERROR_STATE,
                 CLOSING_BRACKET_OUTSIDE_COMMENT_ERROR_STATE-> true;
            default -> false;
        };
    }

    public DFAState getNextState(CharacterType characterType){
        return transitionTable.get(this).get(characterType);
    }

    public boolean isFinal() {
        return switch (this) {
            case DIGIT_FINAL,
                 IDENTIFIER_OR_KEYWORD_FINAL,
                 ADD_OPERATOR_FINAL,
                 MULL_OPERATOR_FINAL,
                 GROUP_SYMBOL_FINAL,
                 EQUAL_FINAL,
                 LESS_THAN_FINAL,
                 LESS_THAN_EQUAL_FINAL,
                 NOT_EQUAL_FINAL,
                 GREATER_THAN_FINAL,
                 GREATER_THAN_EQUAL_FINAL,
                 DELIMITER_FINAL,
                 MODULO_FINAL,
                 EOF_FINAL
                    -> true;
            default -> false;
        };
    }

    public boolean triggersDFARestart(){
        return this == WHITESPACE_FINAL || this == COMMENT_FINAL;
    }
}