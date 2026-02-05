package com.example.hangman.engine;

public final class HangmanStages {
    private HangmanStages() {}

    public static String stage(int errors, int maxErrors) {
        int normalized = normalize(errors, maxErrors);

        return switch (normalized) {
            case 0 -> """
                 +---+
                 |   |
                     |
                     |
                     |
                     |
                =========
                """;
            case 1 -> """
                 +---+
                 |   |
                 O   |
                     |
                     |
                     |
                =========
                """;
            case 2 -> """
                 +---+
                 |   |
                 O   |
                 |   |
                     |
                     |
                =========
                """;
            case 3 -> """
                 +---+
                 |   |
                 O   |
                /|   |
                     |
                     |
                =========
                """;
            case 4 -> """
                 +---+
                 |   |
                 O   |
                /|\\  |
                     |
                     |
                =========
                """;
            case 5 -> """
                 +---+
                 |   |
                 O   |
                /|\\  |
                /    |
                     |
                =========
                """;
            default -> """
                 +---+
                 |   |
                 O   |
                /|\\  |
                / \\  |
                     |
                =========
                """;
        };
    }

    private static int normalize(int errors, int maxErrors) {
        if (maxErrors <= 0) return 6;
        double ratio = Math.min(1.0, Math.max(0.0, (double) errors / (double) maxErrors));
        return (int) Math.round(ratio * 6.0);
    }
}
