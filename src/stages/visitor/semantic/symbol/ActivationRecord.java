package stages.visitor.semantic.symbol;

import java.util.ArrayList;
import java.util.List;

public class ActivationRecord {
    private final List<Variable> temporaryVariables;
    private final List<Variable> localVariables;
    private final List<Parameter> formalParameters;
    private int startingQuadAddress;
    private Parameter returnValue;
    private int recordLength;


    public ActivationRecord() {
        this.temporaryVariables = new ArrayList<>();
        this.formalParameters = new ArrayList<>();
        this.localVariables = new ArrayList<>();
        this.recordLength = 12;
    }

    public void addTemporaryVariable(Variable variable) {
        this.temporaryVariables.add(variable);
        variable.setOffset(this.recordLength);
        this.recordLength += variable.getDataType().getByteSize();

    }

    public void addFormalParameter(Parameter parameter) {
        this.formalParameters.add(parameter);
        parameter.setOffset(this.recordLength);
        this.recordLength += parameter.getDataType().getByteSize();

    }

    public void addLocalVariable(Variable variable) {
        this.localVariables.add(variable);
        variable.setOffset(this.recordLength);
        this.recordLength += variable.getDataType().getByteSize();
    }

    public void setStartingQuadAddress(int startingQuadAddress) {
        this.startingQuadAddress = startingQuadAddress;
    }

    public int countFormalParameters() {
        return this.formalParameters.size();
    }

    public void setReturnValue(Parameter returnValue) {
        this.returnValue = returnValue;
    }

    public Parameter getReturnValue() {
        return this.returnValue;
    }

    @Override
    public String toString() {
        return "ActivationRecord{" +
                "startingQuadAddress=" + startingQuadAddress +
                ", returnValue=" + returnValue +
                ", formalParameters=" + formalParameters +
                ", localVariables=" + localVariables +
                ", temporaryVariables=" + temporaryVariables +
                ", recordLength=" + recordLength +
                '}';
    }
}