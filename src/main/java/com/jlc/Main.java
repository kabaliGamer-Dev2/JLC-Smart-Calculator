package com.jlc;

import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static class SessionContext {
        public Double lastResult = null;
        public final Map<String, Double> variables = new LinkedHashMap<>();

        public void record(double result) {
            this.lastResult = result;
            this.variables.put("ans", result);
            this.variables.put("last", result);
        }

        public void setVariable(String name, double val) {
            this.variables.put(name.toLowerCase().trim(), val);
        }
    }

    private static final SessionContext GLOBAL_SESSION = new SessionContext();
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        Config config = Config.load();
        if (args.length > 0) {
            String query = String.join(" ", args);
            if (query.startsWith("/")) {
                Scanner scanner = new Scanner(System.in, "UTF-8");
                handleSlashCommand(query, scanner, config, GLOBAL_SESSION, "CLI");
                System.exit(0);
            } else if (query.startsWith("--direct ") || query.startsWith("-d ")) {
                String expr = query.replaceFirst("^(--direct|-d)\\s+", "");
                runSingleDirect(expr, GLOBAL_SESSION);
            } else {
                runSingleQuery(query, config, GLOBAL_SESSION);
            }
        } else {
            UI.printBanner();
            runMainMenu(config, GLOBAL_SESSION);
        }
    }

    private static void runSingleQuery(String query, Config config, SessionContext session) {
        System.out.println(UI.ASH + "> Parsing: " + UI.WHITE_BOLD + query + UI.RESET);
        System.out.println(UI.ORANGE_BOLD + "> " + processNLP(query, config, session, "CLI") + UI.RESET);
    }

    private static void runSingleDirect(String expression, SessionContext session) {
        System.out.println(UI.ASH + "> Direct Math: " + UI.WHITE_BOLD + expression + UI.RESET);
        System.out.println(UI.ORANGE_BOLD + "> " + processDirect(expression, session, expression, "CLI", System.currentTimeMillis()) + UI.RESET);
    }

    private static void runMainMenu(Config config, SessionContext session) {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        while (true) {
            UI.printMainMenu();
            if (!scanner.hasNextLine()) break;
            String choice = scanner.nextLine().trim();

            if (choice.startsWith("/")) {
                if (handleSlashCommand(choice, scanner, config, session, "MENU")) {
                    continue;
                }
            }

            if (choice.equals("1") || choice.equalsIgnoreCase("chat")) {
                runChatMode(scanner, config, session);
            } else if (choice.equals("2") || choice.equalsIgnoreCase("direct")) {
                runDirectMathMode(scanner, session);
            } else if (choice.equals("3") || choice.equalsIgnoreCase("memory")) {
                printMemory();
            } else if (choice.equals("4") || choice.equalsIgnoreCase("db") || choice.equalsIgnoreCase("history")) {
                runDatabaseMenu(scanner, session, config);
            } else if (choice.equals("5") || choice.equalsIgnoreCase("exit") || choice.equalsIgnoreCase("q")) {
                System.out.println("\n" + UI.ORANGE_BOLD + "Thank you for using JLC. Goodbye! 👋\n" + UI.RESET);
                break;
            } else {
                System.out.println(UI.ORANGE + "⚠️  Invalid option. Please enter 1, 2, 3, 4, or 5 (or /help)." + UI.RESET);
            }
        }
    }

    private static void runDatabaseMenu(Scanner scanner, SessionContext session, Config config) {
        while (true) {
            System.out.println("\n" + UI.ASH_DARK + "===================== " + UI.ORANGE_BOLD + "📜 PERMANENT CALCULATION DATABASE" + UI.ASH_DARK + " =====================" + UI.RESET);
            System.out.println("   " + UI.ORANGE_BOLD + "[1]" + UI.RESET + " 📋 View Recent Calculations (Audit Trail)");
            System.out.println("   " + UI.ORANGE_BOLD + "[2]" + UI.RESET + " 🔍 Search History by Keyword / Equation / Date");
            System.out.println("   " + UI.ORANGE_BOLD + "[3]" + UI.RESET + " 📊 Calculation Statistics & Performance Analytics");
            System.out.println("   " + UI.ORANGE_BOLD + "[4]" + UI.RESET + " 💾 Export Full History to CSV File");
            System.out.println("   " + UI.ORANGE_BOLD + "[5]" + UI.RESET + " 🔢 View Active Session Variables");
            System.out.println("   " + UI.ORANGE_BOLD + "[6]" + UI.RESET + " 🗑️  Clear Permanent Database");
            System.out.println("   " + UI.ORANGE_BOLD + "[7]" + UI.RESET + " ⬅️  Back to Main Menu");
            System.out.println(UI.ASH_DARK + "----------------------------------------------------------------------------------" + UI.RESET);
            System.out.print(UI.ORANGE_BOLD + "Enter option (1-7)" + UI.ASH + " [or /switch]: " + UI.RESET);

            if (!scanner.hasNextLine()) break;
            String opt = scanner.nextLine().trim();

            if (opt.startsWith("/")) {
                if (handleSlashCommand(opt, scanner, config, session, "DB")) {
                    continue;
                }
            }

            if (opt.equals("1")) {
                printHistoryRecords(HistoryDatabase.getInstance().getRecent(20), "Recent Calculations (Latest 20)");
            } else if (opt.equals("2")) {
                System.out.print(UI.ASH + "Enter search keyword or value: " + UI.RESET);
                if (scanner.hasNextLine()) {
                    String query = scanner.nextLine().trim();
                    List<HistoryDatabase.CalculationRecord> matches = HistoryDatabase.getInstance().search(query);
                    printHistoryRecords(matches, "Search Results for \"" + query + "\" (" + matches.size() + " matches)");
                }
            } else if (opt.equals("3")) {
                printAnalytics();
            } else if (opt.equals("4")) {
                String filename = "calculation_history.csv";
                boolean ok = HistoryDatabase.getInstance().exportCsv(filename);
                if (ok) {
                    System.out.println("\n" + UI.ORANGE_BOLD + "✅ Successfully exported database to: " + filename + "\n" + UI.RESET);
                } else {
                    System.out.println("\n" + UI.ORANGE + "❌ Failed to export CSV.\n" + UI.RESET);
                }
            } else if (opt.equals("5")) {
                printVariables(session);
            } else if (opt.equals("6")) {
                HistoryDatabase.getInstance().clear();
                System.out.println("\n" + UI.ORANGE_BOLD + "✅ Permanent calculation history database has been cleared.\n" + UI.RESET);
            } else if (opt.equals("7") || opt.equalsIgnoreCase("back") || opt.equalsIgnoreCase("menu")) {
                break;
            } else {
                System.out.println(UI.ORANGE + "⚠️  Invalid choice. Please select 1-7." + UI.RESET);
            }
        }
    }

    private static void runChatMode(Scanner scanner, Config config, SessionContext session) {
        System.out.println("\n" + UI.ASH_DARK + "--- " + UI.ORANGE_BOLD + "💬 AI Natural Language Chat Mode" + UI.ASH_DARK + " ---" + UI.RESET);
        System.out.println(UI.ASH + "Ask math questions, continue calculations ('now multiply by 2'), or teach rules ('/learn grand = 1000')." + UI.RESET);
        System.out.println(UI.ASH + "Slash Commands: " + UI.ORANGE_BOLD + "/history" + UI.ASH + ", " + UI.ORANGE_BOLD + "/switch 2" + UI.ASH + ", " + UI.ORANGE_BOLD + "/stats" + UI.ASH + ", " + UI.ORANGE_BOLD + "/skip" + UI.ASH + ", " + UI.ORANGE_BOLD + "/help" + UI.ASH + ", " + UI.ORANGE_BOLD + "/menu" + UI.RESET + "\n");

        while (true) {
            System.out.print(UI.ORANGE_BOLD + "[AI Chat]" + UI.ASH + " > You: " + UI.RESET);
            if (!scanner.hasNextLine()) return;
            String input = scanner.nextLine().trim();

            if (input.startsWith("/")) {
                String cmd = input.toLowerCase();
                if (cmd.equals("/menu") || cmd.equals("/back")) {
                    break;
                }
                if (cmd.equals("/skip")) {
                    System.out.println(UI.ASH + "⏭️  Skipped.\n" + UI.RESET);
                    continue;
                }
                if (cmd.startsWith("/switch ") || cmd.startsWith("/mode ")) {
                    String target = cmd.replaceFirst("^/(switch|mode)\\s+", "").trim();
                    if (target.equals("2") || target.equalsIgnoreCase("direct")) {
                        runDirectMathMode(scanner, session);
                        return;
                    }
                }
                if (handleSlashCommand(input, scanner, config, session, "AI_CHAT")) {
                    continue;
                }
            }

            if (input.equalsIgnoreCase("menu") || input.equalsIgnoreCase("back")) {
                break;
            }
            if (input.equalsIgnoreCase("skip")) {
                System.out.println(UI.ASH + "⏭️  Skipped.\n" + UI.RESET);
                continue;
            }
            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("\n" + UI.ORANGE_BOLD + "Goodbye! 👋\n" + UI.RESET);
                System.exit(0);
            }
            if (handleRecallCommand(input, session)) {
                continue;
            }
            if (input.toLowerCase().startsWith("learn:")) {
                handleLearnCommand(input.substring(6).trim());
                continue;
            }
            if (handleVariableAssignment(input, session, "AI_CHAT")) {
                continue;
            }
            if (input.isEmpty()) continue;

            String result = processNLP(input, config, session, "AI_CHAT");
            System.out.println(UI.ORANGE_BOLD + "[AI Chat]" + UI.ASH + " > AI: " + UI.WHITE_BOLD + result + UI.RESET + "\n");
        }
    }

    private static void runDirectMathMode(Scanner scanner, SessionContext session) {
        System.out.println("\n" + UI.ASH_DARK + "--- " + UI.ORANGE_BOLD + "🔢 Direct Math & Number Mode (Instant Engine)" + UI.ASH_DARK + " ---" + UI.RESET);
        System.out.println(UI.ASH + "Enter math expressions directly. Supports 'ans', 'last', variables ('x = 50'), and recall ('#3')." + UI.RESET);
        System.out.println(UI.ASH + "Slash Commands: " + UI.ORANGE_BOLD + "/history" + UI.ASH + ", " + UI.ORANGE_BOLD + "/switch 1" + UI.ASH + ", " + UI.ORANGE_BOLD + "/stats" + UI.ASH + ", " + UI.ORANGE_BOLD + "/skip" + UI.ASH + ", " + UI.ORANGE_BOLD + "/help" + UI.ASH + ", " + UI.ORANGE_BOLD + "/menu" + UI.RESET + "\n");

        while (true) {
            System.out.print(UI.ORANGE_BOLD + "[Direct Math]" + UI.ASH + " > " + UI.RESET);
            if (!scanner.hasNextLine()) return;
            String input = scanner.nextLine().trim();

            if (input.startsWith("/")) {
                String cmd = input.toLowerCase();
                if (cmd.equals("/menu") || cmd.equals("/back")) {
                    break;
                }
                if (cmd.equals("/skip")) {
                    System.out.println(UI.ASH + "⏭️  Skipped.\n" + UI.RESET);
                    continue;
                }
                if (cmd.startsWith("/switch ") || cmd.startsWith("/mode ")) {
                    String target = cmd.replaceFirst("^/(switch|mode)\\s+", "").trim();
                    if (target.equals("1") || target.equalsIgnoreCase("chat")) {
                        runChatMode(new Scanner(System.in, "UTF-8"), Config.load(), session);
                        return;
                    }
                }
                if (handleSlashCommand(input, scanner, Config.load(), session, "DIRECT_MATH")) {
                    continue;
                }
            }

            if (input.equalsIgnoreCase("menu") || input.equalsIgnoreCase("back")) {
                break;
            }
            if (input.equalsIgnoreCase("skip")) {
                System.out.println(UI.ASH + "⏭️  Skipped.\n" + UI.RESET);
                continue;
            }
            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("\n" + UI.ORANGE_BOLD + "Goodbye! 👋\n" + UI.RESET);
                System.exit(0);
            }
            if (handleRecallCommand(input, session)) {
                continue;
            }
            if (handleVariableAssignment(input, session, "DIRECT_MATH")) {
                continue;
            }
            if (input.isEmpty()) continue;

            long start = System.currentTimeMillis();
            String result = processDirect(input, session, input, "DIRECT_MATH", start);
            System.out.println(UI.ORANGE_BOLD + "[Direct Math]" + UI.ASH + " > " + UI.WHITE_BOLD + result + UI.RESET + "\n");
        }
    }

    private static boolean handleSlashCommand(String input, Scanner scanner, Config config, SessionContext session, String fromMode) {
        String clean = input.trim();
        String cmd = clean.split("\\s+")[0].toLowerCase();
        String arg = clean.length() > cmd.length() ? clean.substring(cmd.length()).trim() : "";

        switch (cmd) {
            case "/help":
            case "/?":
                UI.printSlashHelp();
                return true;

            case "/history":
            case "/h":
                printHistoryRecords(HistoryDatabase.getInstance().getRecent(15), "Permanent Calculation History");
                return true;

            case "/search":
                if (arg.isEmpty()) {
                    System.out.print(UI.ASH + "Enter search query: " + UI.RESET);
                    if (scanner.hasNextLine()) arg = scanner.nextLine().trim();
                }
                List<HistoryDatabase.CalculationRecord> matches = HistoryDatabase.getInstance().search(arg);
                printHistoryRecords(matches, "Search Results for \"" + arg + "\"");
                return true;

            case "/stats":
            case "/analytics":
                printAnalytics();
                return true;

            case "/vars":
            case "/variables":
                printVariables(session);
                return true;

            case "/memory":
            case "/rules":
                printMemory();
                return true;

            case "/clear":
            case "/clearcache":
            case "/clear-cache":
            case "/clear-memory":
                MemoryStore.getInstance().clear();
                System.out.println(UI.ORANGE_BOLD + "✅ Cache memory & learned rules have been cleared!\n" + UI.RESET);
                return true;

            case "/clear-history":
                HistoryDatabase.getInstance().clear();
                System.out.println(UI.ORANGE_BOLD + "✅ Permanent calculation history cleared.\n" + UI.RESET);
                return true;

            case "/export":
                String file = arg.isEmpty() ? "calculation_history.csv" : arg;
                HistoryDatabase.getInstance().exportCsv(file);
                System.out.println(UI.ORANGE_BOLD + "✅ Exported calculation history to: " + file + "\n" + UI.RESET);
                return true;

            case "/recall":
                if (!arg.isEmpty()) {
                    handleRecallCommand(arg, session);
                    return true;
                }
                System.out.println(UI.ASH + "Usage: /recall <#id> (e.g. /recall 3)\n" + UI.RESET);
                return true;

            case "/learn":
                if (!arg.isEmpty()) {
                    handleLearnCommand(arg);
                    return true;
                }
                System.out.println(UI.ASH + "Usage: /learn <phrase> = <value> (e.g. /learn grand = 1000)\n" + UI.RESET);
                return true;

            case "/switch":
            case "/mode":
                if (arg.equals("1") || arg.equalsIgnoreCase("chat")) {
                    runChatMode(scanner, config, session);
                    return true;
                } else if (arg.equals("2") || arg.equalsIgnoreCase("direct")) {
                    runDirectMathMode(scanner, session);
                    return true;
                } else if (arg.equals("3") || arg.equalsIgnoreCase("memory")) {
                    printMemory();
                    return true;
                } else if (arg.equals("4") || arg.equalsIgnoreCase("db")) {
                    runDatabaseMenu(scanner, session, config);
                    return true;
                }
                System.out.println(UI.ASH + "Usage: /switch <1|2|3|4> (1: AI Chat, 2: Direct Math, 3: Memory, 4: DB)\n" + UI.RESET);
                return true;

            case "/plot":
                String plotExpr = arg.isEmpty() ? "sin(x)" : arg;
                System.out.println(AsciiPlotter.plot(plotExpr, -10, 10, 60, 16));
                return true;

            case "/convert":
                UnitConverter.ConversionResult conv = UnitConverter.parseAndConvert(arg);
                if (conv != null) {
                    System.out.println(UI.ORANGE_BOLD + "🔄 Unit Conversion (" + conv.category + "):\n" + UI.RESET +
                            UI.ASH + "   " + conv.fromValue + " " + conv.fromUnit + " = " +
                            UI.GOLD_BOLD + ResponseFormatter.formatNumber(conv.toValue) + " " + conv.toUnit + "\n" + UI.RESET);
                } else {
                    System.out.println(UI.ASH + "Usage: /convert <value> <from_unit> to <to_unit> (e.g. /convert 100 km to miles, /convert 532 nm to m)\n" + UI.RESET);
                }
                return true;

            case "/const":
            case "/constants":
                printConstants();
                return true;

            case "/solve":
                handleSolveCommand(arg);
                return true;

            case "/diff":
                if (!arg.isEmpty()) {
                    String dRes = SymbolicEngine.differentiatePolynomial(arg);
                    System.out.println(UI.ORANGE_BOLD + "d/dx (" + arg + ") = " + UI.GOLD_BOLD + dRes + "\n" + UI.RESET);
                } else {
                    System.out.println(UI.ASH + "Usage: /diff <polynomial> (e.g. /diff 3x^3 + 5x^2 - 4x + 7)\n" + UI.RESET);
                }
                return true;

            case "/complex":
                handleComplexCommand(arg);
                return true;

            case "/matrix":
                handleMatrixMenu(scanner);
                return true;

            case "/base":
                handleBaseCommand(arg);
                return true;

            case "/finance":
                handleFinanceMenu(scanner);
                return true;

            case "/benchmark":
                System.out.println(UI.ASH + "Running 100,000 iterations benchmark against standard Java Math..." + UI.RESET);
                BenchmarkRunner.BenchmarkResult bench = BenchmarkRunner.runBenchmark(100000);
                System.out.println(bench);
                return true;

            case "/test":
                System.out.println(UI.ASH + "Running JLC automated unit test suite across 10 scientific domains..." + UI.RESET);
                List<TestSuite.TestResult> testResults = TestSuite.runAll();
                printTestResults(testResults);
                return true;

            case "/skip":
                System.out.println(UI.ASH + "⏭️  Skipped.\n" + UI.RESET);
                return true;

            case "/exit":
            case "/quit":
                System.out.println("\n" + UI.ORANGE_BOLD + "Goodbye! 👋\n" + UI.RESET);
                System.exit(0);
                return true;

            default:
                if (cmd.startsWith("#") && cmd.substring(1).matches("^\\d+$")) {
                    return handleRecallCommand(cmd, session);
                }
                return false;
        }
    }

    private static boolean handleRecallCommand(String input, SessionContext session) {
        String trimmed = input.trim();
        Long id = null;
        if (trimmed.startsWith("#") && trimmed.substring(1).matches("^\\d+$")) {
            id = Long.parseLong(trimmed.substring(1));
        } else if (trimmed.toLowerCase().startsWith("recall ") && trimmed.substring(7).trim().matches("^\\d+$")) {
            id = Long.parseLong(trimmed.substring(7).trim());
        }
        if (id != null) {
            HistoryDatabase.CalculationRecord rec = HistoryDatabase.getInstance().getById(id);
            if (rec != null) {
                session.record(rec.result);
                System.out.println(UI.ORANGE_BOLD + "✅ Recalled Record #" + rec.id + " (" + rec.query + ") = " + rec.result + " (saved into 'ans')\n" + UI.RESET);
                return true;
            } else {
                System.out.println(UI.ORANGE + "⚠️  Record #" + id + " not found in database.\n" + UI.RESET);
                return true;
            }
        }
        return false;
    }

    private static boolean handleVariableAssignment(String input, SessionContext session, String mode) {
        String trimmed = input.trim();
        if (trimmed.toLowerCase().startsWith("let ")) {
            trimmed = trimmed.substring(4).trim();
        }
        if (trimmed.contains("=") && !trimmed.contains("==") && !trimmed.toLowerCase().startsWith("learn:")) {
            String[] parts = trimmed.split("=", 2);
            String varName = parts[0].trim();
            String expr = parts[1].trim();
            if (varName.matches("^[a-zA-Z][a-zA-Z0-9]*$") && !MathEngine.isSafe(varName) && !expr.isEmpty()) {
                try {
                    long start = System.currentTimeMillis();
                    double val = MathEngine.evaluate(expr, session.variables);
                    long latency = System.currentTimeMillis() - start;
                    session.setVariable(varName, val);
                    session.record(val);
                    HistoryDatabase.getInstance().record(mode, input, varName + " = " + val, val, latency);
                    System.out.println(UI.ORANGE_BOLD + "✅ Stored variable: " + varName + " = " + val + "\n" + UI.RESET);
                    return true;
                } catch (Exception ignored) {}
            }
        }
        return false;
    }

    private static void handleLearnCommand(String body) {
        if (body.contains("->") || body.contains("==")) {
            String delimiter = body.contains("->") ? "->" : "==";
            String[] parts = body.split(delimiter, 2);
            String question = parts[0].trim();
            String expr = parts[1].trim();
            MemoryStore.getInstance().addExample(question, expr);
            System.out.println(UI.ORANGE_BOLD + "✅ Learned calculation pattern: \"" + question + "\" -> " + expr + "\n" + UI.RESET);
        } else if (body.contains("=") || body.contains("is")) {
            String delimiter = body.contains("=") ? "=" : "is";
            String[] parts = body.split(delimiter, 2);
            String alias = parts[0].trim();
            String replacement = parts[1].trim();
            MemoryStore.getInstance().addRule(alias, replacement);
            System.out.println(UI.ORANGE_BOLD + "✅ Learned vocabulary rule: \"" + alias + "\" = " + replacement + "\n" + UI.RESET);
        } else {
            System.out.println(UI.ORANGE + "⚠️  Format: '/learn phrase = value' or '/learn question -> expression'\n" + UI.RESET);
        }
    }

    private static void printMemory() {
        MemoryStore store = MemoryStore.getInstance();
        System.out.println("\n" + UI.ASH_DARK + "=========================== " + UI.ORANGE_BOLD + "🧠 LEARNED MEMORY STORE" + UI.ASH_DARK + " ===========================" + UI.RESET);
        System.out.println(UI.ORANGE_BOLD + "Custom Vocabulary & Constants:" + UI.RESET);
        for (Map.Entry<String, String> entry : store.getRules().entrySet()) {
            System.out.println(UI.ASH + "  • " + UI.WHITE_BOLD + "\"" + entry.getKey() + "\"" + UI.ASH + " -> " + UI.ORANGE + entry.getValue() + UI.RESET);
        }
        if (!store.getExamples().isEmpty()) {
            System.out.println("\n" + UI.ORANGE_BOLD + "Learned Few-Shot Patterns (" + store.getExamples().size() + " total):" + UI.RESET);
            int start = Math.max(0, store.getExamples().size() - 10);
            for (int i = start; i < store.getExamples().size(); i++) {
                MemoryStore.LearnedExample ex = store.getExamples().get(i);
                System.out.println(UI.ASH + "  • " + UI.WHITE_BOLD + "\"" + ex.input + "\"" + UI.ASH + " => " + UI.ORANGE + ex.expression + UI.RESET);
            }
        }
        System.out.println(UI.ASH_DARK + "===================================================================================\n" + UI.RESET);
    }

    private static void printHistoryRecords(List<HistoryDatabase.CalculationRecord> records, String title) {
        System.out.println("\n" + UI.ASH_DARK + "=========================== " + UI.ORANGE_BOLD + "📜 " + title + UI.ASH_DARK + " ===========================" + UI.RESET);
        if (records.isEmpty()) {
            System.out.println(UI.ASH + "  (No matching calculation records found)" + UI.RESET);
        } else {
            for (HistoryDatabase.CalculationRecord r : records) {
                System.out.printf(UI.ORANGE_BOLD + "  [#%d]" + UI.ASH + " [%s] " + UI.WHITE_BOLD + "%s" + UI.ASH + " | Mode: %s (%dms)\n" +
                                UI.ORANGE + "      -> Expression: " + UI.WHITE_BOLD + "%s\n" +
                                UI.ORANGE_BOLD + "      -> Result: " + UI.GOLD_BOLD + "%s\n" + UI.RESET,
                        r.id, r.timestamp, r.query, r.mode, r.latencyMs, r.expression, r.result);
            }
        }
        System.out.println(UI.ASH_DARK + "===================================================================================\n" + UI.RESET);
    }

    private static void printAnalytics() {
        HistoryDatabase.DatabaseStats stats = HistoryDatabase.getInstance().getStats();
        System.out.println("\n" + UI.ASH_DARK + "=========================== " + UI.ORANGE_BOLD + "📊 CALCULATION ANALYTICS" + UI.ASH_DARK + " ===========================" + UI.RESET);
        System.out.println(UI.ASH + "  • Total Calculations: " + UI.ORANGE_BOLD + stats.totalRecords + UI.RESET);
        if (stats.totalRecords > 0) {
            System.out.println(UI.ASH + "  • Average Calculation Result: " + UI.WHITE_BOLD + stats.avgResult + UI.RESET);
            System.out.println(UI.ASH + "  • Minimum Result: " + UI.WHITE_BOLD + stats.minResult + UI.RESET);
            System.out.println(UI.ASH + "  • Maximum Result: " + UI.WHITE_BOLD + stats.maxResult + UI.RESET);
            System.out.println(UI.ASH + "  • Average Engine Latency: " + UI.ORANGE + stats.avgLatencyMs + " ms" + UI.RESET);
            System.out.println(UI.ASH + "  • Mode Distribution:" + UI.RESET);
            for (Map.Entry<String, Integer> e : stats.modeCounts.entrySet()) {
                System.out.println(UI.ASH + "      - " + e.getKey() + ": " + UI.ORANGE + e.getValue() + " queries" + UI.RESET);
            }
        }
        System.out.println(UI.ASH_DARK + "===================================================================================\n" + UI.RESET);
    }

    private static void printVariables(SessionContext session) {
        System.out.println("\n" + UI.ASH_DARK + "=========================== " + UI.ORANGE_BOLD + "🔢 SESSION VARIABLES" + UI.ASH_DARK + " ===========================" + UI.RESET);
        if (session.variables.isEmpty()) {
            System.out.println(UI.ASH + "  (No variables stored yet. Set one via: 'x = 50' or '/vars')" + UI.RESET);
        } else {
            for (Map.Entry<String, Double> entry : session.variables.entrySet()) {
                System.out.println(UI.ASH + "  • " + UI.ORANGE_BOLD + entry.getKey() + UI.ASH + " = " + UI.WHITE_BOLD + entry.getValue() + UI.RESET);
            }
        }
        System.out.println(UI.ASH_DARK + "===================================================================================\n" + UI.RESET);
    }

    private static String processDirect(String input, SessionContext session, String originalQuery, String mode, long startTime) {
        try {
            double result = MathEngine.evaluate(input, session.variables);
            long latency = System.currentTimeMillis() - startTime;
            session.record(result);
            HistoryDatabase.getInstance().record(mode, originalQuery, input, result, latency);
            return ResponseFormatter.formatResult(input, result);
        } catch (ArithmeticException e) {
            return ResponseFormatter.formatDivByZero();
        } catch (IllegalArgumentException e) {
            String fallback = FallbackParser.extractWithRegex(input);
            if (fallback != null) {
                try {
                    double result = MathEngine.evaluate(fallback, session.variables);
                    long latency = System.currentTimeMillis() - startTime;
                    session.record(result);
                    HistoryDatabase.getInstance().record(mode, originalQuery, fallback, result, latency);
                    return ResponseFormatter.formatResult(fallback, result);
                } catch (Exception ignored) {}
            }
            return ResponseFormatter.formatInvalid();
        }
    }

    private static String processNLP(String input, Config config, SessionContext session, String mode) {
        long startTime = System.currentTimeMillis();

        // 1. Fast Cache check
        String cached = MemoryStore.getInstance().getCachedExpression(input);
        if (cached != null) {
            try {
                double result = MathEngine.evaluate(cached, session.variables);
                long latency = System.currentTimeMillis() - startTime;
                session.record(result);
                HistoryDatabase.getInstance().record(mode + "_CACHED", input, cached, result, latency);
                return ResponseFormatter.formatResult(cached, result) + " (from memory cache ⚡)";
            } catch (Exception ignored) {}
        }

        // 2. Async call with responsive timeout & non-blocking fallback
        String expression = null;
        try {
            LLMClient llm = new LLMClient(config);
            Future<String> future = EXECUTOR.submit(() -> llm.sendPrompt(input, session.lastResult));
            String raw = future.get(8, TimeUnit.SECONDS); // 8 second timeout
            JsonParser.ParseResult parsed = JsonParser.extractExpression(raw);
            if (!parsed.hasError() && parsed.confidence >= 0.75) {
                expression = parsed.expression;
            }
        } catch (TimeoutException e) {
            System.out.println(UI.ASH + "  [info] LLM timed out, switching to instant regex parser..." + UI.RESET);
        } catch (Exception e) {
            // Regex fallback
        }

        if (expression == null || expression.isEmpty()) {
            expression = FallbackParser.extractWithRegex(input);
            if (expression == null) {
                return ResponseFormatter.formatNoMath();
            }
        }

        String resultStr = processDirect(expression, session, input, mode, startTime);
        if (!resultStr.startsWith("I couldn't") && !resultStr.startsWith("That doesn't")) {
            MemoryStore.getInstance().autoLearn(input, expression);
        }
        return resultStr;
    }

    private static void printConstants() {
        System.out.println("\n" + UI.ASH_DARK + "=========================== " + UI.ORANGE_BOLD + "🔬 UNIVERSAL & SCIENTIFIC CONSTANTS" + UI.ASH_DARK + " ===========================" + UI.RESET);
        for (ScientificConstants.ConstantInfo c : ScientificConstants.getAll().values()) {
            System.out.printf(UI.ORANGE_BOLD + "  • %-12s" + UI.WHITE_BOLD + "%-30s" + UI.GOLD_BOLD + "%-20s" + UI.ASH + "%-15s" + UI.ASH_LIGHT + "%s\n" + UI.RESET,
                    c.symbol, c.name, ResponseFormatter.formatNumber(c.value), c.unit, c.description);
        }
        System.out.println(UI.ASH_DARK + "===================================================================================================\n" + UI.RESET);
    }

    private static void handleSolveCommand(String equation) {
        if (equation.isEmpty()) {
            System.out.println(UI.ASH + "Usage: /solve <quadratic equation> (e.g. /solve x^2 - 5x + 6 = 0)\n" + UI.RESET);
            return;
        }
        // Quadratic regex: ax^2 + bx + c = 0
        String clean = equation.replaceAll("\\s+", "").replace("=0", "").replace("-", "+-");
        double a = 0, b = 0, c = 0;
        String[] terms = clean.split("\\+");
        for (String term : terms) {
            if (term.isEmpty()) continue;
            if (term.contains("x^2")) {
                String coeff = term.replace("x^2", "");
                a += coeff.isEmpty() ? 1 : (coeff.equals("-") ? -1 : Double.parseDouble(coeff));
            } else if (term.contains("x")) {
                String coeff = term.replace("x", "");
                b += coeff.isEmpty() ? 1 : (coeff.equals("-") ? -1 : Double.parseDouble(coeff));
            } else {
                c += Double.parseDouble(term);
            }
        }
        if (a == 0 && b == 0) {
            System.out.println(UI.ORANGE + "⚠️  Could not parse equation: " + equation + "\n" + UI.RESET);
            return;
        }
        SymbolicEngine.QuadraticSolution sol = SymbolicEngine.solveQuadratic(a, b, c);
        System.out.println(UI.ORANGE_BOLD + "📐 Quadratic Solution for " + equation + ":\n" + UI.RESET +
                UI.ASH + "   " + UI.GOLD_BOLD + sol + "\n" + UI.RESET);
    }

    private static void handleComplexCommand(String expr) {
        if (expr.isEmpty()) {
            System.out.println(UI.ASH + "Usage: /complex <complex expression> (e.g. /complex (3+4i) * (1-2i))\n" + UI.RESET);
            return;
        }
        try {
            if (expr.contains("*")) {
                String[] parts = expr.split("\\*");
                ComplexNumber c1 = ComplexNumber.parse(parts[0].replaceAll("[()]", ""));
                ComplexNumber c2 = ComplexNumber.parse(parts[1].replaceAll("[()]", ""));
                ComplexNumber res = c1.multiply(c2);
                System.out.println(UI.ORANGE_BOLD + "✨ Complex Product: " + UI.GOLD_BOLD + res + UI.ASH + " | Polar: " + res.toPolarString() + "\n" + UI.RESET);
            } else if (expr.contains("/")) {
                String[] parts = expr.split("/");
                ComplexNumber c1 = ComplexNumber.parse(parts[0].replaceAll("[()]", ""));
                ComplexNumber c2 = ComplexNumber.parse(parts[1].replaceAll("[()]", ""));
                ComplexNumber res = c1.divide(c2);
                System.out.println(UI.ORANGE_BOLD + "✨ Complex Quotient: " + UI.GOLD_BOLD + res + UI.ASH + " | Polar: " + res.toPolarString() + "\n" + UI.RESET);
            } else {
                ComplexNumber c = ComplexNumber.parse(expr.replaceAll("[()]", ""));
                System.out.println(UI.ORANGE_BOLD + "✨ Complex Number: " + UI.GOLD_BOLD + c + UI.ASH +
                        " | |z| = " + ResponseFormatter.formatNumber(c.magnitude()) +
                        " | θ = " + String.format("%.2f°", c.phaseDegrees()) +
                        " | Polar: " + c.toPolarString() + "\n" + UI.RESET);
            }
        } catch (Exception e) {
            System.out.println(UI.ORANGE + "⚠️  Complex error: " + e.getMessage() + "\n" + UI.RESET);
        }
    }

    private static void handleMatrixMenu(Scanner scanner) {
        System.out.println("\n" + UI.ASH_DARK + "--- " + UI.ORANGE_BOLD + "🔢 Matrix & Vector Calculator" + UI.ASH_DARK + " ---" + UI.RESET);
        System.out.println("  [1] Determinant (2x2 or 3x3)");
        System.out.println("  [2] Matrix Inverse");
        System.out.println("  [3] Matrix Multiplication");
        System.out.println("  [4] 3D Vector Cross Product");
        System.out.print("Select (1-4): ");
        if (!scanner.hasNextLine()) return;
        String choice = scanner.nextLine().trim();

        try {
            if (choice.equals("1")) {
                System.out.print("Enter matrix (e.g. [[1,2],[3,4]]): ");
                String matStr = scanner.nextLine().trim();
                MatrixEngine.Matrix m = MatrixEngine.Matrix.parse(matStr);
                System.out.println(UI.ORANGE_BOLD + "det(A) = " + UI.GOLD_BOLD + m.determinant() + "\n" + UI.RESET);
            } else if (choice.equals("2")) {
                System.out.print("Enter matrix (e.g. [[1,2],[3,4]]): ");
                String matStr = scanner.nextLine().trim();
                MatrixEngine.Matrix m = MatrixEngine.Matrix.parse(matStr);
                System.out.println(UI.ORANGE_BOLD + "A⁻¹ =\n" + UI.GOLD_BOLD + m.inverse() + "\n" + UI.RESET);
            } else if (choice.equals("3")) {
                System.out.print("Enter Matrix A (e.g. [[1,2],[3,4]]): ");
                String m1Str = scanner.nextLine().trim();
                System.out.print("Enter Matrix B (e.g. [[2,0],[1,2]]): ");
                String m2Str = scanner.nextLine().trim();
                MatrixEngine.Matrix m1 = MatrixEngine.Matrix.parse(m1Str);
                MatrixEngine.Matrix m2 = MatrixEngine.Matrix.parse(m2Str);
                System.out.println(UI.ORANGE_BOLD + "A * B =\n" + UI.GOLD_BOLD + m1.multiply(m2) + "\n" + UI.RESET);
            } else if (choice.equals("4")) {
                System.out.print("Enter Vector A (x, y, z): ");
                String[] aTokens = scanner.nextLine().trim().split("[,\\s]+");
                System.out.print("Enter Vector B (x, y, z): ");
                String[] bTokens = scanner.nextLine().trim().split("[,\\s]+");
                double[] a = { Double.parseDouble(aTokens[0]), Double.parseDouble(aTokens[1]), Double.parseDouble(aTokens[2]) };
                double[] b = { Double.parseDouble(bTokens[0]), Double.parseDouble(bTokens[1]), Double.parseDouble(bTokens[2]) };
                double[] cross = MatrixEngine.vectorCross3D(a, b);
                System.out.printf(UI.ORANGE_BOLD + "A × B = [%.4f, %.4f, %.4f]\n\n" + UI.RESET, cross[0], cross[1], cross[2]);
            }
        } catch (Exception e) {
            System.out.println(UI.ORANGE + "⚠️  Matrix error: " + e.getMessage() + "\n" + UI.RESET);
        }
    }

    private static void handleBaseCommand(String arg) {
        if (arg.isEmpty()) {
            System.out.println(UI.ASH + "Usage: /base <number or roman numeral> (e.g. /base 255, /base 0xFF, /base MMXXVI)\n" + UI.RESET);
            return;
        }
        try {
            String clean = arg.trim();
            if (clean.matches("^[IVXLCDMivxlcdm]+$")) {
                int dec = BaseConverter.fromRoman(clean);
                System.out.println(UI.ORANGE_BOLD + "Roman Numeral \"" + clean.toUpperCase() + "\" = " + UI.GOLD_BOLD + dec + "\n" + UI.RESET);
            } else if (clean.startsWith("0x") || clean.startsWith("0X")) {
                String dec = BaseConverter.fromHexToDecimal(clean);
                System.out.println(UI.ORANGE_BOLD + "Hex " + clean + " = " + UI.GOLD_BOLD + dec + " (Decimal)\n" + UI.RESET);
            } else if (clean.startsWith("0b") || clean.startsWith("0B")) {
                String dec = BaseConverter.fromBinToDecimal(clean);
                String hex = BaseConverter.fromBinToHex(clean);
                System.out.println(UI.ORANGE_BOLD + "Binary " + clean + " = " + UI.GOLD_BOLD + dec + " (Decimal)" + UI.ASH + " | 0x" + hex + " (Hex)\n" + UI.RESET);
            } else {
                String bin = BaseConverter.toBinary(clean);
                String hex = BaseConverter.toHex(clean);
                String oct = BaseConverter.toOctal(clean);
                int num = Integer.parseInt(clean);
                String roman = (num > 0 && num < 4000) ? BaseConverter.toRoman(num) : "N/A";
                System.out.println(UI.ORANGE_BOLD + "Radix Conversions for " + clean + ":\n" + UI.RESET +
                        UI.ASH + "  • Binary:      " + UI.WHITE_BOLD + bin + "\n" +
                        UI.ASH + "  • Hexadecimal: " + UI.WHITE_BOLD + "0x" + hex + "\n" +
                        UI.ASH + "  • Octal:       " + UI.WHITE_BOLD + oct + "\n" +
                        UI.ASH + "  • Roman:       " + UI.WHITE_BOLD + roman + "\n" + UI.RESET);
            }
        } catch (Exception e) {
            System.out.println(UI.ORANGE + "⚠️  Base conversion error: " + e.getMessage() + "\n" + UI.RESET);
        }
    }

    private static void handleFinanceMenu(Scanner scanner) {
        System.out.println("\n" + UI.ASH_DARK + "--- " + UI.ORANGE_BOLD + "💰 Financial Calculator Engine" + UI.ASH_DARK + " ---" + UI.RESET);
        System.out.println("  [1] Compound Interest");
        System.out.println("  [2] Loan EMI Calculator");
        System.out.println("  [3] SIP (Systematic Investment Plan) Future Value");
        System.out.print("Select (1-3): ");
        if (!scanner.hasNextLine()) return;
        String choice = scanner.nextLine().trim();

        try {
            if (choice.equals("1")) {
                System.out.print("Principal Amount: ");
                double p = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("Annual Interest Rate (%): ");
                double r = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("Compounding Frequency per Year (e.g. 1, 4, 12): ");
                int n = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Duration (Years): ");
                double t = Double.parseDouble(scanner.nextLine().trim());
                System.out.println("\n" + FormulaEngine.compoundInterest(p, r, n, t) + "\n");
            } else if (choice.equals("2")) {
                System.out.print("Loan Principal: ");
                double p = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("Annual Interest Rate (%): ");
                double r = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("Loan Tenure (Months): ");
                int m = Integer.parseInt(scanner.nextLine().trim());
                System.out.println("\n" + FormulaEngine.loanEmi(p, r, m) + "\n");
            } else if (choice.equals("3")) {
                System.out.print("Monthly Investment Amount: ");
                double p = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("Expected Annual Return (%): ");
                double r = Double.parseDouble(scanner.nextLine().trim());
                System.out.print("Duration (Months): ");
                int m = Integer.parseInt(scanner.nextLine().trim());
                System.out.println("\n" + FormulaEngine.sipCalculator(p, r, m) + "\n");
            }
        } catch (Exception e) {
            System.out.println(UI.ORANGE + "⚠️  Finance error: " + e.getMessage() + "\n" + UI.RESET);
        }
    }

    private static void printTestResults(List<TestSuite.TestResult> results) {
        System.out.println("\n" + UI.ASH_DARK + "=========================== " + UI.ORANGE_BOLD + "🧪 JLC AUTOMATED TEST SUITE REPORT" + UI.ASH_DARK + " ===========================" + UI.RESET);
        int passed = 0;
        for (TestSuite.TestResult tr : results) {
            if (tr.passed) {
                passed++;
                System.out.printf(UI.ORANGE_BOLD + "  ✅ [%-12s] " + UI.WHITE_BOLD + "%-35s" + UI.ASH + "%s\n" + UI.RESET, tr.category, tr.name, tr.details);
            } else {
                System.out.printf(UI.ORANGE + "  ❌ [%-12s] " + UI.WHITE_BOLD + "%-35s" + UI.ORANGE + "%s\n" + UI.RESET, tr.category, tr.name, tr.details);
            }
        }
        System.out.println(UI.ASH_DARK + "---------------------------------------------------------------------------------------------------" + UI.RESET);
        System.out.printf(UI.ORANGE_BOLD + "  Summary: %d / %d Tests Passed (%.1f%% Accuracy)\n" + UI.RESET, passed, results.size(), (double) passed / results.size() * 100.0);
        System.out.println(UI.ASH_DARK + "===================================================================================================\n" + UI.RESET);
    }
}
