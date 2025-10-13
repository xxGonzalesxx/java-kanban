package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;
    // Продолжительность(duration) задачи, оценка того, сколько времени она займёт в минутах.
    // Это значение требуется хранить в экземпляре класса
    private Duration duration;

    //Дата и время(LocalDateTime -> statTime), когда предполагается приступить к выполнению задачи.
    // Здесь лучше всего подойдёт
    private LocalDateTime startTime;

    public Task(int id, String name, String description, Status status) {
        this(id, name, description, status, null, null);
    }

    public Task(int id, String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration=duration;
        this.startTime=startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    public LocalDateTime getEndTime(){
        if (startTime==null || duration==null){
            return null;
        }
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Task other = (Task) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}
