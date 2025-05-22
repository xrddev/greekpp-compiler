package stages.backend.symbol;

import java.util.*;

public class ActivationRecord {
    private final Map<String,TemporaryVariable> temporaryLocalVariables;
    private final Map<String,LocalVariable> localLocalVariables;
    private final Map<String,Parameter> formalParameters;
    private int startingQuadAddress;
    private int recordLength;


    public ActivationRecord() {
        this.temporaryLocalVariables = new HashMap<>();
        this.formalParameters = new HashMap<>();
        this.localLocalVariables = new HashMap<>();
        this.recordLength = 12;
    }

    public void addAllTemporaryVariables(Collection<TemporaryVariable> temporaryVariables) {
      temporaryVariables.forEach(this::addTemporaryVariable);
    }

    private void addTemporaryVariable(TemporaryVariable temporaryVariable) {
        this.temporaryLocalVariables.put(temporaryVariable.getName(), temporaryVariable);
        temporaryVariable.setOffset(this.recordLength);
        this.recordLength += temporaryVariable.getDataType().getByteSize();

    }

    public void addFormalParameter(Parameter parameter) {
        this.formalParameters.put(parameter.getName(), parameter);
        parameter.setOffset(this.recordLength);
        this.recordLength += parameter.getDataType().getByteSize();

    }

    public void addLocalVariable(LocalVariable localVariable) {
        this.localLocalVariables.put(localVariable.getName(), localVariable);
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
            int i = 0;
            for (TemporaryVariable t : temporaryLocalVariables.values()) {
                sb.append(t.getName())
                        .append(":")
                        .append(t.getDataType())
                        .append("@")
                        .append(t.getOffset());
                if (++i < temporaryLocalVariables.size()) sb.append(", ");
            }
            sb.append("\n");
        }

        sb.append("  Local Variables:     ");
        if (localLocalVariables.isEmpty()) {
            sb.append("none\n");
        } else {
            int i = 0;
            for (LocalVariable v : localLocalVariables.values()) {
                sb.append(v.getName())
                        .append(":")
                        .append(v.getDataType())
                        .append("@")
                        .append(v.getOffset());
                if (++i < localLocalVariables.size()) sb.append(", ");
            }
            sb.append("\n");
        }

        sb.append("  Formal Parameters:   ");
        if (formalParameters.isEmpty()) {
            sb.append("none\n");
        } else {
            int i = 0;
            for (Parameter p : formalParameters.values()) {
                sb.append(p.getName())
                        .append(":")
                        .append(p.getDataType())
                        .append("@")
                        .append(p.getOffset());
                if (++i < formalParameters.size()) sb.append(", ");
            }
            sb.append("\n");
        }

        sb.append("  StartingQuadAddress: ").append(startingQuadAddress).append("\n");
        sb.append("  Record Size:         ").append(recordLength).append(" bytes");

        return sb.toString();
    }


    public int getRecordLength(){
        return this.recordLength;
    }

    public int getStartingQuadAddress() {
        return startingQuadAddress;
    }
}