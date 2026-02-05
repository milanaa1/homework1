package com.example.hangman.cli;

import com.example.hangman.engine.*;

import java.util.Scanner;

public class ConsoleUI {

    private final GameEngine engine;

    public ConsoleUI(GameEngine engine) {
        this.engine = engine;
    }

    public void runInteractive(CliArgs cliArgs) {
        java.util.Scanner sc = new java.util.Scanner(System.in);

        java.util.Optional<com.example.hangman.engine.Category> category = cliArgs.category();
        java.util.Optional<com.example.hangman.engine.Difficulty> difficulty = cliArgs.difficulty();

        //выбор категории
        if (category.isEmpty()) {
            System.out.println("Выберите категорию или нажмите Enter для случайной:");
            for (com.example.hangman.engine.Category c : com.example.hangman.engine.Category.values()) {
                System.out.println(" - " + c);
            }
            System.out.print("> ");
            String input = sc.nextLine().trim();

            if (!input.isEmpty()) {
                try {
                    category = java.util.Optional.of(com.example.hangman.engine.Category.valueOf(input.toUpperCase()));
                } catch (Exception ignored) {
                    System.out.println("Неверная категория, будет выбрана случайно.");
                }
            }
        }

        // если сложность не передана аргументами спрашиваем
        if (difficulty.isEmpty()) {
            System.out.println("\nВыберите сложность или нажмите Enter для случайной:");
            for (com.example.hangman.engine.Difficulty d : com.example.hangman.engine.Difficulty.values()) {
                System.out.println(" - " + d);
            }
            System.out.print("> ");
            String input = sc.nextLine().trim();

            if (!input.isEmpty()) {
                try {
                    difficulty = java.util.Optional.of(com.example.hangman.engine.Difficulty.valueOf(input.toUpperCase()));
                } catch (Exception ignored) {
                    System.out.println("Неверная сложность, будет выбрана случайно");
                }
            }
        }

        // старт игры
        com.example.hangman.engine.GameSession session = engine.startNewGame(
                category,
                difficulty,
                cliArgs.maxErrors()
        );

        while (session.state() == com.example.hangman.engine.GameState.IN_PROGRESS) {
            clearScreen();

            System.out.println("ВИСЕЛИЦА");
            System.out.println("Категория: " + session.category());
            System.out.println("Сложность: " + session.difficulty());
            System.out.println("Осталось попыток: " + (session.maxErrors() - session.errorsMade()));
            System.out.println("Подсказка: " + session.hint());
            System.out.println();

            System.out.println(com.example.hangman.engine.HangmanStages.stage(session.errorsMade(), session.maxErrors()));
            System.out.println("Слово: " + session.maskedWord());
            System.out.println("Ошибки: " + session.wrongLetters());
            System.out.println();

            System.out.print("Введите букву: ");
            String input = sc.nextLine();

            com.example.hangman.engine.GuessResult r = engine.guess(session, input);
            System.out.println(r.message());
            sleep(r.status() == com.example.hangman.engine.GuessStatus.INVALID_INPUT ? 900 : 650);
        }

        clearScreen();
        System.out.println(com.example.hangman.engine.HangmanStages.stage(session.errorsMade(), session.maxErrors()));

        if (session.state() == com.example.hangman.engine.GameState.WON) {
            System.out.println(" Победа! Слово: " + session.answer());
        } else {
            System.out.println(" Поражение! Слово было: " + session.answer());
        }
    }


    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
