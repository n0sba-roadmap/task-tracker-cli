package project.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import project.model.Task;

public class TaskDaoImpl extends Db implements TaskDao {

    private static final Logger LOGGER = Logger.getLogger(TaskDaoImpl.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        try {
            // Register the JSR310 Module with ObjectMapper to able to work with LocalDateTime in Java
            mapper.registerModule(new JavaTimeModule());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }   
    
    @Override
    public List<Task> getListTaskFromFile(File storageFile) throws IOException {
        try {
            String storageFileValue = Files.readString(storageFile.toPath());
            List<Task> listTask = new ArrayList<>();
            // parse file to list
            if (!"".equals(storageFileValue) && null != storageFileValue) {
                listTask = mapper.readValue(storageFile, mapper.getTypeFactory().constructCollectionType(List.class, Task.class));
            }
            return listTask;
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public void writeListTaskToFile(List<Task> listTask, File storageFile) throws IOException {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(storageFile, listTask);
        } catch (IOException e) {
            throw e;
        }
    }

    public List<Task> list(String status) {
        List<Task> listTask = new ArrayList<>();
        try {
            File storageFile = getStorageFile();
            listTask = getListTaskFromFile(storageFile);
            if (!"".equals(status) && status != null) {
                listTask = listTask.stream().filter(item->status.equals(item.getStatus())).collect(Collectors.toList());
            }
            LOGGER.log(Level.INFO, "List task from storage: {0}", listTask);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "", e);
        }
        return listTask;
    }
    
}
