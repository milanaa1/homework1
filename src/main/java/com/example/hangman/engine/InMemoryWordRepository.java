package com.example.hangman.engine;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class InMemoryWordRepository implements WordRepository {

    private final Map<Category, List<WordEntry>> data = new EnumMap<>(Category.class);

    public InMemoryWordRepository() {
        data.put(Category.ANIMALS, List.of(
                new WordEntry("бабушка", "близкий родственник"),
                new WordEntry("крокодил", "большая рептилия"),
                new WordEntry("дельфин", "умное морское животное"),
                new WordEntry("жираф", "животное с длинной шеей")
        ));

        data.put(Category.FOOD, List.of(
                new WordEntry("пельмени", "традиционное блюдо"),
                new WordEntry("шоколад", "сладость из какао"),
                new WordEntry("макароны", "изделия из теста"),
                new WordEntry("борщ", "суп со свёклой")
        ));

        data.put(Category.CITIES, List.of(
                new WordEntry("таллин", "столица Эстонии"),
                new WordEntry("париж", "город любви"),
                new WordEntry("варшава", "столица Польши"),
                new WordEntry("берлин", "столица Германии")
        ));

        data.put(Category.TECH, List.of(
                new WordEntry("компилятор", "переводит код в машинный"),
                new WordEntry("интерфейс", "способ взаимодействия"),
                new WordEntry("алгоритм", "последовательность шагов"),
                new WordEntry("рефакторинг", "улучшение кода без изменения поведения")
        ));
    }

    @Override
    public List<WordEntry> words(Category category) {
        return data.getOrDefault(category, List.of());
    }
}

