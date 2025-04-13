package stages.syntax_tree_buildup.parser;

import stages.syntax_tree_buildup.ASTNode;
import stages.lexer.Token;
import errors.ParserErrors;
import stages.lexer.Lexer;
import stages.syntax_tree_buildup.scope.ScopeManager;

public class Parser {

    private final Lexer lexer;
    private final ASTNode ABSRoot;
    private final ScopeManager scopeManager;
    private static final boolean SYMBOL_TABLE_ENABLED = true;
    private Token lookAheadToken;


    public Parser(Lexer lexer){
        this.lexer = lexer;
        this.lookAheadToken = this.lexer.getNextToken();

        this.scopeManager = new ScopeManager();
        this.ABSRoot = this.parseProgram();
    }

    private void consumeToken(){
        this.lookAheadToken = this.lexer.getNextToken();
    }

    private boolean lookAheadTokenStringEqualsTo(String string){
        return this.lookAheadToken.getRecognizedString().equals(string);
    }
    private boolean lookAheadTokenString_NOT_EqualTo(String string){
        return !this.lookAheadToken.getRecognizedString().equals(string);
    }
    private boolean lookAheadTokenFamilyEqualsTo(Token.TokenFamily tokenFamily){
        return this.lookAheadToken.getFamily() == tokenFamily;
    }
    private boolean lookAheadTokenFamily_NOT_EqualsTo(Token.TokenFamily tokenFamily){
        return this.lookAheadToken.getFamily() != tokenFamily;
    }
    public ASTNode getABSRoot(){
        return this.ABSRoot;
    }
    public ScopeManager getScopeManager(){return this.scopeManager;}

    ///////////////////////////////////////////////////////////////////////
    //Greek++ grammar rules :

    private ASTNode programEnd(){
        if(this.lookAheadTokenString_NOT_EqualTo("τέλος_προγράμματος"))
            ParserErrors.programEndKeywordIsMissing(this.lookAheadToken);

        ASTNode programEndNode = new ASTNode(this.lookAheadToken.getRecognizedString(),
                                                ASTNode.NodeType.KEYWORD,
                                                this.lookAheadToken.getLine(),
                                                this.lookAheadToken.getColumn());
        this.consumeToken();
        if(this.notEOFAfterProgramEnd())
            ParserErrors.EOFMissingAfterProgramEndReached(this.lookAheadToken);
        return programEndNode;
    }

    private boolean notEOFAfterProgramEnd(){
        return (this.lookAheadToken.getFamily() != Token.TokenFamily.EOF) ||
                (this.lookAheadToken.getFamily() == Token.TokenFamily.EOF && this.lookAheadToken.getRecognizedStringLength() > 0);
    }

    public ASTNode parseProgram(){
        ASTNode root = new ASTNode(ASTNode.NodeType.ROOT);

        root.addChild(this.keyword("πρόγραμμα"));
        root.addChild(this.ID(ASTNode.NodeType.PROGRAM_NAME));
        root.addChild(this.programBlock());

        return root;
    }

    private ASTNode programBlock(){
        ASTNode programBlockNode =  new ASTNode(ASTNode.NodeType.PROGRAM_BLOCK);

        programBlockNode.addChild(this.declarations());
        programBlockNode.addChild(this.subprograms());
        programBlockNode.addChild(this.keyword("αρχή_προγράμματος"));
        programBlockNode.addChild(this.sequence());
        programBlockNode.addChild(this.programEnd());

        return programBlockNode;
    }

    private ASTNode declarations(){
        ASTNode declarationsNode = new ASTNode(ASTNode.NodeType.DECLARATIONS);

        while(this.lookAheadTokenStringEqualsTo("δήλωση")){
            declarationsNode.addChild(this.keyword("δήλωση"));
            declarationsNode.addChild(this.varList(ASTNode.NodeType.VARIABLE_IDENTIFIER));
        }
        //Declarations are optional
        return declarationsNode;
    }

    private ASTNode varList(ASTNode.NodeType identifierType){
        ASTNode varListNode = new ASTNode(ASTNode.NodeType.VAR_LIST);

        varListNode.addChild(this.ID(identifierType));
        while (this.lookAheadTokenStringEqualsTo(",")){
            varListNode.addChild(this.comma());
            varListNode.addChild(this.ID(identifierType));
        }
        return varListNode;
    }

    private ASTNode subprograms() {
        ASTNode subprogramsNode = new ASTNode(ASTNode.NodeType.SUBPROGRAMS);

        while (true) {
            switch (lookAheadToken.getRecognizedString()) {
                case "συνάρτηση" -> subprogramsNode.addChild(this.function());
                case "διαδικασία" -> subprogramsNode.addChild(this.procedure());
                default -> {return subprogramsNode;}
            }
        }
    }

    private ASTNode function(){
        ASTNode functionNode = new ASTNode(ASTNode.NodeType.FUNCTION);
        this.scopeManager.openScope();

        functionNode.addChild(this.keyword("συνάρτηση"));
        functionNode.addChild(this.ID(ASTNode.NodeType.FUNCTION_IDENTIFIER));
        functionNode.addChild(this.parenthesisOpen());
        functionNode.addChild(this.formalParametersList());
        functionNode.addChild(this.closeParenthesis());
        functionNode.addChild(this.functionBlock());

        this.scopeManager.closeScope();
        return functionNode;
    }

    private ASTNode procedure(){
        ASTNode procedureNode = new ASTNode(ASTNode.NodeType.PROCEDURE);
        this.scopeManager.openScope();

        procedureNode.addChild(this.keyword("διαδικασία"));
        procedureNode.addChild(this.ID(ASTNode.NodeType.PROCEDURE_IDENTIFIER));
        procedureNode.addChild(this.parenthesisOpen());
        procedureNode.addChild(this.formalParametersList());
        procedureNode.addChild(this.closeParenthesis());
        procedureNode.addChild(this.procedureBlock());

        this.scopeManager.closeScope();
        return procedureNode;
    }

    private ASTNode formalParametersList(){
        ASTNode formalParametersListNode = new ASTNode(ASTNode.NodeType.FORMAL_PARAMETERS_LIST);

        if (this.lookAheadTokenFamilyEqualsTo(Token.TokenFamily.IDENTIFIER))
            formalParametersListNode.addChild(this.varList(ASTNode.NodeType.PARAMETER_IDENTIFIER));

        //Formal Parameters List is optional
        return formalParametersListNode;
    }

    private ASTNode functionBlock(){
        ASTNode functionBlockNode = new ASTNode(ASTNode.NodeType.FUNCTION_BLOCK);

        functionBlockNode.addChild(this.keyword("διαπροσωπεία"));
        functionBlockNode.addChild(this.functionInput());
        functionBlockNode.addChild(this.functionOutput());
        functionBlockNode.addChild(this.declarations());
        functionBlockNode.addChild(this.subprograms());
        functionBlockNode.addChild(this.keyword("αρχή_συνάρτησης"));
        functionBlockNode.addChild(this.sequence());
        functionBlockNode.addChild(this.keyword("τέλος_συνάρτησης"));
        return functionBlockNode;
    }

    private ASTNode procedureBlock(){
        ASTNode procedureBlock = new ASTNode(ASTNode.NodeType.PROCEDURE_BLOCK);

        procedureBlock.addChild(this.keyword("διαπροσωπεία"));
        procedureBlock.addChild(this.functionInput());
        procedureBlock.addChild(this.functionOutput());
        procedureBlock.addChild(this.declarations());
        procedureBlock.addChild(this.subprograms());
        procedureBlock.addChild(this.keyword("αρχή_διαδικασίας"));
        procedureBlock.addChild(this.sequence());
        procedureBlock.addChild(this.keyword("τέλος_διαδικασίας"));

        return procedureBlock;
    }

    private ASTNode functionInput(){
        ASTNode functionInputNode = new ASTNode(ASTNode.NodeType.FUNCTION_INPUT);

        if(this.lookAheadTokenStringEqualsTo("είσοδος")){
            functionInputNode.addChild(this.keyword("είσοδος"));
            functionInputNode.addChild(this.varList(ASTNode.NodeType.PARAMETER_INOUT_DECLARATION));
        }
        //Function Input is optional
        return functionInputNode;
    }

    private ASTNode functionOutput(){
        ASTNode functionOutputNode = new ASTNode(ASTNode.NodeType.FUNCTION_OUTPUT);

        if(this.lookAheadTokenStringEqualsTo("έξοδος")) {
            functionOutputNode.addChild(this.keyword("έξοδος"));
            functionOutputNode.addChild(this.varList(ASTNode.NodeType.PARAMETER_INOUT_DECLARATION));
        }
        //Function Output is optional
        return functionOutputNode;
    }

    private ASTNode sequence() {
        ASTNode sequenceNode = new ASTNode(ASTNode.NodeType.SEQUENCE);

        sequenceNode.addChild(this.statement());
        while (this.lookAheadTokenStringEqualsTo(";")){
            sequenceNode.addChild(this.semicolon());
            sequenceNode.addChild(this.statement());
        }

        return sequenceNode;
    }

    private ASTNode statement(){
        ASTNode statementNode = new ASTNode(ASTNode.NodeType.STATEMENT);

        statementNode.addChild(switch (this.lookAheadToken.getRecognizedString()) {
            case "εάν" -> this.ifStatement();
            case "όσο" -> this.whileStatement();
            case "επανάλαβε" -> this.doStatement();
            case "για" -> this.forStatement();
            case "διάβασε" -> this.inputStatement();
            case "γράψε" -> this.printStatement();
            case "εκτέλεσε" -> this.callStatement();
            default -> this.assigmentStatement();
        });
        return statementNode;
    }

    private ASTNode assigmentStatement(){
        ASTNode assigmentStatementNode = new ASTNode(ASTNode.NodeType.ASSIGMENT_STATEMENT);

        assigmentStatementNode.addChild(this.ID(ASTNode.NodeType.VARIABLE_USAGE));
        assigmentStatementNode.addChild(this.colon());
        assigmentStatementNode.addChild(this.equal());
        assigmentStatementNode.addChild(this.expression());

        return assigmentStatementNode;
    }

    private ASTNode ifStatement(){
        ASTNode ifStatementNode = new ASTNode(ASTNode.NodeType.IF_STATEMENT);

        ifStatementNode.addChild(this.keyword("εάν"));
        ifStatementNode.addChild(this.condition());
        ifStatementNode.addChild(this.keyword("τότε"));
        ifStatementNode.addChild(this.sequence());
        ifStatementNode.addChild(this.elseStatement());
        ifStatementNode.addChild(this.keyword("εάν_τέλος"));

        return ifStatementNode;
    }

    private ASTNode elseStatement(){
        ASTNode elseStatementNode = new ASTNode(ASTNode.NodeType.ELSE_STATEMENT);

        if(this.lookAheadTokenStringEqualsTo("αλλιώς")){
            elseStatementNode.addChild(this.keyword("αλλιώς"));
            elseStatementNode.addChild(this.sequence());
        }
        ///Else statement is optional
        return elseStatementNode;
    }

    private ASTNode whileStatement(){
        ASTNode whileStatementNode = new ASTNode(ASTNode.NodeType.WHILE_STATEMENT);

        whileStatementNode.addChild(this.keyword("όσο"));
        whileStatementNode.addChild(this.condition());
        whileStatementNode.addChild(this.keyword("επανάλαβε"));
        whileStatementNode.addChild(this.sequence());
        whileStatementNode.addChild(this.keyword("όσο_τέλος"));

        return whileStatementNode;
    }

    private ASTNode doStatement(){
        ASTNode doStatementNode = new ASTNode(ASTNode.NodeType.DO_STATEMENT);

        doStatementNode.addChild(this.keyword("επανάλαβε"));
        doStatementNode.addChild(this.sequence());
        doStatementNode.addChild(this.keyword("μέχρι"));
        doStatementNode.addChild(this.condition());

        return doStatementNode;
    }

    private ASTNode forStatement(){
        ASTNode forStatementNode = new ASTNode(ASTNode.NodeType.FOR_STATEMENT);
        this.scopeManager.openScope();

        forStatementNode.addChild(this.keyword("για"));
        forStatementNode.addChild(this.ID(ASTNode.NodeType.VARIABLE_IDENTIFIER));
        forStatementNode.addChild(this.colon());
        forStatementNode.addChild(this.equal());
        forStatementNode.addChild(this.expression());
        forStatementNode.addChild(this.keyword("έως"));
        forStatementNode.addChild(this.expression());
        forStatementNode.addChild(this.step());
        forStatementNode.addChild(this.keyword("επανάλαβε"));
        forStatementNode.addChild(this.sequence());
        forStatementNode.addChild(this.keyword("για_τέλος"));

        this.scopeManager.closeScope();
        return forStatementNode;
    }

    private ASTNode step(){
        ASTNode stepNode = new ASTNode(ASTNode.NodeType.STEP);

        if(this.lookAheadTokenStringEqualsTo("με_βήμα")){
            stepNode.addChild(this.keyword("με_βήμα"));
            stepNode.addChild(this.expression());
        }
        //Step is optional
        return stepNode;
    }

    private ASTNode printStatement(){
        ASTNode printStatementNode = new ASTNode(ASTNode.NodeType.PRINT_STATEMENT);

        printStatementNode.addChild(this.keyword("γράψε"));
        printStatementNode.addChild(this.expression());

        return printStatementNode;
    }

    private ASTNode inputStatement(){
        ASTNode inputStatementNode = new ASTNode(ASTNode.NodeType.INPUT_STATEMENT);

        inputStatementNode.addChild(this.keyword("διάβασε"));
        inputStatementNode.addChild(this.ID(ASTNode.NodeType.VARIABLE_USAGE));

        return inputStatementNode;
    }

    private ASTNode callStatement(){
        ASTNode callStatementNode = new ASTNode(ASTNode.NodeType.CALL_STATEMENT);

        callStatementNode.addChild(this.keyword("εκτέλεσε"));
        callStatementNode.addChild(this.ID(ASTNode.NodeType.SUBROUTINE_USAGE));
        callStatementNode.addChild(this.idTail());

        return callStatementNode;
    }

    private ASTNode idTail(){
        ASTNode idTailNode = new ASTNode(ASTNode.NodeType.ID_TAIL);

        if(this.lookAheadTokenStringEqualsTo("("))
            idTailNode.addChild(this.actualParameters());
        //idTail is optional
        return idTailNode;
    }

    private ASTNode actualParameters(){
        ASTNode actualParametersNode = new ASTNode(ASTNode.NodeType.ACTUAL_PARAMETERS);

        actualParametersNode.addChild(this.parenthesisOpen());
        actualParametersNode.addChild(this.actualParameterList());
        actualParametersNode.addChild(this.closeParenthesis());

        return actualParametersNode;
    }

    private ASTNode actualParameterList(){
        ASTNode actualParameterListNode = new ASTNode(ASTNode.NodeType.ACTUAL_PARAMETER_LIST);

        if(this.lookAheadTokenString_NOT_EqualTo(")")){
            actualParameterListNode.addChild(this.actualParameterItem());
            while (this.lookAheadTokenStringEqualsTo(",")){
                actualParameterListNode.addChild(this.comma());
                actualParameterListNode.addChild(this.actualParameterItem());
            }
        }
        //Parameter List is optional
        return actualParameterListNode;
    }

    private ASTNode actualParameterItem(){
        ASTNode actualParameterItemNode = new ASTNode(ASTNode.NodeType.ACTUAL_PARAMETER_ITEM);

        if(this.lookAheadTokenStringEqualsTo("%")){
            actualParameterItemNode.addChild(this.referenceOperator());
            actualParameterItemNode.addChild(this.ID(ASTNode.NodeType.PARAMETER_USAGE));
        }else {
            actualParameterItemNode.addChild(this.expression());
        }

        return actualParameterItemNode;
    }

    private ASTNode condition(){
        ASTNode conditionNode = new ASTNode(ASTNode.NodeType.CONDITION);

        conditionNode.addChild(this.boolTerm());
        while (this.lookAheadTokenStringEqualsTo("ή")){
            conditionNode.addChild(this.keyword("ή"));
            conditionNode.addChild(this.boolTerm());
        }
        return conditionNode;
    }

    private ASTNode boolTerm(){
        ASTNode boolTermNode = new ASTNode(ASTNode.NodeType.BOOL_TERM);

        boolTermNode.addChild(this.boolFactor());
        while(this.lookAheadTokenStringEqualsTo("και")){
            boolTermNode.addChild(this.keyword("και"));
            boolTermNode.addChild(this.boolFactor());
        }
        return boolTermNode;
    }

    private ASTNode boolFactor(){
        ASTNode boolFactorNode = new ASTNode(ASTNode.NodeType.BOOL_FACTOR);

        switch (this.lookAheadToken.getRecognizedString()){
            case "όχι" -> {
                boolFactorNode.addChild(this.keyword("όχι"));
                boolFactorNode.addChild(this.squareBracketOpen());
                boolFactorNode.addChild(this.condition());
                boolFactorNode.addChild(this.squareBracketClose());
            }
            case "[" -> {
                boolFactorNode.addChild(this.squareBracketOpen());
                boolFactorNode.addChild(this.condition());
                boolFactorNode.addChild(this.squareBracketClose());
            }
            default -> {
                boolFactorNode.addChild(this.expression());
                boolFactorNode.addChild(this.relationalOperator());
                boolFactorNode.addChild(this.expression());
            }
        }
        return boolFactorNode;
    }

    private ASTNode expression(){
        ASTNode expressionNode = new ASTNode(ASTNode.NodeType.EXPRESSION);

        expressionNode.addChild(this.optionalSign());
        expressionNode.addChild(this.term());
        while (this.lookAheadTokenFamilyEqualsTo(Token.TokenFamily.ADD_OPERATOR)){
            expressionNode.addChild(this.addOperator());
            expressionNode.addChild(this.term());
        }

        return expressionNode;
    }

    private ASTNode term(){
        ASTNode termNode = new ASTNode(ASTNode.NodeType.TERM);

        termNode.addChild(this.factor());
        while(this.lookAheadTokenFamilyEqualsTo(Token.TokenFamily.MUL_OPERATOR)){
            termNode.addChild(this.mulOperator());
            termNode.addChild(this.factor());
        }

        return termNode;
    }

    private ASTNode factor(){
        ASTNode factorNode = new ASTNode(ASTNode.NodeType.FACTOR);

        switch (this.lookAheadToken.getFamily()){
            case Token.TokenFamily.NUMBER ->
                    factorNode.addChild(this.INTEGER());
            case Token.TokenFamily.IDENTIFIER -> {
                //Default case : Identifier is a variable;
                ASTNode IDNode = this.ID(ASTNode.NodeType.VARIABLE_USAGE);
                ASTNode IDTailNode = this.idTail();

                //If idTailIs is present, identifier is function call
                if(!IDTailNode.getChildren().isEmpty())
                    IDNode.setNodeType(ASTNode.NodeType.FUNCTION_CALL_IN_ASSIGMENT);

                factorNode.addChild(IDNode);
                factorNode.addChild(IDTailNode);
            }
            default -> {
                factorNode.addChild(this.parenthesisOpen());
                factorNode.addChild(this.expression());
                factorNode.addChild(this.closeParenthesis());
            }
        }
        return factorNode;
    }


    private ASTNode optionalSign(){
        ASTNode optionalSignNode = new ASTNode(ASTNode.NodeType.OPTIONAL_SIGN);

        if(this.lookAheadTokenFamilyEqualsTo(Token.TokenFamily.ADD_OPERATOR))
            optionalSignNode.addChild(this.addOperator());

        return optionalSignNode;
    }



    // Terminal Nodes

    private ASTNode relationalOperator(){
        if(this.lookAheadTokenFamily_NOT_EqualsTo(Token.TokenFamily.REL_OPERATOR))
            ParserErrors.wrongTokenFamily(this.lookAheadToken, Token.TokenFamily.REL_OPERATOR);

        ASTNode relationalOperatorNode = new ASTNode(
                this.lookAheadToken.getRecognizedString(),
                ASTNode.NodeType.REL_OPERATOR,
                this.lookAheadToken.getLine(),
                this.lookAheadToken.getColumn());

        this.consumeToken();
        return relationalOperatorNode;
    }

    private ASTNode addOperator(){
        if(this.lookAheadTokenFamily_NOT_EqualsTo(Token.TokenFamily.ADD_OPERATOR))
            ParserErrors.wrongTokenFamily(this.lookAheadToken, Token.TokenFamily.ADD_OPERATOR);

        ASTNode addOperatorNode = new ASTNode(
                this.lookAheadToken.getRecognizedString(),
                ASTNode.NodeType.ADD_OPERATOR,
                this.lookAheadToken.getLine(),
                this.lookAheadToken.getColumn());

        this.consumeToken();
        return addOperatorNode;
    }

    private ASTNode mulOperator(){
        if(this.lookAheadTokenFamily_NOT_EqualsTo(Token.TokenFamily.MUL_OPERATOR))
            ParserErrors.wrongTokenFamily(this.lookAheadToken, Token.TokenFamily.MUL_OPERATOR);

        ASTNode mulOperatorNode = new ASTNode(
                this.lookAheadToken.getRecognizedString(),
                ASTNode.NodeType.MUL_OPERATOR,
                this.lookAheadToken.getLine(),
                this.lookAheadToken.getColumn());

        this.consumeToken();
        return mulOperatorNode;
    }

    private ASTNode squareBracketOpen(){
        if(this.lookAheadTokenString_NOT_EqualTo("["))
            ParserErrors.wrongTokenString(this.lookAheadToken,"[");

        ASTNode squareBracketOpenNode = new ASTNode(
                this.lookAheadToken.getRecognizedString(),
                ASTNode.NodeType.GROUP_SYMBOL,
                this.lookAheadToken.getLine(),
                this.lookAheadToken.getColumn());

        this.consumeToken();
        return squareBracketOpenNode;
    }

    private ASTNode squareBracketClose(){
        if(this.lookAheadTokenString_NOT_EqualTo("]"))
            ParserErrors.wrongTokenString(this.lookAheadToken,"]");

        ASTNode squareBracketCloseNode = new ASTNode(
                this.lookAheadToken.getRecognizedString(),
                ASTNode.NodeType.GROUP_SYMBOL,
                this.lookAheadToken.getLine(),
                this.lookAheadToken.getColumn());

        this.consumeToken();
        return squareBracketCloseNode;
    }

    private ASTNode parenthesisOpen(){
        if(this.lookAheadTokenString_NOT_EqualTo("("))
            ParserErrors.wrongTokenString(this.lookAheadToken, "(");

        ASTNode parenthesisOpenNode = new ASTNode(
                this.lookAheadToken.getRecognizedString(),
                ASTNode.NodeType.GROUP_SYMBOL,
                this.lookAheadToken.getLine(),
                this.lookAheadToken.getColumn());

        this.consumeToken();
        return parenthesisOpenNode;
    }

    private ASTNode closeParenthesis(){
        if(this.lookAheadTokenString_NOT_EqualTo(")"))
            ParserErrors.wrongTokenString(this.lookAheadToken,")");

        ASTNode parenthesisCloseNode =
                new ASTNode(
                        this.lookAheadToken.getRecognizedString(),
                        ASTNode.NodeType.GROUP_SYMBOL,
                        this.lookAheadToken.getLine(),
                        this.lookAheadToken.getColumn());

        this.consumeToken();
        return parenthesisCloseNode;
    }

    private ASTNode comma(){
        if(this.lookAheadTokenString_NOT_EqualTo(","))
            ParserErrors.wrongTokenString(this.lookAheadToken,",");

        ASTNode commaNode = new ASTNode(
                this.lookAheadToken.getRecognizedString(),
                ASTNode.NodeType.DELIMITER,
                this.lookAheadToken.getLine(),
                this.lookAheadToken.getColumn());


        this.consumeToken();
        return commaNode;
    }

    private ASTNode colon() {
        if(this.lookAheadTokenString_NOT_EqualTo(":"))
            ParserErrors.assigmentOperatorExpected(
                    this.lookAheadToken.getRecognizedString(),
                    this.lookAheadToken.getLine(),
                    this.lookAheadToken.getColumn());

        ASTNode colonNode = new ASTNode(
                this.lookAheadToken.getRecognizedString(),
                ASTNode.NodeType.DELIMITER,
                this.lookAheadToken.getLine(),
                this.lookAheadToken.getColumn());

        this.consumeToken();
        return colonNode;
    }

    private ASTNode equal(){
        if(this.lookAheadTokenString_NOT_EqualTo("="))
            ParserErrors.wrongTokenString(this.lookAheadToken,"=");

        ASTNode equalNode = new ASTNode(
                this.lookAheadToken.getRecognizedString(),
                ASTNode.NodeType.REL_OPERATOR,
                this.lookAheadToken.getLine(),
                this.lookAheadToken.getColumn());


        this.consumeToken();
        return equalNode;
    }

    private ASTNode referenceOperator(){
        if(this.lookAheadTokenFamily_NOT_EqualsTo(Token.TokenFamily.REFERENCE_OPERATOR))
            ParserErrors.wrongTokenFamily(this.lookAheadToken, Token.TokenFamily.REFERENCE_OPERATOR);

        ASTNode referenceOperatorNode = new ASTNode(
                        this.lookAheadToken.getRecognizedString(),
                        ASTNode.NodeType.REFERENCE_OPERATOR,
                        this.lookAheadToken.getLine(),
                        this.lookAheadToken.getColumn());

        this.consumeToken();
        return referenceOperatorNode;
    }

    private ASTNode semicolon(){
        if(this.lookAheadTokenString_NOT_EqualTo(";"))
            ParserErrors.wrongTokenString(this.lookAheadToken,";");

        ASTNode semicolonNode = new ASTNode(
                this.lookAheadToken.getRecognizedString(),
                ASTNode.NodeType.DELIMITER,
                this.lookAheadToken.getLine(),
                this.lookAheadToken.getColumn());

        this.consumeToken();
        return semicolonNode;
    }

    private ASTNode keyword(String keywordString){
        if(this.lookAheadTokenFamily_NOT_EqualsTo(Token.TokenFamily.KEYWORD)
                || this.lookAheadTokenString_NOT_EqualTo(keywordString))
            ParserErrors.wrongTokenFamilyDetailed(this.lookAheadToken, keywordString, Token.TokenFamily.KEYWORD);

        ASTNode keywordNode = new ASTNode(
                this.lookAheadToken.getRecognizedString(),
                ASTNode.NodeType.KEYWORD,
                this.lookAheadToken.getLine(),
                this.lookAheadToken.getColumn());

        this.consumeToken();
        return keywordNode;
    }

    private ASTNode ID(ASTNode.NodeType nodeType){
        if(this.lookAheadTokenFamily_NOT_EqualsTo(Token.TokenFamily.IDENTIFIER))
            ParserErrors.wrongTokenFamily(this.lookAheadToken, Token.TokenFamily.IDENTIFIER);

        if(this.lookAheadToken.getRecognizedStringLength() > 30)
            ParserErrors.identifierMaxCharLimitReached(this.lookAheadToken);

        ASTNode ID = new ASTNode(
                this.lookAheadToken.getRecognizedString(),
                nodeType,
                this.lookAheadToken.getLine(),
                this.lookAheadToken.getColumn());

        this.scopeManager.registerSymbol(SYMBOL_TABLE_ENABLED,ID);
        this.scopeManager.bindNodeWithCurrentScope(SYMBOL_TABLE_ENABLED,ID.getId());
        this.consumeToken();
        return ID;
    }

    private ASTNode INTEGER(){
        int value = 0;
        try{
            value = Integer.parseInt(lookAheadToken.getRecognizedString());
        }catch (NumberFormatException e){
            ParserErrors.integerNumberTooLarge(
                    this.lookAheadToken.getRecognizedString(),
                    this.lookAheadToken.getLine(),
                    this.lookAheadToken.getColumn());
        }

        if(value < - 32767 || value > 32767)
            ParserErrors.integerNumberTooLarge(
                    this.lookAheadToken.getRecognizedString(),
                    this.lookAheadToken.getLine(),
                    this.lookAheadToken.getColumn());

        ASTNode INTEGERNode = new ASTNode(
                String.valueOf(value),
                ASTNode.NodeType.NUMBER,
                this.lookAheadToken.getLine(),
                this.lookAheadToken.getColumn());

        this.consumeToken();
        return INTEGERNode;
    }
}
