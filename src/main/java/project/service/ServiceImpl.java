package project.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.dao.Db;
import project.dao.TaskDao;
import project.model.Task;
import project.model.TaskStatusEnum;

public class ServiceImpl implements Service {

    private static final Logger LOGGER = Logger.getLogger(ServiceImpl.class.getName());
    private static final ObjectMapper mapper = new ObjectMapper();

    private final TaskDao taskDao;

    public ServiceImpl(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void handleCommand(String[] args) {
        int argsSize = args.length;
        if (argsSize == 0) return; 
        String func = args[0];
        String[] funcArgs = {};
        if (argsSize > 1) {
            funcArgs = Arrays.copyOfRange(args, 1, argsSize);
        }
        int funcArsSize = funcArgs.length;

        String taskName = "";
        String status = "";
        String id = "";
        switch (func) {
            case "add":
                if (funcArsSize > 1) return;
                taskName = funcArgs[0];
                add(taskName);
                break;
            case "update":
                if (funcArsSize != 2) return;
                id = funcArgs[0];
                taskName = funcArgs[1];
                update(taskName, id);
                break;
            case "delete":
                if (funcArsSize != 1) return;
                id = funcArgs[0];
                delete(id);
                break;
            case "mark-in-progress":
                status = TaskStatusEnum.INPROGRESS.getValue();
                if (funcArsSize != 1) return;
                id = funcArgs[0];
                mark(status, id);
                break;
            case "mark-done":
                status = TaskStatusEnum.DONE.getValue();
                if (funcArsSize != 1) return;
                id = funcArgs[0];
                mark(status, id);
                break;
            case "list":
                if (funcArsSize > 1) return;
                if (funcArsSize == 1) {
                    status = funcArgs[0];
                }
                list(status);
                break;
            default:
        }
    }

    @Override
    public void add(String taskName) {
        try {
            // parse file to list
            File storageFile = Db.getStorageFile();
            List<Task> listTask = this.taskDao.getListTaskFromFile(storageFile);
            LOGGER.log(Level.INFO, "List task from storage: {0}", listTask);
            // create new task
            Task task = new Task();
            String id = UUID.randomUUID().toString(); 
            task.setId(id);
            task.setStatus(TaskStatusEnum.TODO.getValue());
            task.setDescription(taskName);
            task.setCreatedAt(LocalDateTime.now());
            task.setUpdatedAt(LocalDateTime.now());
            // add new task to list
            listTask.add(task);
            // parse list to file
            this.taskDao.writeListTaskToFile(listTask, storageFile);
            LOGGER.log(Level.INFO, "Task added successfully (ID: {0})", id);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }

    @Override
    public void update(String taskName, String id) {
        try {
            // parse file to list
            File storageFile = Db.getStorageFile();
            List<Task> listTask = this.taskDao.getListTaskFromFile(storageFile);
            LOGGER.log(Level.INFO, "List task from storage: {0}", listTask);
            // update task
            Task foundTask = listTask.stream().filter(item->id.equals(item.getId())).findFirst().orElse(null);
            if (foundTask == null) {
                LOGGER.log(Level.WARNING, "Can not find task (ID: {0})", id);
                return;
            }
            LOGGER.log(Level.INFO, "Update task id {0} name from {1} to {2}", new Object[]{foundTask.getId(), foundTask.getDescription(), taskName});
            foundTask.setDescription(taskName);
            // parse  list to file
            this.taskDao.writeListTaskToFile(listTask, storageFile);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            // parse file to list
            File storageFile = Db.getStorageFile();
            List<Task> listTask = this.taskDao.getListTaskFromFile(storageFile);
            LOGGER.log(Level.INFO, "List task from storage: {0}", listTask);
            // delete task
            listTask.removeIf(item->id.equals(item.getId()));
            LOGGER.log(Level.INFO, "Delete id {0} successfull", id);
            // parse list to file
            this.taskDao.writeListTaskToFile(listTask, storageFile);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }

    @Override
    public void mark(String status, String id) {
        try {
            // parse file to list
            File storageFile = Db.getStorageFile();
            List<Task> listTask = this.taskDao.getListTaskFromFile(storageFile);
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
            this.taskDao.writeListTaskToFile(listTask, storageFile);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "", e);
        }
    }

    @Override
    public List<Task> list(String status) {
        List<Task> listTask = new ArrayList<>();
        try {
            File storageFile = Db.getStorageFile();
            listTask = this.taskDao.getListTaskFromFile(storageFile);
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
