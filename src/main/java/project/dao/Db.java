package project.dao;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Db {
    
    private static final Logger LOGGER = Logger.getLogger(Db.class.getName());
    private static final String PATH_FILE = "src/resource/task.json";
    public File getStorageFile() {
        File file = null;
        try {
            file = new File(PATH_FILE);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "{0}", e);
        }
        return file;
    }
}
