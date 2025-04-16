package stages.visitor.semantic.symbol;

import java.util.ArrayList;
import java.util.List;

public class Procedure extends Entity {
    int startingQuad;
    int frameLength;
    List<Parameter> formalParameters;

    public Procedure(String name) {
        super(name);
        this.formalParameters = new ArrayList<>();
    }

    public int getStartingQuad() {
        return startingQuad;
    }

    public void setStartingQuad(int startingQuad) {
        this.startingQuad = startingQuad;
    }

    public int getFrameLength() {
        return frameLength;
    }

    public void setFrameLength(int frameLength) {
        this.frameLength = frameLength;
    }

    public List<Parameter> getFormalParameters() {
        return formalParameters;
    }

    public void setFormalParameters(List<Parameter> formalParameters) {
        this.formalParameters = formalParameters;
    }

    @Override
    public String toString() {
        return "Procedure{" +
                "name='" + name + '\'' +
                ", formalParameters=" + formalParameters +
                ", startingQuad=" + startingQuad +
                ", frameLength=" + frameLength +
                '}';
    }
}
