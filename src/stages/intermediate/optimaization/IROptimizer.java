package stages.intermediate.optimaization;

import stages.intermediate.generation.Quad;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class IROptimizer {
    private final Map<Integer, Quad> quadMap;

    public IROptimizer(List<Quad> quadList){
        quadMap = new TreeMap<>();
        for(int i = 0 ; i < quadList.size() ; i++){
            quadMap.put(i, quadList.get(i));
        }
        this.optimize();
    }

    private void optimize(){
        this.removeThreadingJumps();
        this.removeTrivialJumps();
        this.mergeConsecutiveJumps();
        this.removeRedundantAssignments();
        this.tempBeforeOrAfterAssignRemoval();
        this.optimizeConstantExpressions();
    }

    public void removeThreadingJumps() {
        for (Map.Entry<Integer, Quad> entry : quadMap.entrySet()) {
            Quad quad = entry.getValue();

            if ("jump".equals(quad.getOperator())) {
                int target = Integer.parseInt(quad.getResult());

                while (quadMap.containsKey(target)) {
                    Quad targetQuad = quadMap.get(target);

                    if ("jump".equals(targetQuad.getOperator())) {
                        target = Integer.parseInt(targetQuad.getResult());
                    } else {
                        break;
                    }
                }

                quad.setResult(String.valueOf(target));
            }
        }
    }

    public void removeTrivialJumps() {
        for (Map.Entry<Integer, Quad> entry : quadMap.entrySet()) {
            int currentLabel = entry.getKey();
            Quad currentQuad = entry.getValue();

            if ("jump".equals(currentQuad.getOperator())) {
                int jumpTarget = Integer.parseInt(currentQuad.getResult());

                if (jumpTarget == currentLabel + 1) {
                    quadMap.remove(currentLabel);
                }
            }
        }
    }

    public void mergeConsecutiveJumps() {
        for(int i = 0; i < this.quadMap.size() - 2; i++){
            Quad quad1 = this.quadMap.get(i);
            Quad quad2 = this.quadMap.get(i + 1);

            if("jump".equals(quad1.getOperator()) && "jump".equals(quad2.getOperator()) && quad1.getResult().equals(quad2.getResult())){
                this.quadMap.remove(i);
            }
        }
    }

    public void tempBeforeOrAfterAssignRemoval() {
        List<Integer> toBeRemoved = new ArrayList<>();
        List<Integer> keys = new ArrayList<>(this.quadMap.keySet());

        for (int i = 0; i < keys.size() - 1; i++) {
            Quad quad1 = this.quadMap.get(keys.get(i));
            Quad quad2 = this.quadMap.get(keys.get(i + 1));

            if(quad1.operatorIsAddOperator() && quad2.operatorIsEqualSign() && quad1.getResult().equals(quad2.getOperand1())){
                quad1.setResult(quad2.getResult());
                toBeRemoved.add(keys.get(i + 1));
            }

            if(quad1.operatorIsEqualSign() && quad2.operatorIsAddOperator() && quad2.getOperand1().equals(quad1.getResult())){
                quad1.setResult(quad2.getOperand1());
                toBeRemoved.add(keys.get(i + 1));
            }


        }

        toBeRemoved.forEach(this.quadMap::remove);
    }

    public void removeRedundantAssignments() {
        quadMap.entrySet().removeIf(entry -> {
            Quad quad = entry.getValue();
            return ":=".equals(quad.getOperator()) &&
                    quad.getOperand1().equals(quad.getResult());
        });
    }

    public void optimizeConstantExpressions() {
        quadMap.replaceAll((key, quad) -> {
            if ("+".equals(quad.getOperator()) || "*".equals(quad.getOperator())) {
                try {
                    int operand1 = Integer.parseInt(quad.getOperand1());
                    int operand2 = Integer.parseInt(quad.getOperand2());
                    int result = switch (quad.getOperator()) {
                        case "+" -> operand1 + operand2;
                        case "*" -> operand1 * operand2;
                        default -> throw new IllegalStateException("Unexpected value: " + quad.getOperator());
                    };
                    return new Quad(":=", String.valueOf(result), null, quad.getResult());
                } catch (NumberFormatException e) {
                    return quad;
                }
            }
            return quad;
        });
    }

    public void printOptimizedQuads(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("greekpp_OPTIMIZED_QUADS.int"))) {
            int maxDigits = String.valueOf(this.quadMap.size() - 1).length();
            String format = "%" + maxDigits + "d : ";

            for(int i = 1; i < this.quadMap.size(); i++){
                writer.write(String.format(format, i) + this.quadMap.get(i));
                writer.newLine();
            }

            System.out.println("Quads have been successfully written to the file greekpp_OPTIMIZED_QUADS.int");
        } catch (IOException e) {
            System.err.println("Error while writing to the file: " + e.getMessage());
        }
    }
}

