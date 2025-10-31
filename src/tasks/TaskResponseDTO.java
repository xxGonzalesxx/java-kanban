package tasks;

public class TaskResponseDTO {
    public int id;
    public String name;
    public String description;
    public Status status;
    public String type;

    public TaskResponseDTO(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
        this.status = task.getStatus();
        this.type = task.getType().toString();
    }
}
