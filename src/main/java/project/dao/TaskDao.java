package project.dao;

public interface TaskDao {
    void add(String name);
    void update(String id, String name);
    void delete(String id);
    void mark(String status, String id);
    void list(String status);
}