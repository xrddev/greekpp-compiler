# greek++ Compiler
Full Implementation of a Recursive-Descent, Multi-Phase Compiler Targeting RISC-V

## 🚀 Introduction

This project presents a complete implementation of a multi-phase compiler for the **greek++ programming language**.  
The compiler performs lexical analysis, recursive-descent parsing, semantic analysis, intermediate code generation, and final code emission targeting the **RISC-V architecture**.

The greek++ language is intentionally minimal (integer-based), yet expressive enough to support core programming constructs such as:

- 🧩 Functions and procedures
- 🔀 Conditional statements
- 🔁 Iterative constructs (loops)
- 📥 Input and output operations
- ♻️ Recursive calls
- 🔗 Parameter passing (by value and by reference)
- 🗂️ Nested function and procedure declarations

---

## 🧱 Compiler Architecture

The greek++ compiler follows a structured multi-phase compilation pipeline, transforming high-level source code into executable RISC-V assembly. The process, coordinated by the `GreekPP` main class, consists of the following stages:

1️⃣ **Lexical Analysis**  
The source file is scanned character-by-character and converted into a stream of tokens (identifiers, keywords, literals, operators, etc.), forming the lexical foundation of the program.

2️⃣ **Syntactic Analysis & AST Construction**  
The token stream is parsed according to the greek++ grammar. An Abstract Syntax Tree (AST) is constructed, representing the hierarchical syntactic structure of the program.

3️⃣ **Semantic Analysis & Intermediate Representation**  
The AST is traversed to perform scope management, symbol resolution, and semantic validation. During this phase, the compiler generates an intermediate representation in the form of quadruples (quads), providing a machine-independent model of the program logic.

4️⃣ **Output Generation**
- The intermediate representation is exported to a `.int` file.
- Scope and symbol table information are exported to a `.sym` file.
- The intermediate quadruples are translated into RISC-V assembly instructions and written to a `.s` file, producing executable assembly code ready for simulation.


────────────────────────────

### 🔗 Compilation Flow Diagram

```text
           +-------------------+
           |   Source File     |
           +-------------------+
                    |
                    v
           +-------------------+
           |  Lexical Analysis |
           |      (Lexer)      |
           +-------------------+
                    |
                    v
           +-------------------+
           | Syntactic Analysis|
           |     (Parser)      |
           |    --> AST        |
           +-------------------+
                    |
                    v
           +------------------------------+
           | Semantic Analysis & IR Gen   |
           | (IRGenerator + QuadManager)  |
           | --> Quadruples (Quads)       |
           +------------------------------+
                    |
                    v
           +----------------------------+
           |       Output Files         |
           | - Intermediate Code (.int) |
           | - Symbol Table (.sym)      |
           +----------------------------+
                    |
                    v
           +----------------------------------+
           | Final Code Generation  |
           |      --> RISC-V Assembly Code    |
           +----------------------------------+
```
---
# How to run :

### Requirements:

- Java 21 installed
- `java` and `javac` available in the terminal (added to system PATH)

### Compile :


```bash
javac .\GreekPP.java
java GreekPP <filename>
```
---

# End-to-End Compilation Example:


## 📄 Example Program: `forLoop`

The following greek++ program, named `forLoop`, demonstrates multiple loop variants (default, positive, and negative step), nested function calls, parameter passing, and expression evaluation within loop bounds.


```gpp
πρόγραμμα forLoop
	δήλωση a, b, c, d, i, j, k, p

	διαδικασία for_loop_default_step()
	διαπροσωπεία
	αρχή_διαδικασίας
		για i := 1 έως 10 επανάλαβε
			a := a + a * b
		για_τέλος
	τέλος_διαδικασίας

	διαδικασία for_loop_positive_step()
	διαπροσωπεία
	αρχή_διαδικασίας
		για j := 2 έως 30 με_βήμα 4 επανάλαβε
			a := a + a * b
		για_τέλος
	τέλος_διαδικασίας

	διαδικασία for_lοοp_negative_step()
	διαπροσωπεία
	αρχή_διαδικασίας
		για k := 100 έως 20 με_βήμα -5 επανάλαβε
			a := a + a * b
		για_τέλος
	τέλος_διαδικασίας

	συνάρτηση max(x,y)
	διαπροσωπεία
	είσοδος x , y
	αρχή_συνάρτησης
		εάν x > y τότε
			max := x
		αλλιώς
			max := y
		εάν_τέλος
	τέλος_συνάρτησης

	διαδικασία for_lοοp_with_expressions()
	διαπροσωπεία
	αρχή_διαδικασίας
		για p := (max(max(12/4 ,30/5), max(2 + 1, 4)) / 2) / 7 έως 1 + 2*4 + 1 με_βήμα 10 / 5 επανάλαβε
			γράψε p
		για_τέλος
	τέλος_διαδικασίας

αρχή_προγράμματος
	a := 10;
	εκτέλεσε for_loop_default_step();
	εκτέλεσε for_loop_positive_step();
	εκτέλεσε for_lοοp_negative_step();
	εκτέλεσε for_lοοp_with_expressions()
τέλος_προγράμματος
```
---
## 🌳 Abstract Syntax Tree (AST)

The recursive-descent parser constructs an Abstract Syntax Tree (AST) that represents the syntactic structure of the program according to the greek++ grammar.

Unlike the raw token stream, the AST encodes the hierarchical relationships between expressions, statements, procedures, and control-flow constructs, while omitting syntactic details that are no longer needed (such as delimiters and grouping symbols).

The resulting tree serves as the core intermediate structure of the compiler.

During semantic analysis and intermediate code generation, the AST is traversed using the Visitor pattern, allowing each compilation phase to process the program structure in a modular and extensible manner.


```text
└─ (0) [NODE: ROOT]
   ├─ (1) [TOKEN: KEYWORD : <πρόγραμμα>]
   ├─ (2) [TOKEN: PROGRAM_NAME_IDENTIFIER : <forLoop>]
   └─ (3) [NODE: PROGRAM_BLOCK]
      ├─ (4) [NODE: DECLARATIONS]
      │  ├─ (5) [TOKEN: KEYWORD : <δήλωση>]
      │  └─ (6) [NODE: VAR_LIST]
      │     ├─ (7) [TOKEN: VARIABLE_IDENTIFIER : <a>]
      │     ├─ (8) [TOKEN: DELIMITER : <,>]
      │     ├─ (9) [TOKEN: VARIABLE_IDENTIFIER : <b>]
      │     ├─ (10) [TOKEN: DELIMITER : <,>]
      │     ├─ (11) [TOKEN: VARIABLE_IDENTIFIER : <c>]
      │     ├─ (12) [TOKEN: DELIMITER : <,>]
      │     ├─ (13) [TOKEN: VARIABLE_IDENTIFIER : <d>]
      │     ├─ (14) [TOKEN: DELIMITER : <,>]
      │     ├─ (15) [TOKEN: VARIABLE_IDENTIFIER : <i>]
      │     ├─ (16) [TOKEN: DELIMITER : <,>]
      │     ├─ (17) [TOKEN: VARIABLE_IDENTIFIER : <j>]
      │     ├─ (18) [TOKEN: DELIMITER : <,>]
      │     ├─ (19) [TOKEN: VARIABLE_IDENTIFIER : <k>]
      │     ├─ (20) [TOKEN: DELIMITER : <,>]
      │     └─ (21) [TOKEN: VARIABLE_IDENTIFIER : <p>]
      ├─ (22) [NODE: SUBPROGRAMS]
      │  ├─ (23) [NODE: PROCEDURE]
      │  │  ├─ (24) [TOKEN: KEYWORD : <διαδικασία>]
      │  │  ├─ (25) [TOKEN: PROCEDURE_IDENTIFIER : <for_loop_default_step>]
      │  │  ├─ (26) [TOKEN: GROUP_SYMBOL : <(>]
      │  │  ├─ (27) [NODE: FORMAL_PARAMETERS_LIST]
      │  │  ├─ (28) [TOKEN: GROUP_SYMBOL : <)>]
      │  │  └─ (29) [NODE: PROCEDURE_BLOCK]
      │  │     ├─ (30) [TOKEN: KEYWORD : <διαπροσωπεία>]
      │  │     ├─ (31) [NODE: FUNCTION_INPUT]
      │  │     ├─ (32) [NODE: FUNCTION_OUTPUT]
      │  │     ├─ (33) [NODE: DECLARATIONS]
      │  │     ├─ (34) [NODE: SUBPROGRAMS]
      │  │     ├─ (35) [TOKEN: KEYWORD : <αρχή_διαδικασίας>]
      │  │     ├─ (36) [NODE: SEQUENCE]
      │  │     │  └─ (37) [NODE: STATEMENT]
      │  │     │     └─ (38) [NODE: FOR_STATEMENT]
      │  │     │        ├─ (39) [TOKEN: KEYWORD : <για>]
      │  │     │        ├─ (40) [TOKEN: VARIABLE_IDENTIFIER : <i>]
      │  │     │        ├─ (41) [TOKEN: DELIMITER : <:>]
      │  │     │        ├─ (42) [TOKEN: REL_OPERATOR : <=>]
      │  │     │        ├─ (43) [NODE: EXPRESSION]
      │  │     │        │  ├─ (44) [NODE: OPTIONAL_SIGN]
      │  │     │        │  └─ (45) [NODE: TERM]
      │  │     │        │     └─ (46) [NODE: FACTOR]
      │  │     │        │        └─ (47) [TOKEN: NUMBER : <1>]
      │  │     │        ├─ (48) [TOKEN: KEYWORD : <έως>]
      │  │     │        ├─ (49) [NODE: EXPRESSION]
      │  │     │        │  ├─ (50) [NODE: OPTIONAL_SIGN]
      │  │     │        │  └─ (51) [NODE: TERM]
      │  │     │        │     └─ (52) [NODE: FACTOR]
      │  │     │        │        └─ (53) [TOKEN: NUMBER : <10>]
      │  │     │        ├─ (54) [NODE: STEP]
      │  │     │        ├─ (55) [TOKEN: KEYWORD : <επανάλαβε>]
      │  │     │        ├─ (56) [NODE: SEQUENCE]
      │  │     │        │  └─ (57) [NODE: STATEMENT]
      │  │     │        │     └─ (58) [NODE: ASSIGMENT_STATEMENT]
      │  │     │        │        ├─ (59) [TOKEN: VARIABLE_USAGE : <a>]
      │  │     │        │        ├─ (60) [TOKEN: DELIMITER : <:>]
      │  │     │        │        ├─ (61) [TOKEN: REL_OPERATOR : <=>]
      │  │     │        │        └─ (62) [NODE: EXPRESSION]
      │  │     │        │           ├─ (63) [NODE: OPTIONAL_SIGN]
      │  │     │        │           ├─ (64) [NODE: TERM]
      │  │     │        │           │  └─ (65) [NODE: FACTOR]
      │  │     │        │           │     ├─ (66) [TOKEN: VARIABLE_USAGE : <a>]
      │  │     │        │           │     └─ (67) [NODE: ID_TAIL]
      │  │     │        │           ├─ (68) [TOKEN: ADD_OPERATOR : <+>]
      │  │     │        │           └─ (69) [NODE: TERM]
      │  │     │        │              ├─ (70) [NODE: FACTOR]
      │  │     │        │              │  ├─ (71) [TOKEN: VARIABLE_USAGE : <a>]
      │  │     │        │              │  └─ (72) [NODE: ID_TAIL]
      │  │     │        │              ├─ (73) [TOKEN: MUL_OPERATOR : <*>]
      │  │     │        │              └─ (74) [NODE: FACTOR]
      │  │     │        │                 ├─ (75) [TOKEN: VARIABLE_USAGE : <b>]
      │  │     │        │                 └─ (76) [NODE: ID_TAIL]
      │  │     │        └─ (77) [TOKEN: KEYWORD : <για_τέλος>]
      │  │     └─ (78) [TOKEN: KEYWORD : <τέλος_διαδικασίας>]
      │  ├─ (79) [NODE: PROCEDURE]
      │  │  ├─ (80) [TOKEN: KEYWORD : <διαδικασία>]
      │  │  ├─ (81) [TOKEN: PROCEDURE_IDENTIFIER : <for_loop_positive_step>]
      │  │  ├─ (82) [TOKEN: GROUP_SYMBOL : <(>]
      │  │  ├─ (83) [NODE: FORMAL_PARAMETERS_LIST]
      │  │  ├─ (84) [TOKEN: GROUP_SYMBOL : <)>]
      │  │  └─ (85) [NODE: PROCEDURE_BLOCK]
      │  │     ├─ (86) [TOKEN: KEYWORD : <διαπροσωπεία>]
      │  │     ├─ (87) [NODE: FUNCTION_INPUT]
      │  │     ├─ (88) [NODE: FUNCTION_OUTPUT]
      │  │     ├─ (89) [NODE: DECLARATIONS]
      │  │     ├─ (90) [NODE: SUBPROGRAMS]
      │  │     ├─ (91) [TOKEN: KEYWORD : <αρχή_διαδικασίας>]
      │  │     ├─ (92) [NODE: SEQUENCE]
      │  │     │  └─ (93) [NODE: STATEMENT]
      │  │     │     └─ (94) [NODE: FOR_STATEMENT]
      │  │     │        ├─ (95) [TOKEN: KEYWORD : <για>]
      │  │     │        ├─ (96) [TOKEN: VARIABLE_IDENTIFIER : <j>]
      │  │     │        ├─ (97) [TOKEN: DELIMITER : <:>]
      │  │     │        ├─ (98) [TOKEN: REL_OPERATOR : <=>]
      │  │     │        ├─ (99) [NODE: EXPRESSION]
      │  │     │        │  ├─ (100) [NODE: OPTIONAL_SIGN]
      │  │     │        │  └─ (101) [NODE: TERM]
      │  │     │        │     └─ (102) [NODE: FACTOR]
      │  │     │        │        └─ (103) [TOKEN: NUMBER : <2>]
      │  │     │        ├─ (104) [TOKEN: KEYWORD : <έως>]
      │  │     │        ├─ (105) [NODE: EXPRESSION]
      │  │     │        │  ├─ (106) [NODE: OPTIONAL_SIGN]
      │  │     │        │  └─ (107) [NODE: TERM]
      │  │     │        │     └─ (108) [NODE: FACTOR]
      │  │     │        │        └─ (109) [TOKEN: NUMBER : <30>]
      │  │     │        ├─ (110) [NODE: STEP]
      │  │     │        │  ├─ (111) [TOKEN: KEYWORD : <με_βήμα>]
      │  │     │        │  └─ (112) [NODE: EXPRESSION]
      │  │     │        │     ├─ (113) [NODE: OPTIONAL_SIGN]
      │  │     │        │     └─ (114) [NODE: TERM]
      │  │     │        │        └─ (115) [NODE: FACTOR]
      │  │     │        │           └─ (116) [TOKEN: NUMBER : <4>]
      │  │     │        ├─ (117) [TOKEN: KEYWORD : <επανάλαβε>]
      │  │     │        ├─ (118) [NODE: SEQUENCE]
      │  │     │        │  └─ (119) [NODE: STATEMENT]
      │  │     │        │     └─ (120) [NODE: ASSIGMENT_STATEMENT]
      │  │     │        │        ├─ (121) [TOKEN: VARIABLE_USAGE : <a>]
      │  │     │        │        ├─ (122) [TOKEN: DELIMITER : <:>]
      │  │     │        │        ├─ (123) [TOKEN: REL_OPERATOR : <=>]
      │  │     │        │        └─ (124) [NODE: EXPRESSION]
      │  │     │        │           ├─ (125) [NODE: OPTIONAL_SIGN]
      │  │     │        │           ├─ (126) [NODE: TERM]
      │  │     │        │           │  └─ (127) [NODE: FACTOR]
      │  │     │        │           │     ├─ (128) [TOKEN: VARIABLE_USAGE : <a>]
      │  │     │        │           │     └─ (129) [NODE: ID_TAIL]
      │  │     │        │           ├─ (130) [TOKEN: ADD_OPERATOR : <+>]
      │  │     │        │           └─ (131) [NODE: TERM]
      │  │     │        │              ├─ (132) [NODE: FACTOR]
      │  │     │        │              │  ├─ (133) [TOKEN: VARIABLE_USAGE : <a>]
      │  │     │        │              │  └─ (134) [NODE: ID_TAIL]
      │  │     │        │              ├─ (135) [TOKEN: MUL_OPERATOR : <*>]
      │  │     │        │              └─ (136) [NODE: FACTOR]
      │  │     │        │                 ├─ (137) [TOKEN: VARIABLE_USAGE : <b>]
      │  │     │        │                 └─ (138) [NODE: ID_TAIL]
      │  │     │        └─ (139) [TOKEN: KEYWORD : <για_τέλος>]
      │  │     └─ (140) [TOKEN: KEYWORD : <τέλος_διαδικασίας>]
      │  ├─ (141) [NODE: PROCEDURE]
      │  │  ├─ (142) [TOKEN: KEYWORD : <διαδικασία>]
      │  │  ├─ (143) [TOKEN: PROCEDURE_IDENTIFIER : <for_lοοp_negative_step>]
      │  │  ├─ (144) [TOKEN: GROUP_SYMBOL : <(>]
      │  │  ├─ (145) [NODE: FORMAL_PARAMETERS_LIST]
      │  │  ├─ (146) [TOKEN: GROUP_SYMBOL : <)>]
      │  │  └─ (147) [NODE: PROCEDURE_BLOCK]
      │  │     ├─ (148) [TOKEN: KEYWORD : <διαπροσωπεία>]
      │  │     ├─ (149) [NODE: FUNCTION_INPUT]
      │  │     ├─ (150) [NODE: FUNCTION_OUTPUT]
      │  │     ├─ (151) [NODE: DECLARATIONS]
      │  │     ├─ (152) [NODE: SUBPROGRAMS]
      │  │     ├─ (153) [TOKEN: KEYWORD : <αρχή_διαδικασίας>]
      │  │     ├─ (154) [NODE: SEQUENCE]
      │  │     │  └─ (155) [NODE: STATEMENT]
      │  │     │     └─ (156) [NODE: FOR_STATEMENT]
      │  │     │        ├─ (157) [TOKEN: KEYWORD : <για>]
      │  │     │        ├─ (158) [TOKEN: VARIABLE_IDENTIFIER : <k>]
      │  │     │        ├─ (159) [TOKEN: DELIMITER : <:>]
      │  │     │        ├─ (160) [TOKEN: REL_OPERATOR : <=>]
      │  │     │        ├─ (161) [NODE: EXPRESSION]
      │  │     │        │  ├─ (162) [NODE: OPTIONAL_SIGN]
      │  │     │        │  └─ (163) [NODE: TERM]
      │  │     │        │     └─ (164) [NODE: FACTOR]
      │  │     │        │        └─ (165) [TOKEN: NUMBER : <100>]
      │  │     │        ├─ (166) [TOKEN: KEYWORD : <έως>]
      │  │     │        ├─ (167) [NODE: EXPRESSION]
      │  │     │        │  ├─ (168) [NODE: OPTIONAL_SIGN]
      │  │     │        │  └─ (169) [NODE: TERM]
      │  │     │        │     └─ (170) [NODE: FACTOR]
      │  │     │        │        └─ (171) [TOKEN: NUMBER : <20>]
      │  │     │        ├─ (172) [NODE: STEP]
      │  │     │        │  ├─ (173) [TOKEN: KEYWORD : <με_βήμα>]
      │  │     │        │  └─ (174) [NODE: EXPRESSION]
      │  │     │        │     ├─ (175) [NODE: OPTIONAL_SIGN]
      │  │     │        │     │  └─ (176) [TOKEN: ADD_OPERATOR : <->]
      │  │     │        │     └─ (177) [NODE: TERM]
      │  │     │        │        └─ (178) [NODE: FACTOR]
      │  │     │        │           └─ (179) [TOKEN: NUMBER : <5>]
      │  │     │        ├─ (180) [TOKEN: KEYWORD : <επανάλαβε>]
      │  │     │        ├─ (181) [NODE: SEQUENCE]
      │  │     │        │  └─ (182) [NODE: STATEMENT]
      │  │     │        │     └─ (183) [NODE: ASSIGMENT_STATEMENT]
      │  │     │        │        ├─ (184) [TOKEN: VARIABLE_USAGE : <a>]
      │  │     │        │        ├─ (185) [TOKEN: DELIMITER : <:>]
      │  │     │        │        ├─ (186) [TOKEN: REL_OPERATOR : <=>]
      │  │     │        │        └─ (187) [NODE: EXPRESSION]
      │  │     │        │           ├─ (188) [NODE: OPTIONAL_SIGN]
      │  │     │        │           ├─ (189) [NODE: TERM]
      │  │     │        │           │  └─ (190) [NODE: FACTOR]
      │  │     │        │           │     ├─ (191) [TOKEN: VARIABLE_USAGE : <a>]
      │  │     │        │           │     └─ (192) [NODE: ID_TAIL]
      │  │     │        │           ├─ (193) [TOKEN: ADD_OPERATOR : <+>]
      │  │     │        │           └─ (194) [NODE: TERM]
      │  │     │        │              ├─ (195) [NODE: FACTOR]
      │  │     │        │              │  ├─ (196) [TOKEN: VARIABLE_USAGE : <a>]
      │  │     │        │              │  └─ (197) [NODE: ID_TAIL]
      │  │     │        │              ├─ (198) [TOKEN: MUL_OPERATOR : <*>]
      │  │     │        │              └─ (199) [NODE: FACTOR]
      │  │     │        │                 ├─ (200) [TOKEN: VARIABLE_USAGE : <b>]
      │  │     │        │                 └─ (201) [NODE: ID_TAIL]
      │  │     │        └─ (202) [TOKEN: KEYWORD : <για_τέλος>]
      │  │     └─ (203) [TOKEN: KEYWORD : <τέλος_διαδικασίας>]
      │  ├─ (204) [NODE: FUNCTION]
      │  │  ├─ (205) [TOKEN: KEYWORD : <συνάρτηση>]
      │  │  ├─ (206) [TOKEN: FUNCTION_IDENTIFIER : <max>]
      │  │  ├─ (207) [TOKEN: GROUP_SYMBOL : <(>]
      │  │  ├─ (208) [NODE: FORMAL_PARAMETERS_LIST]
      │  │  │  └─ (209) [NODE: VAR_LIST]
      │  │  │     ├─ (210) [TOKEN: PARAMETER_IDENTIFIER : <x>]
      │  │  │     ├─ (211) [TOKEN: DELIMITER : <,>]
      │  │  │     └─ (212) [TOKEN: PARAMETER_IDENTIFIER : <y>]
      │  │  ├─ (213) [TOKEN: GROUP_SYMBOL : <)>]
      │  │  └─ (214) [NODE: FUNCTION_BLOCK]
      │  │     ├─ (215) [TOKEN: KEYWORD : <διαπροσωπεία>]
      │  │     ├─ (216) [NODE: FUNCTION_INPUT]
      │  │     │  ├─ (217) [TOKEN: KEYWORD : <είσοδος>]
      │  │     │  └─ (218) [NODE: VAR_LIST]
      │  │     │     ├─ (219) [NODE: PARAMETER_INOUT_DECLARATION]
      │  │     │     ├─ (220) [TOKEN: DELIMITER : <,>]
      │  │     │     └─ (221) [NODE: PARAMETER_INOUT_DECLARATION]
      │  │     ├─ (222) [NODE: FUNCTION_OUTPUT]
      │  │     ├─ (223) [NODE: DECLARATIONS]
      │  │     ├─ (224) [NODE: SUBPROGRAMS]
      │  │     ├─ (225) [TOKEN: KEYWORD : <αρχή_συνάρτησης>]
      │  │     ├─ (226) [NODE: SEQUENCE]
      │  │     │  └─ (227) [NODE: STATEMENT]
      │  │     │     └─ (228) [NODE: IF_STATEMENT]
      │  │     │        ├─ (229) [TOKEN: KEYWORD : <εάν>]
      │  │     │        ├─ (230) [NODE: CONDITION]
      │  │     │        │  └─ (231) [NODE: BOOL_TERM]
      │  │     │        │     └─ (232) [NODE: BOOL_FACTOR]
      │  │     │        │        ├─ (233) [NODE: EXPRESSION]
      │  │     │        │        │  ├─ (234) [NODE: OPTIONAL_SIGN]
      │  │     │        │        │  └─ (235) [NODE: TERM]
      │  │     │        │        │     └─ (236) [NODE: FACTOR]
      │  │     │        │        │        ├─ (237) [TOKEN: VARIABLE_USAGE : <x>]
      │  │     │        │        │        └─ (238) [NODE: ID_TAIL]
      │  │     │        │        ├─ (239) [TOKEN: REL_OPERATOR : <>>]
      │  │     │        │        └─ (240) [NODE: EXPRESSION]
      │  │     │        │           ├─ (241) [NODE: OPTIONAL_SIGN]
      │  │     │        │           └─ (242) [NODE: TERM]
      │  │     │        │              └─ (243) [NODE: FACTOR]
      │  │     │        │                 ├─ (244) [TOKEN: VARIABLE_USAGE : <y>]
      │  │     │        │                 └─ (245) [NODE: ID_TAIL]
      │  │     │        ├─ (246) [TOKEN: KEYWORD : <τότε>]
      │  │     │        ├─ (247) [NODE: SEQUENCE]
      │  │     │        │  └─ (248) [NODE: STATEMENT]
      │  │     │        │     └─ (249) [NODE: ASSIGMENT_STATEMENT]
      │  │     │        │        ├─ (250) [TOKEN: VARIABLE_USAGE : <max>]
      │  │     │        │        ├─ (251) [TOKEN: DELIMITER : <:>]
      │  │     │        │        ├─ (252) [TOKEN: REL_OPERATOR : <=>]
      │  │     │        │        └─ (253) [NODE: EXPRESSION]
      │  │     │        │           ├─ (254) [NODE: OPTIONAL_SIGN]
      │  │     │        │           └─ (255) [NODE: TERM]
      │  │     │        │              └─ (256) [NODE: FACTOR]
      │  │     │        │                 ├─ (257) [TOKEN: VARIABLE_USAGE : <x>]
      │  │     │        │                 └─ (258) [NODE: ID_TAIL]
      │  │     │        ├─ (259) [NODE: ELSE_STATEMENT]
      │  │     │        │  ├─ (260) [TOKEN: KEYWORD : <αλλιώς>]
      │  │     │        │  └─ (261) [NODE: SEQUENCE]
      │  │     │        │     └─ (262) [NODE: STATEMENT]
      │  │     │        │        └─ (263) [NODE: ASSIGMENT_STATEMENT]
      │  │     │        │           ├─ (264) [TOKEN: VARIABLE_USAGE : <max>]
      │  │     │        │           ├─ (265) [TOKEN: DELIMITER : <:>]
      │  │     │        │           ├─ (266) [TOKEN: REL_OPERATOR : <=>]
      │  │     │        │           └─ (267) [NODE: EXPRESSION]
      │  │     │        │              ├─ (268) [NODE: OPTIONAL_SIGN]
      │  │     │        │              └─ (269) [NODE: TERM]
      │  │     │        │                 └─ (270) [NODE: FACTOR]
      │  │     │        │                    ├─ (271) [TOKEN: VARIABLE_USAGE : <y>]
      │  │     │        │                    └─ (272) [NODE: ID_TAIL]
      │  │     │        └─ (273) [TOKEN: KEYWORD : <εάν_τέλος>]
      │  │     └─ (274) [TOKEN: KEYWORD : <τέλος_συνάρτησης>]
      │  └─ (275) [NODE: PROCEDURE]
      │     ├─ (276) [TOKEN: KEYWORD : <διαδικασία>]
      │     ├─ (277) [TOKEN: PROCEDURE_IDENTIFIER : <for_lοοp_with_expressions>]
      │     ├─ (278) [TOKEN: GROUP_SYMBOL : <(>]
      │     ├─ (279) [NODE: FORMAL_PARAMETERS_LIST]
      │     ├─ (280) [TOKEN: GROUP_SYMBOL : <)>]
      │     └─ (281) [NODE: PROCEDURE_BLOCK]
      │        ├─ (282) [TOKEN: KEYWORD : <διαπροσωπεία>]
      │        ├─ (283) [NODE: FUNCTION_INPUT]
      │        ├─ (284) [NODE: FUNCTION_OUTPUT]
      │        ├─ (285) [NODE: DECLARATIONS]
      │        ├─ (286) [NODE: SUBPROGRAMS]
      │        ├─ (287) [TOKEN: KEYWORD : <αρχή_διαδικασίας>]
      │        ├─ (288) [NODE: SEQUENCE]
      │        │  └─ (289) [NODE: STATEMENT]
      │        │     └─ (290) [NODE: FOR_STATEMENT]
      │        │        ├─ (291) [TOKEN: KEYWORD : <για>]
      │        │        ├─ (292) [TOKEN: VARIABLE_IDENTIFIER : <p>]
      │        │        ├─ (293) [TOKEN: DELIMITER : <:>]
      │        │        ├─ (294) [TOKEN: REL_OPERATOR : <=>]
      │        │        ├─ (295) [NODE: EXPRESSION]
      │        │        │  ├─ (296) [NODE: OPTIONAL_SIGN]
      │        │        │  └─ (297) [NODE: TERM]
      │        │        │     ├─ (298) [NODE: FACTOR]
      │        │        │     │  ├─ (299) [TOKEN: GROUP_SYMBOL : <(>]
      │        │        │     │  ├─ (300) [NODE: EXPRESSION]
      │        │        │     │  │  ├─ (301) [NODE: OPTIONAL_SIGN]
      │        │        │     │  │  └─ (302) [NODE: TERM]
      │        │        │     │  │     ├─ (303) [NODE: FACTOR]
      │        │        │     │  │     │  ├─ (304) [TOKEN: FUNCTION_CALL_IN_ASSIGMENT : <max>]
      │        │        │     │  │     │  └─ (305) [NODE: ID_TAIL]
      │        │        │     │  │     │     └─ (306) [NODE: ACTUAL_PARAMETERS]
      │        │        │     │  │     │        ├─ (307) [TOKEN: GROUP_SYMBOL : <(>]
      │        │        │     │  │     │        ├─ (308) [NODE: ACTUAL_PARAMETER_LIST]
      │        │        │     │  │     │        │  ├─ (309) [NODE: ACTUAL_PARAMETER_ITEM]
      │        │        │     │  │     │        │  │  └─ (310) [NODE: EXPRESSION]
      │        │        │     │  │     │        │  │     ├─ (311) [NODE: OPTIONAL_SIGN]
      │        │        │     │  │     │        │  │     └─ (312) [NODE: TERM]
      │        │        │     │  │     │        │  │        └─ (313) [NODE: FACTOR]
      │        │        │     │  │     │        │  │           ├─ (314) [TOKEN: FUNCTION_CALL_IN_ASSIGMENT : <max>]
      │        │        │     │  │     │        │  │           └─ (315) [NODE: ID_TAIL]
      │        │        │     │  │     │        │  │              └─ (316) [NODE: ACTUAL_PARAMETERS]
      │        │        │     │  │     │        │  │                 ├─ (317) [TOKEN: GROUP_SYMBOL : <(>]
      │        │        │     │  │     │        │  │                 ├─ (318) [NODE: ACTUAL_PARAMETER_LIST]
      │        │        │     │  │     │        │  │                 │  ├─ (319) [NODE: ACTUAL_PARAMETER_ITEM]
      │        │        │     │  │     │        │  │                 │  │  └─ (320) [NODE: EXPRESSION]
      │        │        │     │  │     │        │  │                 │  │     ├─ (321) [NODE: OPTIONAL_SIGN]
      │        │        │     │  │     │        │  │                 │  │     └─ (322) [NODE: TERM]
      │        │        │     │  │     │        │  │                 │  │        ├─ (323) [NODE: FACTOR]
      │        │        │     │  │     │        │  │                 │  │        │  └─ (324) [TOKEN: NUMBER : <12>]
      │        │        │     │  │     │        │  │                 │  │        ├─ (325) [TOKEN: MUL_OPERATOR : </>]
      │        │        │     │  │     │        │  │                 │  │        └─ (326) [NODE: FACTOR]
      │        │        │     │  │     │        │  │                 │  │           └─ (327) [TOKEN: NUMBER : <4>]
      │        │        │     │  │     │        │  │                 │  ├─ (328) [TOKEN: DELIMITER : <,>]
      │        │        │     │  │     │        │  │                 │  └─ (329) [NODE: ACTUAL_PARAMETER_ITEM]
      │        │        │     │  │     │        │  │                 │     └─ (330) [NODE: EXPRESSION]
      │        │        │     │  │     │        │  │                 │        ├─ (331) [NODE: OPTIONAL_SIGN]
      │        │        │     │  │     │        │  │                 │        └─ (332) [NODE: TERM]
      │        │        │     │  │     │        │  │                 │           ├─ (333) [NODE: FACTOR]
      │        │        │     │  │     │        │  │                 │           │  └─ (334) [TOKEN: NUMBER : <30>]
      │        │        │     │  │     │        │  │                 │           ├─ (335) [TOKEN: MUL_OPERATOR : </>]
      │        │        │     │  │     │        │  │                 │           └─ (336) [NODE: FACTOR]
      │        │        │     │  │     │        │  │                 │              └─ (337) [TOKEN: NUMBER : <5>]
      │        │        │     │  │     │        │  │                 └─ (338) [TOKEN: GROUP_SYMBOL : <)>]
      │        │        │     │  │     │        │  ├─ (339) [TOKEN: DELIMITER : <,>]
      │        │        │     │  │     │        │  └─ (340) [NODE: ACTUAL_PARAMETER_ITEM]
      │        │        │     │  │     │        │     └─ (341) [NODE: EXPRESSION]
      │        │        │     │  │     │        │        ├─ (342) [NODE: OPTIONAL_SIGN]
      │        │        │     │  │     │        │        └─ (343) [NODE: TERM]
      │        │        │     │  │     │        │           └─ (344) [NODE: FACTOR]
      │        │        │     │  │     │        │              ├─ (345) [TOKEN: FUNCTION_CALL_IN_ASSIGMENT : <max>]
      │        │        │     │  │     │        │              └─ (346) [NODE: ID_TAIL]
      │        │        │     │  │     │        │                 └─ (347) [NODE: ACTUAL_PARAMETERS]
      │        │        │     │  │     │        │                    ├─ (348) [TOKEN: GROUP_SYMBOL : <(>]
      │        │        │     │  │     │        │                    ├─ (349) [NODE: ACTUAL_PARAMETER_LIST]
      │        │        │     │  │     │        │                    │  ├─ (350) [NODE: ACTUAL_PARAMETER_ITEM]
      │        │        │     │  │     │        │                    │  │  └─ (351) [NODE: EXPRESSION]
      │        │        │     │  │     │        │                    │  │     ├─ (352) [NODE: OPTIONAL_SIGN]
      │        │        │     │  │     │        │                    │  │     ├─ (353) [NODE: TERM]
      │        │        │     │  │     │        │                    │  │     │  └─ (354) [NODE: FACTOR]
      │        │        │     │  │     │        │                    │  │     │     └─ (355) [TOKEN: NUMBER : <2>]
      │        │        │     │  │     │        │                    │  │     ├─ (356) [TOKEN: ADD_OPERATOR : <+>]
      │        │        │     │  │     │        │                    │  │     └─ (357) [NODE: TERM]
      │        │        │     │  │     │        │                    │  │        └─ (358) [NODE: FACTOR]
      │        │        │     │  │     │        │                    │  │           └─ (359) [TOKEN: NUMBER : <1>]
      │        │        │     │  │     │        │                    │  ├─ (360) [TOKEN: DELIMITER : <,>]
      │        │        │     │  │     │        │                    │  └─ (361) [NODE: ACTUAL_PARAMETER_ITEM]
      │        │        │     │  │     │        │                    │     └─ (362) [NODE: EXPRESSION]
      │        │        │     │  │     │        │                    │        ├─ (363) [NODE: OPTIONAL_SIGN]
      │        │        │     │  │     │        │                    │        └─ (364) [NODE: TERM]
      │        │        │     │  │     │        │                    │           └─ (365) [NODE: FACTOR]
      │        │        │     │  │     │        │                    │              └─ (366) [TOKEN: NUMBER : <4>]
      │        │        │     │  │     │        │                    └─ (367) [TOKEN: GROUP_SYMBOL : <)>]
      │        │        │     │  │     │        └─ (368) [TOKEN: GROUP_SYMBOL : <)>]
      │        │        │     │  │     ├─ (369) [TOKEN: MUL_OPERATOR : </>]
      │        │        │     │  │     └─ (370) [NODE: FACTOR]
      │        │        │     │  │        └─ (371) [TOKEN: NUMBER : <2>]
      │        │        │     │  └─ (372) [TOKEN: GROUP_SYMBOL : <)>]
      │        │        │     ├─ (373) [TOKEN: MUL_OPERATOR : </>]
      │        │        │     └─ (374) [NODE: FACTOR]
      │        │        │        └─ (375) [TOKEN: NUMBER : <7>]
      │        │        ├─ (376) [TOKEN: KEYWORD : <έως>]
      │        │        ├─ (377) [NODE: EXPRESSION]
      │        │        │  ├─ (378) [NODE: OPTIONAL_SIGN]
      │        │        │  ├─ (379) [NODE: TERM]
      │        │        │  │  └─ (380) [NODE: FACTOR]
      │        │        │  │     └─ (381) [TOKEN: NUMBER : <1>]
      │        │        │  ├─ (382) [TOKEN: ADD_OPERATOR : <+>]
      │        │        │  ├─ (383) [NODE: TERM]
      │        │        │  │  ├─ (384) [NODE: FACTOR]
      │        │        │  │  │  └─ (385) [TOKEN: NUMBER : <2>]
      │        │        │  │  ├─ (386) [TOKEN: MUL_OPERATOR : <*>]
      │        │        │  │  └─ (387) [NODE: FACTOR]
      │        │        │  │     └─ (388) [TOKEN: NUMBER : <4>]
      │        │        │  ├─ (389) [TOKEN: ADD_OPERATOR : <+>]
      │        │        │  └─ (390) [NODE: TERM]
      │        │        │     └─ (391) [NODE: FACTOR]
      │        │        │        └─ (392) [TOKEN: NUMBER : <1>]
      │        │        ├─ (393) [NODE: STEP]
      │        │        │  ├─ (394) [TOKEN: KEYWORD : <με_βήμα>]
      │        │        │  └─ (395) [NODE: EXPRESSION]
      │        │        │     ├─ (396) [NODE: OPTIONAL_SIGN]
      │        │        │     └─ (397) [NODE: TERM]
      │        │        │        ├─ (398) [NODE: FACTOR]
      │        │        │        │  └─ (399) [TOKEN: NUMBER : <10>]
      │        │        │        ├─ (400) [TOKEN: MUL_OPERATOR : </>]
      │        │        │        └─ (401) [NODE: FACTOR]
      │        │        │           └─ (402) [TOKEN: NUMBER : <5>]
      │        │        ├─ (403) [TOKEN: KEYWORD : <επανάλαβε>]
      │        │        ├─ (404) [NODE: SEQUENCE]
      │        │        │  └─ (405) [NODE: STATEMENT]
      │        │        │     └─ (406) [NODE: PRINT_STATEMENT]
      │        │        │        ├─ (407) [TOKEN: KEYWORD : <γράψε>]
      │        │        │        └─ (408) [NODE: EXPRESSION]
      │        │        │           ├─ (409) [NODE: OPTIONAL_SIGN]
      │        │        │           └─ (410) [NODE: TERM]
      │        │        │              └─ (411) [NODE: FACTOR]
      │        │        │                 ├─ (412) [TOKEN: VARIABLE_USAGE : <p>]
      │        │        │                 └─ (413) [NODE: ID_TAIL]
      │        │        └─ (414) [TOKEN: KEYWORD : <για_τέλος>]
      │        └─ (415) [TOKEN: KEYWORD : <τέλος_διαδικασίας>]
      ├─ (416) [TOKEN: KEYWORD : <αρχή_προγράμματος>]
      ├─ (417) [NODE: SEQUENCE]
      │  ├─ (418) [NODE: STATEMENT]
      │  │  └─ (419) [NODE: ASSIGMENT_STATEMENT]
      │  │     ├─ (420) [TOKEN: VARIABLE_USAGE : <a>]
      │  │     ├─ (421) [TOKEN: DELIMITER : <:>]
      │  │     ├─ (422) [TOKEN: REL_OPERATOR : <=>]
      │  │     └─ (423) [NODE: EXPRESSION]
      │  │        ├─ (424) [NODE: OPTIONAL_SIGN]
      │  │        └─ (425) [NODE: TERM]
      │  │           └─ (426) [NODE: FACTOR]
      │  │              └─ (427) [TOKEN: NUMBER : <10>]
      │  ├─ (428) [TOKEN: DELIMITER : <;>]
      │  ├─ (429) [NODE: STATEMENT]
      │  │  └─ (430) [NODE: CALL_STATEMENT]
      │  │     ├─ (431) [TOKEN: KEYWORD : <εκτέλεσε>]
      │  │     ├─ (432) [TOKEN: SUBROUTINE_USAGE : <for_loop_default_step>]
      │  │     └─ (433) [NODE: ID_TAIL]
      │  │        └─ (434) [NODE: ACTUAL_PARAMETERS]
      │  │           ├─ (435) [TOKEN: GROUP_SYMBOL : <(>]
      │  │           ├─ (436) [NODE: ACTUAL_PARAMETER_LIST]
      │  │           └─ (437) [TOKEN: GROUP_SYMBOL : <)>]
      │  ├─ (438) [TOKEN: DELIMITER : <;>]
      │  ├─ (439) [NODE: STATEMENT]
      │  │  └─ (440) [NODE: CALL_STATEMENT]
      │  │     ├─ (441) [TOKEN: KEYWORD : <εκτέλεσε>]
      │  │     ├─ (442) [TOKEN: SUBROUTINE_USAGE : <for_loop_positive_step>]
      │  │     └─ (443) [NODE: ID_TAIL]
      │  │        └─ (444) [NODE: ACTUAL_PARAMETERS]
      │  │           ├─ (445) [TOKEN: GROUP_SYMBOL : <(>]
      │  │           ├─ (446) [NODE: ACTUAL_PARAMETER_LIST]
      │  │           └─ (447) [TOKEN: GROUP_SYMBOL : <)>]
      │  ├─ (448) [TOKEN: DELIMITER : <;>]
      │  ├─ (449) [NODE: STATEMENT]
      │  │  └─ (450) [NODE: CALL_STATEMENT]
      │  │     ├─ (451) [TOKEN: KEYWORD : <εκτέλεσε>]
      │  │     ├─ (452) [TOKEN: SUBROUTINE_USAGE : <for_lοοp_negative_step>]
      │  │     └─ (453) [NODE: ID_TAIL]
      │  │        └─ (454) [NODE: ACTUAL_PARAMETERS]
      │  │           ├─ (455) [TOKEN: GROUP_SYMBOL : <(>]
      │  │           ├─ (456) [NODE: ACTUAL_PARAMETER_LIST]
      │  │           └─ (457) [TOKEN: GROUP_SYMBOL : <)>]
      │  ├─ (458) [TOKEN: DELIMITER : <;>]
      │  └─ (459) [NODE: STATEMENT]
      │     └─ (460) [NODE: CALL_STATEMENT]
      │        ├─ (461) [TOKEN: KEYWORD : <εκτέλεσε>]
      │        ├─ (462) [TOKEN: SUBROUTINE_USAGE : <for_lοοp_with_expressions>]
      │        └─ (463) [NODE: ID_TAIL]
      │           └─ (464) [NODE: ACTUAL_PARAMETERS]
      │              ├─ (465) [TOKEN: GROUP_SYMBOL : <(>]
      │              ├─ (466) [NODE: ACTUAL_PARAMETER_LIST]
      │              └─ (467) [TOKEN: GROUP_SYMBOL : <)>]
      └─ (468) [NODE: PROGRAM_END_KEYWORD]
```
---

## 📑 Generated Symbol Table (.sym)

During semantic analysis, the compiler constructs a hierarchical symbol table reflecting scope nesting, variable allocation, parameter modes, and activation record layout.

The following excerpt is produced in the `.sym` file after compiling the example program:


```text
Closing scope    || Depth: 1
Variables:
  (none)
Subroutines:
  (none)
---

Closing scope    || Depth: 1
Variables:
  (none)
Subroutines:
  (none)
---

Closing scope    || Depth: 1
Variables:
  (none)
Subroutines:
  (none)
---

Closing scope    || Depth: 1
Variables:
  LocalVariable{name='max', dataType=Integer, offset=20, scopeDepth=1}
  Parameter{name='y', dataType=Integer, offset=16, mode=input, scopeDepth=1}
  Parameter{name='x', dataType=Integer, offset=12, mode=input, scopeDepth=1}
Subroutines:
  (none)
---

Closing scope    || Depth: 1
Variables:
  (none)
Subroutines:
  (none)
---

Closing scope    || Depth: 0
Variables:
  LocalVariable{name='p', dataType=Integer, offset=40, scopeDepth=0}
  LocalVariable{name='k', dataType=Integer, offset=36, scopeDepth=0}
  LocalVariable{name='j', dataType=Integer, offset=32, scopeDepth=0}
  LocalVariable{name='i', dataType=Integer, offset=28, scopeDepth=0}
  LocalVariable{name='d', dataType=Integer, offset=24, scopeDepth=0}
  LocalVariable{name='c', dataType=Integer, offset=20, scopeDepth=0}
  LocalVariable{name='b', dataType=Integer, offset=16, scopeDepth=0}
  LocalVariable{name='a', dataType=Integer, offset=12, scopeDepth=0}
Subroutines:
  ── Procedure: for_lοοp_with_expressions ──
     Scope Depth: 1
      ActivationRecord:
        Temporary Variables: $T_21:Integer@12, $T_11:Integer@16, $T_22:Integer@20, $T_20:Integer@24, $T_14:Integer@28, $T_15:Integer@32, $T_12:Integer@36, $T_23:Integer@40, $T_13:Integer@44, $T_18:Integer@48, $T_19:Integer@52, $T_16:Integer@56, $T_17:Integer@60
        Local Variables:     none
        Formal Parameters:   none
        StartingQuadAddress: 58
        Record Size:         64 bytes
  ── Procedure: $$$_Main_$$$ ──
     Scope Depth: 0
      ActivationRecord:
        Temporary Variables: none
        Local Variables:     p:Integer@40, a:Integer@12, b:Integer@16, c:Integer@20, d:Integer@24, i:Integer@28, j:Integer@32, k:Integer@36
        Formal Parameters:   none
        StartingQuadAddress: 93
        Record Size:         44 bytes
  ── Procedure: for_lοοp_negative_step ──
     Scope Depth: 1
      ActivationRecord:
        Temporary Variables: $T_8:Integer@12, $T_7:Integer@16, $T_9:Integer@20, $T_10:Integer@24
        Local Variables:     none
        Formal Parameters:   none
        StartingQuadAddress: 33
        Record Size:         28 bytes
  ── Function: max ──
     Scope Depth: 1
    Return Type: Integer
      ActivationRecord:
        Temporary Variables: none
        Local Variables:     max:Integer@20
        Formal Parameters:   x:Integer@12, y:Integer@16
        StartingQuadAddress: 50
        Record Size:         24 bytes
  ── Procedure: for_loop_positive_step ──
     Scope Depth: 1
      ActivationRecord:
        Temporary Variables: $T_4:Integer@12, $T_6:Integer@16, $T_5:Integer@20
        Local Variables:     none
        Formal Parameters:   none
        StartingQuadAddress: 17
        Record Size:         24 bytes
  ── Procedure: for_loop_default_step ──
     Scope Depth: 1
      ActivationRecord:
        Temporary Variables: $T_2:Integer@12, $T_1:Integer@16, $T_3:Integer@20
        Local Variables:     none
        Formal Parameters:   none
        StartingQuadAddress: 1
        Record Size:         24 bytes
```
---

## 🧩 Generated Intermediate Representation (.int)

After semantic validation, the AST is lowered into an intermediate representation based on quadruples (three-address code).

This representation makes control flow, temporary variables, and procedure calls explicit before final code generation.

The following quads are generated for the example program:


```text
 0 : begin_block, for_loop_default_step, _, _
 1 : :=, 1, _, i
 2 : >=, 1, 0, 4
 3 : jump, _, _, 6
 4 : <=, i, 10, 8
 5 : jump, _, _, 15
 6 : >=, i, 10, 8
 7 : jump, _, _, 15
 8 : *, a, b, $T_1
 9 : +, a, $T_1, $T_2
10 : :=, $T_2, _, a
11 : +, i, 1, $T_3
12 : :=, $T_3, _, i
13 : >=, 1, 0, 4
14 : jump, _, _, 6
15 : end_block, for_loop_default_step, _, _
16 : begin_block, for_loop_positive_step, _, _
17 : :=, 2, _, j
18 : >=, 4, 0, 20
19 : jump, _, _, 22
20 : <=, j, 30, 24
21 : jump, _, _, 31
22 : >=, j, 30, 24
23 : jump, _, _, 31
24 : *, a, b, $T_4
25 : +, a, $T_4, $T_5
26 : :=, $T_5, _, a
27 : +, j, 4, $T_6
28 : :=, $T_6, _, j
29 : >=, 4, 0, 20
30 : jump, _, _, 22
31 : end_block, for_loop_positive_step, _, _
32 : begin_block, for_lοοp_negative_step, _, _
33 : :=, 100, _, k
34 : -, 0, 5, $T_7
35 : >=, $T_7, 0, 37
36 : jump, _, _, 39
37 : <=, k, 20, 41
38 : jump, _, _, 48
39 : >=, k, 20, 41
40 : jump, _, _, 48
41 : *, a, b, $T_8
42 : +, a, $T_8, $T_9
43 : :=, $T_9, _, a
44 : +, k, $T_7, $T_10
45 : :=, $T_10, _, k
46 : >=, $T_7, 0, 37
47 : jump, _, _, 39
48 : end_block, for_lοοp_negative_step, _, _
49 : begin_block, max, _, _
50 : >, x, y, 52
51 : jump, _, _, 54
52 : :=, x, _, max
53 : jump, _, _, 55
54 : :=, y, _, max
55 : retv, _, _, max
56 : end_block, max, _, _
57 : begin_block, for_lοοp_with_expressions, _, _
58 : /, 12, 4, $T_11
59 : /, 30, 5, $T_12
60 : par, $T_11, cv, _
61 : par, $T_12, cv, _
62 : par, $T_13, ret, _
63 : call, _, _, max
64 : +, 2, 1, $T_14
65 : par, $T_14, cv, _
66 : par, 4, cv, _
67 : par, $T_15, ret, _
68 : call, _, _, max
69 : par, $T_13, cv, _
70 : par, $T_15, cv, _
71 : par, $T_16, ret, _
72 : call, _, _, max
73 : /, $T_16, 2, $T_17
74 : /, $T_17, 7, $T_18
75 : :=, $T_18, _, p
76 : *, 2, 4, $T_19
77 : +, 1, $T_19, $T_20
78 : +, $T_20, 1, $T_21
79 : /, 10, 5, $T_22
80 : >=, $T_22, 0, 82
81 : jump, _, _, 84
82 : <=, p, $T_21, 86
83 : jump, _, _, 91
84 : >=, p, $T_21, 86
85 : jump, _, _, 91
86 : out, _, _, p
87 : +, p, $T_22, $T_23
88 : :=, $T_23, _, p
89 : >=, $T_22, 0, 82
90 : jump, _, _, 84
91 : end_block, for_lοοp_with_expressions, _, _
92 : begin_block, $$$_Main_$$$, _, _
93 : :=, 10, _, a
94 : call, _, _, for_loop_default_step
95 : call, _, _, for_loop_positive_step
96 : call, _, _, for_lοοp_negative_step
97 : call, _, _, for_lοοp_with_expressions
98 : halt, _, _, _
99 : end_block, $$$_Main_$$$, _, _
```

---

## 🧮 Generated RISC-V Assembly (.s)

The intermediate quadruples are translated into executable RISC-V assembly instructions.

Each procedure:
- Allocates its own activation record
- Preserves the return address (`ra`)
- Uses explicit stack offsets derived from the symbol table
- Restores the stack frame upon return

The following excerpt illustrates the generated assembly for the example program:


```text
	.data
	str_nl: .asciz "\n"
	.text
	

	j LMain
	

# begin_block, for_loop_default_step, _, _
L0:
	sw ra, 0(sp)
	

# :=, 1, _, i
L1:
	li t1, 1
	lw t0, 4(sp)
	addi t0 , t0, 28
	sw t1, 0(t0)
	

# >=, 1, 0, 4
L2:
	li t1, 1
	li t2, 0
	bge t1, t2, L4
	

# jump, _, _, 6
L3:
	j L6
	

# <=, i, 10, 8
L4:
	lw t0, 4(sp)
	addi t0 , t0, 28
	lw t1, 0(t0)
	li t2, 10
	ble t1, t2, L8
	

# jump, _, _, 15
L5:
	j L15
	

# >=, i, 10, 8
L6:
	lw t0, 4(sp)
	addi t0 , t0, 28
	lw t1, 0(t0)
	li t2, 10
	bge t1, t2, L8
	

# jump, _, _, 15
L7:
	j L15
	

# *, a, b, $T_1
L8:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t1, 0(t0)
	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t2, 0(t0)
	mul t1, t1, t2
	sw t1, 16(sp)
	

# +, a, $T_1, $T_2
L9:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t1, 0(t0)
	lw t2, 16(sp)
	add t1, t1, t2
	sw t1, 12(sp)
	

# :=, $T_2, _, a
L10:
	lw t1, 12(sp)
	lw t0, 4(sp)
	addi t0 , t0, 12
	sw t1, 0(t0)
	

# +, i, 1, $T_3
L11:
	lw t0, 4(sp)
	addi t0 , t0, 28
	lw t1, 0(t0)
	li t2, 1
	add t1, t1, t2
	sw t1, 20(sp)
	

# :=, $T_3, _, i
L12:
	lw t1, 20(sp)
	lw t0, 4(sp)
	addi t0 , t0, 28
	sw t1, 0(t0)
	

# >=, 1, 0, 4
L13:
	li t1, 1
	li t2, 0
	bge t1, t2, L4
	

# jump, _, _, 6
L14:
	j L6
	

# end_block, for_loop_default_step, _, _
L15:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, for_loop_positive_step, _, _
L16:
	sw ra, 0(sp)
	

# :=, 2, _, j
L17:
	li t1, 2
	lw t0, 4(sp)
	addi t0 , t0, 32
	sw t1, 0(t0)
	

# >=, 4, 0, 20
L18:
	li t1, 4
	li t2, 0
	bge t1, t2, L20
	

# jump, _, _, 22
L19:
	j L22
	

# <=, j, 30, 24
L20:
	lw t0, 4(sp)
	addi t0 , t0, 32
	lw t1, 0(t0)
	li t2, 30
	ble t1, t2, L24
	

# jump, _, _, 31
L21:
	j L31
	

# >=, j, 30, 24
L22:
	lw t0, 4(sp)
	addi t0 , t0, 32
	lw t1, 0(t0)
	li t2, 30
	bge t1, t2, L24
	

# jump, _, _, 31
L23:
	j L31
	

# *, a, b, $T_4
L24:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t1, 0(t0)
	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t2, 0(t0)
	mul t1, t1, t2
	sw t1, 12(sp)
	

# +, a, $T_4, $T_5
L25:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t1, 0(t0)
	lw t2, 12(sp)
	add t1, t1, t2
	sw t1, 20(sp)
	

# :=, $T_5, _, a
L26:
	lw t1, 20(sp)
	lw t0, 4(sp)
	addi t0 , t0, 12
	sw t1, 0(t0)
	

# +, j, 4, $T_6
L27:
	lw t0, 4(sp)
	addi t0 , t0, 32
	lw t1, 0(t0)
	li t2, 4
	add t1, t1, t2
	sw t1, 16(sp)
	

# :=, $T_6, _, j
L28:
	lw t1, 16(sp)
	lw t0, 4(sp)
	addi t0 , t0, 32
	sw t1, 0(t0)
	

# >=, 4, 0, 20
L29:
	li t1, 4
	li t2, 0
	bge t1, t2, L20
	

# jump, _, _, 22
L30:
	j L22
	

# end_block, for_loop_positive_step, _, _
L31:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, for_lοοp_negative_step, _, _
L32:
	sw ra, 0(sp)
	

# :=, 100, _, k
L33:
	li t1, 100
	lw t0, 4(sp)
	addi t0 , t0, 36
	sw t1, 0(t0)
	

# -, 0, 5, $T_7
L34:
	li t1, 0
	li t2, 5
	sub t1, t1, t2
	sw t1, 16(sp)
	

# >=, $T_7, 0, 37
L35:
	lw t1, 16(sp)
	li t2, 0
	bge t1, t2, L37
	

# jump, _, _, 39
L36:
	j L39
	

# <=, k, 20, 41
L37:
	lw t0, 4(sp)
	addi t0 , t0, 36
	lw t1, 0(t0)
	li t2, 20
	ble t1, t2, L41
	

# jump, _, _, 48
L38:
	j L48
	

# >=, k, 20, 41
L39:
	lw t0, 4(sp)
	addi t0 , t0, 36
	lw t1, 0(t0)
	li t2, 20
	bge t1, t2, L41
	

# jump, _, _, 48
L40:
	j L48
	

# *, a, b, $T_8
L41:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t1, 0(t0)
	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t2, 0(t0)
	mul t1, t1, t2
	sw t1, 12(sp)
	

# +, a, $T_8, $T_9
L42:
	lw t0, 4(sp)
	addi t0 , t0, 12
	lw t1, 0(t0)
	lw t2, 12(sp)
	add t1, t1, t2
	sw t1, 20(sp)
	

# :=, $T_9, _, a
L43:
	lw t1, 20(sp)
	lw t0, 4(sp)
	addi t0 , t0, 12
	sw t1, 0(t0)
	

# +, k, $T_7, $T_10
L44:
	lw t0, 4(sp)
	addi t0 , t0, 36
	lw t1, 0(t0)
	lw t2, 16(sp)
	add t1, t1, t2
	sw t1, 24(sp)
	

# :=, $T_10, _, k
L45:
	lw t1, 24(sp)
	lw t0, 4(sp)
	addi t0 , t0, 36
	sw t1, 0(t0)
	

# >=, $T_7, 0, 37
L46:
	lw t1, 16(sp)
	li t2, 0
	bge t1, t2, L37
	

# jump, _, _, 39
L47:
	j L39
	

# end_block, for_lοοp_negative_step, _, _
L48:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, max, _, _
L49:
	sw ra, 0(sp)
	

# >, x, y, 52
L50:
	lw t1, 12(sp)
	lw t2, 16(sp)
	bgt t1, t2, L52
	

# jump, _, _, 54
L51:
	j L54
	

# :=, x, _, max
L52:
	lw t1, 12(sp)
	sw t1, 20(sp)
	

# jump, _, _, 55
L53:
	j L55
	

# :=, y, _, max
L54:
	lw t1, 16(sp)
	sw t1, 20(sp)
	

# retv, _, _, max
L55:
	lw t0, 8(sp)
	lw t1, 20(sp)
	sw t1, 0(t0)
	

# end_block, max, _, _
L56:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, for_lοοp_with_expressions, _, _
L57:
	sw ra, 0(sp)
	

# /, 12, 4, $T_11
L58:
	li t1, 12
	li t2, 4
	div t1, t1, t2
	sw t1, 16(sp)
	

# /, 30, 5, $T_12
L59:
	li t1, 30
	li t2, 5
	div t1, t1, t2
	sw t1, 36(sp)
	

# par, $T_11, cv, _
L60:
	# Ignored. Call quad will handle it.
	

# par, $T_12, cv, _
L61:
	# Ignored. Call quad will handle it.
	

# par, $T_13, ret, _
L62:
	# Ignored. Call quad will handle it.
	

# call, _, _, max
L63:
	mv t3, sp
	addi sp, sp, -24
	lw t3, 4(t3)
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 16
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter $T_11 ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 36
	lw t0, 0(t0)
	sw t0, 16(sp)
	# parameter $T_12 ↑↑↑

	addi t0,t3, 44
	sw t0, 8(sp)
	# ret par ↑↑↑

	sw ra, 0(sp)
	jal L49
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# +, 2, 1, $T_14
L64:
	li t1, 2
	li t2, 1
	add t1, t1, t2
	sw t1, 28(sp)
	

# par, $T_14, cv, _
L65:
	# Ignored. Call quad will handle it.
	

# par, 4, cv, _
L66:
	# Ignored. Call quad will handle it.
	

# par, $T_15, ret, _
L67:
	# Ignored. Call quad will handle it.
	

# call, _, _, max
L68:
	mv t3, sp
	addi sp, sp, -24
	lw t3, 4(t3)
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 28
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter $T_14 ↑↑↑

	li t0, 4
	sw t0, 16(sp)
	# parameter 4 ↑↑↑

	addi t0,t3, 32
	sw t0, 8(sp)
	# ret par ↑↑↑

	sw ra, 0(sp)
	jal L49
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# par, $T_13, cv, _
L69:
	# Ignored. Call quad will handle it.
	

# par, $T_15, cv, _
L70:
	# Ignored. Call quad will handle it.
	

# par, $T_16, ret, _
L71:
	# Ignored. Call quad will handle it.
	

# call, _, _, max
L72:
	mv t3, sp
	addi sp, sp, -24
	lw t3, 4(t3)
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 44
	lw t0, 0(t0)
	sw t0, 12(sp)
	# parameter $T_13 ↑↑↑

	lw t0, 4(sp)
	addi t0 , t0, 32
	lw t0, 0(t0)
	sw t0, 16(sp)
	# parameter $T_15 ↑↑↑

	addi t0,t3, 56
	sw t0, 8(sp)
	# ret par ↑↑↑

	sw ra, 0(sp)
	jal L49
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# /, $T_16, 2, $T_17
L73:
	lw t1, 56(sp)
	li t2, 2
	div t1, t1, t2
	sw t1, 60(sp)
	

# /, $T_17, 7, $T_18
L74:
	lw t1, 60(sp)
	li t2, 7
	div t1, t1, t2
	sw t1, 48(sp)
	

# :=, $T_18, _, p
L75:
	lw t1, 48(sp)
	lw t0, 4(sp)
	addi t0 , t0, 40
	sw t1, 0(t0)
	

# *, 2, 4, $T_19
L76:
	li t1, 2
	li t2, 4
	mul t1, t1, t2
	sw t1, 52(sp)
	

# +, 1, $T_19, $T_20
L77:
	li t1, 1
	lw t2, 52(sp)
	add t1, t1, t2
	sw t1, 24(sp)
	

# +, $T_20, 1, $T_21
L78:
	lw t1, 24(sp)
	li t2, 1
	add t1, t1, t2
	sw t1, 12(sp)
	

# /, 10, 5, $T_22
L79:
	li t1, 10
	li t2, 5
	div t1, t1, t2
	sw t1, 20(sp)
	

# >=, $T_22, 0, 82
L80:
	lw t1, 20(sp)
	li t2, 0
	bge t1, t2, L82
	

# jump, _, _, 84
L81:
	j L84
	

# <=, p, $T_21, 86
L82:
	lw t0, 4(sp)
	addi t0 , t0, 40
	lw t1, 0(t0)
	lw t2, 12(sp)
	ble t1, t2, L86
	

# jump, _, _, 91
L83:
	j L91
	

# >=, p, $T_21, 86
L84:
	lw t0, 4(sp)
	addi t0 , t0, 40
	lw t1, 0(t0)
	lw t2, 12(sp)
	bge t1, t2, L86
	

# jump, _, _, 91
L85:
	j L91
	

# out, _, _, p
L86:
	lw t0, 4(sp)
	addi t0 , t0, 40
	lw t0, 0(t0)
	mv a0, t0
	li a7, 1
	ecall
	la a0, str_nl
	li a7, 4
	ecall
	

# +, p, $T_22, $T_23
L87:
	lw t0, 4(sp)
	addi t0 , t0, 40
	lw t1, 0(t0)
	lw t2, 20(sp)
	add t1, t1, t2
	sw t1, 40(sp)
	

# :=, $T_23, _, p
L88:
	lw t1, 40(sp)
	lw t0, 4(sp)
	addi t0 , t0, 40
	sw t1, 0(t0)
	

# >=, $T_22, 0, 82
L89:
	lw t1, 20(sp)
	li t2, 0
	bge t1, t2, L82
	

# jump, _, _, 84
L90:
	j L84
	

# end_block, for_lοοp_with_expressions, _, _
L91:
	lw ra, 0(sp)
	jr ra
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 1)   || Assembly batch for this scope generated and flushed successfully ||
	

# begin_block, $$$_Main_$$$, _, _
L92:
LMain:
	addi sp, sp, -44
	

# :=, 10, _, a
L93:
	li t1, 10
	sw t1, 12(sp)
	

# call, _, _, for_loop_default_step
L94:
	mv t3, sp
	addi sp, sp, -24
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	sw ra, 0(sp)
	jal L0
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# call, _, _, for_loop_positive_step
L95:
	mv t3, sp
	addi sp, sp, -24
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	sw ra, 0(sp)
	jal L16
	# call ↑↑↑

	addi sp, sp, 24
	# Free callee stack ↑↑↑
	

# call, _, _, for_lοοp_negative_step
L96:
	mv t3, sp
	addi sp, sp, -28
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	sw ra, 0(sp)
	jal L32
	# call ↑↑↑

	addi sp, sp, 28
	# Free callee stack ↑↑↑
	

# call, _, _, for_lοοp_with_expressions
L97:
	mv t3, sp
	addi sp, sp, -64
	sw t3, 4(sp)
	# Allocate callee stack and handle dynamic link ↑↑↑

	sw ra, 0(sp)
	jal L57
	# call ↑↑↑

	addi sp, sp, 64
	# Free callee stack ↑↑↑
	

# halt, _, _, _
L98:
	li a0, 0
	li a7, 93
	ecall
	

# end_block, $$$_Main_$$$, _, _
L99:
	

# ↑↑↑ Exiting current scope ↑↑↑ (Depth: 0)   || Assembly batch for this scope generated and flushed successfully ||
```

---

## 📘 Detailed Documentation

For a complete technical analysis of the compiler design, semantic rules,
intermediate representation strategy, and code generation process,
refer to the full project report:

-  [Greek++ Compiler Report (English)](report/report_compilers_v1_english.pdf)

-  [Greek++ Compiler Report (Greek)](report/report_compilers_v1_greek.pdf)

---
## 👤 Author

> GitHub: [xrddev](https://github.com/xrddev)


## 📝 License

Released under the [MIT License](LICENSE). Originally built as part of a university project.

