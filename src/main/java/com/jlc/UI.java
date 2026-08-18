package com.jlc;

public class UI {

    // Color Palette: Orange, Ash / Slate Gray, Dark Black Accents
    public static final String RESET = "\u001B[0m";
    public static final String ORANGE = "\u001B[38;5;208m";
    public static final String ORANGE_BOLD = "\u001B[1;38;5;208m";
    public static final String GOLD = "\u001B[38;5;214m";
    public static final String GOLD_BOLD = "\u001B[1;38;5;214m";
    public static final String ASH = "\u001B[38;5;245m";
    public static final String ASH_LIGHT = "\u001B[38;5;250m";
    public static final String ASH_DARK = "\u001B[38;5;238m";
    public static final String WHITE_BOLD = "\u001B[1;37m";
    public static final String BLACK = "\u001B[30m";
    public static final String BG_ORANGE = "\u001B[48;5;208m";
    public static final String BG_ASH = "\u001B[48;5;236m";

    public static void printBanner() {
        System.out.println(ASH_DARK + "==========================================================================================" + RESET);
        System.out.println(ORANGE_BOLD + 
            "       _   ___     ___     _    _      __  __   ___ __  __   _   ___ _____ \n" +
            "    _ | | /   \\   /   \\   | |  | |    |  \\/  | / __|  \\/  | /_\\ | _ \\_   _|\n" +
            "   | || |/ /\\  \\ / /\\  \\  | |__| |__  | |\\/| | \\__ \\ |\\/| |/ _ \\|   / | |  \n" +
            "    \\__/|_/  \\__\\_/  \\__\\ |____|____|_|_|  |_|_|___/_|  |_/_/ \\_\\_|_\\ |_|  \n" +
            "          ___   _   _    ___ _   _ _      _ _____ ___  ___                 \n" +
            "         / __| /_\\ | |  / __| | | | |    /_\\_   _/ _ \\| _ \\                \n" +
            "        | (__ / _ \\| |_| (__| |_| | |__ / _ \\| || (_) |   /                \n" +
            "         \\___/_/ \\_\\____\\___|\\___/|____/_/ \\_\\_| \\___/|_|_\\                " + RESET);
        System.out.println(ASH_LIGHT + "             🧮 Smart Natural Language & Direct Precision Math Engine 🤖" + RESET);
        System.out.println(ORANGE_BOLD + "             👑 Author: KABALI GAMER" + RESET + ASH + " | Version 1.0.0-PRO" + RESET);
        System.out.println(ASH + "             Tip: Type " + ORANGE_BOLD + "/help" + ASH + " anytime to see slash commands (/history, /switch, /stats)" + RESET);
        System.out.println(ASH_DARK + "==========================================================================================" + RESET);
    }

    public static void printMainMenu() {
        System.out.println("\n" + ASH_DARK + "-------------------------------- " + ORANGE_BOLD + "SELECT MODE" + ASH_DARK + " ---------------------------------" + RESET);
        System.out.println("  " + ORANGE_BOLD + "[1]" + RESET + " " + WHITE_BOLD + "💬 AI Chat Mode" + RESET + ASH + "       - Natural language, word problems & multi-turn dialog" + RESET);
        System.out.println("  " + ORANGE_BOLD + "[2]" + RESET + " " + WHITE_BOLD + "🔢 Direct Math Mode" + RESET + ASH + "   - Instant numeric expressions, equations & variables" + RESET);
        System.out.println("  " + ORANGE_BOLD + "[3]" + RESET + " " + WHITE_BOLD + "🧠 Learned Memory" + RESET + ASH + "     - View custom vocabulary rules & dynamic cache" + RESET);
        System.out.println("  " + ORANGE_BOLD + "[4]" + RESET + " " + WHITE_BOLD + "📜 History Database" + RESET + ASH + "   - Permanent audit log, search, analytics & CSV export" + RESET);
        System.out.println("  " + ORANGE_BOLD + "[5]" + RESET + " " + WHITE_BOLD + "❌ Exit" + RESET);
        System.out.println(ASH_DARK + "------------------------------------------------------------------------------------------" + RESET);
        System.out.print(ORANGE_BOLD + "Enter choice (1-5)" + ASH + " [or type /help]: " + RESET);
    }

    public static void printSlashHelp() {
        System.out.println("\n" + ASH_DARK + "================================= " + ORANGE_BOLD + "⚡ JLC SCIENTIFIC COMMAND SUITE" + ASH_DARK + " =================================" + RESET);
        System.out.println(ORANGE_BOLD + "  📊 Core Calculations & Database:" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/history" + RESET + " or " + ORANGE_BOLD + "/h" + RESET + ASH + "          - View permanent calculation audit history" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/search <keyword>" + RESET + ASH + "      - Search history by formula, keyword, or number" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/stats" + RESET + ASH + "                  - View calculation analytics, latency & metrics" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/switch <1|2|3|4>" + RESET + ASH + "      - Switch mode: [1] AI Chat, [2] Direct Math, [3] Memory, [4] DB" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/vars" + RESET + ASH + "                   - View active session variables (ans, x, y...)" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/memory" + RESET + " or " + ORANGE_BOLD + "/rules" + RESET + ASH + "     - View learned vocabulary rules & dynamic cache" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/recall <#id>" + RESET + " or " + ORANGE_BOLD + "#<id>" + RESET + ASH + " - Recall previous result from database into 'ans'" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/export" + RESET + ASH + "                 - Export permanent history to CSV file" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/clear" + RESET + " or " + ORANGE_BOLD + "/clear-cache" + RESET + ASH + " - Clear memory cache & reset learned rules" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/clear-history" + RESET + ASH + "          - Clear permanent calculation database" + RESET);
        System.out.println();
        System.out.println(ORANGE_BOLD + "  🔬 Scientific, Engineering & Math Tools:" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/plot <expr>" + RESET + ASH + "            - Plot 2D ASCII graphs (e.g. '/plot sin(x)', '/plot x^2 - 4')" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/convert <val> <from> to <to>" + RESET + ASH + " - Convert units (e.g. '/convert 5 km to miles')" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/const" + RESET + " or " + ORANGE_BOLD + "/constants" + RESET + ASH + " - Inspect 30+ physical, astronomical & math constants" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/solve <equation>" + RESET + ASH + "       - Solve quadratic equations (e.g. '/solve x^2 - 5x + 6 = 0')" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/diff <polynomial>" + RESET + ASH + "     - Symbolic differentiation (e.g. '/diff 3x^3 + 5x^2 - 4x')" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/complex <expr>" + RESET + ASH + "         - Complex numbers (e.g. '/complex (3+4i) * (1-2i)')" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/matrix" + RESET + ASH + "                 - Matrix determinant, inverse, multiplication & dot/cross products" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/base <val>" + RESET + ASH + "             - Base conversions: Hex, Binary, Octal, Decimal, Roman" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/finance" + RESET + ASH + "                - Financial calculators: Compound Interest, EMI, SIP" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/benchmark" + RESET + ASH + "              - Run performance benchmark against standard Java Math" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/test" + RESET + ASH + "                   - Run automated 10-domain unit test suite" + RESET);
        System.out.println();
        System.out.println(ORANGE_BOLD + "  ⚙️ General Navigation:" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/skip" + RESET + ASH + "                   - Skip current prompt or operation" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/menu" + RESET + " or " + ORANGE_BOLD + "/back" + RESET + ASH + "       - Return to main mode menu" + RESET);
        System.out.println("    " + ORANGE_BOLD + "/exit" + RESET + " or " + ORANGE_BOLD + "/quit" + RESET + ASH + "       - Exit the application" + RESET);
        System.out.println(ASH_DARK + "===================================================================================================\n" + RESET);
    }
}
