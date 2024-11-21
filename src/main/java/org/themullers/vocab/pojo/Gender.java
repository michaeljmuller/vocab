package org.themullers.vocab.pojo;

public enum Gender {
    MASCULINE("masc", "masculine"), FEMININE("fem", "feminine");

    protected String shortName;
    protected String longName;

    Gender(String shortName, String longName) {
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
