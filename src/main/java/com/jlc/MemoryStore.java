package com.jlc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MemoryStore {

    private static final String MEMORY_FILE = "learned_memory.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static MemoryStore instance;

    public static class MemoryData {
        public Map<String, String> rules = new LinkedHashMap<>();
        public List<LearnedExample> examples = new ArrayList<>();
        public Map<String, String> cache = new LinkedHashMap<>();
    }

    public static class LearnedExample {
        public String input;
        public String expression;

        public LearnedExample() {}
        public LearnedExample(String input, String expression) {
            this.input = input;
            this.expression = expression;
        }
    }

    private MemoryData data = new MemoryData();

    private MemoryStore() {
        load();
    }

    public static synchronized MemoryStore getInstance() {
        if (instance == null) {
            instance = new MemoryStore();
        }
        return instance;
    }

    public synchronized void load() {
        try {
            Path path = Paths.get(MEMORY_FILE);
            if (Files.exists(path)) {
                String json = Files.readString(path, StandardCharsets.UTF_8);
                MemoryData loaded = GSON.fromJson(json, MemoryData.class);
                if (loaded != null) {
                    if (loaded.rules == null) loaded.rules = new LinkedHashMap<>();
                    if (loaded.examples == null) loaded.examples = new ArrayList<>();
                    if (loaded.cache == null) loaded.cache = new LinkedHashMap<>();
                    this.data = loaded;
                    return;
                }
            }
        } catch (Exception e) {
            System.err.println("[warn] Could not load memory: " + e.getMessage());
        }
        // Initialize default common mathematical words/rules if empty
        initDefaults();
    }

    private void initDefaults() {
        if (data.rules.isEmpty()) {
            data.rules.put("dozen", "12");
            data.rules.put("baker's dozen", "13");
            data.rules.put("pair", "2");
            data.rules.put("score", "20");
            data.rules.put("century", "100");
            data.rules.put("half a century", "50");
            data.rules.put("gross", "144");
        }
        save();
    }

    public synchronized void save() {
        try {
            String json = GSON.toJson(data);
            Files.writeString(Paths.get(MEMORY_FILE), json, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("[warn] Could not save memory: " + e.getMessage());
        }
    }

    public synchronized void addRule(String alias, String replacement) {
        data.rules.put(alias.toLowerCase().trim(), replacement.trim());
        save();
    }

    public synchronized void addExample(String input, String expression) {
        // Prevent duplicate examples
        data.examples.removeIf(e -> e.input.equalsIgnoreCase(input.trim()));
        data.examples.add(new LearnedExample(input.trim(), expression.trim()));
        if (data.examples.size() > 50) {
            data.examples.remove(0); // keep most recent 50
        }
        data.cache.put(normalizeKey(input), expression.trim());
        save();
    }

    public synchronized void autoLearn(String input, String expression) {
        String key = normalizeKey(input);
        data.cache.put(key, expression.trim());
        
        // If it's a distinct sentence or unique pattern, save to dynamic examples
        boolean exists = data.examples.stream().anyMatch(e -> e.input.equalsIgnoreCase(input.trim()));
        if (!exists && input.trim().split("\\s+").length >= 3) {
            data.examples.add(new LearnedExample(input.trim(), expression.trim()));
            if (data.examples.size() > 50) {
                data.examples.remove(0);
            }
        }
        save();
    }

    public synchronized String getCachedExpression(String input) {
        return data.cache.get(normalizeKey(input));
    }

    public synchronized String applyRules(String text) {
        if (text == null) return null;
        String result = text;
        for (Map.Entry<String, String> entry : data.rules.entrySet()) {
            String pattern = "\\b" + PatternQuote(entry.getKey()) + "\\b";
            result = result.replaceAll("(?i)" + pattern, entry.getValue());
        }
        return result;
    }

    public synchronized String getPromptInjection() {
        StringBuilder sb = new StringBuilder();
        if (!data.rules.isEmpty()) {
            sb.append("\nLearned Custom Vocabulary & Terms:\n");
            for (Map.Entry<String, String> entry : data.rules.entrySet()) {
                sb.append("- \"").append(entry.getKey()).append("\" -> ").append(entry.getValue()).append("\n");
            }
        }
        if (!data.examples.isEmpty()) {
            sb.append("\nLearned Dynamic Examples:\n");
            int count = 0;
            // Get the most recent 10 examples
            int start = Math.max(0, data.examples.size() - 10);
            for (int i = start; i < data.examples.size(); i++) {
                LearnedExample ex = data.examples.get(i);
                sb.append("User: \"").append(ex.input).append("\"\n");
                sb.append("Assistant: {\"expression\": \"").append(ex.expression).append("\", \"confidence\": 1.0, \"error\": null}\n");
            }
        }
        return sb.toString();
    }

    public synchronized Map<String, String> getRules() {
        return Collections.unmodifiableMap(data.rules);
    }

    public synchronized List<LearnedExample> getExamples() {
        return Collections.unmodifiableList(data.examples);
    }

    public synchronized void clear() {
        data = new MemoryData();
        initDefaults();
        save();
    }

    private static String normalizeKey(String text) {
        return text == null ? "" : text.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    private static String PatternQuote(String s) {
        return java.util.regex.Pattern.quote(s);
    }
}
