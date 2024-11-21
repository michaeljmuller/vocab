package org.themullers.vocab;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.stereotype.Controller
public class Controller {

    VocabularyFile vocabularyFile;

    public Controller(VocabularyFile vocabularyFile) {
        this.vocabularyFile = vocabularyFile;
    }

    @GetMapping("/")
    public ModelAndView home() {
        var mv = new ModelAndView("home");
        mv.addObject("vocab", vocabularyFile.getEntries());
        mv.addObject("spanishWords", vocabularyFile.getSpanishWords());
        mv.addObject("englishWords", vocabularyFile.getEnglishWords());
        return mv;
    }
}
