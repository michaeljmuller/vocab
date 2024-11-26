package org.themullers.vocab;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.stereotype.Controller
public class Controller {

    Vocabulary vocabulary;
    ProgressPersister progressPersister;

    public Controller(Vocabulary vocabulary, ProgressPersister progressPersister) {
        this.vocabulary = vocabulary;
        this.progressPersister = progressPersister;
    }

    @GetMapping("/spanish-to-english")
    public ModelAndView spanishToEnglish() {
        var mv = new ModelAndView("spanish-to-english");
        mv.addObject("vocab", vocabulary.getEntries());
        mv.addObject("spanishWords", vocabulary.getSpanishWords());
        mv.addObject("englishWords", vocabulary.getEnglishWords());
        return mv;
    }

    @GetMapping("/")
    public ModelAndView home() {
        var mv = new ModelAndView("home");
        mv.addObject("wordCount", vocabulary.getEntries().size());
        mv.addObject("expandedSpanishWordCount", vocabulary.getSpanishWords().size());
        mv.addObject("spanishWordsLearned", progressPersister.getProgress().getSpanishWordsLearned().size());
        return mv;
    }

}
