import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

class SentenceHelperTest {

    @Test
    void getSentence() {
        DictionaryHelper.getInstance().load();
        final String text = "A boy can do everything for girl! He is just kidding.";
        final TokenModel[][] sentences = SentenceHelper.getInstance().getMaskedSentences(text);
        System.out.println(new Gson().toJson(sentences));
    }
}