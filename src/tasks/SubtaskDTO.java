package tasks;

public class SubtaskDTO {
    public int id;
    public String name;
    public String description;
    public Status status;
    public int epicId;  // ← важное поле!
}