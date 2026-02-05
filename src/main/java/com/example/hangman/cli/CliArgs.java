package com.example.hangman.cli;

import com.example.hangman.engine.Category;
import com.example.hangman.engine.Difficulty;

import java.util.Optional;

public record CliArgs(
        Optional<Category> category,
        Optional<Difficulty> difficulty,
        Optional<Integer> maxErrors
) {
    public static CliArgs empty() {
        return new CliArgs(Optional.empty(), Optional.empty(), Optional.empty());
    }
}
