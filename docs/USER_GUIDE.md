# 📖 JLC User Guide & Command Manual

> **Author & Lead Architect:** KABALI GAMER  
> **Project:** Java LLM Smart Calculator (JLC)

---

## 1. Quick Start

### 1.1 Launch Interactive Shell
```bash
java -jar target/jlc-1.0.jar
```

### 1.2 Direct Single-Query Execution
```bash
# Natural Language Query
java -jar target/jlc-1.0.jar "What is the escape velocity of Earth in km/s?"

# Direct Math Evaluation
java -jar target/jlc-1.0.jar --direct "sqrt(144) + 5! - (3^4) + log10(1000)"

# Direct CLI Slash Commands
java -jar target/jlc-1.0.jar "/plot sin(x)"
java -jar target/jlc-1.0.jar "/convert 100 km to miles"
java -jar target/jlc-1.0.jar "/solve x^2 - 5x + 6 = 0"
java -jar target/jlc-1.0.jar "/test"
```

---

## 2. Interactive Modes

### [1] 💬 AI Chat Mode
- Accepts natural language math word problems and conversational queries.
- Multi-turn context awareness:
  ```text
  [AI Chat] > You: What is 25 times 4?
  [AI Chat] > AI: Expression: 25 * 4 | Result: 100
  [AI Chat] > You: Now add 50 to that
  [AI Chat] > AI: Expression: 100 + 50 | Result: 150
  ```

### [2] 🔢 Direct Math Mode
- Immediate numerical evaluation bypassing LLMs for maximum speed.
- Supports variables and previous result access (`ans`):
  ```text
  [Direct Math] > x = 50
  [Direct Math] > Variable 'x' set to: 50
  [Direct Math] > y = 20
  [Direct Math] > Variable 'y' set to: 20
  [Direct Math] > x * y + 10
  [Direct Math] > Expression: 50 * 20 + 10 | Result: 1010
  ```

### [3] 🧠 Learned Memory Store
- View auto-learned vocabulary pairs and cache expressions.
- Teach new custom vocabulary rules:
  ```text
  /learn score = 20
  /learn gross = 144
  /learn grand = 1000
  ```

### [4] 📜 History Database
- Permanent audit log of all calculations.
- Search records by keyword (`/search <query>`), view stats (`/stats`), export to CSV (`/export`), and recall results by ID (`/recall #3` or `#3`).

---

## 3. Complete Slash Command Reference

| Slash Command | Action | Example |
| :--- | :--- | :--- |
| `/test` | Run 10-domain automated unit test suite | `/test` |
| `/benchmark` | Run 100,000-iteration performance benchmark | `/benchmark` |
| `/plot <expr>` | Renders 2D ASCII function plot | `/plot sin(x)` |
| `/convert <val> <from> to <to>` | Converts measurement units | `/convert 100 km to miles` |
| `/solve <equation>` | Solves quadratic equation roots | `/solve x^2 - 5x + 6 = 0` |
| `/diff <poly>` | Symbolic polynomial differentiation | `/diff 3x^3 + 5x^2 - 4x` |
| `/complex <expr>` | Complex arithmetic & polar forms | `/complex (3+4i) * (1-2i)` |
| `/matrix` | Interactive matrix determinants & cross products | `/matrix` |
| `/base <val>` | Radix & Roman numeral conversions | `/base 0xFF` or `/base MMXXVI` |
| `/const` | Displays 30+ physical & astronomical constants | `/const` |
| `/finance` | Interactive Compound Interest, EMI, & SIP calculator | `/finance` |
| `/history` or `/h` | View permanent calculation database | `/history` |
| `/search <query>` | Full-text search across calculation database | `/search escape` |
| `/stats` | View performance & mode analytics | `/stats` |
| `/vars` | Inspect session variables | `/vars` |
| `/memory` | View learned vocabulary rules & cache | `/memory` |
| `/recall <#id>` | Recall past result into variable `ans` | `/recall #1` or `#1` |
| `/learn <rule>` | Teach new vocabulary rule | `/learn gross = 144` |
| `/export [file]` | Export database to CSV file | `/export` |
| `/clear` | Clear memory cache & vocabulary rules | `/clear` |
| `/clear-history` | Wipes calculation audit history database | `/clear-history` |
| `/switch <1-4>` | Switch active interaction mode | `/switch 1` |
| `/skip` | Skip current calculation prompt | `/skip` |
| `/menu` or `/back` | Return to main menu | `/menu` |
| `/exit` or `/quit` | Exit the application | `/exit` |
