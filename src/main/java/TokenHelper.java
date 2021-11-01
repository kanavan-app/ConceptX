import java.util.ArrayList;
import java.util.List;

public final class TokenHelper {
    private static TokenHelper instance;

    public static TokenHelper getInstance() {
        if (instance == null) {
            instance = new TokenHelper();
        }
        return instance;
    }

    private List<Integer> getCodePoints(final String text) {
        final List<Integer> result = new ArrayList<>();
        for (int offset = 0; offset < text.length(); ) {
            final int codePoint = text.codePointAt(offset);
            result.add(codePoint);
            offset += Character.charCount(codePoint);
        }
        return result;
    }

    private String normalize(final String text) {
        return text.trim().toLowerCase();
    }

    public TokenModel[] getTokens(final String text) {
        final List<TokenModel> result = new ArrayList<>();
        final List<Integer> codePoints = getCodePoints(normalize(text));
        for (int i = 0; i < codePoints.size(); i++) {
            if (!Character.isWhitespace(codePoints.get(i))) {
                if (i + 1 == codePoints.size() || (i + 1 < codePoints.size() && Character.isWhitespace(codePoints.get(i + 1)))) {
                    final TokenModel tokenModel = result.get(result.size() - 1);
                    tokenModel.end = i;
                    tokenModel.name += Character.toString(codePoints.get(i));
                    tokenModel.id = DictionaryHelper.getInstance().getId(tokenModel.name);
                    result.set(result.size() - 1, tokenModel);
                } else if (i == 0 || (i - 1 > 0 && Character.isWhitespace(codePoints.get(i - 1)))) {
                    final TokenModel tokenModel = new TokenModel();
                    tokenModel.start = i;
                    tokenModel.name = Character.toString(codePoints.get(i));
                    result.add(tokenModel);
                } else {
                    final TokenModel tokenModel = result.get(result.size() - 1);
                    tokenModel.name += Character.toString(codePoints.get(i));
                    result.set(result.size() - 1, tokenModel);
                }
            }
        }
        return result.toArray(new TokenModel[0]);
    }

    public TokenModel[][] getSentences(final String text) {
        final List<List<TokenModel>> result = new ArrayList<>();
        final TokenModel[] tokens = getTokens(text);
        for (int i = 0; i < tokens.length; i++) {
            if (result.size() == 0) {
                result.add(new ArrayList<>());
            }
            result.get(result.size() - 1).add(tokens[i]);
            if (tokens[i].name.contains("!") || tokens[i].name.contains("?") || (tokens[i].name.contains(".") && i + 1 < tokens.length && tokens[i + 1].id != DictionaryHelper.getInstance().getId(Config.UNK_TOKEN))) {
                result.add(new ArrayList<>());
            }
        }
        return toArray(result);
    }

    private TokenModel[][] toArray(final List<List<TokenModel>> list) {
        final TokenModel[][] result = new TokenModel[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i).toArray(new TokenModel[0]);
        }
        return result;
    }
}
