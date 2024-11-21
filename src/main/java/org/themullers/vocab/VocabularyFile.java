package org.themullers.vocab;

import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.themullers.vocab.pojo.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

@Component
public class VocabularyFile {

    private final Log logger = LogFactory.getLog(VocabularyFile.class);

    protected String csv;
    protected List<VocabularyEntry> entries = new LinkedList<>();
    protected List<SpanishWord> spanishWords = new LinkedList<>();
    protected List<EnglishWord> englishWords = new LinkedList<>();

    public VocabularyFile(@Value("${csv.file}") String csv) {
        this.csv = csv;
    }

    @PostConstruct
    public void load() {
        try (var parser = CSVParser.parse(new File(csv), StandardCharsets.UTF_8, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build())) {
            for (var record : parser.getRecords()) {

                var spanish      = getString(record, 0);
                var spanishMasc  = getString(record, 1);
                var spanishFem   = getString(record, 2);
                var verbInfo     = getString(record, 3);
                var partOfSpeech = getPartOfSpeech(record, 4);
                var quantity     = getString(record, 5);
                var english      = getString(record, 6);
                var englishFem   = getString(record, 7);
                var priority     = getInt(record, 8);
                var tags         = getList(record, 9);
                var lesson       = getInt(record, 10);

                entries.add(new VocabularyEntry(spanish, spanishMasc, spanishFem, verbInfo, partOfSpeech, quantity, english, englishFem, priority, tags, lesson));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        buildSpanishWordList();
        buildEnglishWordList();
    }

    public List<VocabularyEntry> getEntries() {
        return entries;
    }

    public List<SpanishWord> getSpanishWords() {
        return spanishWords;
    }

    public List<EnglishWord> getEnglishWords() {
        return englishWords;
    }

    // HELPERS

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

    protected String getString(CSVRecord record, int index) {
        var value = record.get(index);
        if (value != null) {
            value = value.trim();
        }
        return value;
    }

    protected int getInt(CSVRecord record, int index) {
        var value = record.get(index);
        if (value != null && !value.isBlank()) {
            try {
                return Integer.parseInt(value);
            }
            catch (NumberFormatException e) {
                System.out.println("bad int: " + value);
            }
            return 0;
        }
        else {
            return 0;
        }
    }

    protected List<String> getList(CSVRecord record, int index) {
        var list = new LinkedList<String>();
        var value = record.get(index);
        if (value != null && !value.isBlank()) {
            for (var item : value.split(",")) {
                list.add(item.trim());
            }
        }
        return list;
    }

    protected PartOfSpeech getPartOfSpeech(CSVRecord record, int index) {
        var value = record.get(index);
        if (value == null || value.isBlank()) {
            return PartOfSpeech.OTHER;
        }
        else {
            switch (value.toLowerCase()) {
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
