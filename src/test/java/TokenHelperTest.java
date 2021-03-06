import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

class TokenHelperTest {

    @Test
    void getSentences() {
        DictionaryHelper.getInstance().load();
        final String text = "A boy can do everything for girl! He is just kidding.";
        final TokenModel[][] sentences = TokenHelper.getInstance().getSentences(text);
        System.out.println(new Gson().toJson(sentences));
    }
}