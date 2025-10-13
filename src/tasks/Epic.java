package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtaskIds;// список ID подзадач
    private LocalDateTime endTime;

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status,null,null);
        this.subtaskIds = new ArrayList<>();
    }
    public Epic(int id, String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        super(id, name, description, status, null, null);
        this.subtaskIds = new ArrayList<>();
    }
    @Override
    public void setDuration(Duration duration) {
        // Epic игнорирует
    }
    @Override
    public void setStartTime(LocalDateTime startTime) {
        // Epic игнорирует
    }
    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int id) {
        subtaskIds.add(id);
    }

    public void clearSubtaskIds() {
        subtaskIds.clear();
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }
    @Override
    public Duration getDuration(){
        return null;
    }
    @Override
    public LocalDateTime getStartTime(){
        return null;
    }
    @Override
    public LocalDateTime getEndTime(){
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}
