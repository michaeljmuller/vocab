package org.themullers.vocab.pojo;

import java.util.LinkedList;
import java.util.List;

public class EnglishWord extends Word {
    protected String english;
    protected List<String> spanish = new LinkedList<>();

    public EnglishWord(int wordId, String english, PartOfSpeech partOfSpeech, Gender gender, boolean isPlural, String verbInfo) {
        super(wordId, partOfSpeech, gender, isPlural, verbInfo);
        this.english = english;
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

    public void addSpanish(String spanish) {
        this.spanish.add(spanish);
    }
}
