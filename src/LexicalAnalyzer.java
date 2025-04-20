import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

public class LexicalAnalyzer {

    private ArrayList<Character> lexemeBuffer = new ArrayList<>();
    private Token currentToken;
    private int currentCharCode;

    public ArrayList<ArrayList<Token>> lineWiseTokens = new ArrayList<>();
    public ArrayList<Token> allTokens = new ArrayList<>();
    private ArrayList<String> sourceLines = new ArrayList<>();

    private int lineCounter;
    private ArrayList<Token> tokenList = new ArrayList<>();
    private HashMap<String, Token> reservedWordsMap = new HashMap<>();

    public LexicalAnalyzer(File file) throws IOException {
        defineTokenTypes();
        mapReservedWords();
        initTokenCollection();

        // Read source lines for pretty output
        BufferedReader lineReader = new BufferedReader(new FileReader(file));
        String currentLine;
        while ((currentLine = lineReader.readLine()) != null) {
            sourceLines.add(currentLine);
        }
        lineReader.close();

        FileReader stream = new FileReader(file);
        PushbackReader reader = new PushbackReader(stream);

        while ((currentCharCode = reader.read()) != -1) {
            handleNewLine();
            scanToken(reader);
        }

        reader.close();
        displayTokenOutput();
    }

    private void initTokenCollection() {
        lineCounter = 1;
        lineWiseTokens.add(new ArrayList<>());
    }

    private void handleNewLine() {
        if ((char) currentCharCode == '\n') {
            lineCounter++;
            lineWiseTokens.add(new ArrayList<>());
        }
    }

    private void defineTokenTypes() {
        tokenList.add(new Token("EQUAL_BOOL", ""));
        tokenList.add(new Token("IDENT", ""));
        tokenList.add(new Token("PERIOD", ""));
        tokenList.add(new Token("INT_LIT", ""));
        tokenList.add(new Token("LEFT_PAREN", ""));
        tokenList.add(new Token("ASSIGN_OP", ""));
        tokenList.add(new Token("SEMI_COLON", ""));
        tokenList.add(new Token("STRING_LIT", ""));
        tokenList.add(new Token("COMMA", ""));
        tokenList.add(new Token("RIGHT_PAREN", ""));
        tokenList.add(new Token("TRUE", ""));
        tokenList.add(new Token("GREATER_EQ_BOOL", ""));
        tokenList.add(new Token("WHILE_STMT", ""));
        tokenList.add(new Token("SUB_OP", ""));
        tokenList.add(new Token("LESS_BOOL", ""));
        tokenList.add(new Token("IF_STMT", ""));
        tokenList.add(new Token("MULT_OP", ""));
        tokenList.add(new Token("DOUBLE_IDENT", ""));
        tokenList.add(new Token("FOR_STMT", ""));
        tokenList.add(new Token("CHAR_LIT", ""));
        tokenList.add(new Token("DOUBLE_LIT", ""));
        tokenList.add(new Token("RIGHT_BRACKET", ""));
        tokenList.add(new Token("LEFT_BRACKET", ""));
        tokenList.add(new Token("ADD_OP", ""));
        tokenList.add(new Token("INT_IDENT", ""));
        tokenList.add(new Token("FALSE", ""));
        tokenList.add(new Token("DIV_OP", ""));
        tokenList.add(new Token("LESS_EQ_BOOL", ""));
        tokenList.add(new Token("GREATER_BOOL", ""));
        tokenList.add(new Token("ELSE_STMT", ""));
    }

    private void mapReservedWords() {
        reservedWordsMap.put("if", tokenList.get(15));
        reservedWordsMap.put("else", tokenList.get(29));
        reservedWordsMap.put("for", tokenList.get(18));
        reservedWordsMap.put("while", tokenList.get(12));
        reservedWordsMap.put("int", tokenList.get(24));
        reservedWordsMap.put("double", tokenList.get(17));
        reservedWordsMap.put("true", tokenList.get(10));
        reservedWordsMap.put("false", tokenList.get(25));
    }

    private void collectChar(char ch) {
        if (lexemeBuffer.size() <= 98 && ch != '\0') {
            lexemeBuffer.add(ch);
        }
    }

    private int readNextChar(PushbackReader reader) throws IOException {
        return reader.read();
    }

    private void processOperator(char ch, PushbackReader reader) throws IOException {
        switch (ch) {
            case '(':
                collectChar(ch); currentToken = tokenList.get(4); break;
            case ')':
                collectChar(ch); currentToken = tokenList.get(9); break;
            case '{':
                collectChar(ch); currentToken = tokenList.get(22); break;
            case '}':
                collectChar(ch); currentToken = tokenList.get(21); break;
            case ',':
                collectChar(ch); currentToken = tokenList.get(8); break;
            case ';':
                collectChar(ch); currentToken = tokenList.get(6); break;
            case '.':
                collectChar(ch); currentToken = tokenList.get(2); break;
            case '+':
                collectChar(ch);
                currentCharCode = readNextChar(reader);
                if ((char) currentCharCode == '=') {
                    collectChar((char) currentCharCode);
                    currentToken = tokenList.get(5);
                } else {
                    reader.unread(currentCharCode);
                    currentToken = tokenList.get(23);
                }
                break;
            case '-':
                collectChar(ch); currentToken = tokenList.get(13); break;
            case '*':
                collectChar(ch); currentToken = tokenList.get(16); break;
            case '/':
                collectChar(ch); currentToken = tokenList.get(26); break;
            case '=':
                collectChar(ch);
                currentCharCode = readNextChar(reader);
                if ((char) currentCharCode == '=') {
                    collectChar((char) currentCharCode);
                    currentToken = tokenList.get(0);
                } else {
                    reader.unread(currentCharCode);
                    currentToken = tokenList.get(5);
                }
                break;
            case '>':
                collectChar(ch);
                currentCharCode = readNextChar(reader);
                if ((char) currentCharCode == '=') {
                    collectChar((char) currentCharCode);
                    currentToken = tokenList.get(11);
                } else {
                    reader.unread(currentCharCode);
                    currentToken = tokenList.get(28);
                }
                break;
            case '<':
                collectChar(ch);
                currentCharCode = readNextChar(reader);
                if ((char) currentCharCode == '=') {
                    collectChar((char) currentCharCode);
                    currentToken = tokenList.get(27);
                } else {
                    reader.unread(currentCharCode);
                    currentToken = tokenList.get(14);
                }
                break;
            default:
                break;
        }
    }

    private void scanToken(PushbackReader reader) throws IOException {
        char currentChar = (char) currentCharCode;
    
        if (Character.isLetter(currentChar)) {
            collectChar(currentChar);
            while (true) {
                currentCharCode = readNextChar(reader);
                currentChar = (char) currentCharCode;
                if (Character.isLetterOrDigit(currentChar)) {
                    collectChar(currentChar);
                } else {
                    reader.unread(currentCharCode);
                    break;
                }
            }
    
            String lexemeStr = buildLexemeString(lexemeBuffer);
            if (!lexemeStr.matches("^[A-Za-z][A-Za-z0-9]*$")) {
                currentToken = new Token("UNKNOWN", lexemeStr);
            } else {
                currentToken = reservedWordsMap.getOrDefault(lexemeStr, tokenList.get(1)); // IDENT
            }
        }
    
        else if (Character.isDigit(currentChar)) {
            int decimalSeen = 0;
            collectChar(currentChar);
            while (true) {
                currentCharCode = readNextChar(reader);
                currentChar = (char) currentCharCode;
    
                if (Character.isDigit(currentChar)) {
                    collectChar(currentChar);
                } else if (currentChar == '.' && decimalSeen == 0) {
                    collectChar(currentChar);
                    decimalSeen++;
                } else {
                    reader.unread(currentCharCode);
                    break;
                }
            }
            currentToken = (decimalSeen > 0) ? tokenList.get(20) : tokenList.get(3); // DOUBLE_LIT or INT_LIT
        }
    
        else if (Character.isWhitespace(currentChar)) {
            return;
        }
    
        else {
            processOperator(currentChar, reader);
        }
    
        String lexemeStr = buildLexemeString(lexemeBuffer);
        if (!lexemeStr.isEmpty()) {
            Token fullToken = new Token(currentToken.getToken(), lexemeStr);
            lineWiseTokens.get(lineCounter - 1).add(fullToken);
            allTokens.add(fullToken);
        }
    
        lexemeBuffer.clear();
    }
    
    private void displayTokenOutput() {
        System.out.println("=================================================");
        for (int i = 0; i < lineWiseTokens.size(); i++) {
            ArrayList<Token> tokensForLine = lineWiseTokens.get(i);
            if (tokensForLine.isEmpty()) continue;

            String originalLine = (i < sourceLines.size()) ? sourceLines.get(i) : "";
            System.out.println("Line " + (i + 1) + ": " + originalLine);

            for (Token token : tokensForLine) {
                System.out.printf("Lexeme: %-15s Token: %s%n", token.getLexeme(), token.getToken());
            }

            System.out.println();
        }
        System.out.println("=================================================");
    }

    private String buildLexemeString(ArrayList<Character> buffer) {
        StringBuilder builder = new StringBuilder(buffer.size());
        for (Character ch : buffer) {
            builder.append(ch);
        }
        return builder.toString();
    }
}
