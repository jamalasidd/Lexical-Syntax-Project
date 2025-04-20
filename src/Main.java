import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File(
            "ParseProjectPascalCode.txt"
        );
        
        //parses through all the lexemes to collect
        LexicalAnalyzer lex = new LexicalAnalyzer(file);
        
    
    }

    
}