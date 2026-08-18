package com.jlc;

import com.google.gson.JsonObject;

public class JsonParser {

    public static class ParseResult {
        public final String expression;
        public final double confidence;
        public final String error;

        public ParseResult(String expression, double confidence, String error) {
            this.expression = expression;
            this.confidence = confidence;
            this.error = error;
        }

        public boolean hasError() {
            return error != null && !error.isEmpty();
        }
    }

    /** Parses the LLM's JSON reply. Handles optional markdown code fences. */
    public static ParseResult extractExpression(String rawJson) {
        String json = rawJson == null ? "" : rawJson.trim();
        if (json.startsWith("```")) {
            json = json.replaceFirst("^```[a-zA-Z]*\\s*", "").replaceAll("```\\s*$", "").trim();
        }
        try {
            JsonObject obj = com.google.gson.JsonParser.parseString(json).getAsJsonObject();
            String expression = obj.has("expression") && !obj.get("expression").isJsonNull()
                    ? obj.get("expression").getAsString() : "";
            double confidence = obj.has("confidence") && !obj.get("confidence").isJsonNull()
                    ? obj.get("confidence").getAsDouble() : 0.0;
            String error = obj.has("error") && !obj.get("error").isJsonNull()
                    ? obj.get("error").getAsString() : "";
            return new ParseResult(expression, confidence, error);
        } catch (Exception e) {
            return new ParseResult("", 0.0, "Malformed JSON: " + e.getMessage());
        }
    }
}
