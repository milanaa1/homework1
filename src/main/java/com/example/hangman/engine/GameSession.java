package com.example.hangman.engine;

import java.util.LinkedHashSet;
import java.util.Set;

public class GameSession {

    private final Category category;
    private final Difficulty difficulty;

    private final String answer;
    private final String hint;
    private final int maxErrors;

    private final Set<Character> correct = new LinkedHashSet<>();
    private final Set<Character> wrong = new LinkedHashSet<>();

    private GameState state = GameState.IN_PROGRESS;

    public GameSession(Category category, Difficulty difficulty, String answer, String hint, int maxErrors) {
        this.category = category;
        this.difficulty = difficulty;
        this.answer = answer;
        this.hint = hint;
        this.maxErrors = maxErrors;
    }

    public Category category() { return category; }
    public Difficulty difficulty() { return difficulty; }
    public String answer() { return answer; }
    public String hint() { return hint; }
    public int maxErrors() { return maxErrors; }
    public int errorsMade() { return wrong.size(); }
    public GameState state() { return state; }

    public Set<Character> correctLetters() { return correct; }
    public Set<Character> wrongLetters() { return wrong; }

    boolean alreadyTried(char c) { return correct.contains(c) || wrong.contains(c); }
    void addCorrect(char c) { correct.add(c); }
    void addWrong(char c) { wrong.add(c); }
    void setState(GameState s) { this.state = s; }

    public boolean isFullyGuessed() {
        for (int i = 0; i < answer.length(); i++) {
            char ch = answer.charAt(i);
            if (Character.isLetter(ch) && !correct.contains(ch)) return false;
        }
        return true;
    }

    public String maskedWord() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < answer.length(); i++) {
            char ch = answer.charAt(i);
            if (!Character.isLetter(ch)) sb.append(ch);
            else if (correct.contains(ch)) sb.append(ch);
            else sb.append('_');
            if (i < answer.length() - 1) sb.append(' ');
        }
        return sb.toString();
    }
}

