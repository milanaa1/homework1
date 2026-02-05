package com.example.hangman.engine;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GameEngine {

    private final WordRepository repo;
    private final Random random;

    public GameEngine(WordRepository repo, Random random) {
        this.repo = repo;
        this.random = random;
    }

    public GameSession startNewGame(
            Optional<Category> categoryOpt,
            Optional<Difficulty> difficultyOpt,
            Optional<Integer> maxErrorsOpt
    ) {
        Category category = categoryOpt.orElseGet(this::randomCategory);
        Difficulty difficulty = difficultyOpt.orElseGet(this::randomDifficulty);

        WordEntry entry = pickWord(category, difficulty);
        String answer = normalize(entry.word());
        String hint = entry.hint();

        validateAnswerOrThrow(answer, difficulty);

        int maxErrors = maxErrorsOpt.orElse(difficulty.defaultMaxErrors());
        if (maxErrors <= 0) {
            throw new IllegalArgumentException("maxErrors должно быть > 0");
        }

        return new GameSession(category, difficulty, answer, hint, maxErrors);
    }

    public GuessResult guess(GameSession session, String rawInput) {
        if (session.state() != GameState.IN_PROGRESS) {
            return new GuessResult(
                    GuessStatus.INVALID_INPUT,
                    session.state(),
                    session.errorsMade(),
                    session.maxErrors(),
                    "Игра уже завершена."
            );
        }

        String input = rawInput == null ? "" : rawInput.trim().toLowerCase();

        // если ввод больше 1 символа, то повтор ввода, состояние не меняем
        if (input.length() != 1) {
            return new GuessResult(
                    GuessStatus.INVALID_INPUT,
                    session.state(),
                    session.errorsMade(),
                    session.maxErrors(),
                    "Введите ровно одну букву"
            );
        }

        char c = input.charAt(0);

        if (!Character.isLetter(c)) {
            return new GuessResult(
                    GuessStatus.INVALID_INPUT,
                    session.state(),
                    session.errorsMade(),
                    session.maxErrors(),
                    "Нужно ввести букву"
            );
        }

        if (session.alreadyTried(c)) {
            return new GuessResult(
                    GuessStatus.ALREADY_TRIED,
                    session.state(),
                    session.errorsMade(),
                    session.maxErrors(),
                    "Эта буква уже была"
            );
        }

        if (contains(session.answer(), c)) {
            session.addCorrect(c);

            if (session.isFullyGuessed()) {
                session.setState(GameState.WON);
                return new GuessResult(
                        GuessStatus.CORRECT,
                        session.state(),
                        session.errorsMade(),
                        session.maxErrors(),
                        "Верно! Слово угадано полностью"
                );
            }

            return new GuessResult(
                    GuessStatus.CORRECT,
                    session.state(),
                    session.errorsMade(),
                    session.maxErrors(),
                    "Верно!"
            );
        } else {
            session.addWrong(c);

            if (session.errorsMade() >= session.maxErrors()) {
                session.setState(GameState.LOST);
                return new GuessResult(
                        GuessStatus.WRONG,
                        session.state(),
                        session.errorsMade(),
                        session.maxErrors(),
                        "Неверно. Ошибки закончились."
                );
            }

            int left = session.maxErrors() - session.errorsMade();
            return new GuessResult(
                    GuessStatus.WRONG,
                    session.state(),
                    session.errorsMade(),
                    session.maxErrors(),
                    "Неверно. Осталось попыток: " + left
            );
        }
    }


    private Category randomCategory() {
        Category[] values = Category.values();
        return values[random.nextInt(values.length)];
    }

    private Difficulty randomDifficulty() {
        Difficulty[] values = Difficulty.values();
        return values[random.nextInt(values.length)];
    }

    private WordEntry pickWord(Category category, Difficulty difficulty) {
        List<WordEntry> words = repo.words(category);
        if (words == null || words.isEmpty()) {
            throw new IllegalStateException("В категории нет слов: " + category);
        }

        List<WordEntry> filtered = words.stream()
                .map(w -> new WordEntry(normalize(w.word()), w.hint()))
                .filter(w -> isLengthOk(w.word(), difficulty))
                .toList();

        if (filtered.isEmpty()) {
            throw new IllegalStateException("Нет слов подходящей длины для " + category + " / " + difficulty);
        }

        return filtered.get(random.nextInt(filtered.size()));
    }

    private static boolean isLengthOk(String word, Difficulty d) {
        int len = word.length();
        return len >= d.minWordLength() && len <= d.maxWordLength();
    }

    private static void validateAnswerOrThrow(String answer, Difficulty difficulty) {
        if (answer == null) {
            throw new IllegalArgumentException("Слово null");
        }
        String trimmed = answer.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Слово пустое");
        }
        if (!isLengthOk(trimmed, difficulty)) {
            throw new IllegalArgumentException("Слово некорректной длины для сложности: " + trimmed);
        }
    }

    private static String normalize(String word) {
        return word == null ? "" : word.trim().toLowerCase();
    }

    private static boolean contains(String answer, char c) {
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == c) return true;
        }
        return false;
    }
}
