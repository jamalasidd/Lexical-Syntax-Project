public class Token {
    private String tokenType;
    private String lexeme;

    public Token(String tokenType, String lexeme) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
    }

    public String getToken() {
        return tokenType;
    }

    public String getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return tokenType;
    }
}
