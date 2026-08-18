# Java LLM Smart Calculator (JLC) 🧮🤖⚡

<div align="center">

[![Java Version](https://img.shields.io/badge/Java-17%2B-orange.svg?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square)](LICENSE)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg?style=flat-square)]()
[![Tests](https://img.shields.io/badge/Tests-23%2F23%20Passed%20(100%25)-success.svg?style=flat-square)]()
[![Author](https://img.shields.io/badge/Author-KABALI%20GAMER-gold.svg?style=flat-square)]()

**A High-Performance Hybrid Computing Platform & Mathematical Intelligence Suite**  
*Decoupling Natural Language Intent from Deterministic High-Precision Calculation*

</div>

---

## 👑 Author & Lead Architect
**KABALI GAMER**  
*Vision, System Architecture, Scientific Engineering & Verification Suite*

---

## 🌟 What is JLC?

**Java LLM Smart Calculator (JLC)** is a hybrid computing engine that bridges Large Language Models (LLMs) with high-precision Java execution engines. Instead of relying on LLMs to perform unreliable mental math, JLC uses the AI strictly as an **Intent Parser** to translate natural language word problems into rigorous mathematical expressions. The compiled Java Engine then executes the calculation deterministically with **zero hallucination and bit-for-bit precision**.

```text
==========================================================================================
       _   ___     ___     _    _      __  __   ___ __  __   _   ___ _____ 
    _ | | /   \   /   \   | |  | |    |  \/  | / __|  \/  | /_\ | _ \_   _|
   | || |/ /\  \ / /\  \  | |__| |__  | |\/| | \__ \ |\/| |/ _ \|   / | |  
    \__/|_/  \__\_/  \__\ |____|____|_|_|  |_|_|___/_|  |_/_/ \_\_|_\ |_|  
          ___   _   _    ___ _   _ _      _ _____ ___  ___                 
         / __| /_\ | |  / __| | | | |    /_\_   _/ _ \| _ \                
        | (__ / _ \| |_| (__| |_| | |__ / _ \| || (_) |   /                
         \___/_/ \_\____\___|\___/|____/_/ \_\_| \___/|_|_\                
             🧮 Smart Natural Language & Direct Precision Math Engine 🤖
             👑 Author: KABALI GAMER | Version 1.0.0-PRO
             Tip: Type /help anytime to see slash commands (/history, /switch, /stats)
==========================================================================================
```

---

## 🚀 30 Key Super-Capabilities

1. **Precision Power Operator (`^`)**: Pure mathematical power evaluation (via right-associative exponentiation and `Math.pow`) replacing Java's bitwise XOR.
2. **Scientific SI Notation**: Native parsing of numbers like `6.67430×10^-11`, `5.9722×10^24`, and `2.91×10^-10`.
3. **Zero Precision Loss**: Micro and quantum scales (e.g. $3.73 \times 10^{-19}\text{ J}$) and astronomical scales ($1.8 \times 10^{14}\text{ J}$) retain full IEEE 754 double precision without overflow or zeroing.
4. **Formula Engine**: Automated physics, astrophysics, thermodynamics, and chemistry solvers (`FormulaEngine.java`).
5. **Unit Recognition & Conversions**: Convert across 10 categories (length, mass, time, energy, pressure, temperature, frequency, power, angles, data).
6. **Advanced Functions**: `sqrt()`, `cbrt()`, `abs()`, `log10()`, `ln()`, `exp()`, `sin()`, `cos()`, `tan()`, `asin()`, `acos()`, `atan()`, `sinh()`, `cosh()`, `tanh()`, `floor()`, `ceil()`, `round()`, `gcd()`, `lcm()`, `fib()`, `isprime()`, and factorials (`!`).
7. **Universal Scientific Constants**: 30+ cataloged physical, astronomical, and mathematical constants ($\pi$, $e$, $G$, $c$, $h$, $\hbar$, $\varepsilon_0$, $\mu_0$, $N_A$, $k_B$, $M_\odot$, $M_\oplus$, $R_\oplus$, etc.).
8. **Natural Language Understanding**: Complex conversational word problems without requiring math syntax.
9. **Step-by-Step Resolution**: Formula explanations with equation display, variable substitution, and intermediate calculation steps.
10. **Error Diagnostics**: Clear, actionable error messages for syntax mistakes, undefined logarithms, and divide-by-zero.
11. **Dynamic Memory & Learning**: Auto-learning vocabulary rules (`/learn grand = 1000`) and few-shot caching (`learned_memory.json`).
12. **Permanent Calculation Database**: File-based persistent audit history (`calculation_history.json`), full-text search, CSV export, and recall by ID (`#<id>`).
13. **AI Confidence Scoring**: Validates confidence before expression execution.
14. **Matrix & Vector Algebra**: Determinants ($2\times 2, 3\times 3$), inverses, multiplication, transpose, eigenvalues, and 3D vector dot and cross products (`MatrixEngine.java`).
15. **Symbolic Mathematics**: Quadratic equation solver ($ax^2+bx+c=0$), 2x2 system of linear equations solver, polynomial differentiation (`SymbolicEngine.java`).
16. **Numerical Stability**: Safe precision arithmetic preventing `Long.MAX_VALUE` overflows.
17. **ASCII Graph Plotter**: 2D terminal function graphing for functions like $y = \sin(x)$, $y = x^2 - 4$, and $y = \cos(x)$ (`AsciiPlotter.java`).
18. **Radix & Base Conversions**: Instant conversion between Hexadecimal, Binary, Octal, Decimal, and Roman Numerals (`BaseConverter.java`).
19. **Complex Numbers**: Full complex arithmetic ($a+bi$), polar coordinates ($r \angle \theta$), magnitude, and phase (`ComplexNumber.java`).
20. **Financial Engine**: Compound Interest ($A = P(1+r/n)^{nt}$), Loan EMI calculator, SIP future value, CAGR, and ROI.
21. **Engineering Calculators**: Electrical and mechanical formulas (Ohm's law, kinetic energy, potential energy).
22. **Chemistry Calculators**: Molarity, pH ($-\log_{10}[H^+]$), and Ideal Gas Law ($P = nRT/V$).
23. **Astrophysics Calculators**: Escape velocity ($v_e = \sqrt{2GM/R}$), Schwarzschild radius ($r_s = 2GM/c^2$), orbital period ($T = 2\pi\sqrt{r^3/GM}$), Stefan-Boltzmann radiated power ($P = 4\pi R^2\sigma T^4$).
24. **Caching Validation**: Verified expression storage to ensure only 100% correct evaluations are cached.
25. **Modular Architecture**: Independent, decoupled domain engines (`MathEngine`, `FormulaEngine`, `MatrixEngine`, `SymbolicEngine`, `UnitConverter`, `BaseConverter`, `ComplexNumber`, `AsciiPlotter`, `BenchmarkRunner`, `TestSuite`).
26. **Vibrant Terminal UI**: Orange, Ash & Black color scheme with ASCII banners and interactive mode selection.
27. **Benchmark Runner (`/benchmark`)**: Compares JLC evaluation throughput against standard Java Math across 100,000 operations.
28. **Detailed Logging**: Calculation duration metrics, latency analytics, and mode tracking.
29. **100% Offline Knowledge Pack**: Universal constants, formulas, and units operate without requiring internet access.
30. **Automated Unit Test Suite (`/test`)**: Comprehensive test suite covering 10 scientific domains with 100% pass guarantee.

---

## 🛠️ Build & Installation

```bash
# Clone the repository
git clone https://github.com/kabali-gamer/java-llm-calculator.git
cd java-llm-calculator

# Build standalone shaded JAR with Maven
mvn clean package -DskipTests
```

---

## ⚙️ Configuration (`.env`)

Copy the template to create your `.env` file:
```bash
cp .env.example .env
```

Set your preferred LLM provider in `.env`:
```properties
LLM_MODE=GROQ
GROQ_API_KEY=your_groq_api_key_here
MODEL_NAME=openai/gpt-oss-120b
```

---

## 🚀 Running JLC

### 1. Interactive Multi-Mode Shell
```bash
java -jar target/jlc-1.0.jar
```

### 2. Headless Direct CLI Queries
```bash
# Physics: Escape Velocity
java -jar target/jlc-1.0.jar "Using G = 6.67430×10^-11, M = 5.9722×10^24 kg, and R = 6.371×10^6 m, calculate Earth's escape velocity in km/s"

# Quantum Physics: Photon Energy
java -jar target/jlc-1.0.jar "Using Planck's constant h = 6.62607015×10^-34 J·s and the speed of light c = 299792458 m/s, calculate the energy of a photon with wavelength 532 nm."

# Discrete Math: GCD & LCM
java -jar target/jlc-1.0.jar "Find the GCD and LCM of 123456 and 789012."
```

### 3. Direct CLI Slash Commands
```bash
# Automated Unit Test Suite
java -jar target/jlc-1.0.jar "/test"

# Performance Benchmark (100k iterations)
java -jar target/jlc-1.0.jar "/benchmark"

# 2D ASCII Graph Plotter
java -jar target/jlc-1.0.jar "/plot sin(x)"

# Unit Conversion
java -jar target/jlc-1.0.jar "/convert 100 km to miles"

# Quadratic Equation Solver
java -jar target/jlc-1.0.jar "/solve x^2 - 5x + 6 = 0"

# Radix / Base Conversion
java -jar target/jlc-1.0.jar "/base 0xFF"

# Polynomial Derivative
java -jar target/jlc-1.0.jar "/diff 3x^3 + 5x^2 - 4x + 7"

# Inspect Universal Constants
java -jar target/jlc-1.0.jar "/const"
```

---

## 📜 Complete Slash Command Reference

| Command | Action | Example |
| :--- | :--- | :--- |
| `/test` | Run 10-domain automated unit test suite | `/test` |
| `/benchmark` | Benchmark JLC engine vs standard Java Math | `/benchmark` |
| `/plot <expr>` | Plot ASCII 2D function graph | `/plot sin(x)` |
| `/convert <val> <from> to <to>` | Convert between compatible units | `/convert 5 km to miles` |
| `/solve <equation>` | Solve quadratic equations | `/solve x^2 - 5x + 6 = 0` |
| `/diff <poly>` | Symbolic polynomial derivative | `/diff 3x^3 + 5x^2 - 4x` |
| `/complex <expr>` | Complex arithmetic and polar conversion | `/complex (3+4i) * (1-2i)` |
| `/matrix` | Matrix determinants, inverses, cross products | `/matrix` |
| `/base <val>` | Radix & Roman numeral conversions | `/base 255` or `/base MMXXVI` |
| `/const` | Display 30+ physical & universal constants | `/const` |
| `/finance` | Compound interest, EMI, and SIP calculators | `/finance` |
| `/history` or `/h` | View permanent calculation database | `/history` |
| `/search <query>` | Search past calculations by keyword | `/search escape` |
| `/stats` | View performance and usage analytics | `/stats` |
| `/export` | Export calculation history to CSV | `/export` |
| `/clear` | Clear memory cache & reset rules | `/clear` |
| `/clear-history` | Clear calculation database | `/clear-history` |
| `/switch <1-4>` | Switch active interaction mode | `/switch 1` |

---

## 📚 In-Depth Documentation

For complete specifications, architectural diagrams, and engineering logs, explore the [`docs/`](./docs) directory:
- [**System Architecture & Design**](./docs/ARCHITECTURE.md)
- [**30 Feature Deep-Dive & Domain Solvers**](./docs/FEATURES.md)
- [**Testing, Verification & Performance Benchmarks**](./docs/TESTING_AND_BENCHMARKS.md)
- [**User Guide & Command Manual**](./docs/USER_GUIDE.md)
- [**Development Chronology & Engineering Log**](./docs/CHANGELOG.md)
- [**Master Architectural Blueprint**](./BLUEPRINT.md)

---

## 📄 License
Released under the **MIT License**. Created with ❤️ by **KABALI GAMER**.
