package com.jlc;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class LLMClient {

    private static final Gson GSON = new Gson();
    private final Config config;
    private final HttpClient http;
    private final String systemPrompt;

    public LLMClient(Config config) {
        this.config = config;
        this.http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        this.systemPrompt = loadSystemPrompt();
    }

    private String getFullSystemPrompt() {
        return this.systemPrompt + "\n" + MemoryStore.getInstance().getPromptInjection();
    }

    public String sendPrompt(String userInput) throws Exception {
        return sendPrompt(userInput, null);
    }

    public String sendPrompt(String userInput, Double previousResult) throws Exception {
        String formattedInput = userInput;
        if (previousResult != null) {
            formattedInput = "[Context: Previous result was " + previousResult + ". If the user refers to 'it', 'that', 'ans', 'now', or continues an operation, use 'ans' or " + previousResult + "]\nUser query: " + userInput;
        }
        Exception last = null;
        for (int attempt = 0; attempt < 2; attempt++) {
            try {
                if (config.mode.equals("GROQ")) {
                    return callGroq(formattedInput);
                } else if (config.mode.equals("OPENAI") || config.mode.equals("CLOUD")) {
                    return callOpenAi(formattedInput);
                } else {
                    return callOllama(formattedInput);
                }
            } catch (Exception e) {
                last = e;
                Thread.sleep(2000L * (attempt + 1)); // 2s backoff
            }
        }
        throw last;
    }

    private String callGroq(String userInput) throws Exception {
        if (config.groqApiKey == null || config.groqApiKey.isBlank()) {
            throw new IllegalArgumentException("GROQ_API_KEY is not set. Please add GROQ_API_KEY to your .env file or export it in your environment.");
        }
        return callChatCompletions(config.groqHost, config.groqApiKey, config.modelName, userInput);
    }

    private String callOpenAi(String userInput) throws Exception {
        if (config.openAiKey == null || config.openAiKey.isBlank()) {
            throw new IllegalArgumentException("OPENAI_API_KEY is not set. Please add OPENAI_API_KEY to your .env file or export it in your environment.");
        }
        return callChatCompletions("https://api.openai.com/v1/chat/completions", config.openAiKey, config.modelName, userInput);
    }

    private String callChatCompletions(String endpoint, String apiKey, String model, String userInput) throws Exception {
        JsonArray messages = new JsonArray();
        JsonObject system = new JsonObject();
        system.addProperty("role", "system");
        system.addProperty("content", getFullSystemPrompt());
        JsonObject user = new JsonObject();
        user.addProperty("role", "user");
        user.addProperty("content", userInput);
        messages.add(system);
        messages.add(user);

        JsonObject format = new JsonObject();
        format.addProperty("type", "json_object");
        JsonObject body = new JsonObject();
        body.addProperty("model", model);
        body.add("messages", messages);
        body.add("response_format", format);

        String json = post(endpoint, body.toString(), "Authorization", "Bearer " + apiKey);
        return GSON.fromJson(json, JsonObject.class)
                .getAsJsonArray("choices").get(0).getAsJsonObject()
                .getAsJsonObject("message").get("content").getAsString();
    }

    private String callOllama(String userInput) throws Exception {
        JsonObject body = new JsonObject();
        body.addProperty("model", config.modelName);
        body.addProperty("prompt", getFullSystemPrompt() + "\n\nUser: " + userInput);
        body.addProperty("stream", false);
        body.addProperty("format", "json");

        String json = post(config.ollamaHost + "/api/generate", body.toString());
        return GSON.fromJson(json, JsonObject.class).get("response").getAsString();
    }

    private String post(String url, String jsonBody, String... headers) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8));
        if (headers.length > 0) {
            builder.headers(headers);
        }
        HttpResponse<String> response = http.send(builder.build(), HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() >= 400) {
            throw new IOException("HTTP " + response.statusCode() + ": " + response.body());
        }
        return response.body();
    }

    private static String loadSystemPrompt() {
        try (InputStream in = LLMClient.class.getResourceAsStream("/ai_instructions.txt")) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "You are a Math Intent Parser for a Java application. Extract the math equation from the user's text. Convert words to numbers. Output ONLY JSON. Do not solve the equation.";
        }
    }
}
