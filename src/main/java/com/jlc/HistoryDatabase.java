package com.jlc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class HistoryDatabase {

    private static final String DB_FILE = "calculation_history.json";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type LIST_TYPE = new TypeToken<List<CalculationRecord>>() {}.getType();

    private static HistoryDatabase instance;

    public static class CalculationRecord {
        public long id;
        public String timestamp;
        public String mode;       // AI_CHAT | DIRECT_MATH | CLI
        public String query;
        public String expression;
        public double result;
        public long latencyMs;

        public CalculationRecord() {}

        public CalculationRecord(long id, String timestamp, String mode, String query, String expression, double result, long latencyMs) {
            this.id = id;
            this.timestamp = timestamp;
            this.mode = mode;
            this.query = query;
            this.expression = expression;
            this.result = result;
            this.latencyMs = latencyMs;
        }
    }

    public static class DatabaseStats {
        public int totalRecords;
        public double minResult;
        public double maxResult;
        public double avgResult;
        public double avgLatencyMs;
        public Map<String, Integer> modeCounts = new LinkedHashMap<>();
    }

    private final List<CalculationRecord> records = new ArrayList<>();
    private long nextId = 1;

    private HistoryDatabase() {
        load();
    }

    public static synchronized HistoryDatabase getInstance() {
        if (instance == null) {
            instance = new HistoryDatabase();
        }
        return instance;
    }

    public synchronized void load() {
        records.clear();
        try {
            Path path = Paths.get(DB_FILE);
            if (Files.exists(path)) {
                String json = Files.readString(path, StandardCharsets.UTF_8);
                List<CalculationRecord> loaded = GSON.fromJson(json, LIST_TYPE);
                if (loaded != null) {
                    records.addAll(loaded);
                    for (CalculationRecord r : records) {
                        if (r.id >= nextId) {
                            nextId = r.id + 1;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[warn] Could not load calculation database: " + e.getMessage());
        }
    }

    public synchronized void save() {
        try {
            String json = GSON.toJson(records);
            Files.writeString(Paths.get(DB_FILE), json, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("[warn] Could not write calculation database: " + e.getMessage());
        }
    }

    public synchronized CalculationRecord record(String mode, String query, String expression, double result, long latencyMs) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        CalculationRecord rec = new CalculationRecord(nextId++, timestamp, mode, query, expression, result, latencyMs);
        records.add(rec);
        save();
        return rec;
    }

    public synchronized List<CalculationRecord> getAll() {
        return Collections.unmodifiableList(new ArrayList<>(records));
    }

    public synchronized List<CalculationRecord> getRecent(int limit) {
        int size = records.size();
        if (size <= limit) {
            return new ArrayList<>(records);
        }
        return new ArrayList<>(records.subList(size - limit, size));
    }

    public synchronized CalculationRecord getById(long id) {
        // Binary search or direct lookup since IDs are monotonic
        for (CalculationRecord r : records) {
            if (r.id == id) return r;
        }
        return null;
    }

    public synchronized List<CalculationRecord> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return Collections.emptyList();
        String lower = keyword.toLowerCase().trim();
        return records.stream()
                .filter(r -> (r.query != null && r.query.toLowerCase().contains(lower))
                          || (r.expression != null && r.expression.toLowerCase().contains(lower))
                          || String.valueOf(r.result).contains(lower)
                          || r.timestamp.contains(lower)
                          || (r.mode != null && r.mode.toLowerCase().contains(lower)))
                .collect(Collectors.toList());
    }

    public synchronized DatabaseStats getStats() {
        DatabaseStats stats = new DatabaseStats();
        stats.totalRecords = records.size();
        if (records.isEmpty()) return stats;

        double sumResult = 0;
        double min = Double.MAX_VALUE;
        double max = -Double.MAX_VALUE;
        long totalLatency = 0;

        for (CalculationRecord r : records) {
            sumResult += r.result;
            min = Math.min(min, r.result);
            max = Math.max(max, r.result);
            totalLatency += r.latencyMs;
            stats.modeCounts.put(r.mode, stats.modeCounts.getOrDefault(r.mode, 0) + 1);
        }

        stats.minResult = min;
        stats.maxResult = max;
        stats.avgResult = Math.round((sumResult / records.size()) * 100.0) / 100.0;
        stats.avgLatencyMs = Math.round(((double) totalLatency / records.size()) * 10.0) / 10.0;
        return stats;
    }

    public synchronized boolean exportCsv(String destinationPath) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("ID,Timestamp,Mode,Query,Expression,Result,LatencyMs\n");
            for (CalculationRecord r : records) {
                sb.append(r.id).append(",")
                  .append("\"").append(r.timestamp).append("\",")
                  .append("\"").append(r.mode).append("\",")
                  .append("\"").append(r.query.replace("\"", "\"\"")).append("\",")
                  .append("\"").append(r.expression.replace("\"", "\"\"")).append("\",")
                  .append(r.result).append(",")
                  .append(r.latencyMs).append("\n");
            }
            Files.writeString(Paths.get(destinationPath), sb.toString(), StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            System.err.println("[warn] Export to CSV failed: " + e.getMessage());
            return false;
        }
    }

    public synchronized void clear() {
        records.clear();
        nextId = 1;
        save();
    }
}
