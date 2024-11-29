package org.themullers.vocab;

import jakarta.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.themullers.vocab.pojo.Gender;
import org.themullers.vocab.pojo.Language;
import org.themullers.vocab.pojo.WordAndGender;

@Component
public class ProgressPersister {

    private final Log logger = LogFactory.getLog(ProgressPersister.class);

    Database db;
    Vocabulary vocabulary;

    public ProgressPersister(Database db, Vocabulary vocabulary) {
        this.db = db;
        this.vocabulary = vocabulary;
    }

    public Progress getProgress() {
        var progress = (Progress) session().getAttribute("progress");
        if (progress == null) {

            logger.debug("no progress in session, restoring from database");

            progress = new Progress();
            var knownSpanishWords = db.getKnownWords(Language.SPANISH);
            logger.debug(String.format("user has learned %d spanish words already", knownSpanishWords.size()));

            // loop through all the spanish words, adding them to the "known" or "not known" maps
            var spanishWords = vocabulary.getSpanishWords();
            logger.debug(String.format("there are %d words in the vocabulary file", spanishWords.size()));
            for (var spanishWord : spanishWords) {
                if (knownSpanishWords.contains(new WordAndGender(spanishWord.getWordId(), spanishWord.getGender()))) {
                    logger.debug(String.format("adding %s as a known spanish word", spanishWord.getSpanish()));
                    progress.spanishWordsLearned.put(spanishWord.getWordAndGender(), spanishWord);
                }
                else {
                    logger.debug(String.format("adding %s as a NOT known spanish word", spanishWord.getSpanish()));
                    progress.spanishWordsNotLearned.put(spanishWord.getWordAndGender(), spanishWord);
                }
            }
            session().setAttribute("progress", progress);
        }
        return progress;
    }

    public void recordSpanishWordAnswer(int wordId, Gender gender, boolean isCorrect) {
        var progress = getProgress();
        if (isCorrect) {
            var wordAndGender = new WordAndGender(wordId, gender);
            var word = progress.spanishWordsNotLearned.remove(wordAndGender);
            progress.spanishWordsLearned.put(wordAndGender, word);
            db.incrementCorrect(wordId, gender, Language.SPANISH);
        }
        else {
            db.incrementIncorrect(wordId, gender, Language.SPANISH);
        }

        var word = progress.spanishWordsLearned.get(new WordAndGender(wordId, gender));

        logger.debug(String.format("answer for %s was %s", word == null ? null : word.getSpanish(), isCorrect));
        logger.debug(String.format("learned %d of %d words", progress.spanishWordsLearned.size(), progress.spanishWordsLearned.size() + progress.spanishWordsNotLearned.size()));
    }

    // from https://stackoverflow.com/a/1629239
    public static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow creation
    }
}
