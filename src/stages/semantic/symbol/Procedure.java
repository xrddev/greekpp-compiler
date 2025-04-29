package stages.semantic.symbol;

public class Procedure extends Entity {
    final ActivationRecord activationRecord;

    public Procedure(String name) {
        super(name);
        this.activationRecord = new ActivationRecord();
    }

    public ActivationRecord getActivationRecord() {
        return activationRecord;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("── Procedure: ").append(name).append(" ──\n");
        sb.append(indent(activationRecord.toString(), "    "));
        return sb.toString();
    }


    private String indent(String text, String prefix) {
        StringBuilder result = new StringBuilder();
        for (String line : text.split("\n")) {
            result.append(prefix).append(line).append("\n");
        }
        return result.toString();
    }
}
