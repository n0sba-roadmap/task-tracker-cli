package project.service;

import java.util.List;

import project.model.Task;

public interface Service {
    void handleCommand(String[] args);
    void add(String taskName);
    void update(String taskName, String id);
    void delete(String id);
    void mark(String status, String id);
    List<Task> list(String status);
}
