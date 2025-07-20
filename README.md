# greek++ Compiler: Full Implementation of a Recursive-Descent, Multi-Phase Compiler Targeting RISC-V
<br>

## 📑 Table of Contents

1. [🚀 Introduction](#-introduction)
2. [🧱 Compiler Architecture](#-compiler-architecture)
3. [🔤 Lexical Analysis](#-lexical-analysis)
4. [🗂️ Syntactic Analysis](#-syntactic-analysis)
5. [✅ Semantic Analysis](#-semantic-analysis)
6. [⚙️ Intermediate Code Generation](#-intermediate-code-generation)
7. [📒 Symbol Table](#-symbol-table)
8. [🖥️ Final Code Generation (RISC-V)](#-final-code-generation-risc-v)
9. [🚀 How to Run](#-how-to-run)
10. [📝 Examples](#-examples)
11. [⚠️ Error Handling](#-error-handling)
12. [👤 Credits](#-credits)

<br><br><br>

---


## 🚀 Introduction

This project implements a full compiler for the **greek++ programming language**, a compact yet expressive language designed to capture the essential features of modern programming.

Although greek++ is intentionally simple (supporting only integer data types), it provides a wide range of key programming constructs, including:


- 🛠️ **Functions and Procedures**
- 🔁 **Loops:**
- ✅ **Conditionals**
- 📥 **Input/Output Operations**
- ♻️ **Recursive Calls**
- 🔗 **Parameter Passing by value & by reference***
- 🗂️ **Nested Function & Procedure Declarations**

<br><br><br>

---

## 🧱 Compiler Architecture

The greek++ compiler follows a **multi-phase pipeline** that systematically transforms high-level source code into an intermediate representation. The main phases, as orchestrated in the `GreekPP` main class, are:

1️⃣ **Lexical Analysis:**  
The source file is loaded and scanned character-by-character. Tokens are generated (identifiers, keywords, numbers, operators, etc.).

2️⃣ **Syntactic Analysis & AST Construction:**  
The token stream is parsed according to the grammar of greek++, and an Abstract Syntax Tree (AST) is constructed, capturing the full hierarchical structure of the program.

3️⃣ **Semantic Analysis & Intermediate Code Generation:**  
The AST is traversed to ensure semantic correctness (scope handling, type checking, symbol resolution). Intermediate code is generated in the form of **quadruples (quads)**, which provide a platform-independent representation of the program's logic.

4️⃣ **Output Generation:**
- The intermediate code is saved to a `.int` file.
- A symbol table log (with scopes, variables, and subroutines) is saved to a `.sym` file.

5️⃣ Final Code Generation:**  
**final code generation** targeting the **RISC-V architecture**. This stage will translate the intermediate quadruples into executable RISC-V assembly code, ready for simulation or deployment.

<br><br>
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
<br><br><br>

---

## 🔤 Lexical Analysis

The lexical analysis phase is implemented via a **deterministic finite automaton (DFA)** that scans the source code character-by-character and generates tokens. This process is **crucial** because it transforms the raw code into a structured stream of tokens, which are then consumed by the parser.

### 🧩 Key Classes & Their Roles

- **`CharStream`:**  
  Responsible for reading characters from the source file. It keeps track of the current position, line, and column, offering methods like:
    - `peekNextChar()`: Peek at the next character.
    - `consumeNextChar()`: Consume the current character and advance.
    - Line & column tracking for precise error reporting.

- **`Lexer`:**  
  The core of lexical analysis. It:
    - Utilizes a DFA (`DFAState` enum) to transition between states based on the next character's type (`CharacterType`).
    - Detects token boundaries and creates a new `Token` object once a final DFA state is reached.
    - Skips whitespace and comments automatically using `DFAState.triggersDFARestart()`.
    - Provides methods:
        - `getNextToken()`: Retrieves the next token.
        - `printAllTokens()`: Debug method to print all tokens until EOF.

- **`Token`:**  
  Encapsulates all information about a recognized token:
    - `TokenFamily`: Categorizes the token (e.g., `KEYWORD`, `IDENTIFIER`, `NUMBER`, `ADD_OPERATOR`, etc.).
    - Holds metadata like:
        - Start and end indices in the file.
        - Line and column of occurrence.
        - Recognized string value (actual text).

- **`LexerErrors`:**  
  Provides detailed error reporting when an invalid character sequence is encountered (e.g., illegal symbols, misplaced brackets, invalid transitions).

- **`DFAState` & `CharacterType`:**
    - `DFAState`: Represents the states of the automaton, including:
        - Start/final/error states.
        - Transitions such as `IDENTIFIER_OR_KEYWORD_FINAL`, `DIGIT_FINAL`, etc.
    - `CharacterType`: Categorizes characters (e.g., `LETTER`, `DIGIT`, `ADD`, `SUBTRACT`, `GROUP_SYMBOL`, etc.).

<br><br>
────────────────────────────


### 🧮 Enums & DFA Logic

The lexical analysis engine leverages a **highly modular design using enums** to implement the finite automaton (DFA) in a **concise and performant way**.

#### 🗂️ `DFAState`

This enum represents **all possible states of the DFA** during lexical analysis. It includes:

- **Scanning states:** e.g., `DIGIT`, `IDENTIFIER_OR_KEYWORD`, `ADD_OPERATOR`.
- **Final states:** e.g., `DIGIT_FINAL`, `IDENTIFIER_OR_KEYWORD_FINAL`.
- **Error states:** e.g., `ILLEGAL_CHARACTER_ERROR_STATE`, `LETTER_AFTER_DIGIT_ERROR_STATE`.

Each state:
- Defines if it’s a **final state** (`isFinal()`), meaning the token is complete.
- Specifies if it **triggers a DFA restart** (e.g., whitespace, comments) via `triggersDFARestart()`.
- Can identify if it’s an **error state** (e.g., illegal characters) via `isErrorState()`.

**Transition logic:**
- Uses a static `EnumMap<DFAState, Map<CharacterType, DFAState>>` to store the **transition table**.
- The static block `initializeTransitionTable()` sets up all the transitions between states based on input `CharacterType`.
- **Example:**
    - From `START`, if you get `DIGIT`, you move to `DIGIT`.
    - Stay in `DIGIT` on more digits.
    - If a non-digit is seen, move to `DIGIT_FINAL` (finishing the token).

This is a **high-performance way** to build a DFA in Java, because:
- EnumMap ensures **O(1) lookup speed.**
- The enums themselves are **type-safe and self-documenting.**

<br><br>
────────────────────────────


#### ⚡ Why This Approach Is Highly Efficient

The use of a **transition table implemented via `EnumMap<DFAState, Map<CharacterType, DFAState>>`** provides multiple layers of performance and design benefits:

1️⃣ **O(1) Transition Lookup:**
- `EnumMap` in Java is a **highly optimized implementation** for enums, backed internally by an **array** rather than a hash table.
- This allows **constant-time access** to the next DFA state based on the current state and the character type.
- Result: Even in large files, **every character transition is lightning-fast.**

2️⃣ **Type Safety & Clarity:**
- Using enums (`DFAState` and `CharacterType`) ensures **no magic strings or integers** are used for state or character type representation.
- This makes the code **self-documenting**: anyone reading the table instantly sees meaningful names like `DIGIT`, `ADD_OPERATOR`, etc.

3️⃣ **Memory Efficiency:**
- Because `EnumMap` is based on arrays internally, its **memory footprint is minimal** compared to standard `HashMap`.
- The same applies to the inner `Map<CharacterType, DFAState>` when implemented as an `EnumMap`, keeping everything tight.

4️⃣ **Clear DFA Visualization:**
- The transition table provides a **clean, tabular mapping** between current states and input character types, which reflects the classic way DFA tables are described in theory.
- This makes it **easy to verify correctness** and spot errors or missing transitions.

5️⃣ **Easy Extensibility:**
- Need to add a new operator or token type?  
  → Simply **extend the enum** and add a new entry in the transition table without touching core logic.
- This follows the **Open/Closed Principle** in design.

6️⃣ **Built-in Error Handling:**
- Error states like `LETTER_AFTER_DIGIT_ERROR_STATE` are part of the DFA, making **invalid sequences detectable within the same fast lookup**—no need for extra validation passes.

<br>

🛠️ **In summary:**  
By combining **enums** with **EnumMap-based transition tables**, the lexer achieves:
- **Fast execution (O(1) per char).**
- **Minimal memory overhead.**
- **Maximum readability and maintainability.**

This is arguably the **most optimal way to build a DFA in Java** for a lexer, blending both **theoretical clarity** and **practical performance.**

<br><br>
────────────────────────────


#### 🔤 `CharacterType`

Categorizes **every possible character** into a type:
- Examples:
    - `LETTER`, `DIGIT`, `ADD`, `SUBTRACT`
    - `PARENTHESIS`, `GROUP_SYMBOL`, `CURLY_BRACKET_OPEN`
    - `EOF`, `ILLEGAL_CHARACTER`

The static method `CharacterType.of(char c)` **maps each character** from the source file to the correct `CharacterType`.  
This handles:
- Latin & Greek alphabets.
- Symbols, whitespace, comments.
- Special cases: `EOF` (end-of-file), `ILLEGAL_CHARACTER`.


<br><br>
────────────────────────────


#### 🏷️ `TokenFamily`

Defines **high-level categories** for tokens (what kind of token each DFA final state produces):

- `KEYWORD`
- `IDENTIFIER`
- `NUMBER`
- `ADD_OPERATOR`
- `REL_OPERATOR`
- `GROUP_SYMBOL`
- `REFERENCE_OPERATOR`
- `EOF`
- And more.

The static method `TokenFamily.fromDFAState(...)` **maps a final DFA state** to its corresponding `TokenFamily`, e.g.:
- `DIGIT_FINAL` → `NUMBER`
- `IDENTIFIER_OR_KEYWORD_FINAL` → (checks if it's a reserved word or an identifier)
- `ADD_OPERATOR_FINAL` → `ADD_OPERATOR`


<br><br>
────────────────────────────


### 🔄 Lexical Flow

1️⃣ **Initialization:**
- The `GreekPP` main class creates a `Lexer` instance, initializing it with a `CharStream` that reads the provided file.

2️⃣ **Character Scanning:**
- `Lexer.getNextToken()` begins in the `DFAState.START` state.
- Each character is classified via `CharacterType.of(char c)` and the DFA determines the next state.

3️⃣ **State Handling:**
- Whitespace and comments are **skipped** without producing tokens.
- When a **final state** is reached (e.g., recognizing an identifier or number), the `Lexer` constructs a `Token`.

4️⃣ **Error Handling:**
- If an illegal transition is made (e.g., a digit followed by a letter), `LexerErrors.IllegalStateTransition(...)` is invoked, and the compilation **aborts gracefully** with a detailed error message.

5️⃣ **Token Emission:**
- The produced `Token` is returned to the parser for syntactic analysis.

### ✔️ Highlights

- **Greek alphabet support:** The lexer recognizes both **Latin and Greek letters**, enabling a wide range of identifier names.
- **Comprehensive keyword set:** Tokens include constructs like `πρόγραμμα`, `διάβασε`, `γράψε`, `συνάρτηση`, and more.
- **Robust error reporting:** Every token includes precise line and column information, and the DFA handles edge cases like EOF and illegal characters gracefully.

────────────────────────────

<br><br>

#### 🛠️ Improved Example: Full DFA Walkthrough

<br>

**Example 1:**

Lexer tokenizes the input: `x + 5;`

---

🔑 **Step 1: `x` (Identifier)**

1. **CharStream:** Feeds `'x'`
2. **CharacterType:** `CharacterType.of('x') → LETTER`
3. **DFA transitions:**
    - `START` + `LETTER` → `IDENTIFIER_OR_KEYWORD`
    - Next char: `' '` (space)
        - `IDENTIFIER_OR_KEYWORD` + `WHITESPACE` → triggers `IDENTIFIER_OR_KEYWORD_FINAL`
4. **Token created:**
    - Recognized string: `x`
    - Family: `IDENTIFIER` (checked against `KEYWORDS`)

---

⬜ **Step 2: Whitespace**

1. **CharStream:** Feeds `' '`
2. **CharacterType:** `CharacterType.of(' ') → WHITESPACE`
3. **DFA transitions:**
    - `START` + `WHITESPACE` → `WHITESPACE`
    - Keeps consuming spaces if more exist
    - Next char is `'+'`:
        - `WHITESPACE` + `ADD` → triggers `WHITESPACE_FINAL`
4. **Action:** DFA **restarts silently** (no token is produced)

---

➕ **Step 3: `+` (Add Operator)**

1. **CharStream:** Feeds `'+'`
2. **CharacterType:** `CharacterType.of('+') → ADD`
3. **DFA transitions:**
    - `START` + `ADD` → `ADD_OPERATOR`
    - Next char: `' '` (space)
        - `ADD_OPERATOR` + `WHITESPACE` → triggers `ADD_OPERATOR_FINAL`
4. **Token created:**
    - Recognized string: `+`
    - Family: `ADD_OPERATOR`

---

⬜ **Step 4: Whitespace**

_(Same as Step 2: whitespace is skipped and DFA restarts silently)_

---

🔢 **Step 5: `5` (Number)**

1. **CharStream:** Feeds `'5'`
2. **CharacterType:** `CharacterType.of('5') → DIGIT`
3. **DFA transitions:**
    - `START` + `DIGIT` → `DIGIT`
    - Next char: `';'`
        - `DIGIT` + `SEMICOLON` → triggers `DIGIT_FINAL`
4. **Token created:**
    - Recognized string: `5`
    - Family: `NUMBER`

---

✴️ **Step 6: `;` (Delimiter)**

1. **CharStream:** Feeds `';'`
2. **CharacterType:** `CharacterType.of(';') → SEMICOLON`
3. **DFA transitions:**
    - `START` + `SEMICOLON` → `DELIMITER`
    - Next char: EOF
        - `DELIMITER` + `EOF` → triggers `DELIMITER_FINAL`
4. **Token created:**
    - Recognized string: `;`
    - Family: `DELIMITER`

---

🏁 **Step 7: EOF**

1. **CharStream:** Reaches EOF
2. **CharacterType:** `CharacterType.of(EOF) → EOF`
3. **DFA transitions:**
    - Any state + `EOF` → moves to `EOF_FINAL`
4. **Token created:** Token: `EOF`

---

✅ **Full Token Stream:**

| Token  | String | Family        |
|--------|--------|---------------|
| `x`    | x      | IDENTIFIER    |
| `+`    | +      | ADD_OPERATOR  |
| `5`    | 5      | NUMBER        |
| `;`    | ;      | DELIMITER     |
| `EOF`  | -      | EOF           |

<br><br>

**Example 2:**

Lexer tokenizes the input: `x := max(a, b)`

<br>

🆔 **Step 1: `x` (Identifier)**

1. **CharStream:** Feeds `'x'`
2. **CharacterType:** `CharacterType.of('x') → LETTER`
3. **DFA transitions:**
    - `START` + `LETTER` → `IDENTIFIER_OR_KEYWORD`
    - Next char: space
        - `IDENTIFIER_OR_KEYWORD` + `WHITESPACE` → triggers `IDENTIFIER_OR_KEYWORD_FINAL`
4. **Token created:**
    - Recognized string: `x`
    - Family: `IDENTIFIER`

---

⚙️ **Step 2: `:=` (Assignment Operator)**

1. **CharStream:** Feeds `':'`
2. **CharacterType:** `CharacterType.of(':') → GROUP_SYMBOL`
3. **DFA transitions:**
    - `START` + `GROUP_SYMBOL` → `GROUP_SYMBOL`
    - Next char: `'='`
        - `GROUP_SYMBOL` + `EQUAL` → triggers `ASSIGNMENT_OPERATOR_FINAL`
4. **Token created:**
    - Recognized string: `:=`
    - Family: `ASSIGNMENT_OPERATOR`

---

🆔 **Step 3: `max` (Identifier / Function Name)**

1. **CharStream:** Feeds `'m'`
2. **CharacterType:** `CharacterType.of('m') → LETTER`
3. **DFA transitions:**
    - `START` + `LETTER` → `IDENTIFIER_OR_KEYWORD`
    - Next char: `'a'`
        - `IDENTIFIER_OR_KEYWORD` + `LETTER` → stays in `IDENTIFIER_OR_KEYWORD`
    - Next char: `'x'`
        - `IDENTIFIER_OR_KEYWORD` + `LETTER` → stays in `IDENTIFIER_OR_KEYWORD`
    - Next char: `'('`
        - `IDENTIFIER_OR_KEYWORD` + `PARENTHESIS` → triggers `IDENTIFIER_OR_KEYWORD_FINAL`
4. **Token created:**
    - Recognized string: `max`
    - Family: `IDENTIFIER` (recognized as a function at parsing stage)

---

*(Whitespace between `:=` and `max` skipped silently)*

---

🟢 **Step 4: `(` (Left Parenthesis)**

1. **CharStream:** Feeds `'('`
2. **CharacterType:** `CharacterType.of('(') → PARENTHESIS`
3. **DFA transitions:**
    - `START` + `PARENTHESIS` → `GROUP_SYMBOL`
    - Next char: `'a'`
        - triggers `GROUP_SYMBOL_FINAL`
4. **Token created:**
    - Recognized string: `(`
    - Family: `GROUP_SYMBOL`

---

🆔 **Step 5: `a` (Identifier)**

1. **CharStream:** Feeds `'a'`
2. **CharacterType:** `CharacterType.of('a') → LETTER`
3. **DFA transitions:**
    - `START` + `LETTER` → `IDENTIFIER_OR_KEYWORD`
    - Next char: `','`
        - `IDENTIFIER_OR_KEYWORD` + `COMMA` → triggers `IDENTIFIER_OR_KEYWORD_FINAL`
4. **Token created:**
    - Recognized string: `a`
    - Family: `IDENTIFIER`

---

✴️ **Step 6: `,` (Comma Delimiter)**

1. **CharStream:** Feeds `','`
2. **CharacterType:** `CharacterType.of(',') → COMMA`
3. **DFA transitions:**
    - `START` + `COMMA` → `DELIMITER`
    - Next char: `'b'`
        - `DELIMITER` + `LETTER` → triggers `DELIMITER_FINAL`
4. **Token created:**
    - Recognized string: `,`
    - Family: `DELIMITER`

---

🆔 **Step 7: `b` (Identifier)**

1. **CharStream:** Feeds `'b'`
2. **CharacterType:** `CharacterType.of('b') → LETTER`
3. **DFA transitions:**
    - `START` + `LETTER` → `IDENTIFIER_OR_KEYWORD`
    - Next char: `')'`
        - `IDENTIFIER_OR_KEYWORD` + `PARENTHESIS` → triggers `IDENTIFIER_OR_KEYWORD_FINAL`
4. **Token created:**
    - Recognized string: `b`
    - Family: `IDENTIFIER`

---

🟢 **Step 8: `)` (Right Parenthesis)**

1. **CharStream:** Feeds `')'`
2. **CharacterType:** `CharacterType.of(')') → PARENTHESIS`
3. **DFA transitions:**
    - `START` + `PARENTHESIS` → `GROUP_SYMBOL`
    - Next char: EOF
        - `GROUP_SYMBOL` + `EOF` → triggers `GROUP_SYMBOL_FINAL`
4. **Token created:**
    - Recognized string: `)`
    - Family: `GROUP_SYMBOL`

---

🏁 **Step 9: EOF**

1. **CharStream:** Reaches EOF
2. **CharacterType:** `CharacterType.of(EOF) → EOF`
3. **DFA transitions:**
    - Any state + `EOF` → moves to `EOF_FINAL`
4. **Token created:** Token: `EOF`

---

✅ **Full Token Stream:**

| Token               | String   | Family               |
|---------------------|----------|----------------------|
| `x`                 | x        | IDENTIFIER           |
| `:=`                | :=       | ASSIGNMENT_OPERATOR  |
| `max`               | max      | IDENTIFIER (Function)|
| `(`                 | (        | GROUP_SYMBOL         |
| `a`                 | a        | IDENTIFIER           |
| `,`                 | ,        | DELIMITER            |
| `b`                 | b        | IDENTIFIER           |
| `)`                 | )        | GROUP_SYMBOL         |
| `EOF`               | -        | EOF                  |



## 🚫 **Illegal DFA Transitions & Error Handling**

In `GreekPP`, the DFA is carefully designed to handle **all valid transitions**, but also to **trap illegal ones** proactively.

### 🔍 **How Illegal Transitions Work:**

- Each `DFAState` has a **map of transitions** (implemented with `EnumMap`) that links:

  ```
  Map<DFAState, Map<CharacterType, DFAState>>
  ```

- **Valid transitions:** For every character type (e.g., `DIGIT`, `LETTER`), if a legal next state exists, it's mapped.

- **Illegal transitions:** For every state (except specific ones like `COMMENT`), the table **explicitly maps** any illegal character type to an **error state**.

For example:

- If you're in `DIGIT` and get a `LETTER` (like `12a`), the transition goes to:

  ```
  LETTER_AFTER_DIGIT_ERROR_STATE
  ```

- Unexpected symbols like a stray `}` (outside of comments) hit:

  ```
  CLOSING_BRACKET_OUTSIDE_COMMENT_ERROR_STATE
  ```

- Completely unrecognized characters hit:

  ```
  ILLEGAL_CHARACTER_ERROR_STATE
  ```

### ⚠️ **Error Reporting Logic:**

When such an error state is entered:

1. The `LexerErrors.IllegalStateTransition()` method is called.
2. The message includes:
    - **Line & column info**
    - **Character causing the error**
    - A **specific explanation**, e.g.,
      `"A digit cannot be followed directly from a letter."`
3. Compilation **aborts immediately.**

This design is:

- 🔥 **Fast:** thanks to `EnumMap`, all lookups are O(1).
- 🛡️ **Robust:** every illegal situation is **explicitly covered**.
- 🧾 **Descriptive:** errors are **granular**, not generic.

---

## ❌ **Example: Tokenizing `2 - 12α` (Illegal Input)**

This code **triggers an error** because of the invalid token `12α` (digit followed by letter).

---

### 🔢 **Step 1: `2` (Number)**

1. **CharStream:** Feeds `'2'`
2. **CharacterType:** `CharacterType.of('2') → DIGIT`
3. **DFA transitions:**
    - `START` + `DIGIT` → `DIGIT`
    - Next char: `' '` (space)
        - `DIGIT` + `WHITESPACE` → `DIGIT_FINAL`
4. **Token created:**
    - Recognized string: `2`
    - Family: `NUMBER`

---

⬜ **Step 2: Whitespace**

1. **CharStream:** Feeds `' '`
2. **CharacterType:** `CharacterType.of(' ') → WHITESPACE`
3. **DFA transitions:**
    - `START` + `WHITESPACE` → `WHITESPACE`
    - Next char: `'-'` → `WHITESPACE_FINAL`
4. **Action:** DFA **restarts silently.**

---

➖ **Step 3: `-` (Minus Operator)**

1. **CharStream:** Feeds `'-'`
2. **CharacterType:** `CharacterType.of('-') → MULL_OR_MINUS`
3. **DFA transitions:**
    - `START` + `MULL_OR_MINUS` → `MULL_OPERATOR`
    - Next char: `' '` → `MULL_OPERATOR_FINAL`
4. **Token created:**
    - Recognized string: `-`
    - Family: `MULL_OPERATOR`

---

⬜ **Step 4: Whitespace**

_(Same as before: skipped silently)_

---

🔢 **Step 5: `1` (Beginning of Number)**

1. **CharStream:** Feeds `'1'`
2. **CharacterType:** `CharacterType.of('1') → DIGIT`
3. **DFA transitions:**
    - `START` + `DIGIT` → `DIGIT`
    - Next char: `'2'`
        - `DIGIT` + `DIGIT` → stays in `DIGIT`
    - **Current token so far: `12`**

---

❌ **Step 6: `α` (Error Occurs)**

1. **CharStream:** Feeds `'α'`
2. **CharacterType:** `CharacterType.of('α') → LETTER`
3. **DFA transitions:**
    - Current state: `DIGIT`
    - Incoming: `LETTER`
    - → **Moves to `LETTER_AFTER_DIGIT_ERROR_STATE`**

---

🚨 **Error Handling:**

The DFA **immediately triggers**:

```
Lexical Error ! || Line : X , Column : Y ||
Character <α> at illegal position.
A digit cannot be followed directly from a letter.
Aborting compilation -
```

_(Line/column depend on where the `α` was in your source code)_

---

✅ **Lexer output up to the error:**

| Token | String | Family                         |
| ----- | ------ | ------------------------------ |
| `2`   | 2      | NUMBER                         |
| `-`   | -      | MULL_OPERATOR                  |
| Error | 12α    | LETTER_AFTER_DIGIT_ERROR_STATE |

---

💎 **Why is this important?**

- The DFA **stops tokenizing immediately when an illegal transition is detected**.
- This design ensures that the lexer is **robust**:
    - It can handle correct code fast,
    - and also **report precise, detailed errors** when needed.





.... to be continued. Read reports for detailed info 
