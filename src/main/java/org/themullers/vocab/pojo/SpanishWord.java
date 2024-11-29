package org.themullers.vocab.pojo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;
import java.util.List;

public class SpanishWord extends Word {
    private final Log logger = LogFactory.getLog(SpanishWord.class);

    protected String spanish;
    protected List<String> english = new LinkedList<>();

    public SpanishWord(int wordId, String spanish, PartOfSpeech partOfSpeech, Gender gender, boolean isPlural, String verbInfo, String english) {
        super(wordId, partOfSpeech, gender, isPlural, verbInfo);
        this.spanish = spanish;
        setEnglish(english);

        logger.debug(toString());
    }

    @Override
    public String toString() {
        switch (partOfSpeech) {
            case NOUN:
                var number = isPlural() ? "plural" : "singular";
                if (gender == null) {
                    return String.format("spanish word: %s (noun, %s) -- %s", spanish, number, english.toString());
                }
                else {
                    return String.format("spanish word: %s (%s noun, %s) -- %s", spanish, number, gender.getLongName(), english.toString());
                }
            case VERB:
                if (verbInfo != null && !verbInfo.isBlank()) {
                    return String.format("spanish word: %s (verb %s) -- %s", spanish, verbInfo, english.toString());
                }
                else {
                    return String.format("spanish word: %s (verb) -- %s", spanish, english.toString());
                }
            case ADJECTIVE:
                if (gender == null) {
                    return String.format("spanish word: %s (adjective) -- %s", spanish, english.toString());
                }
                else {
                    return String.format("spanish word: %s (%s adjective) -- %s", spanish, gender.getLongName(), english.toString());
                }
            default:
                return String.format("spanish word: %s -- %s", spanish, english.toString());
        }
    }

    public String getSpanish() {
        return spanish;
    }

    public void setSpanish(String spanish) {
        this.spanish = spanish;
    }

    public List<String> getEnglish() {
        return english;
    }

    public void setEnglish(List<String> english) {
        this.english = english;
    }

    public void setEnglish(String english) {
        if (english != null && !english.isBlank()) {
            for (var s : english.split(",")) {
                this.english.add(s.trim());
            }
        }
    }
}
