import org.junit.jupiter.api.Test;

final class FileHelperTest {

    @Test
    void test() {
        FileHelper.getInstance().create("test");
        FileHelper.getInstance().write("test", "Hello World!");
        final String data = FileHelper.getInstance().read("test");
        System.out.println(data);
    }
}