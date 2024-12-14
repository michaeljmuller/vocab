package org.themullers.vocab.pojo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;
import java.util.List;

public class EnglishWord extends Word {

    private final Log logger = LogFactory.getLog(EnglishWord.class);

    protected String english;
    protected List<String> spanish = new LinkedList<>();

    public EnglishWord(int wordId, String english, PartOfSpeech partOfSpeech, Gender gender, boolean isPlural, String verbInfo, String spanish) {
        super(wordId, partOfSpeech, gender, isPlural, verbInfo);
        this.english = english;
        setSpanish(spanish);

        logger.debug(toString());
    }

    @Override
    public String toString() {
        switch (partOfSpeech) {
            case NOUN:
                var number = isPlural() ? "plural" : "singular";
                if (gender == null) {
                    return String.format("english word: %s (noun, %s) -- %s", english, number, spanish.toString());
                }
                else {
                    return String.format("english word: %s (%s noun, %s) -- %s", english, number, gender.getLongName(), spanish.toString());
                }
            case VERB:
                if (verbInfo != null && !verbInfo.isBlank()) {
                    return String.format("english word: %s (verb) -- %s %s", english, spanish.toString(), verbInfo);
                }
                else {
                    return String.format("english word: %s (verb) -- %s", english, spanish.toString());
                }
            case ADJECTIVE:
                return String.format("english word: %s (adjective) -- %s", english, spanish.toString());
            default:
                return String.format("english word: %s -- %s", english, spanish.toString());
        }
    }


    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public List<String> getSpanish() {
        return spanish;
    }

    public void setSpanish(List<String> spanish) {
        this.spanish = spanish;
    }

    public void setSpanish(String spanish) {
        if (spanish != null && !spanish.isBlank()) {
            for (var s : spanish.split(",")) {
                this.spanish.add(s.trim());
            }
        }
    }
}
