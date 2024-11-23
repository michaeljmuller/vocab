package org.themullers.vocab.pojo;

public enum Language {
    ENGLISH(1),
    SPANISH(2);

    protected final int id;

    Language(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
