package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;

    // КОНСТРУКТОР С 5 ПАРАМЕТРАМИ (для тестов)
    public Subtask(int id, String name, String description, Status status, int epicId) {
        super(id, name, description, status, null, null);
        this.epicId = epicId;
    }

    // Конструктор с 7 параметрами
    public Subtask(int id, String name, String description, Status status, int epicId,
                   Duration duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", epicId=" + epicId +
                '}';
    }
}