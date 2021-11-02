import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class SentenceHelper {
    private static SentenceHelper instance;

    public static SentenceHelper getInstance() {
        if (instance == null) {
            instance = new SentenceHelper();
        }
        return instance;
    }

    private List<Integer> getMaskTokenPositions(final TokenModel[] tokens) {
        final List<Integer> result = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].id < DictionaryHelper.getInstance().getId(Config.UNK_TOKEN)) {
                result.add(i);
            }
        }
        return getRandomElements(result, getMaskTokenCounts(result.size()));
    }

    private int getMaskTokenCounts(final int size) {
        return (int) Math.ceil(size * (double) Config.MASK_TOKEN_PERCENTAGE / 100);
    }

    private List<Integer> getRandomElements(final List<Integer> source, final int count) {
        final List<Integer> result = new ArrayList<>();
        final Random random = new Random();
        for (int i = 0; i < count; i++) {
            final int randomIndex = random.nextInt(source.size());
            result.add(source.get(randomIndex));
            source.remove(randomIndex);
        }
        return result;
    }

    public TokenModel[][] getSentences(final String text) {
        final TokenModel[][] sentences = TokenHelper.getInstance().getSentences(text);
        final TokenModel[][] result = new TokenModel[sentences.length][];
        for (int i = 0; i < sentences.length; i++) {
            result[i] = pad(eos(sos(sentences[i])));
        }
        return result;
    }

    public TokenModel[][] getMaskedSentences(final String text) {
        final TokenModel[][] sentences = TokenHelper.getInstance().getSentences(text);
        final TokenModel[][] result = new TokenModel[sentences.length][];
        for (int i = 0; i < sentences.length; i++) {
            result[i] = mask(pad(eos(sos(sentences[i]))));
        }
        return result;
    }

    private TokenModel[] mask(final TokenModel[] tokens) {
        final TokenModel[] result = new TokenModel[tokens.length];
        final List<Integer> positions = getMaskTokenPositions(tokens);
        for (int i = 0; i < tokens.length; i++) {
            final TokenModel tokenModel = tokens[i];
            if (positions.contains(i)) {
                tokenModel.id = DictionaryHelper.getInstance().getId(Config.MASK_TOKEN);
            }
            result[i] = tokenModel;
        }
        return result;
    }

    private TokenModel[] pad(final TokenModel[] tokens) {
        if (tokens.length < Config.MINIMUM_SENTENCE_LENGTH) {
            final TokenModel[] result = new TokenModel[Config.MINIMUM_SENTENCE_LENGTH];
            final TokenModel tokenModel = new TokenModel();
            tokenModel.name = Config.PAD_TOKEN;
            tokenModel.id = DictionaryHelper.getInstance().getId(Config.PAD_TOKEN);
            Arrays.fill(result, tokenModel);
            System.arraycopy(tokens, 0, result, 0, tokens.length);
            return result;
        }
        return tokens;
    }

    private TokenModel[] sos(final TokenModel[] tokens) {
        final TokenModel[] result = new TokenModel[tokens.length + 1];
        final TokenModel tokenModel = new TokenModel();
        tokenModel.name = Config.SOS_TOKEN;
        tokenModel.id = DictionaryHelper.getInstance().getId(Config.SOS_TOKEN);
        result[0] = tokenModel;
        System.arraycopy(tokens, 0, result, 1, tokens.length);
        return result;
    }

    private TokenModel[] eos(final TokenModel[] tokens) {
        final TokenModel[] result = new TokenModel[tokens.length + 1];
        final TokenModel tokenModel = new TokenModel();
        tokenModel.name = Config.EOS_TOKEN;
        tokenModel.id = DictionaryHelper.getInstance().getId(Config.EOS_TOKEN);
        result[result.length - 1] = tokenModel;
        System.arraycopy(tokens, 0, result, 0, tokens.length);
        return result;
    }
}
