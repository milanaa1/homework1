package com.example.hangman.engine;

import java.util.List;

public interface WordRepository {
    List<WordEntry> words(Category category);
}

