package project.dao;

import java.io.File;
import java.io.IOException;
import java.util.List;

import project.model.Task;

public interface TaskDao {
    List<Task> getListTaskFromFile(File storageFile) throws IOException;
    void writeListTaskToFile(List<Task> listTask, File storageFile) throws IOException;
}