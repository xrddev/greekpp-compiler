package common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ASTNode {
    public enum NodeType {
        ROOT,
        PROGRAM_BLOCK,
        SEQUENCE,
        EXPRESSION,
        FUNCTION,
        PROCEDURE,
        IF_STATEMENT,
        WHILE_STATEMENT,
        VAR_LIST,
        SUBPROGRAMS,
        FORMAL_PARAMETERS_LIST,
        FUNCTION_BLOCK,
        PROCEDURE_BLOCK,
        FUNCTION_INPUT,
        FUNCTION_OUTPUT,
        STATEMENT,
        ASSIGMENT_STATEMENT,
        ELSE_STATEMENT,
        DO_STATEMENT,
        FOR_STATEMENT,
        STEP,
        PRINT_STATEMENT,
        INPUT_STATEMENT,
        CALL_STATEMENT,
        ID_TAIL,
        ACTUAL_PARAMETERS,
        ACTUAL_PARAMETER_LIST,
        ACTUAL_PARAMETER_ITEM,
        CONDITION,
        BOOL_FACTOR,
        BOOL_TERM,
        TERM,
        FACTOR,
        OPTIONAL_SIGN,
        DECLARATIONS,
        KEYWORD,
        ADD_OPERATOR,
        MUL_OPERATOR,
        REL_OPERATOR,
        REFERENCE_OPERATOR,
        DELIMITER,
        GROUP_SYMBOL,
        VARIABLE_IDENTIFIER,
        PARAMETER_IDENTIFIER,
        FUNCTION_IDENTIFIER,
        PROCEDURE_IDENTIFIER,
        NUMBER, PROGRAM_NAME, ASSIGMENT_IDENTIFIER;

        public boolean isToken(){
            return switch (this){
                case KEYWORD, ADD_OPERATOR, MUL_OPERATOR, REL_OPERATOR, REFERENCE_OPERATOR,
                     DELIMITER, GROUP_SYMBOL,VARIABLE_IDENTIFIER, PARAMETER_IDENTIFIER, FUNCTION_IDENTIFIER,
                     PROCEDURE_IDENTIFIER, NUMBER , PROGRAM_NAME-> true;
                default -> false;
            };
        }

        public boolean is(NodeType nodeType){
            return this == nodeType;
        }
    }
    private final String value;
    private final NodeType nodeType;
    private final List<ASTNode> children;
    private final int line;
    private final int column;

    public ASTNode(NodeType nodeType){
        this.value = null;
        this.line = this.column = 0;
        this.nodeType = nodeType;
        this.children = new ArrayList<>();
    }

    public ASTNode(String value, NodeType nodeType, int line , int column){
        this.value = value;
        this.nodeType = nodeType;
        this.line = line;
        this.column = column;
        this.children = new ArrayList<>();
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public String getValue() {
        return value;
    }

    public List<ASTNode> getChildren(){
        return Collections.unmodifiableList(this.children);
    }

    public void addChild(ASTNode child){
        this.children.add(child);
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }

    public void printNodeInfo(){
        System.out.println("ASTNode{" +
                "value='" + value + '\'' +
                ", nodeType=" + nodeType +
                ", line=" + line +
                ", column=" + column +
                '}');
    }

    @Override
    public String toString() {
        return toString("", true);
    }

    private String toString(String indent, boolean last) {
        StringBuilder sb = new StringBuilder();

        sb.append(indent);
        if (last) {
            sb.append("└─ ");
            indent += "   ";
        } else {
            sb.append("├─ ");
            indent += "│  ";
        }

        if (this.getNodeType().isToken()) {
            sb.append("[TOKEN: ").append(this.getNodeType()).append(" : <").append(value).append(">]\n");
        } else {
            sb.append("[NODE: ").append(nodeType).append("]\n");
        }

        for (int i = 0; i < children.size(); i++) {
            boolean isLast = (i == children.size() - 1);
            sb.append(children.get(i).toString(indent, isLast));
        }
        return sb.toString();
    }
}
