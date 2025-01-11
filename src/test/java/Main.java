import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("This is an info log!");
        logger.debug("This is a debug log!");
        logger.error("This is an error log!");
        System.out.println("Completed");
    }
}
