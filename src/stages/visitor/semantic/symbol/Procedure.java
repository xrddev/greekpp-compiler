package stages.visitor.semantic.symbol;

import java.util.ArrayList;
import java.util.List;

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
        return "Procedure{" +
                "name='" + name + '\'' +
                ", activationRecord=" + activationRecord +
                '}';
    }
}
