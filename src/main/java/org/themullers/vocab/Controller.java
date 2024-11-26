package org.themullers.vocab;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.stereotype.Controller
public class Controller {

    Vocabulary vocabulary;

    public Controller(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    @GetMapping("/")
    public ModelAndView home() {
        var mv = new ModelAndView("home");
        mv.addObject("vocab", vocabulary.getEntries());
        mv.addObject("spanishWords", vocabulary.getSpanishWords());
        mv.addObject("englishWords", vocabulary.getEnglishWords());
        return mv;
    }
}
