package stages.intermediate;

import stages.intermediate.semantic.symbol.LocalVariable;

public record VariableOperand(LocalVariable value) implements Operand {}
