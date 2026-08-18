# Product Requirements Document (PRD)
## Project Name: Java LLM Calculator (JLC)
## Version: 1.0
## Date: October 26, 2023
## Environment: Linux / WSL (Windows Subsystem for Linux)

---

## 1. Executive Summary
The Java LLM Calculator (JLC) is a smart calculation tool built in Java. Unlike traditional calculators that require strict syntax (e.g., `2+2`), JLC uses a Large Language Model (LLM) to understand natural language input (e.g., "What is 2 plus 2?"). However, to ensure 100% accuracy, the actual mathematical computation is performed by the Java engine, not the LLM.

## 2. Target Audience
- Developers using Linux/WSL environments.
- Users who want to perform calculations using natural language.
- Users who require high accuracy in mathematical results.

## 3. Core Features

### 3.1 Natural Language Input
- The system must accept input in plain English (or configurable languages).
- **Example Inputs:**
  - "Calculate 50 plus 20"
  - "What is 100 divided by 4?"
  - "5 * 5"
  - "I have 10 apples and eat 2, how many left?"

### 3.2 Hybrid Calculation Engine
- **LLM Role:** Parse the user input to extract numbers and operators (Intent Recognition).
- **Java Role:** Perform the actual arithmetic operation using standard Java libraries (`java.math` or `ScriptEngine`).
- **Constraint:** The LLM must **never** output the final calculated number directly; it must output the structured equation for Java to solve.

### 3.3 Automatic Execution
- No need to press an "Equals" button if using CLI.
- If using GUI, calculation triggers automatically upon valid expression detection.

### 3.4 Linux/WSL Compatibility
- The application must run via command line (`java -jar`) on WSL.
- No Windows-specific paths (e.g., use `/home/user` instead of `C:\Users`).
- Dependencies must be manageable via `apt` or included in the build.

## 4. Functional Requirements

| ID | Requirement | Priority |
|----|-------------|----------|
| FR1 | System shall parse natural language text to extract mathematical expressions. | High |
| FR2 | System shall validate extracted expressions before calculation. | High |
| FR3 | System shall handle +, -, *, / operations. | High |
| FR4 | System shall handle division by zero gracefully. | Medium |
| FR5 | System shall return the result in a friendly natural language response. | Medium |
| FR6 | System shall run on Java 11+ within WSL. | High |

## 5. Non-Functional Requirements

| ID | Requirement | Description |
|----|-------------|-------------|
| NFR1 | Accuracy | Mathematical results must be 100% accurate (no LLM hallucination). |
| NFR2 | Latency | Response time should be under 2 seconds (depending on LLM API speed). |
| NFR3 | Security | No sensitive user data should be sent to the LLM API beyond the math query. |
| NFR4 | Portability | Must run on any Linux distro supporting Java. |

## 6. AI Integration Strategy
- **Model:** Lightweight LLM (e.g., Llama-3-8B via Ollama) or API (OpenAI).
- **Prompting:** Strict system instructions to output only JSON or structured math expressions.
- **Fallback:** If LLM fails to parse, default to standard Java regex parsing.

## 7. Success Criteria
- User can type "one hundred plus fifty" and get "150".
- User can type "10 / 0" and get an error message, not a crash.
- Application compiles and runs on WSL without GUI dependency errors (if CLI mode).

## 8. Out of Scope (For V1)
- Complex scientific functions (sin, cos, log) - *Reserved for V2*.
- Graphical User Interface (GUI) - *CLI focused for V1, GUI optional*.
- Multi-language support (English only for V1).

---
