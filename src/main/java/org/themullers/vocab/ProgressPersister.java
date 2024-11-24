package org.themullers.vocab;

import jakarta.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.themullers.vocab.pojo.Language;
import org.themullers.vocab.rest.Api;

@Component
public class ProgressPersister {

    private final Log logger = LogFactory.getLog(ProgressPersister.class);

    Database db;
    VocabularyFile vocabularyFile;

    public ProgressPersister(Database db, VocabularyFile vocabularyFile) {
        this.db = db;
        this.vocabularyFile = vocabularyFile;
    }

    public Progress getProgress() {
        var progress = (Progress) session().getAttribute("progress");
        if (progress == null) {

            logger.debug("no progress in session, restoring from database");

            progress = new Progress();
            var knownSpanishWords = db.getKnownWords(Language.SPANISH.getId());
            logger.debug(String.format("user has learned %d spanish words already", knownSpanishWords.size()));

            // loop through all the spanish words, adding them to the "known" or "not known" maps
            var spanishWords = vocabularyFile.getSpanishWords();
            logger.debug(String.format("there are %d words in the vocabulary file", spanishWords.size()));
            for (var spanishWord : spanishWords) {
                if (knownSpanishWords.contains(spanishWord.getSpanish())) {
                    logger.debug(String.format("adding %s as a known spanish word", spanishWord.getSpanish()));
                    progress.spanishWordsLearned.put(spanishWord.getSpanish(), spanishWord);
                }
                else {
                    logger.debug(String.format("adding %s as a NOT known spanish word", spanishWord.getSpanish()));
                    progress.spanishWordsNotLearned.put(spanishWord.getSpanish(), spanishWord);
                }
            }
            session().setAttribute("progress", progress);
        }
        return progress;
    }

    public void recordSpanishWordAnswer(String spanishWord, boolean isCorrect) {
        var progress = getProgress();
        if (isCorrect) {
            var word = progress.spanishWordsNotLearned.remove(spanishWord);
            progress.spanishWordsLearned.put(spanishWord, word);
            db.incrementCorrect(spanishWord, Language.SPANISH.getId());
        }
        else {
            db.incrementIncorrect(spanishWord, Language.SPANISH.getId());
        }

        logger.debug(String.format("answer for %s was %s", spanishWord, isCorrect));
        logger.debug(String.format("learned %d of %d words", progress.spanishWordsLearned.size(), progress.spanishWordsLearned.size() + progress.spanishWordsNotLearned.size()));
    }

    // from https://stackoverflow.com/a/1629239
    public static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow creation
    }
}
