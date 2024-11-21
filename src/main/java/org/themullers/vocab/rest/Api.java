package org.themullers.vocab.rest;

import jakarta.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.themullers.vocab.VocabularyFile;
import org.themullers.vocab.pojo.SpanishWord;
import org.themullers.vocab.pojo.Word;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
public class Api {

    private final Log logger = LogFactory.getLog(Api.class);

    protected VocabularyFile vocabularyFile;

    public Api(VocabularyFile vocabularyFile) {
        this.vocabularyFile = vocabularyFile;
    }

    @GetMapping("/word")
    public Word getWord() {
        var unlearned = (Map<String, SpanishWord>) session().getAttribute("unlearnedWords");
        var learned = (Map<String, SpanishWord>) session().getAttribute("learnedWords");
        if (unlearned == null) {
            unlearned = vocabularyFile.getSpanishWords().stream().collect(Collectors.toMap(SpanishWord::getSpanish, word -> word, (a,b) -> a));
            session().setAttribute("unlearnedWords", unlearned);
            learned = new HashMap<String, SpanishWord>();
            session().setAttribute("learnedWords", learned);
        }
        var words = new ArrayList<>(unlearned.values());
        return words.get(ThreadLocalRandom.current().nextInt(words.size()));
    }

    @PutMapping("/word")
    public void putWord(@RequestBody Answer answer) {
        var unlearned = (Map<String, SpanishWord>) session().getAttribute("unlearnedWords");
        var learned = (Map<String, SpanishWord>) session().getAttribute("learnedWords");
        if (answer.answeredCorrectly()) {
            var word = unlearned.remove(answer.word());
            learned.put(answer.word(), word);
        }
        logger.debug(String.format("answer for %s was %s", answer.word(), answer.answeredCorrectly()));
        logger.debug(String.format("learned %d of %d words", learned.size(), learned.size() + unlearned.size()));
    }

    // from https://stackoverflow.com/a/1629239
    public static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow creation
    }
}
