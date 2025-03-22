package stages;

import stages.lexer.CharStream;
import stages.lexer.Lexer;
import stages.parser.Parser;
import stages.semantic.SemanticAnalyzer;


public class GreekPP {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Error! Compiling Failed || Invalid Syntax");
            System.err.println("Usage : java GreekPP <filename>");
            System.exit(-1);
        }

        String codeFilePath = args[0];

        //Stage 1
        Lexer lexer = new Lexer(new CharStream(codeFilePath));
        //Stage 2
        Parser parser = new Parser(lexer);
        //Stage 3
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(parser.getABSRoot());
        //Stage 4

    }
}

