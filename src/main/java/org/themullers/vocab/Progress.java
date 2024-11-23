package org.themullers.vocab;

import org.themullers.vocab.pojo.SpanishWord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Progress {
    Map<String, SpanishWord> spanishWordsLearned = new HashMap<>();
    Map<String, SpanishWord> spanishWordsNotLearned = new HashMap<>();

    public SpanishWord getRandomUnlearnedSpanishWord() {
        var words = new ArrayList<>(spanishWordsNotLearned.values());
        var word = words.get(ThreadLocalRandom.current().nextInt(words.size()));
        return word;
    }

    public Map<String, SpanishWord> getSpanishWordsLearned() {
        return spanishWordsLearned;
    }

    public void setSpanishWordsLearned(Map<String, SpanishWord> spanishWordsLearned) {
        this.spanishWordsLearned = spanishWordsLearned;
    }

    public Map<String, SpanishWord> getSpanishWordsNotLearned() {
        return spanishWordsNotLearned;
    }

    public void setSpanishWordsNotLearned(Map<String, SpanishWord> spanishWordsNotLearned) {
        this.spanishWordsNotLearned = spanishWordsNotLearned;
    }
}
