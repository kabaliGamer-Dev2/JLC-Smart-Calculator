# 🏗️ JLC System Architecture & Technical Design

> **Author & Lead Architect:** KABALI GAMER  
> **Project:** Java LLM Smart Calculator (JLC)  
> **Target Runtime:** Java 17+ (WSL2 / Linux / Cross-Platform)

---

## 1. Overview & Core Architectural Paradigm

The **Java LLM Smart Calculator (JLC)** employs a **Hybrid Dual-Engine Architecture**. It decouples natural language understanding from mathematical evaluation to completely eliminate LLM arithmetic hallucination.

```
+-------------------------------------------------------------------------+
|                          USER INTERFACE LAYER                           |
|       Terminal Shell (Orange/Ash/Black ANSI)  •  Direct CLI Query       |
+------------------------------------+------------------------------------+
                                     |
                                     v
+------------------------------------+------------------------------------+
|                         ROUTING & CACHE LAYER                           |
|         MemoryStore Fast-Cache  •  Slash Command Dispatcher             |
+------------------+----------------------------------+-------------------+
                   | Cache Hit                        | Cache Miss
                   v                                  v
+------------------------------------+   +--------------------------------+
|       INSTANT EXECUTION            |   |     INTENT PARSING LAYER       |
|    Evaluates cached expression     |   |   • Groq API / OpenAI Client   |
|         Latency < 1 ms             |   |   • Local Ollama Client        |
|                                    |   |   • Offline Regex Fallback     |
+------------------+-----------------+   +----------------+---------------+
                   |                                      |
                   |                                      v
                   |                     +--------------------------------+
                   |                     |       SECURITY VALIDATOR       |
                   |                     |  Whitelist Token & AST Checks  |
                   |                     +----------------+---------------+
                   |                                      |
                   +-------------------+------------------+
                                       |
                                       v
+--------------------------------------+----------------------------------+
|                   DETERMINISTIC EVALUATION ENGINES                      |
|  • MathEngine (exp4j + custom AST)     • MatrixEngine (Linear Algebra)  |
|  • FormulaEngine (Physics/Astro/Chem)  • SymbolicEngine (Calculus/Quad) |
|  • UnitConverter (10 SI Dimensions)    • ComplexNumber (Polar/Euler)    |
|  • BaseConverter (Hex/Bin/Oct/Roman)   • AsciiPlotter (2D Curves)       |
+--------------------------------------+----------------------------------+
                                       |
                                       v
+--------------------------------------+----------------------------------+
|                      PRECISION & FORMATTING LAYER                       |
|   • Safe Precision Bounds (No Long Overflow: 9.22e18 Protection)        |
|   • Scientific Micro/Quantum Scale Preservation (e.g. 3.73e-19 J)       |
+--------------------------------------+----------------------------------+
                                       |
                                       v
+--------------------------------------+----------------------------------+
|                     PERSISTENCE & AUDIT DATABASE                        |
|   • HistoryDatabase (calculation_history.json & CSV export)             |
|   • MemoryStore Dynamic Rule Auto-Learner (learned_memory.json)         |
+-------------------------------------------------------------------------+
```

---

## 2. Component Structure & Data Flow

### 2.1 Intent Parsing Layer (`LLMClient.java`, `JsonParser.java`, `FallbackParser.java`)
1. **User Prompt Ingestion:** Takes conversational natural language (e.g., *"What is the gravitational force between Earth and Moon?"*).
2. **Dynamic Context Injection:** Reads session variables, last answer (`ans`), and few-shot rules from `learned_memory.json`.
3. **Structured Schema Output:** The LLM returns strictly compliant JSON:
   ```json
   {
     "intent": "Physics - Gravitational Force",
     "expression": "(6.67430e-11 * 5.9722e24 * 7.34767309e22) / (384400000^2)",
     "confidence": 1.0,
     "error": null
   }
   ```
4. **Fallback Redundancy:** If the LLM call times out (8-second timeout) or network fails, `FallbackParser` extracts mathematical intent via regex without interrupting the user.

### 2.2 Security Validation Layer (`MathEngine.isSafe()`)
- Validates that extracted strings contain only approved arithmetic operators (`+`, `-`, `*`, `/`, `^`, `!`, `%`), numbers, and whitelisted identifiers (`sqrt`, `sin`, `gcd`, `pi`, `G`, etc.).
- Completely prevents Java code injection or unauthorized system calls.

### 2.3 Mathematical Evaluation Layer (`MathEngine.java`)
- Custom AST builder implementing right-associative exponentiation (`^`) with `Math.pow`.
- Registered custom operators and functions:
  - Factorial operator: `!`
  - Euclidean algorithms: `gcd(a, b)`, `lcm(a, b)`
  - Number theory: `isprime(n)`, `fib(n)`
  - Trigonometric & hyperbolic: `sin`, `cos`, `tan`, `asin`, `acos`, `atan`, `sinh`, `cosh`, `tanh`
  - Angular units: `rad(deg)`, `deg(rad)`

### 2.4 Precision Safety Engine
- Handles values spanning from quantum sub-atomic scales ($10^{-34}$) to cosmological scales ($10^{30}$).
- **The Long Overflow Fix:** Prevents `Math.round(result * 1e12)` from overflowing `Long.MAX_VALUE` ($9.223 \times 10^{18}$) by skipping integer truncation for numbers $\ge 10^7$ or $< 10^{-4}$.

---

## 3. Modular Scientific Domain Engines

| Module | Primary Class | Key Capabilities |
| :--- | :--- | :--- |
| **Astrophysics & Physics** | `FormulaEngine.java` | Escape velocity, Schwarzschild radius, photon energy, de Broglie wavelength, orbital period, Stefan-Boltzmann power, mass-energy ($E=mc^2$). |
| **Linear Algebra** | `MatrixEngine.java` | Determinants ($2\times 2, 3\times 3, N\times N$), matrix inverses, multiplication, transpose, $2\times 2$ eigenvalues, 3D vector cross/dot products. |
| **Symbolic Calculus & Algebra** | `SymbolicEngine.java` | Quadratic equation solver ($ax^2+bx+c=0$), 2x2 linear system solver, polynomial symbolic differentiation. |
| **Measurement & Units** | `UnitConverter.java` | 10 physical dimensions (Length, Mass, Time, Energy, Pressure, Temperature, Frequency, Power, Angles, Data). |
| **Complex Analysis** | `ComplexNumber.java` | Complex arithmetic ($a+bi$), modulus ($|z|$), phase angle ($\theta$), and polar Euler representation. |
| **Radix & Numerals** | `BaseConverter.java` | Hexadecimal (`0x...`), Binary (`0b...`), Octal (`0...`), Decimal, and Roman Numerals ($1 \le n \le 3999$). |
| **Terminal Graphics** | `AsciiPlotter.java` | Continuous 2D ASCII function graphing with automatic axes and scaling. |
| **Performance Benchmarking** | `BenchmarkRunner.java` | High-throughput 100,000-iteration performance testing vs `java.lang.Math`. |
| **Automated Testing** | `TestSuite.java` | Built-in test runner validating 10 domains with 100% ground-truth pass rate. |

---

## 4. Storage & Memory Architecture

```
                               ┌───────────────────────────┐
                               │       MEMORY STORE        │
                               │   (learned_memory.json)   │
                               └─────────────┬─────────────┘
                                             │
                       ┌─────────────────────┴─────────────────────┐
                       ▼                                           ▼
          ┌─────────────────────────┐                 ┌─────────────────────────┐
          │     VOCABULARY RULES    │                 │     FEW-SHOT CACHE      │
          │ "dozen" -> 12           │                 │ Exact question ->       │
          │ "baker's dozen" -> 13   │                 │ Verified formula        │
          │ "grand" -> 1000         │                 └─────────────────────────┘
          └─────────────────────────┘
```

1. **`learned_memory.json`**: Stores learned vocabulary rules and validated expressions for instant sub-millisecond replay.
2. **`calculation_history.json`**: Append-only audit database logging every query, extracted formula, result, mode, and latency timestamp.
3. **CSV Exporting**: Serializes calculation records to standard spreadsheet-ready CSV files via `/export`.
