package stages.semantic.symbol;

import java.util.ArrayList;
import java.util.List;

public class ActivationRecord {
    private final List<TemporaryVariable> temporaryLocalVariables;
    private final List<LocalVariable> localLocalVariables;
    private final List<Parameter> formalParameters;
    private int startingQuadAddress;
    private Parameter returnValue;
    private int recordLength;


    public ActivationRecord() {
        this.temporaryLocalVariables = new ArrayList<>();
        this.formalParameters = new ArrayList<>();
        this.localLocalVariables = new ArrayList<>();
        this.recordLength = 12;
    }

    public void addTemporaryVariable(TemporaryVariable temporaryVariable) {
        this.temporaryLocalVariables.add(temporaryVariable);
        temporaryVariable.setOffset(this.recordLength);
        this.recordLength += temporaryVariable.getDataType().getByteSize();

    }

    public void addFormalParameter(Parameter parameter) {
        this.formalParameters.add(parameter);
        parameter.setOffset(this.recordLength);
        this.recordLength += parameter.getDataType().getByteSize();

    }

    public void addLocalVariable(LocalVariable localVariable) {
        this.localLocalVariables.add(localVariable);
        localVariable.setOffset(this.recordLength);
        this.recordLength += localVariable.getDataType().getByteSize();
    }

    public void setStartingQuadAddress(int startingQuadAddress) {
        this.startingQuadAddress = startingQuadAddress;
    }

    public int countFormalParameters() {
        return this.formalParameters.size();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ActivationRecord:\n");

        sb.append("  Temporary Variables: ");
        if (temporaryLocalVariables.isEmpty()) {
            sb.append("none\n");
        } else {
            for (int i = 0; i < temporaryLocalVariables.size(); i++) {
                LocalVariable t = temporaryLocalVariables.get(i);
                sb.append(t.getName()).append(":").append(t.getDataType()).append("@").append(t.getOffset());
                if (i < temporaryLocalVariables.size() - 1) sb.append(", ");
            }
            sb.append("\n");
        }

        sb.append("  Local Variables:     ");
        if (localLocalVariables.isEmpty()) {
            sb.append("none\n");
        } else {
            for (int i = 0; i < localLocalVariables.size(); i++) {
                LocalVariable v = localLocalVariables.get(i);
                sb.append(v.getName()).append(":").append(v.getDataType()).append("@").append(v.getOffset());
                if (i < localLocalVariables.size() - 1) sb.append(", ");
            }
            sb.append("\n");
        }

        sb.append("  FormalParameters:    ");
        if (formalParameters.isEmpty()) {
            sb.append("none\n");
        } else {
            for (int i = 0; i < formalParameters.size(); i++) {
                Parameter p = formalParameters.get(i);
                sb.append(p.getName()).append(":").append(p.getDataType()).append("@").append(p.getOffset());
                if (i < formalParameters.size() - 1) sb.append(", ");
            }
            sb.append("\n");
        }

        sb.append("  StartingQuadAddress: ").append(startingQuadAddress).append("\n");

        sb.append("  Return Value:        ");
        if (returnValue != null) {
            sb.append(returnValue.getName()).append(":").append(returnValue.getDataType()).append("@").append(returnValue.getOffset()).append("\n");
        } else {
            sb.append("none\n");
        }

        sb.append("  Record Size:         ").append(recordLength).append(" bytes");

        return sb.toString();
    }

}