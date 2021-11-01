public final class SquadModel {
    public String version;

    public static final class Data {
        public String title;

        public static final class Paragraph {
            public static final class Qa {
                public static class Answer {
                    public String text;
                    public int answer_start;
                }

                public static final class PlausibleAnswer extends Answer {
                }

                public String question;
                public String id;
                public boolean is_impossible;
                public Answer[] answers;
                public PlausibleAnswer[] plausible_answers;
            }

            public String context;
            public Qa[] qas;
        }

        public Paragraph[] paragraphs;
    }

    public Data[] data;
}
