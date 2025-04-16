package stages.astree.parser;

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
        PROGRAM_END_KEYWORD,
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
        NUMBER, PROGRAM_NAME,
        VARIABLE_USAGE,
        PARAMETER_INOUT_DECLARATION,
        FUNCTION_CALL_IN_ASSIGMENT, SUBROUTINE_USAGE, PARAMETER_USAGE;

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
    private static long idCounter = 0;
    private final long id;

    private List<Integer> trueList;
    private List<Integer> falseList;

    private String place;
    private NodeType nodeType;
    private final List<ASTNode> children;
    private final int line;
    private final int column;

    public ASTNode(NodeType nodeType){
        this.place = null;
        this.line = this.column = 0;
        this.nodeType = nodeType;
        this.children = new ArrayList<>();
        this.id = idCounter++;
    }

    public ASTNode(NodeType nodeType, String otherNodeMessageForMe){
        this.place = otherNodeMessageForMe;
        this.line = this.column = 0;
        this.nodeType = nodeType;
        this.children = new ArrayList<>();
        this.id = idCounter++;
    }

    public ASTNode(String place, NodeType nodeType, int line , int column){
        this.place = place;
        this.nodeType = nodeType;
        this.line = line;
        this.column = column;
        this.children = new ArrayList<>();
        this.id = idCounter++;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public String getPlace() {
        return place;
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

    public long getId() {
        return this.id;
    }

    public void printNodeInfo(){
        System.out.println("ASTNode{" +
                "attribute='" + place + '\'' +
                ", nodeType=" + nodeType +
                ", line=" + line +
                ", column=" + column +
                ", id=" + id +
                '}');
    }

    public void setNodeType(NodeType nodeType){
        this.nodeType = nodeType;
    }

    public void setPlace(String place){
        this.place = place;
    }

    public List<Integer> getTrueList() {
        return trueList;
    }

    public void setTrueList(List<Integer> trueList) {
        this.trueList = trueList;
    }

    public List<Integer> getFalseList() {
        return falseList;
    }

    public void setFalseList(List<Integer> falseList) {
        this.falseList = falseList;
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

        if (this.getNodeType().isToken() || this.getNodeType().is(NodeType.PARAMETER_USAGE) || this.getNodeType().is(NodeType.SUBROUTINE_USAGE)|| this.getNodeType().is(NodeType.VARIABLE_USAGE) || this.getNodeType().is(NodeType.FUNCTION_CALL_IN_ASSIGMENT)) {
            sb.append("(").append(this.id).append(")"). append(" ").append("[TOKEN: ").append(this.getNodeType()).append(" : <").append(place).append(">]\n");
        } else {
            sb.append("(").append(this.id).append(")"). append(" ").append("[NODE: ").append(nodeType).append("]\n");
        }

        for (int i = 0; i < children.size(); i++) {
            boolean isLast = (i == children.size() - 1);
            sb.append(children.get(i).toString(indent, isLast));
        }
        return sb.toString();
    }
}
