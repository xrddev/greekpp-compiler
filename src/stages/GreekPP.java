package stages;

import stages.syntax_tree_buildup.ASTNode;
import stages.lexer.CharStream;
import stages.lexer.Lexer;
import stages.syntax_tree_buildup.parser.Parser;
import stages.syntax_tree_buildup.scope.ScopeManager;
import stages.syntax_tree_visit.IRGeneratorWithSymbolResolution;

import java.util.Map;


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
        //Step 3 - Generating IR code & resolving the symbol table symbols
        IRGeneratorWithSymbolResolution IRGenerator
                = new IRGeneratorWithSymbolResolution(parser.getABSRoot(), parser.getScopeManager());
        //Step 4

    }
}

