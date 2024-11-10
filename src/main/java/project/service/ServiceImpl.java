package project.service;

import java.util.Arrays;

import project.dao.TaskDao;
import project.model.TaskStatusEnum;

public class ServiceImpl implements Service {

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
        switch (func) {
            case "add":
                add(funcArgs);
                break;
            case "update":
                update(funcArgs);
                break;
            case "delete":
                delete(funcArgs);
                break;
            case "mark-in-progress":
                markInProgress(funcArgs);
                break;
            case "mark-done":
                markDone(funcArgs);
                break;
            case "list":
                list(funcArgs);
                break;
            default:
        }
    }

    @Override
    public void add(String[] args) {
        int argsSize = args.length;
        if (argsSize > 1) return;
        String taskName = args[0];
        this.taskDao.add(taskName);
    }

    @Override
    public void update(String[] args) {
        int argsSize = args.length;
        if (argsSize != 2) return;
        String id = args[0];
        String taskName = args[1];
        this.taskDao.update(id, taskName);
    }

    @Override
    public void delete(String[] args) {
        int argsSize = args.length;
        if (argsSize != 1) return;
        String id = args[0];
        this.taskDao.delete(id);
    }

    @Override
    public void markInProgress(String[] args) {
        String status = TaskStatusEnum.INPROGRESS.getValue();
        int argsSize = args.length;
        if (argsSize != 1) return;
        String id = args[0];
        this.taskDao.mark(status, id);
    }

    @Override
    public void markDone(String[] args) {
        String status = TaskStatusEnum.DONE.getValue();
        int argsSize = args.length;
        if (argsSize != 1) return;
        String id = args[0];
        this.taskDao.mark(status, id);
    }

    @Override
    public void list(String[] args) {
        int argsSize = args.length;
        String status = null;
        if (argsSize > 1) return;
        if (argsSize == 1) {
            status = args[0];
        }
        this.taskDao.list(status);
    }
    
}
