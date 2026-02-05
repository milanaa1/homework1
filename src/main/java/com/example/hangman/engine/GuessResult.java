package com.example.hangman.engine;

public record GuessResult (
    GuessStatus status,
    GameState state,
    int errorsMade,
    int maxErrors,
    String message
) {}
