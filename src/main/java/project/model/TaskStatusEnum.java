package project.model;

public enum TaskStatusEnum {
    TODO("todo"), INPROGRESS("in-progress"), DONE("done");
    private final String value;
    // constructor
    TaskStatusEnum(String value) {
        this.value = value;
    }
    // Getter
    public String getValue() {
        return value;
    }
}
