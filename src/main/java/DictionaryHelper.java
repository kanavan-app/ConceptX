import java.util.*;

public final class DictionaryHelper {
    private static DictionaryHelper instance;
    private final Map<String, Integer> idMap;
    private final Map<String, Integer> frequencyMap;
    private final String file = "dictionary";
    private int id;

    public static DictionaryHelper getInstance() {
        if (instance == null) {
            instance = new DictionaryHelper();
        }
        return instance;
    }

    private DictionaryHelper() {
        idMap = new HashMap<>();
        frequencyMap = new HashMap<>();
    }

    public void load() {
        final DictionaryModel dictionaryModel = FileHelper.getInstance().read(file, DictionaryModel.class);
        for (int i = 0; i < Config.MAXIMUM_TOKEN_ID - 3; i++) {
            idMap.put(dictionaryModel.data[i].name, dictionaryModel.data[i].id);
        }
        idMap.put(Config.UNK_TOKEN, Config.MAXIMUM_TOKEN_ID - 3);
        idMap.put(Config.PAD_TOKEN, Config.MAXIMUM_TOKEN_ID - 2);
        idMap.put(Config.SOS_TOKEN, Config.MAXIMUM_TOKEN_ID - 1);
        idMap.put(Config.EOS_TOKEN, Config.MAXIMUM_TOKEN_ID);
    }

    private void sort(final DictionaryModel.Data[] data) {
        Arrays.sort(data, new Comparator<DictionaryModel.Data>() {
            @Override
            public int compare(DictionaryModel.Data o1, DictionaryModel.Data o2) {
                return o2.frequency - o1.frequency;
            }
        });
    }

    public int getId(final String name) {
        return idMap.containsKey(name) ? idMap.get(name) : idMap.get(Config.UNK_TOKEN);
    }

    public void set(final String name) {
        if (!frequencyMap.containsKey(name)) {
            id++;
            idMap.put(name, id);
            frequencyMap.put(name, 1);
        } else {
            frequencyMap.put(name, frequencyMap.get(name) + 1);
        }
    }

    private DictionaryModel getModel() {
        final DictionaryModel dictionaryModel = new DictionaryModel();
        dictionaryModel.data = new DictionaryModel.Data[idMap.size()];
        for (Map.Entry<String, Integer> entry : idMap.entrySet()) {
            final DictionaryModel.Data data = new DictionaryModel.Data();
            data.id = entry.getValue();
            data.name = entry.getKey();
            data.frequency = frequencyMap.get(data.name);
            dictionaryModel.data[data.id - 1] = data;
        }
        return dictionaryModel;
    }

    public void save() {
        FileHelper.getInstance().create(file);
        final DictionaryModel dictionaryModel = getModel();
        sort(dictionaryModel.data);
        FileHelper.getInstance().write(file, dictionaryModel);
    }
}
