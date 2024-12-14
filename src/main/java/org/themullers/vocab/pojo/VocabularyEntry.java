package org.themullers.vocab.pojo;

import java.util.List;

public record VocabularyEntry(
        int wordId,
        String spanish,
        String spanishMasc,
        String spanishFem,
        String verbInfo,
        PartOfSpeech partOfSpeech,
        String quantity,
        String english,
        String englishMasc,
        String englishFem,
        int priority,
        List<String> tags,
        int lesson)
{
}
