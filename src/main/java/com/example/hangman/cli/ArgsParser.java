package com.example.hangman.cli;

import com.example.hangman.engine.Category;
import com.example.hangman.engine.Difficulty;

import java.util.Optional;

public final class ArgsParser {
    private ArgsParser() {}

    public static CliArgs parse(String[] args) {
        if (args == null || args.length == 0) return CliArgs.empty();

        Optional<Category> category = Optional.empty();
        Optional<Difficulty> difficulty = Optional.empty();
        Optional<Integer> maxErrors = Optional.empty();

        for (int i = 0; i < args.length; i++) {
            String a = args[i];

            if (a.startsWith("--category=")) {
                category = parseCategory(a.substring("--category=".length()));
            } else if (a.equals("-c") && i + 1 < args.length) {
                category = parseCategory(args[++i]);
            } else if (a.startsWith("--difficulty=")) {
                difficulty = parseDifficulty(a.substring("--difficulty=".length()));
            } else if (a.equals("-d") && i + 1 < args.length) {
                difficulty = parseDifficulty(args[++i]);
            } else if (a.startsWith("--maxErrors=")) {
                maxErrors = parseIntOpt(a.substring("--maxErrors=".length()));
            } else if (a.equals("-m") && i + 1 < args.length) {
                maxErrors = parseIntOpt(args[++i]);
            }
        }

        return new CliArgs(category, difficulty, maxErrors);
    }

    private static Optional<Category> parseCategory(String raw) {
        try {
            return Optional.of(Category.valueOf(raw.trim().toUpperCase()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Optional<Difficulty> parseDifficulty(String raw) {
        try {
            return Optional.of(Difficulty.valueOf(raw.trim().toUpperCase()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static Optional<Integer> parseIntOpt(String raw) {
        try {
            return Optional.of(Integer.parseInt(raw.trim()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
