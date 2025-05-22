package stages.backend.final_code;

import stages.backend.symbol.LocalVariable;

public record VariableOperand(LocalVariable value) implements Operand {}
