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
                spanishWords.add(new SpanishWord(spanish, entry.partOfSpeech(), null, isPlural, entry.verbInfo(), entry.english()));
            }

            var spanishMasc = entry.spanishMasc();
            if (spanishMasc != null && !spanishMasc.isBlank()) {
                spanishWords.add(new SpanishWord(spanishMasc, entry.partOfSpeech(), Gender.MASCULINE, isPlural, entry.verbInfo(), entry.english()));
            }

            var spanishFem = entry.spanishFem();
            if (spanishFem != null && !spanishFem.isBlank()) {
                var english = entry.englishFem() == null || entry.englishFem().isBlank() ? entry.english() : entry.englishFem();

                spanishWords.add(new SpanishWord(spanishFem, entry.partOfSpeech(), Gender.FEMININE, isPlural, entry.verbInfo(), english));
            }
        }
    }

    protected void buildEnglishWordList() {
        for (var entry : entries) {
            var masculine = entry.english();
            var feminine = entry.englishFem();
            boolean isPlural = entry.quantity() != null && "plural".equalsIgnoreCase(entry.quantity());

            var feminineVariantExists = feminine != null && !feminine.isBlank();

            englishWords.add(new EnglishWord(masculine, entry.partOfSpeech(), feminineVariantExists ? Gender.MASCULINE : null, isPlural, entry.verbInfo()));

            if (feminineVariantExists) {
                englishWords.add(new EnglishWord(feminine, entry.partOfSpeech(), Gender.FEMININE, isPlural, entry.verbInfo()));
            }
        }
    }

    protected void loadRow(Object row) {
        var spanish      = getString(get(row, 0));
        var spanishMasc  = getString(get(row, 1));
        var spanishFem   = getString(get(row, 2));
        var verbInfo     = getString(get(row, 3));
        var partOfSpeech = getPartOfSpeech(get(row, 4));
        var quantity     = getString(get(row, 5));
        var english      = getString(get(row, 6));
        var englishFem   = getString(get(row, 7));
        var priority     = getInt(get(row, 8));
        var tags         = getList(get(row, 9));
        var lesson       = getInt(get(row, 10));

        entries.add(new VocabularyEntry(spanish, spanishMasc, spanishFem, verbInfo, partOfSpeech, quantity, english, englishFem, priority, tags, lesson));
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
