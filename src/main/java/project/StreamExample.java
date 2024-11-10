package project;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamExample {
    private static final Logger logger = Logger.getLogger(StreamExample.class.getName());
    private static final String PATH_FILE = "src/resource/task.json";

    public static void main(String[] args) {
        File file = new File(PATH_FILE);

        try (FileInputStream inputStream = new FileInputStream(file)) {
            int data;
            while ((data = inputStream.read()) != -1) { // reads one byte at a time
                logger.log(Level.INFO, "Char: {0}", (char) data);
            }
        } catch (FileNotFoundException fnfe) {
            logger.log(Level.WARNING, "File not found {0}", fnfe);
            // Attempt to create the missing file
            try {
                if (file.createNewFile()) {
                    logger.log(Level.INFO, "File 'example.json' created successfully");
                } else {
                    logger.log(Level.INFO, "Failed to create {}", PATH_FILE);
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "An error occurred while creating the file", e);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "{0}", e);
        }
    }
}
