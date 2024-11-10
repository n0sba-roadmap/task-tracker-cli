package project.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import project.model.Task;
import project.model.TaskStatusEnum;

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
    public void add(String name) {
        try {
            // parse file to list
            File storageFile = getStorageFile();
            List<Task> listTask = getListTaskFromFile(storageFile);
            LOGGER.log(Level.INFO, "List task from storage: {0}", listTask);
            // create new task
            Task task = new Task();
            String id = UUID.randomUUID().toString(); 
            task.setId(id);
            task.setStatus(TaskStatusEnum.TODO.getValue());
            task.setDescription(name);
            task.setCreatedAt(LocalDateTime.now());
            task.setUpdatedAt(LocalDateTime.now());
            // add new task to list
            listTask.add(task);
            // parse list to file
            mapper.writerWithDefaultPrettyPrinter().writeValue(storageFile, listTask);
            LOGGER.log(Level.INFO, "Task added successfully (ID: {0})", id);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }

    @Override
    public void update(String id, String name) {
        try {
            // parse file to list
            File storageFile = getStorageFile();
            List<Task> listTask = getListTaskFromFile(storageFile);
            LOGGER.log(Level.INFO, "List task from storage: {0}", listTask);
            // update task
            Task foundTask = listTask.stream().filter(item->id.equals(item.getId())).findFirst().orElse(null);
            if (foundTask == null) {
                LOGGER.log(Level.WARNING, "Can not find task (ID: {0})", id);
                return;
            }
            LOGGER.log(Level.INFO, "Update task id {0} name from {1} to {2}", new Object[]{foundTask.getId(), foundTask.getDescription(), name});
            foundTask.setDescription(name);
            // parse  list to file
            mapper.writerWithDefaultPrettyPrinter().writeValue(storageFile, listTask);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            // parse file to list
            File storageFile = getStorageFile();
            List<Task> listTask = getListTaskFromFile(storageFile);
            LOGGER.log(Level.INFO, "List task from storage: {0}", listTask);
            // delete task
            listTask.removeIf(item->id.equals(item.getId()));
            LOGGER.log(Level.INFO, "Delete id {0} successfull", id);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }

    @Override
    public void mark(String status, String id) {
        try {
            // parse file to list
            File storageFile = getStorageFile();
            List<Task> listTask = getListTaskFromFile(storageFile);
            LOGGER.log(Level.INFO, "List task from storage: {0}", listTask);
            // mark task with status
            Task foundTask = listTask.stream().filter(item->id.equals(item.getId())).findFirst().orElse(null);
            if (foundTask == null) {
                LOGGER.log(Level.WARNING, "Can not find task (ID: {0})", id);
                return;
            }
            LOGGER.log(Level.INFO, "Mark task id ({0}) from '{1}' to '{2}'", new Object[]{id, foundTask.getStatus(), status});
            foundTask.setStatus(status);
            // parse  list to file
            mapper.writerWithDefaultPrettyPrinter().writeValue(storageFile, listTask);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }

    @Override
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
