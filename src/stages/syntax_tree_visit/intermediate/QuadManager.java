package stages.syntax_tree_visit.intermediate;

import java.util.ArrayList;
import java.util.List;

public class QuadManager {
    private final List<Quad> quads;
    private int tempCounter;

    public QuadManager(){
        this.quads = new ArrayList<>();
        this.tempCounter = 0;
    }


    public void generateQuad(String operator, String operand1, String operand2, String result){
        this.quads.add(new Quad(operator, operand1, operand2, result));
    }

    public int nextQuad(){
        return this.quads.size() + 1;
    }

    public String newTemp(){
        return "$temp_" + this.tempCounter++;
    }

    public List<Integer> emptyList(){
        return new ArrayList<>();
    }

    public List<Integer> makeList(int label){
        return new ArrayList<>(List.of(label));
    }

    public List<Integer> mergeLists(List<Integer> list1, List<Integer> list2){
        list1.addAll(list2);
        return list1;
    }

    public List<Quad> getQuads() {
        return quads;
    }
}
