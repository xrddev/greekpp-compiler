package stages.semantic.intermediate.instructions;

public class RegionMark extends IRInstruction {

    private final String regionOwner;

    public RegionMark(String scope, InstructionType operation, String regionOwner) {
        super(scope, operation);
        this.regionOwner = regionOwner;
    }

    @Override
    public String toString() {
        return "RegionMark{" +
                "scope='" + scope + '\'' +
                ", operation=" + instructionType +
                ", regionOwner='" + regionOwner + '\'' +
                '}';
    }
}
