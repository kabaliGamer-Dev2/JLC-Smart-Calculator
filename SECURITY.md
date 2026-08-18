# 🛡️ Security Policy & Architecture Specification

> **Author & Lead Architect:** KABALI GAMER  
> **Project:** Java LLM Smart Calculator (JLC)  
> **Version:** 1.0.0-PRO  
> **Last Updated:** August 2026

---

## 📑 Table of Contents
1. [Supported Versions](#1-supported-versions)
2. [Reporting Security Vulnerabilities](#2-reporting-security-vulnerabilities)
3. [JLC Security Model & Threat Mitigation](#3-jlc-security-model--threat-mitigation)
   - [3.1 AST Sandbox & Code Injection Defense](#31-ast-sandbox--code-injection-defense)
   - [3.2 Deterministic Execution & Hallucination Elimination](#32-deterministic-execution--hallucination-elimination)
   - [3.3 Credential & API Key Protection](#33-credential--api-key-protection)
   - [3.4 Data Privacy & Offline Isolation](#34-data-privacy--offline-isolation)
   - [3.5 Resource Exhaustion & Denial of Service (DoS) Mitigation](#35-resource-exhaustion--denial-of-service-dos-mitigation)
   - [3.6 Numerical Stability & Overflow Protection](#36-numerical-stability--overflow-protection)
4. [Security Best Practices for Deployments](#4-security-best-practices-for-deployments)

---

## 1. Supported Versions

We provide security updates, vulnerability patches, and integrity checks for the following versions:

| Version | Supported | Status |
| :--- | :---: | :--- |
| **1.0.x-PRO** | ✅ Yes | Current Stable Production Release |
| `< 1.0.0` | ❌ No | Deprecated Legacy Snapshots |

---

## 2. Reporting Security Vulnerabilities

The security and mathematical integrity of the **Java LLM Smart Calculator (JLC)** are of paramount importance. If you discover a security vulnerability, prompt injection vector, or sandbox escape, please report it responsibly:

1. **Email Disclosure:** Reach out to the maintainer at **`kabaliGamer-Dev2@users.noreply.github.com`**.
2. **GitHub Security Advisories:** You can also open a private advisory under the [Security Advisories](https://github.com/kabaliGamer-Dev2/JLC-Smart-Calculator/security/advisories) tab on GitHub.
3. **Response Timeline:**
   - **Initial Acknowledgement:** Within **24 hours**.
   - **Vulnerability Assessment & Triage:** Within **48 hours**.
   - **Patch Release & Security Advisory:** Within **5 business days**.

> ⚠️ **Please DO NOT open public GitHub issues for unresolved security vulnerabilities or zero-day exploits.**

---

## 3. JLC Security Model & Threat Mitigation

```
+-------------------------------------------------------------------------+
|                         UNTRUSTED USER INPUT                            |
+------------------------------------+------------------------------------+
                                     |
                                     v
+------------------------------------+------------------------------------+
|                         LLM INTENT PARSER                               |
|        Translates human query into structured mathematical expression   |
+------------------------------------+------------------------------------+
                                     |
                                     v
+------------------------------------+------------------------------------+
|                      🛡️ SECURITY VALIDATOR                              |
|   • Strict Regex Character Whitelist: [a-zA-Z0-9+\-*/^!%().,\s°eE]+     |
|   • Identifier Whitelist Check (Only registered math/physics tokens)    |
|   • Rejects System, Runtime, ProcessBuilder, Reflection, File calls     |
+------------------+----------------------------------+-------------------+
                   | BLOCKED                          | APPROVED
                   v                                  v
+------------------------------------+   +--------------------------------+
|         ACCESS DENIED              |   |     DETERMINISTIC EVALUATION   |
|   Throws IllegalArgumentException  |   |    exp4j Sandboxed AST Engine  |
+------------------------------------+   +--------------------------------+
```

### 3.1 AST Sandbox & Code Injection Defense
- **Strict Identifier Whitelist:** All expressions must pass `MathEngine.isSafe()` before evaluation. Only approved mathematical, physical, and trigonometric identifiers are permitted:
  ```java
  Set<String> ALLOWED_IDENTIFIERS = Set.of(
      "sqrt", "cbrt", "sin", "cos", "tan", "cot", "asin", "acos", "atan",
      "sinh", "cosh", "tanh", "abs", "log", "log10", "log2", "ln", "exp",
      "floor", "ceil", "fact", "factorial", "pi", "e", "min", "max", "rad", "deg",
      "gcd", "lcm", "fib", "isprime"
  );
  ```
- **No Script Engines / No Eval():** JLC does not use `javax.script.ScriptEngine`, JavaScript `eval()`, or dynamic class loaders. Calculations are parsed exclusively into an Abstract Syntax Tree (AST).
- **Execution Defense:** Classes like `java.lang.Runtime`, `java.lang.System`, `ProcessBuilder`, and file access APIs cannot be referenced or invoked within expressions.

---

### 3.2 Deterministic Execution & Hallucination Elimination
- **Strict Role Decoupling:** Large Language Models are used **only as NLP Intent Parsers** and are explicitly instructed never to calculate results.
- **Verification Threshold:** Any intent extraction with a confidence score below **95% (0.95)** is rejected or flagged for clarification, preventing hallucinations from contaminating calculation pipelines.

---

### 3.3 Credential & API Key Protection
- **Environment Isolation:** API keys (`GROQ_API_KEY`, `OPENAI_API_KEY`) are loaded via [`.env`](.env) and `.gitignore` prevents them from ever being committed to source control.
- **Safe Template Provided:** [`.env.example`](.env.example) contains placeholder values only.
- **Prompt Sanitization:** Internal API tokens and system configuration variables are never included in prompts sent to remote LLM endpoints.

---

### 3.4 Data Privacy & Offline Isolation
- **100% Offline Capability:** When running with local Ollama (`LLM_MODE=LOCAL`) or in Direct Math mode (`--direct`), zero network packets leave the user's machine.
- **No Telemetry:** JLC contains zero telemetry, trackers, or analytics beacons.
- **Local Audit Logs:** History logs (`calculation_history.json`) reside strictly on the user's local filesystem.

---

### 3.5 Resource Exhaustion & Denial of Service (DoS) Mitigation
- **Asynchronous Execution Timeout:** All LLM network operations are bounded by an **8-second timeout** managed via `CompletableFuture` and `ExecutorService`.
- **Responsive `/skip` Support:** Users can instantly interrupt or bypass long-running requests without freezing the terminal shell.
- **Input Complexity Bounds:** Factorial operands are restricted to non-negative integers ($n \le 170$) to prevent CPU lockups.

---

### 3.6 Numerical Stability & Overflow Protection
- **Long Overflow Prevention:** Dynamic magnitude boundary checks prevent numbers exceeding `Long.MAX_VALUE` ($9.223 \times 10^{18}$) from causing casting overflows or clamped results (`9223372.036854776`).
- **Small-Scale Quantum Precision:** Values below $10^{-4}$ (such as Planck constants $10^{-34}$ and photon energy $3.73 \times 10^{-19}\text{ J}$) retain full IEEE 754 precision and are never rounded down to `0.0`.
- **Division-by-Zero Safety:** Handled cleanly with `ArithmeticException` diagnostics rather than runtime crashes.

---

## 4. Security Best Practices for Deployments

1. **File Permissions:** Protect your `.env` configuration file:
   ```bash
   chmod 600 .env
   ```
2. **Java Runtime:** Run JLC with OpenJDK 17 LTS or higher with the latest security updates applied.
3. **Local AI Deployment:** For high-security or air-gapped environments, use Ollama locally:
   ```bash
   export LLM_MODE=LOCAL
   export OLLAMA_HOST=http://localhost:11434
   ```

---

## 👑 Policy Author
Designed & Maintained by **KABALI GAMER**  
*Lead Architect of Java LLM Smart Calculator (JLC)*
