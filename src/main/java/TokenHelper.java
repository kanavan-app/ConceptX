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
        boolean isSpace = true;
        for (int i = 0; i < codePoints.size(); i++) {
            if (!Character.isWhitespace(codePoints.get(i))) {
                if (isSpace) {
                    final TokenModel tokenModel = new TokenModel();
                    tokenModel.start = i;
                    result.add(tokenModel);
                }
                TokenModel tokenModel = result.get(result.size() - 1);
                tokenModel.end = i;
                tokenModel.name = tokenModel.name == null ? Character.toString(codePoints.get(i)) : tokenModel.name + Character.toString(codePoints.get(i));
                tokenModel.id = DictionaryHelper.getInstance().getId(tokenModel.name);
                result.set(result.size() - 1, tokenModel);
            }
            isSpace = Character.isWhitespace(codePoints.get(i));
        }
        return result.toArray(new TokenModel[0]);
    }

    public TokenModel[][] getSentences(final String text) {
        final List<List<TokenModel>> result = new ArrayList<>();
        final TokenModel[] tokens = getTokens(text);
        boolean isFullStop = false;
        boolean isExclamationMark = false;
        boolean isQuestionMark = false;
        for (final TokenModel token : tokens) {
            if (result.size() == 0 || isExclamationMark || isQuestionMark || (isFullStop && token.id != DictionaryHelper.getInstance().getId(Config.UNK_TOKEN))) {
                result.add(new ArrayList<>());
            }
            result.get(result.size() - 1).add(token);
            isFullStop = token.name.contains(".");
            isExclamationMark = token.name.contains("!");
            isQuestionMark = token.name.contains("?");
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
