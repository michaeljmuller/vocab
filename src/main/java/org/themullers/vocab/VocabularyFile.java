package org.themullers.vocab;

import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.themullers.vocab.pojo.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class VocabularyFile extends Vocabulary {

    private final Log logger = LogFactory.getLog(VocabularyFile.class);

    protected String csv;

    public VocabularyFile(@Value("${csv.file}") String csv) {
        this.csv = csv;
    }

    @PostConstruct
    public void load() {
        try (var parser = CSVParser.parse(new File(csv), StandardCharsets.UTF_8, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build())) {
            for (var record : parser.getRecords()) {
                loadRow(record);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        buildSpanishWordList();
        buildEnglishWordList();
    }

    @Override
    public List<VocabularyEntry> getEntries() {
        return entries;
    }

    @Override
    public List<SpanishWord> getSpanishWords() {
        return spanishWords;
    }

    @Override
    public List<EnglishWord> getEnglishWords() {
        return englishWords;
    }

    @Override
    protected String get(Object row, int index) {
        CSVRecord record = (CSVRecord) row;
        return index < record.size() ? record.get(index) : null;
    }
}
