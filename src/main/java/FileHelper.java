import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public final class FileHelper {
    private static FileHelper instance;
    private final String separator;
    private final String directory;
    private final Gson gson;
    private final String folder = "data";
    private final String extension = ".json";

    public static FileHelper getInstance() {
        if (instance == null) {
            instance = new FileHelper();
        }
        return instance;
    }

    private FileHelper() {
        separator = System.getProperty("file.separator");
        directory = System.getProperty("user.dir");
        gson = new Gson();
    }

    public void create(final String name) {
        try {
            final File file = new File(directory + separator + folder + separator + name + extension);
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T read(final String name, final Class<T> clazz) {
        final StringBuilder builder = new StringBuilder();
        try {
            final File file = new File(directory + separator + folder + separator + name + extension);
            final Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                final String data = scanner.nextLine();
                builder.append(data).append("\n");
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gson.fromJson(builder.toString(), clazz);
    }

    public void write(final String name, final Object data) {
        try {
            final FileWriter fileWriter = new FileWriter(directory + separator + folder + separator + name + extension);
            fileWriter.write(gson.toJson(data));
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
