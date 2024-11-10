package project.service;

public interface Service {
    void handleCommand(String[] args);
    void add(String[] args);
    void update(String[] args);
    void delete(String[] args);
    void markInProgress(String[] args);
    void markDone(String[] args);
    void list(String[] args);
}
