package org.themullers.vocab;

import org.themullers.vocab.pojo.EnglishWord;
import org.themullers.vocab.pojo.SpanishWord;
import org.themullers.vocab.pojo.WordAndGender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Progress {
    Map<WordAndGender, SpanishWord> spanishWordsLearned = new HashMap<>();
    Map<WordAndGender, SpanishWord> spanishWordsNotLearned = new HashMap<>();
    Map<WordAndGender, EnglishWord> englishWordsLearned = new HashMap<>();
    Map<WordAndGender, EnglishWord> englishWordsNotLearned = new HashMap<>();

    public SpanishWord getRandomUnlearnedSpanishWord() {
        var words = new ArrayList<>(spanishWordsNotLearned.values());
        var word = words.get(ThreadLocalRandom.current().nextInt(words.size()));
        return word;
    }

    public EnglishWord getRandomUnlearnedEnglishWord() {
        var words = new ArrayList<>(englishWordsNotLearned.values());
        var word = words.get(ThreadLocalRandom.current().nextInt(words.size()));
        return word;
    }

    public Map<WordAndGender, SpanishWord> getSpanishWordsLearned() {
        return spanishWordsLearned;
    }

    public void setSpanishWordsLearned(Map<WordAndGender, SpanishWord> spanishWordsLearned) {
        this.spanishWordsLearned = spanishWordsLearned;
    }

    public Map<WordAndGender, SpanishWord> getSpanishWordsNotLearned() {
        return spanishWordsNotLearned;
    }

    public void setSpanishWordsNotLearned(Map<WordAndGender, SpanishWord> spanishWordsNotLearned) {
        this.spanishWordsNotLearned = spanishWordsNotLearned;
    }

    public Map<WordAndGender, EnglishWord> getEnglishWordsLearned() {
        return englishWordsLearned;
    }

    public void setEnglishWordsLearned(Map<WordAndGender, EnglishWord> englishWordsLearned) {
        this.englishWordsLearned = englishWordsLearned;
    }

    public Map<WordAndGender, EnglishWord> getEnglishWordsNotLearned() {
        return englishWordsNotLearned;
    }

    public void setEnglishWordsNotLearned(Map<WordAndGender, EnglishWord> englishWordsNotLearned) {
        this.englishWordsNotLearned = englishWordsNotLearned;
    }
}
