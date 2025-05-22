package stages.backend.symbol;

public class Procedure extends Entity {
    final ActivationRecord activationRecord;
    int scopeDepth;

    public Procedure(String name, int scopeDepth) {
        super(name);
        this.activationRecord = new ActivationRecord();
        this.scopeDepth = scopeDepth;
    }

    public ActivationRecord getActivationRecord() {
        return activationRecord;
    }

    public int getScopeDepth() {
        return this.scopeDepth;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("── Procedure: ").append(name).append(" ──\n");
        sb.append("   Scope Depth: ").append(scopeDepth).append("\n");
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
