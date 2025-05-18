import stages.parser.ASTNode;
import stages.lexer.CharStream;
import stages.lexer.Lexer;
import stages.parser.Parser;
import stages.intermediate.IntermediateStage;

import java.nio.file.Paths;


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

        //Step 2 - Syntactical Analysis & creating a syntax tree
        Parser parser = new Parser(lexer);
        ASTNode AbstractSyntaxTreeRoot = parser.getABSRoot();
        System.out.println(AbstractSyntaxTreeRoot.toString() + "\n" + "-------");
        System.out.println("* Lexical Analysis Completed");
        System.out.println("* Syntactical Analysis Completed [Syntax tree printed above]");

        //Step 3 - Intermediate Representation Generation and semantic analysis
        IntermediateStage intermediateStage = new IntermediateStage();
        intermediateStage.visit(AbstractSyntaxTreeRoot);
        System.out.println("* Intermediate Representation Generation Completed");
        System.out.println("* Semantic Analysis Completed");

        System.out.println("-------");
        String filename = Paths.get(args[0]).getFileName().toString();
        intermediateStage.getQuadManager().printQuads(filename.substring(0, filename.lastIndexOf('.')));
        intermediateStage.getScopeManager().printScopesLog(filename.substring(0, filename.lastIndexOf('.')));
    }
}

