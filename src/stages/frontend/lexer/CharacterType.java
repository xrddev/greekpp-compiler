package stages.frontend.lexer;

public enum CharacterType {
    EOF,
    LETTER,
    DIGIT,
    WHITESPACE,
    ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO,
    PARENTHESIS, SQUARE_BRACKET, COMMA, LESS_THAN, GREATER_THAN, EQUAL, COLON, SEMICOLON,
    UNDERSCORE, CURLY_BRACKET_OPEN, CURLY_BRACKET_CLOSE,
    ILLEGAL_CHARACTER;

    public static CharacterType of(char c) {
        return switch (c) {
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> DIGIT;
            case ' ', '\t', '\n', '\r' -> WHITESPACE;
            case '+' -> ADD;
            case '-' -> SUBTRACT;
            case '*' -> MULTIPLY;
            case '/' -> DIVIDE;
            case '%' -> MODULO;
            case '(', ')' -> PARENTHESIS;
            case '[', ']' -> SQUARE_BRACKET;
            case '{' -> CURLY_BRACKET_OPEN;
            case '}' -> CURLY_BRACKET_CLOSE;
            case '<' -> LESS_THAN;
            case '>' -> GREATER_THAN;
            case '=' -> EQUAL;
            case '_' -> UNDERSCORE;
            case ';' -> SEMICOLON;
            case ':' -> COLON;
            case ',' -> COMMA;
            case 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                 'u', 'v', 'w', 'x', 'y', 'z',
                 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                 'U', 'V', 'W', 'X', 'Y', 'Z',
                 'Α', 'Β', 'Γ', 'Δ', 'Ε', 'Ζ', 'Η', 'Θ', 'Ι', 'Κ', 'Λ', 'Μ', 'Ν', 'Ξ', 'Ο', 'Π', 'Ρ', 'Σ', 'Τ', 'Υ',
                 'Φ', 'Χ', 'Ψ', 'Ω',
                 'α', 'β', 'γ', 'δ', 'ε', 'ζ', 'η', 'θ', 'ι', 'κ', 'λ', 'μ', 'ν', 'ξ', 'ο', 'π', 'ρ', 'σ', 'ς', 'τ',
                 'υ', 'φ', 'χ', 'ψ', 'ω',
                 'ά', 'έ', 'ή', 'ί', 'ό', 'ύ', 'ώ' -> LETTER;
            case (char) -1 -> EOF; //We peak the chars so we need to accept peeking EOF
            default -> ILLEGAL_CHARACTER;
        };
    }
}