package stages;

import stages.syntax_tree_buildup.ASTNode;
import stages.lexer.CharStream;
import stages.lexer.Lexer;
import stages.syntax_tree_buildup.parser.Parser;
import stages.syntax_tree_buildup.scope.ScopeManager;
import stages.syntax_tree_visit.SemanticIRGenerator;
import stages.syntax_tree_visit.intermediate.Quad;
import stages.syntax_tree_visit.intermediate.QuadManager;


public class GreekPP {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Error! Compiling Failed || Invalid Syntax");
            System.err.println("Usage : java GreekPP <filename>");
            System.exit(-1);
        }

        String codeFilePath = args[0];
        //Step 1 - Lexical Analysis
        Lexer lexer = new Lexer(new CharStream(codeFilePath));

        //Step 2 - Syntactical Analysis & creating syntax tree & filling symbol table with symbols
        Parser parser = new Parser(lexer);
        ASTNode AbstractSyntaxTreeRoot = parser.getABSRoot();
        ScopeManager scopeManager = parser.getScopeManager();

        //Step 3 - Generating IR code & resolving the symbol table
        SemanticIRGenerator irGenerator = new SemanticIRGenerator(AbstractSyntaxTreeRoot, scopeManager);

        //Step 4
        int count = 0;
        for(Quad element : irGenerator.getQuadManager().getQuads()){
            System.out.println(count++ + " : " + element);
        }

    }
}

