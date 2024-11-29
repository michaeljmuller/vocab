package org.themullers.vocab.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.themullers.vocab.ProgressPersister;
import org.themullers.vocab.pojo.Gender;
import org.themullers.vocab.pojo.WordAndProgress;

@RestController
public class Api {

    private final Log logger = LogFactory.getLog(Api.class);

    protected ProgressPersister progressPersister;

    public Api(ProgressPersister progressPersister) {
        this.progressPersister = progressPersister;
    }

    @GetMapping("/spanishWord")
    public WordAndProgress getSpanishWord() {

        var progress = progressPersister.getProgress();
        var word = progress.getRandomUnlearnedSpanishWord();
        var numWordsLearned = progress.getSpanishWordsLearned().size();
        var numWords = numWordsLearned + progress.getSpanishWordsNotLearned().size();
        return new WordAndProgress(word, numWords, numWordsLearned);
    }

    @PutMapping("/spanishWord")
    public void putSpanishWord(@RequestBody Answer answer) {
        progressPersister.recordSpanishWordAnswer(answer.wordId(), Gender.fromLongName(answer.gender()), answer.answeredCorrectly());
    }

    @GetMapping("/englishWord")
    public WordAndProgress getWord() {

        var progress = progressPersister.getProgress();
        var word = progress.getRandomUnlearnedSpanishWord();
        var numWordsLearned = progress.getSpanishWordsLearned().size();
        var numWords = numWordsLearned + progress.getSpanishWordsNotLearned().size();
        return new WordAndProgress(word, numWords, numWordsLearned);
    }

    @PutMapping("/englishWord")
    public void putWord(@RequestBody Answer answer) {
        // progressPersister.recordEnglishWordAnswer(answer.word(), answer.answeredCorrectly());
    }

}
