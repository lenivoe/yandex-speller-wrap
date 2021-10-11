import org.junit.jupiter.api.Test;
import speller.SpellerError;
import speller.Speller;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpellerTest {
    private final Speller speller = new Speller();

    @Test
    void emptyText() {
        var text = "";
        var errors = speller.checkText(text);
        assertEquals(0, errors.length);
    }

    @Test
    void correctSentence() {
        var text = "лисица гордо шагала через опушку";
        var errors = speller.checkText(text);
        assertEquals(0, errors.length);
    }

    @Test
    void twoWordsWrong() {
        var text = "лисица гордо шогала черз опушку";
        var errors = speller.checkText(text);
        assertEquals(2, errors.length);
        for (var err : errors) {
            assertEquals(SpellerError.StatusCode.ERROR_UNKNOWN_WORD, err.getStatusCode());
            assertEquals(err.getPos(), text.indexOf(err.getWord()));
            assertEquals(err.getLen(), err.getWord().length());
        }
    }

    @Test
    void allWordsWrong() {
        var text = "лесица гопдо шогала черз апушку";
        var errors = speller.checkText(text);
        assertEquals(text.split(" ").length, errors.length);
        for (var err : errors) {
            assertEquals(SpellerError.StatusCode.ERROR_UNKNOWN_WORD, err.getStatusCode());
            assertEquals(err.getPos(), text.indexOf(err.getWord()));
            assertEquals(err.getLen(), err.getWord().length());
        }
    }

    @Test
    void englishSentence() {
        var text = "my english is Great Britain";
        var errors = speller.checkText(text, EnumSet.of(Speller.Language.EN));
        assertEquals(0, errors.length);
    }

    @Test
    void wrongLanguage() {
        var text = "лесица гопдо шогала черз апушку";
        var errors = speller.checkText(text, EnumSet.of(Speller.Language.EN));
        assertEquals(0, errors.length);
    }

    @Test
    void allEnglishWordsWrong() {
        var text = "myy enlish ixs Grat Brtain";
        var errors = speller.checkText(text, EnumSet.of(Speller.Language.EN));
        assertEquals(text.split(" ").length, errors.length);
        for (var err : errors) {
            assertEquals(SpellerError.StatusCode.ERROR_UNKNOWN_WORD, err.getStatusCode());
            assertEquals(err.getPos(), text.indexOf(err.getWord()));
            assertEquals(err.getLen(), err.getWord().length());
        }
    }
    @Test
    void correctSentenceAsync() {
        var text = "лисица гордо шагала через опушку";
        var errors = speller.checkTextAsync(text).join();
        assertEquals(0, errors.length);
    }

    @Test
    void twoWordsWrongAsync() {
        var text = "лисица гордо шогала черз опушку";
        var errors = speller.checkTextAsync(text).join();
        assertEquals(2, errors.length);
        for (var err : errors) {
            assertEquals(SpellerError.StatusCode.ERROR_UNKNOWN_WORD, err.getStatusCode());
            assertEquals(err.getPos(), text.indexOf(err.getWord()));
            assertEquals(err.getLen(), err.getWord().length());
        }
    }

    @Test
    void allWordsWrongAsync() {
        var text = "лесица гопдо шогала черз апушку";
        var errors = speller.checkTextAsync(text).join();
        assertEquals(text.split(" ").length, errors.length);
        for (var err : errors) {
            assertEquals(SpellerError.StatusCode.ERROR_UNKNOWN_WORD, err.getStatusCode());
            assertEquals(err.getPos(), text.indexOf(err.getWord()));
            assertEquals(err.getLen(), err.getWord().length());
        }
    }
}
