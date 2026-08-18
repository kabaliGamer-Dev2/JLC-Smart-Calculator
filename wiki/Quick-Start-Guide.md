# 🚀 Quick Start Guide

> **Author & Lead Architect:** KABALI GAMER  
> **Project:** Java LLM Smart Calculator (JLC)

---

## 1. Prerequisites

- **Java Development Kit (JDK 17 or higher)**
  ```bash
  sudo apt update && sudo apt install -y openjdk-17-jdk
  java -version
  ```
- **Apache Maven (3.8+)**
  ```bash
  sudo apt install -y maven
  mvn -version
  ```

---

## 2. Installation & Compilation

```bash
# 1. Clone the repository
git clone https://github.com/kabaliGamer-Dev2/JLC-Smart-Calculator.git
cd JLC-Smart-Calculator

# 2. Build standalone shaded executable JAR
mvn clean package -DskipTests
```

---

## 3. Environment Configuration (`.env`)

Copy the provided template:
```bash
cp .env.example .env
```

Edit your `.env` file to select your preferred AI provider:

### Option A: Groq API (Ultra-Fast Cloud Inference < 300ms)
```properties
LLM_MODE=GROQ
GROQ_API_KEY=your_groq_api_key_here
MODEL_NAME=openai/gpt-oss-120b
```

### Option B: Local Ollama (100% Offline & Private)
```properties
LLM_MODE=LOCAL
OLLAMA_HOST=http://localhost:11434
```

### Option C: OpenAI API
```properties
LLM_MODE=CLOUD
OPENAI_API_KEY=your_openai_api_key_here
```

---

## 4. Running JLC

### 4.1 Interactive Multi-Mode Shell
```bash
java -jar target/jlc-1.0.jar
```

### 4.2 Headless Direct Math Mode
```bash
java -jar target/jlc-1.0.jar --direct "sqrt(144) + 5! - (3^4) + log10(1000)"
```

### 4.3 Natural Language Query CLI
```bash
java -jar target/jlc-1.0.jar "What is the escape velocity of Earth in km/s?"
```

### 4.4 Automated Unit Tests
```bash
java -jar target/jlc-1.0.jar "/test"
```
