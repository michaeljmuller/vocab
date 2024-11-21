package org.themullers.vocab.pojo;

public enum PartOfSpeech {
    NOUN("n", "noun"),
    ADJECTIVE("adj", "adjective"),
    VERB("v", "verb"),
    OTHER("other", "other");

    protected String shortName;
    protected String longName;

    PartOfSpeech(String shortName, String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    @Override
    public String toString() {
        return shortName;
    }
}

