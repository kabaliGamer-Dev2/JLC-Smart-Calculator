package com.jlc;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    public final String mode;        // GROQ | LOCAL (OLLAMA) | CLOUD (OPENAI)
    public final String ollamaHost;
    public final String modelName;
    public final String openAiKey;
    public final String groqApiKey;
    public final String groqHost;

    private Config(String mode, String ollamaHost, String modelName, String openAiKey, String groqApiKey, String groqHost) {
        this.mode = mode;
        this.ollamaHost = ollamaHost;
        this.modelName = modelName;
        this.openAiKey = openAiKey;
        this.groqApiKey = groqApiKey;
        this.groqHost = groqHost;
    }

    private static String getVal(Dotenv dotenv, String key, String defaultValue) {
        String val = null;
        if (dotenv != null) {
            try {
                val = dotenv.get(key);
            } catch (Exception ignored) {}
        }
        if (val == null || val.isBlank()) {
            val = System.getenv(key);
        }
        return (val == null || val.isBlank()) ? defaultValue : val.trim();
    }

    public static Config load() {
        Dotenv dotenv = null;
        try {
            dotenv = Dotenv.configure().directory(".").ignoreIfMissing().load();
        } catch (Exception ignored) {}

        String mode = getVal(dotenv, "LLM_MODE", "GROQ").toUpperCase();
        String groqApiKey = getVal(dotenv, "GROQ_API_KEY", "");
        String openAiKey = getVal(dotenv, "OPENAI_API_KEY", "");
        String ollamaHost = getVal(dotenv, "OLLAMA_HOST", "http://localhost:11434");
        String groqHost = getVal(dotenv, "GROQ_HOST", "https://api.groq.com/openai/v1/chat/completions");

        String defaultModel;
        if (mode.equals("GROQ")) {
            defaultModel = "llama-3.3-70b-versatile";
        } else if (mode.equals("OPENAI") || mode.equals("CLOUD")) {
            defaultModel = "gpt-3.5-turbo";
        } else {
            defaultModel = "llama3";
        }

        String modelName = getVal(dotenv, "MODEL_NAME", defaultModel);

        return new Config(mode, ollamaHost, modelName, openAiKey, groqApiKey, groqHost);
    }
}
