package stages.frontend.lexer;

import errors.LexerErrors;
import stages.frontend.parser.DFAState;

import java.util.Set;

public class Lexer {
    public static final Set<String> KEYWORDS = Set.of(
            "πρόγραμμα", "δήλωση", "εάν", "τότε", "αλλιώς", "εάν_τέλος",
            "επανάλαβε", "μέχρι", "όσο", "όσο_τέλος",
            "για", "έως", "με_βήμα", "για_τέλος",
            "διάβασε", "γράψε", "συνάρτηση", "διαδικασία", "είσοδος", "έξοδος",
            "διαπροσωπεία", "αρχή_συνάρτησης", "τέλος_συνάρτησης",
            "αρχή_διαδικασίας", "τέλος_διαδικασίας",
            "αρχή_προγράμματος", "τέλος_προγράμματος",
            "ή", "εκτέλεσε", "και", "όχι"
    );

    private final CharStream charStream;

    public Lexer(CharStream charStream){
        this.charStream = charStream;
    }

    public Token getNextToken(){
        DFAState currentState = DFAState.START;
        int tokenStart = this.charStream.getPosition();

        while(true){
            char lookAheadChar = this.charStream.peekNextChar();
            CharacterType lookAheadCharType = CharacterType.of(lookAheadChar);


            currentState = currentState.getNextState(lookAheadCharType);

            if(currentState.isErrorState())
                LexerErrors.IllegalStateTransition(
                        lookAheadChar
                        ,currentState
                        ,this.charStream.getLinesCount()
                        ,this.charStream.getColumn());

            if(currentState.triggersDFARestart()){
                currentState = DFAState.START;
                tokenStart = this.charStream.getPosition();
                continue;
            }

            if (currentState.isFinal()) break;  //Final states dont consume lookAhead token.
            this.charStream.consumeNextChar();
        }
        return new Token(currentState, tokenStart, this.charStream);
    }

    public void printAllTokens(){
        Token token;
        do{
            System.out.println(token = this.getNextToken());
        }while (token.getFamily() != Token.TokenFamily.EOF);
    }
}
