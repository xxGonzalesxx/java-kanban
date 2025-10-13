package managers;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected int nextId = 1;


    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public void createTask(Task task) {
        if (hasTimeConflict(task)) {
            throw new ManagerSaveException("Задача пересекается по времени с существующими задачами");
        }
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) {
        if (hasTimeConflict(task)) {
            throw new ManagerSaveException("Задача пересекается по времени с существующими задачами");
        }
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }


    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }


    //  tasks.Epic
    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear(); // удаляем и подзадачи
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void createEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
        }
    }


    //  tasks.Subtask
    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtaskIds();
            updateEpicStatus(epic);
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (hasTimeConflict(subtask)) {
            throw new ManagerSaveException("Подзадача пересекается по времени с существующими задачами");
        }
        if (epics.containsKey(subtask.getEpicId())) {
            subtask.setId(nextId++);
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId());
            updateEpicStatus(epics.get(subtask.getEpicId()));
            calculateEpicTime(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (hasTimeConflict(subtask)) {
            throw new ManagerSaveException("Подзадача пересекается по времени с существующими задачами");
        }
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(epics.get(subtask.getEpicId()));
            calculateEpicTime(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtaskIds().remove(Integer.valueOf(id));
            updateEpicStatus(epic);
            calculateEpicTime(epic);
        }
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(int epicId) {
        return epics.get(epicId).getSubtaskIds().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        // Используем TreeSet для автоматической сортировки
        Set<Task> prioritizedSet = new TreeSet<>(
                Comparator.comparing(
                        Task::getStartTime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                )
        );
        // Добавляем все задачи с указанным startTime
        for (Task task : getAllTasks()) {
            if (task.getStartTime() != null) {
                prioritizedSet.add(task);
            }
        }
        // Добавляем все подзадачи с указанным startTime
        for (Subtask subtask : getAllSubtasks()) {
            if (subtask.getStartTime() != null) {
                prioritizedSet.add(subtask);
            }
        }
        return new ArrayList<>(prioritizedSet);
    }

    // Вспомогательный метод для пересчёта статуса эпика
    protected void updateEpicStatus(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (int id : subtaskIds) {
            Status status = subtasks.get(id).getStatus();
            if (status != Status.NEW) {
                allNew = false;
            }
            if (status != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    //Проверяем на пересечение задач
    private boolean isTimeOverlap(Task task1, Task task2) {
        if (task1.getStartTime() == null || task1.getEndTime() == null ||
                task2.getStartTime() == null || task2.getEndTime() == null) {
            return false; // задачи без времени не пересекаются
        }
        return task1.getStartTime().isBefore(task2.getEndTime()) &&
                task2.getStartTime().isBefore(task1.getEndTime());
    }
    public boolean hasTimeConflict(Task newTask) {
        if (newTask.getStartTime() == null || newTask.getEndTime() == null) {
            return false; // задача без времени не конфликтует
        }

        // Проверяем пересечение с обычными задачами
        for (Task task : getAllTasks()) {
            if (task.getId() != newTask.getId() && isTimeOverlap(task, newTask)) {
                return true;
            }
        }

        // Проверяем пересечение с подзадачами
        for (Subtask subtask : getAllSubtasks()) {
            if (subtask.getId() != newTask.getId() && isTimeOverlap(subtask, newTask)) {
                return true;
            }
        }

        return false;
    }
    private void calculateEpicTime(Epic epic) {
        List<Subtask> epicSubtasks = getSubtasksOfEpic(epic.getId());

        if (epicSubtasks.isEmpty()) {
            epic.setEndTime(null);
            return;
        }

        // Находим самое раннее время начала и самое позднее время окончания
        LocalDateTime earliestStart = null;
        LocalDateTime latestEnd = null;
        Duration totalDuration = Duration.ZERO;

        for (Subtask subtask : epicSubtasks) {
            if (subtask.getStartTime() != null) {
                if (earliestStart == null || subtask.getStartTime().isBefore(earliestStart)) {
                    earliestStart = subtask.getStartTime();
                }
            }

            if (subtask.getEndTime() != null) {
                if (latestEnd == null || subtask.getEndTime().isAfter(latestEnd)) {
                    latestEnd = subtask.getEndTime();
                }
            }

            if (subtask.getDuration() != null) {
                totalDuration = totalDuration.plus(subtask.getDuration());
            }
        }

        epic.setEndTime(latestEnd);
    }
}
