import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

class TokenHelperTest {

    @Test
    void getSentences() {
        DictionaryHelper.getInstance().load();
        final String text = "The Hellenistic period covers the period of ancient Greek (Hellenic) history and Mediterranean history between the death of Alexander the Great in 323 BC and the emergence of the Roman Empire as signified by the Battle of Actium in 31 BC and the subsequent conquest of Ptolemaic Egypt the following year. At this time, Greek cultural influence and power was at its peak in Europe, Africa and Asia, experiencing prosperity and progress in the arts, exploration, literature, theatre, architecture, music, mathematics, philosophy, and science. For example, competitive public games took place, ideas in biology, and popular entertainment in theaters. It is often considered a period of transition, sometimes even of decadence or degeneration, compared to the enlightenment of the Greek Classical era. The Hellenistic period saw the rise of New Comedy, Alexandrian poetry, the Septuagint and the philosophies of Stoicism and Epicureanism. Greek Science was advanced by the works of the mathematician Euclid and the polymath Archimedes. The religious sphere expanded to include new gods such as the Greco-Egyptian Serapis, eastern deities such as Attis and Cybele and the Greek adoption of Buddhism.";
        final TokenModel[][] sentences = TokenHelper.getInstance().getSentences(text);
        System.out.println(new Gson().toJson(sentences));
    }
}