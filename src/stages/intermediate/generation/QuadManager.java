package stages.intermediate.generation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class QuadManager {
    private final List<Quad> quads;
    private int tempCounter;
    private final Map<Integer, List<Quad>> delayedQuads;
    private int delayedQuadsBucketCount;

    public QuadManager(){
        this.quads = new ArrayList<>();
        this.quads.add( //Placeholder, so quads labeling starts from 1 as the given examples are. I prefer starting from 0 tho ;)
                new Quad("$","$","$","0"));
        this.tempCounter = 1;
        this.delayedQuads = new TreeMap<>();
        this.delayedQuadsBucketCount = 0;
    }

    public void openDelayedQuadsLevel(){
        this.delayedQuadsBucketCount++;
    }
    public void closeDelayedQuadsLevel(){
        this.delayedQuadsBucketCount--;
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

    public int getDelayedQuadsBucketCount() {
        return delayedQuadsBucketCount;
    }

    public void backPatch(List<Integer> trueFalseList, int targetLabel){
        trueFalseList.forEach(label -> this.quads.get(label).setResult(String.valueOf(targetLabel)));
    }

    public void printQuads(String programName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(programName + ".int"))) {
            int maxDigits = String.valueOf(this.quads.size() - 1).length();
            String format = "%" + maxDigits + "d : ";

            for (int i = 1; i < this.quads.size(); i++) {
                writer.write(String.format(format, i) + this.quads.get(i));
                writer.newLine();
            }
            System.out.println("Quads have been successfully written to the file " + programName + ".int");
        } catch (IOException e) {
            System.err.println("Error while writing to the file: " + e.getMessage());
        }
    }

    public void generateDelayedQuad(String operator, String operand1, String operand2, String result) {
        Quad quad = new Quad(operator, operand1, operand2, result);
        this.delayedQuads.computeIfAbsent(this.delayedQuadsBucketCount, k -> new ArrayList<>()).add(quad);
    }

    public void flashDelayedQuads(){
        this.quads.addAll(this.delayedQuads.get(this.delayedQuadsBucketCount));
        this.delayedQuads.remove(this.delayedQuadsBucketCount);
    }

    public List<Quad> getQuads(){
        return this.quads;
    }
}
