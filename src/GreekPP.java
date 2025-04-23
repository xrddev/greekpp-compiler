import stages.intermediate.optimaization.IROptimizer;
import stages.parser.ASTNode;
import stages.lexer.CharStream;
import stages.lexer.Lexer;
import stages.parser.Parser;
import stages.intermediate.generation.IRGenerator;


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

        //Step 2 - Syntactical Analysis & creating a syntax tree and filling the symbol table with symbols
        Parser parser = new Parser(lexer);
        ASTNode AbstractSyntaxTreeRoot = parser.getABSRoot();

        //Step 3 - Intermediate Representation Generation and semantic analysis
        IRGenerator irGenerator = new IRGenerator();
        irGenerator.visit(AbstractSyntaxTreeRoot);

        irGenerator.getQuadManager().printQuads();
        irGenerator.getScopeManager().printScopesLog();

        //Step 4 - Quads optimization;
        //IROptimizer irOptimizer = new IROptimizer(irGenerator.getQuadManager().getQuads());


    }
}

