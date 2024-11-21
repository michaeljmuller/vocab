package org.themullers.vocab.pojo;

public class Word {
    protected PartOfSpeech partOfSpeech;
    protected Gender gender;
    protected boolean isPlural;
    protected String verbInfo;

    Word(PartOfSpeech partOfSpeech, Gender gender, boolean isPlural, String verbInfo) {
        this.partOfSpeech = partOfSpeech;
        this.gender = gender;
        this.isPlural = isPlural;
        this.verbInfo = verbInfo;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isPlural() {
        return isPlural;
    }

    public void setPlural(boolean plural) {
        isPlural = plural;
    }

    public String getVerbInfo() {
        return verbInfo;
    }

    public void setVerbInfo(String verbInfo) {
        this.verbInfo = verbInfo;
    }
}
