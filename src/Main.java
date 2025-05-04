import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Point to the file containing the formatted source code
        File file = new File("ParseProjectPascalCode.txt");

   
        LexicalAnalyzer lex = new LexicalAnalyzer(file);

        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lex.lineWiseTokens);
    }
}
