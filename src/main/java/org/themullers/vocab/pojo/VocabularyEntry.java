package org.themullers.vocab.pojo;

import java.util.List;

public record VocabularyEntry(
        String spanish,
        String spanishMasc,
        String spanishFem,
        String verbInfo,
        PartOfSpeech partOfSpeech,
        String quantity,
        String english,
        String englishFem,
        int priority,
        List<String> tags,
        int lesson)
{
}
