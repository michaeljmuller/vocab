package org.themullers.vocab.pojo;

public enum Gender {

    MASCULINE(1, "masc", "masculine"),
    FEMININE(2, "fem", "feminine"),
    NEUTRAL(3, "neutral", "neutral");

    protected final int id;
    protected String shortName;
    protected String longName;

    Gender(int id, String shortName, String longName) {
        this.id = id;
        this.shortName = shortName;
        this.longName = longName;
    }
    
    public String getShortName() {
        return shortName;
    }
    
    public String getLongName() {
        return longName;
    }
    
    public int getId() {
        return id;
    }

    public static Gender fromId(int id) {
        switch (id) {
            case 1: return MASCULINE;
            case 2: return FEMININE;
            case 3: return NEUTRAL;
            default: return null;
        }
    }

    public static Gender fromLongName(String longName) {
        if (longName == null) {
            return null;
        }
        switch (longName.toLowerCase()) {
            case "masculine": return MASCULINE;
            case "feminine": return FEMININE;
            case "neutral": return NEUTRAL;
            default: return null;
        }
    }

    @Override
    public String toString() {
        return shortName;
    }
}
