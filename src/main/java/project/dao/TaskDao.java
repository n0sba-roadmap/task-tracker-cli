package project.dao;

import java.util.List;

import project.model.Task;

public interface TaskDao {
    void add(String name);
    void update(String id, String name);
    void delete(String id);
    void mark(String status, String id);
    List<Task> list(String status);
}