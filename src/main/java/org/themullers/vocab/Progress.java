package org.themullers.vocab;

import org.themullers.vocab.pojo.SpanishWord;
import org.themullers.vocab.pojo.WordAndGender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Progress {
    Map<WordAndGender, SpanishWord> spanishWordsLearned = new HashMap<>();
    Map<WordAndGender, SpanishWord> spanishWordsNotLearned = new HashMap<>();

    public SpanishWord getRandomUnlearnedSpanishWord() {
        var words = new ArrayList<>(spanishWordsNotLearned.values());
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
}
