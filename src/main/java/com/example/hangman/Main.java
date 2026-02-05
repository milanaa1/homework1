package com.example.hangman;

import com.example.hangman.cli.ArgsParser;
import com.example.hangman.cli.CliArgs;
import com.example.hangman.cli.ConsoleUI;
import com.example.hangman.engine.GameEngine;
import com.example.hangman.engine.InMemoryWordRepository;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        GameEngine engine = new GameEngine(new InMemoryWordRepository(), new Random());
        ConsoleUI ui = new ConsoleUI(engine);

        CliArgs cliArgs = ArgsParser.parse(args);
        ui.runInteractive(cliArgs);
    }
}


