import java.util.ArrayList;

public class SyntaxAnalyzer {

    private final ArrayList<ArrayList<Token>> lines;

    public SyntaxAnalyzer(ArrayList<ArrayList<Token>> tokenLines) {
        this.lines = tokenLines;
        analyze();
    }

    private void analyze() {
        for (int i = 0; i < lines.size(); i++) {
            ArrayList<Token> tokens = lines.get(i);
            String verdict = classify(tokens);
            System.out.println("Line " + (i + 1) + ": " + verdict);
        }
    }

    private String classify(ArrayList<Token> t) {
        if (t.isEmpty()) {
            return "IGNORED";
        }
        if (isCommentOnly(t)) {
            return "IGNORED";
        }
        if (isProgramHeader(t)) {
            return "ACCEPT";
        }
        if (isConstDecl(t)) {
            return "ACCEPT";
        }
        if (isVarSection(t)) {
            return "ACCEPT";
        }
        if (isVarDecl(t)) {
            return "ACCEPT";
        }
        if (isBegin(t)) {
            return "ACCEPT";
        }
        if (isEnd(t)) {
            return "ACCEPT";
        }
        if (isAssignment(t)) {
            return "ACCEPT";
        }
        if (isIfStatement(t)) {
            return "ACCEPT";
        }
        if (isWriteOrRead(t)) {
            return "ACCEPT";
        }
        return "REJECT";
    }

    private boolean isCommentOnly(ArrayList<Token> t) {
        // Pascal comment: (* ... *)
        return t.size() >= 4
            && t.get(0).getToken().equals("LEFT_PAREN")
            && t.get(1).getToken().equals("MULT_OP")
            && t.get(t.size() - 2).getToken().equals("MULT_OP")
            && t.get(t.size() - 1).getToken().equals("RIGHT_PAREN");
    }

    private boolean isProgramHeader(ArrayList<Token> t) {
        // PROGRAM <ident> ;
        return t.size() == 3
            && isIdentLexeme(t.get(0), "PROGRAM")
            && t.get(1).getToken().equals("IDENT")
            && t.get(2).getToken().equals("SEMI_COLON");
    }

    private boolean isConstDecl(ArrayList<Token> t) {
        // CONST <ident> := <expr> ;
        if (t.size() < 5) return false;
        if (!isIdentLexeme(t.get(0), "CONST"))        return false;
        if (!t.get(1).getToken().equals("IDENT"))     return false;
        if (!t.get(2).getToken().equals("ASSIGN_OP")) return false;
        if (!t.get(t.size() - 1).getToken().equals("SEMI_COLON")) return false;
        return true;
    }

    private boolean isVarSection(ArrayList<Token> t) {
        // VAR
        return t.size() == 1
            && isIdentLexeme(t.get(0), "VAR");
    }

    private boolean isVarDecl(ArrayList<Token> t) {
        // <ident> : <type> ;
        if (t.size() != 3) return false;
        return t.get(0).getToken().equals("IDENT")
            && t.get(1).getLexeme().equals(":")
            && t.get(2).getToken().equals("IDENT");
    }

    private boolean isBegin(ArrayList<Token> t) {
        // BEGIN
        return t.size() == 1
            && isIdentLexeme(t.get(0), "BEGIN");
    }

    private boolean isEnd(ArrayList<Token> t) {
        // END.
        return t.size() >= 2
            && isIdentLexeme(t.get(0), "END")
            && t.get(1).getToken().equals("PERIOD");
    }

    private boolean isAssignment(ArrayList<Token> t) {
        // <ident> := ... ;
        if (t.size() < 4) return false;
        if (!t.get(0).getToken().equals("IDENT"))   return false;
        if (!t.get(1).getToken().equals("ASSIGN_OP")) return false;
        if (!t.get(t.size() - 1).getToken().equals("SEMI_COLON")) return false;
        return true;
    }

    private boolean isIfStatement(ArrayList<Token> t) {
        // IF_STMT ( ... ) THEN ...
        if (t.isEmpty() || !t.get(0).getToken().equals("IF_STMT")) return false;
        // look for RIGHT_PAREN then THEN
        for (int i = 1; i < t.size() - 1; i++) {
            if (t.get(i).getToken().equals("RIGHT_PAREN")
                && isIdentLexeme(t.get(i + 1), "THEN")) {
                return true;
            }
        }
        return false;
    }

    private boolean isWriteOrRead(ArrayList<Token> t) {
        // Write('...'); or Read(id);
        if (isIdentLexeme(t.get(0), "WRITE") || isIdentLexeme(t.get(0), "READ")) {
            return t.get(t.size() - 1).getToken().equals("SEMI_COLON");
        }
        return false;
    }

    private boolean isIdentLexeme(Token tk, String lit) {
        return tk.getToken().equals("IDENT")
            && tk.getLexeme().equalsIgnoreCase(lit);
    }
}
