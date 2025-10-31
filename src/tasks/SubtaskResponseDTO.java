package tasks;

public class SubtaskResponseDTO {
    public int id;
    public String name;
    public String description;
    public Status status;
    public String type;
    public int epicId;

    public SubtaskResponseDTO(Subtask subtask) {
        this.id = subtask.getId();
        this.name = subtask.getName();
        this.description = subtask.getDescription();
        this.status = subtask.getStatus();
        this.type = subtask.getType().toString();
        this.epicId = subtask.getEpicId();
    }
}
