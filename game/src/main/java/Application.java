import net.runelite.client.RuneLite;

public class Application {

    public static void main(String[] args) {
        try {
            RuneLite.main(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
