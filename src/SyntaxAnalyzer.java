import java.util.ArrayList;

public class SyntaxAnalyzer {

    private ArrayList<ArrayList<Token>> tokenLines;  
    private int tokenIndex = 0;
    private int lineNumber = 1;

    public SyntaxAnalyzer(ArrayList<ArrayList<Token>> tokenLines) {
        this.tokenLines = tokenLines;
        while (lineNumber - 1 < tokenLines.size()) {
            stmts();
            nextLine();
        }
    }

    private Token getTokenType() {
        if (lineNumber - 1 < tokenLines.size() &&
            tokenIndex < tokenLines.get(lineNumber - 1).size()) {
            return tokenLines.get(lineNumber - 1).get(tokenIndex);
        }
        return null;
    }

    private void nextToken() {
        tokenIndex++;
        if (lineNumber - 1 < tokenLines.size() &&
            tokenIndex >= tokenLines.get(lineNumber - 1).size()) {
            nextLine();
        }
    }

    private void nextLine() {
        tokenIndex = 0;
        lineNumber++;
    }

    private boolean expect(String expectedType) {
        Token t = getTokenType();
        if (t != null && t.getToken().equals(expectedType)) {
            nextToken();
            return true;
        } else {
            errorMessage();
            return false;
        }
    }

    private void errorMessage() {
        System.out.println("Line " + lineNumber + ": Error");
    }

    private void stmts() {
        Token t = getTokenType();
        if (t == null) return;

        switch (t.getToken()) {
            case "IF_STMT":
                if_stmt();
                break;
            case "ELSE_STMT":
                else_stmt();
                break;
            case "DIV_OP":
                comment_stmt();
                break;
            case "INT_IDENT":
            case "DOUBLE_IDENT":
                declare_stmt();
                break;
            case "IDENT":
                nextToken();
                assign_stmt();
                break;
            default:

                break;
        }
    }

    private void if_stmt() {
        nextToken();                 
        if (!expect("LEFT_PAREN")) return;
        if (!bool()) return;
        if (!expect("RIGHT_PAREN")) return;
    }

    private void else_stmt() {
        nextToken();                
        stmts();                     
    }

    private void comment_stmt() {
        nextToken();                 
        Token t = getTokenType();
        if (t != null && t.getToken().equals("DIV_OP")) {
        } else {
            errorMessage();
        }
    }

    private void declare_stmt() {
        nextToken();                 
        if (!expect("IDENT")) return;
        Token t = getTokenType();
        if (t != null && t.getToken().equals("ASSIGN_OP")) {
            assign_stmt();
        } else if (t != null && t.getToken().equals("COMMA")) {
            nextToken();
            if (!expect("IDENT")) return;
        }
        expect("SEMI_COLON");
    }

    private void assign_stmt() {
        if (!expect("ASSIGN_OP")) return;
        Token t = getTokenType();
        if (t != null && t.getToken().equals("ASSIGN_OP")) {
            errorMessage();
            return;
        }
        expr();
    }

    private void expr() {
        Token t = getTokenType();
        if (t == null) return;

        if (t.getToken().equals("LEFT_PAREN")) {
            expect("LEFT_PAREN");
            expr();
            expect("RIGHT_PAREN");
        } else if (t.getToken().equals("INT_LIT") ||
                   t.getToken().equals("DOUBLE_LIT") ||
                   t.getToken().equals("IDENT")) {
            nextToken();
            t = getTokenType();
            if (t != null && (t.getToken().equals("ADD_OP") ||
                              t.getToken().equals("SUB_OP") ||
                              t.getToken().equals("DIV_OP") ||
                              t.getToken().equals("MULT_OP"))) {
                nextToken();
                expr();
            }
        }
    }

    private boolean bool() {
        Token t = getTokenType();
        if (t == null) return false;

        if (t.getToken().equals("TRUE") || t.getToken().equals("FALSE")) {
            nextToken();
            return true;
        } else if (arithmitic_value()) {
            if (!check_op()) return false;
            if (arithmitic_value()) return true;
        }
        return false;
    }

    private boolean arithmitic_value() {
        Token t = getTokenType();
        if (t != null && (t.getToken().equals("IDENT") ||
                          t.getToken().equals("INT_LIT") ||
                          t.getToken().equals("DOUBLE_LIT"))) {
            nextToken();
            return true;
        }
        return false;
    }

    private boolean check_op() {
        Token t = getTokenType();
        if (t != null && (t.getToken().equals("EQUAL_OP") ||
                          t.getToken().equals("GREATER_OP") ||
                          t.getToken().equals("LESS_OP") ||
                          t.getToken().equals("GEATER_EQ_OP") ||
                          t.getToken().equals("LESS_EQ_OP"))) {
            nextToken();
            return true;
        }
        return false;
    }
}
