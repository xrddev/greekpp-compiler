package stages.lexer;
import stages.parser.DFAState;


public class Token {
    public enum TokenFamily {
        KEYWORD,
        ADD_OPERATOR,
        MUL_OPERATOR,
        REL_OPERATOR,
        REFERENCE_OPERATOR,
        DELIMITER,
        GROUP_SYMBOL,
        IDENTIFIER,
        NUMBER,
        EOF,
        OTHER;

        public static TokenFamily fromDFAState(DFAState state, CharStream charStreamPointer , int startIndex,
                                               int stopIndex) {
            return switch (state) {
                case IDENTIFIER_OR_KEYWORD_FINAL ->
                        Lexer.KEYWORDS.contains(charStreamPointer.getText(startIndex,stopIndex)) ?
                                KEYWORD : IDENTIFIER;
                case DIGIT_FINAL -> NUMBER;
                case ADD_OPERATOR_FINAL -> ADD_OPERATOR;
                case MULL_OPERATOR_FINAL -> MUL_OPERATOR;
                case GROUP_SYMBOL_FINAL -> GROUP_SYMBOL;
                case DELIMITER_FINAL -> DELIMITER;
                case MODULO_FINAL -> REFERENCE_OPERATOR;
                case EQUAL_FINAL , LESS_THAN_FINAL, LESS_THAN_EQUAL_FINAL
                , NOT_EQUAL_FINAL, GREATER_THAN_FINAL, GREATER_THAN_EQUAL_FINAL -> REL_OPERATOR;
                case EOF_FINAL -> EOF;
                default -> OTHER;
            };
        }
    }

    private final TokenFamily family;
    private final int startIndex;
    private final int stopIndex;
    private final int line;
    private final int column;
    private final CharStream charStreamPointer;

    public Token(DFAState finalState, int startIndex, CharStream charStreamPointer) {
        this.charStreamPointer = charStreamPointer;
        this.line = charStreamPointer.getLinesCount();
        this.startIndex = startIndex;
        this.stopIndex = charStreamPointer.getPosition() - 1;   //Position always looking the next char to be consumed.
        this.column = charStreamPointer.getColumn() - (this.stopIndex - this.startIndex + 1);   // We consider the column index the first char of the token
        this.family = TokenFamily.fromDFAState(finalState, charStreamPointer, startIndex, stopIndex);
    }

    public int getColumn(){
        return this.column;
    }

    public TokenFamily getFamily(){
        return this.family;
    }

    public int getLine(){
        return this.line;
    }

    public int getRecognizedStringLength(){
        return this.stopIndex - this.startIndex + 1;
    }

    public String getRecognizedString(){
        return this.charStreamPointer.getText(this.startIndex, this.stopIndex);
    }

    @Override
    public String toString() {
        return "Token{" +
                "family=" + family +
                ", String='" + this.getRecognizedString() + "'" +
                ", start=" + startIndex +
                ", stop=" + stopIndex +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}
