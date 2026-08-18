# 📜 Complete Slash Commands Reference

> **Author & Lead Architect:** KABALI GAMER  
> **Project:** Java LLM Smart Calculator (JLC)

---

## 🧮 Core Math & Scientific Tools

### `/plot <expression>`
Plots a continuous 2D ASCII curve directly in your terminal shell.
- **Example:** `/plot sin(x)`
- **Example:** `/plot x^2 - 4`
- **Example:** `/plot cos(x) * 2`

### `/convert <value> <from_unit> to <to_unit>`
Converts across 10 physical dimensions (Length, Mass, Time, Energy, Pressure, Temp, Power, Frequency, Angles, Data).
- **Example:** `/convert 100 km to miles` $\rightarrow 62.137\text{ miles}$
- **Example:** `/convert 100 C to F` $\rightarrow 212\text{ °F}$
- **Example:** `/convert 532 nm to m` $\rightarrow 5.32 \times 10^{-7}\text{ m}$

### `/solve <equation>`
Solves quadratic equations ($ax^2 + bx + c = 0$) producing real or complex conjugate roots.
- **Example:** `/solve x^2 - 5x + 6 = 0` $\rightarrow x_1 = 3.0, x_2 = 2.0$
- **Example:** `/solve x^2 + 4 = 0` $\rightarrow x_1 = 2i, x_2 = -2i$

### `/diff <polynomial>`
Calculates symbolic polynomial derivatives using power rules.
- **Example:** `/diff 3x^3 + 5x^2 - 4x + 7` $\rightarrow 9x^2 + 10x - 4$

### `/complex <expression>`
Evaluates complex number arithmetic ($a+bi$), computing modulus ($|z|$), phase angle ($\theta$), and polar form.
- **Example:** `/complex (3+4i) * (1-2i)` $\rightarrow 11.0000 - 2.0000i$ | Polar: $11.1803 \angle -10.30°$

### `/matrix`
Opens the interactive Linear Algebra suite for computing determinants ($2\times 2, 3\times 3$), inverses, matrix multiplication, and 3D vector cross products.

### `/base <value>`
Performs radix and numeral conversions between Hexadecimal, Binary, Octal, Decimal, and Roman Numerals.
- **Example:** `/base 0xFF` $\rightarrow 255$ (Decimal)
- **Example:** `/base MMXXVI` $\rightarrow 2026$ (Decimal)
- **Example:** `/base 255` $\rightarrow$ Bin: `11111111`, Hex: `0xFF`, Oct: `377`, Roman: `CCLV`

### `/const` or `/constants`
Displays a formatted catalog of 30+ physical, astronomical, and mathematical constants ($\pi$, $e$, $G$, $c$, $h$, $\hbar$, $\sigma$, $k_B$, $N_A$, $M_\odot$, $M_\oplus$, etc.).

### `/finance`
Opens the financial mathematics calculator for Compound Interest ($A = P(1+r/n)^{nt}$), Loan EMI payments, and SIP Future Value projections.

---

## 📊 Database & Performance Commands

### `/test`
Executes the built-in automated unit test suite across 10 scientific domains and reports pass/fail statistics.

### `/benchmark`
Runs 100,000 non-trivial AST evaluations to benchmark execution throughput (ops/sec) and numerical precision vs native Java Math.

### `/history` or `/h`
Views the recent calculation records stored in the permanent database (`calculation_history.json`).

### `/search <keyword>`
Full-text keyword search across your calculation database.
- **Example:** `/search escape`
- **Example:** `/search 468`

### `/stats`
Displays calculation performance metrics, total queries evaluated, and average latency (ms).

### `/vars`
Displays all session variables currently defined (`ans`, `x`, `y`, etc.).

### `/memory` or `/rules`
Views all active vocabulary rules and few-shot cache entries.

### `/learn <phrase> = <value>`
Teaches the calculator a new persistent vocabulary definition.
- **Example:** `/learn gross = 144`
- **Example:** `/learn grand = 1000`

### `/recall <#id>` or `#<id>`
Recalls the result of a historical calculation into the active variable `ans`.
- **Example:** `/recall #1`

### `/export [filename.csv]`
Exports your complete calculation history into a standard spreadsheet CSV file.

### `/clear` or `/clear-cache`
Clears the in-memory cache and resets learned rules.

### `/clear-history`
Wipes the permanent calculation audit database.

---

## ⚙️ Navigation Commands

| Command | Action |
| :--- | :--- |
| `/switch <1-4>` | Switch between AI Chat [1], Direct Math [2], Memory [3], and DB [4] |
| `/skip` | Immediately interrupt or skip the current calculation prompt |
| `/menu` or `/back` | Return to the main menu |
| `/exit` or `/quit` | Exit the JLC application |
