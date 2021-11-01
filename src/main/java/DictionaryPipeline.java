public final class DictionaryPipeline {
    private static DictionaryPipeline instance;
    private int record;

    public static DictionaryPipeline getInstance() {
        if (instance == null) {
            instance = new DictionaryPipeline();
        }
        return instance;
    }

    public void fit() {
        final SquadModel squad = FileHelper.getInstance().read("train-v2.0", SquadModel.class);
        if (squad == null) {
            return;
        }
        for (final SquadModel.Data data : squad.data) {
            for (final SquadModel.Data.Paragraph paragraph : data.paragraphs) {
                record++;
                add(paragraph.context);
                System.out.println(record);
                for (final SquadModel.Data.Paragraph.Qa qa : paragraph.qas) {
                    record++;
                    add(qa.question);
                    System.out.println(record);
                }
            }
        }
        DictionaryHelper.getInstance().save();
        System.out.println("finish");
    }

    private void add(final String text) {
        final TokenModel[] tokens = TokenHelper.getInstance().getTokens(text.trim().toLowerCase());
        for (final TokenModel token : tokens) {
            DictionaryHelper.getInstance().set(token.name);
        }
    }
}
