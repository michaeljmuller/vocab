package org.themullers.vocab;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.themullers.vocab.pojo.*;

import java.util.LinkedList;
import java.util.List;

abstract public class Vocabulary {

    private final Log logger = LogFactory.getLog(GoogleSheets.class);

    protected List<VocabularyEntry> entries = new LinkedList<>();
    protected List<SpanishWord> spanishWords = new LinkedList<>();
    protected List<EnglishWord> englishWords = new LinkedList<>();

    public List<VocabularyEntry> getEntries() {
        return entries;
    }

    public List<SpanishWord> getSpanishWords() {
        return spanishWords;
    }

    public List<EnglishWord> getEnglishWords() {
        return englishWords;
    }

    protected void buildSpanishWordList() {
        for (var entry : entries) {

            boolean isPlural = entry.quantity() != null && "plural".equalsIgnoreCase(entry.quantity());

            var spanish = entry.spanish();
            if (spanish != null && !spanish.isBlank()) {
                spanishWords.add(new SpanishWord(entry.wordId(), spanish, entry.partOfSpeech(), Gender.NEUTRAL, isPlural, entry.verbInfo(), entry.english()));
            }

            var spanishMasc = entry.spanishMasc();
            if (spanishMasc != null && !spanishMasc.isBlank()) {
                var english = entry.englishMasc() == null || entry.englishMasc().isBlank() ? entry.english() : entry.englishMasc();
                spanishWords.add(new SpanishWord(entry.wordId(), spanishMasc, entry.partOfSpeech(), Gender.MASCULINE, isPlural, entry.verbInfo(), english));
            }

            var spanishFem = entry.spanishFem();
            if (spanishFem != null && !spanishFem.isBlank()) {
                var english = entry.englishFem() == null || entry.englishFem().isBlank() ? entry.english() : entry.englishFem();

                spanishWords.add(new SpanishWord(entry.wordId(), spanishFem, entry.partOfSpeech(), Gender.FEMININE, isPlural, entry.verbInfo(), english));
            }
        }
    }

    protected void buildEnglishWordList() {
        for (var entry : entries) {

            var englishNeutral = entry.english();
            var englishMasculine = entry.englishMasc();
            var englishFeminine = entry.englishFem();

            boolean isPlural = entry.quantity() != null && "plural".equalsIgnoreCase(entry.quantity());

            if (englishNeutral != null && !englishNeutral.isBlank()) {

                var englishWord = new EnglishWord(entry.wordId(), englishNeutral, entry.partOfSpeech(), Gender.NEUTRAL, isPlural, entry.verbInfo(), entry.spanish());
                englishWord.setSpanish(entry.spanishMasc());
                englishWord.setSpanish(entry.spanishFem());
                englishWords.add(englishWord);

            }

            if (englishMasculine != null && !englishMasculine.isBlank()) {
                englishWords.add(new EnglishWord(entry.wordId(), englishMasculine, entry.partOfSpeech(), Gender.MASCULINE, isPlural, entry.verbInfo(), entry.spanish()));
            }

            if (englishFeminine != null && !englishFeminine.isBlank()) {
                englishWords.add(new EnglishWord(entry.wordId(), englishFeminine, entry.partOfSpeech(), Gender.FEMININE, isPlural, entry.verbInfo(), entry.spanish()));
            }
        }
    }

    protected void loadRow(Object row) {
        int columnNum = 0;
        var wordId       = getInt(get(row, columnNum++));
        var spanish      = getString(get(row, columnNum++));
        var spanishMasc  = getString(get(row, columnNum++));
        var spanishFem   = getString(get(row, columnNum++));
        var verbInfo     = getString(get(row, columnNum++));
        var partOfSpeech = getPartOfSpeech(get(row, columnNum++));
        var quantity     = getString(get(row, columnNum++));
        var english      = getString(get(row, columnNum++));
        var englishMasc  = getString(get(row, columnNum++));
        var englishFem   = getString(get(row, columnNum++));
        var priority     = getInt(get(row, columnNum++));
        var tags         = getList(get(row, columnNum++));
        var lesson       = getInt(get(row, columnNum++));

        entries.add(new VocabularyEntry(wordId, spanish, spanishMasc, spanishFem, verbInfo, partOfSpeech, quantity, english, englishMasc, englishFem, priority, tags, lesson));
    }

    abstract Object get(Object row, int index);

    protected String getString(Object value) {
        return value == null ? null : value.toString().trim();
    }

    protected int getInt(Object value) {
        if (value == null) {
            return 0;
        }
        else if (value instanceof Number number) {
            return number.intValue();
        }
        else {
            var string = value.toString();
            if (string.isBlank()) {
                return 0;
            }
            else {
                try {
                    return Integer.parseInt(value.toString());
                }
                catch (NumberFormatException e) {
                    logger.error("bad int in spreadsheet: " + value);
                }
            }
            return 0;
        }
    }

    protected List<String> getList(Object value) {
        var list = new LinkedList<String>();
        if (value != null) {
            if (value instanceof String) {
                for (var item : value.toString().split(",")) {
                    list.add(item.trim());
                }
            }
            else {
                list.add(value.toString());
            }
        }
        return list;
    }

    protected PartOfSpeech getPartOfSpeech(Object value) {
        if (value == null || value.toString().isBlank()) {
            return PartOfSpeech.OTHER;
        }
        else {
            switch (value.toString().toLowerCase()) {
                case "noun":
                    return PartOfSpeech.NOUN;
                case "verb":
                    return PartOfSpeech.VERB;
                case "adjective":
                    return PartOfSpeech.ADJECTIVE;
                default:
                    logger.error("unrecognized part of speech: " + value);
                    return PartOfSpeech.OTHER;
            }
        }
    }
}
