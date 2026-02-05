package com.example.hangman.engine;

public enum Difficulty {
    EASY, MEDIUM, HARD;

    public int defaultMaxErrors() {
        return switch (this) {
            case EASY -> 8;
            case MEDIUM -> 6;
            case HARD -> 5;
        };
    }

    public int minWordLength() {
        return switch (this) {
            case EASY -> 4;
            case MEDIUM -> 6;
            case HARD -> 8;
        };
    }

    public int maxWordLength() {
        return switch (this) {
            case EASY -> 7;
            case MEDIUM -> 10;
            case HARD -> 30;
        };
    }
}
