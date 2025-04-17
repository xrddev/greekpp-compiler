package stages;

import stages.astree.parser.ASTNode;
import stages.astree.lexer.CharStream;
import stages.astree.lexer.Lexer;
import stages.astree.parser.Parser;
import stages.visitor.IRGenerator;


public class GreekPP {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Error! Compiling Failed || Invalid Syntax");
            System.err.println("Usage : java GreekPP <filename>");
            System.exit(-1);
        }


        //Step 1 - Lexical Analysis
        String codeFilePath = args[0];
        Lexer lexer = new Lexer(new CharStream(codeFilePath));

        //Step 2 - Syntactical Analysis & creating syntax tree & filling symbol table with symbols
        Parser parser = new Parser(lexer);
        ASTNode AbstractSyntaxTreeRoot = parser.getABSRoot();


        IRGenerator irGenerator = new IRGenerator();
        irGenerator.visit(AbstractSyntaxTreeRoot);
        irGenerator.getQuadManager().printQuads();
        irGenerator.getScopeManager().printScopesLog();


    }
}

