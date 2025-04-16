package stages.visitor.intermediate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QuadManager {
    private final List<Quad> quads;
    private int tempCounter;

    public QuadManager(){
        this.quads = new ArrayList<>();
        this.tempCounter = 1;
    }

    public void generateQuad(String operator, String operand1, String operand2, String result){
        this.quads.add(new Quad(operator, operand1, operand2, result));
    }

    public int nextQuad(){
        return this.quads.size();
    }

    public String newTemp(){
        return "$T_" + this.tempCounter++;
    }

    public void backPatch(List<Integer> trueFalseList, int targetLabel){
        trueFalseList.forEach(label -> this.quads.get(label).setResult(String.valueOf(targetLabel)));
    }

    public void printQuads() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("greekpp.int"))) {
            for (int i = 0; i < this.quads.size(); i++) {
                writer.write(i + ": " + this.quads.get(i));
                writer.newLine(); // Move to the next line
            }
            System.out.println("Quads have been successfully written to the file greekpp.int");
        } catch (IOException e) {
            System.err.println("Error while writing to the file: " + e.getMessage());
        }
    }

}
