import managers.TaskManager;
import managers.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicStatusTest {

    private TaskManager manager;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
    }

    @Test
    void epicStatus_shouldBeNewWhenNoSubtasks() {
        Epic epic = new Epic(1, "Epic", "Description", Status.NEW);
        manager.createEpic(epic);

        assertEquals(Status.NEW, manager.getEpicById(epic.getId()).getStatus(),
                "Эпик без подзадач должен иметь статус NEW");
    }

    @Test
    void epicStatus_shouldBeNewWhenAllSubtasksNew() {
        Epic epic = new Epic(1, "Epic", "Desc", Status.NEW);
        manager.createEpic(epic);

        // ИСПОЛЬЗУЙ КОНСТРУКТОР С 5 ПАРАМЕТРАМИ
        Subtask sub1 = new Subtask(2, "Sub1", "Desc", Status.NEW, epic.getId());
        Subtask sub2 = new Subtask(3, "Sub2", "Desc", Status.NEW, epic.getId());
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        assertEquals(Status.NEW, manager.getEpicById(epic.getId()).getStatus(),
                "Эпик со всеми подзадачами NEW должен иметь статус NEW");
    }

    @Test
    void epicStatus_shouldBeDoneWhenAllSubtasksDone() {
        Epic epic = new Epic(1, "Epic", "Desc", Status.NEW);
        manager.createEpic(epic);

        // ИСПОЛЬЗУЙ КОНСТРУКТОР С 5 ПАРАМЕТРАМИ
        Subtask sub1 = new Subtask(2, "Sub1", "Desc", Status.DONE, epic.getId());
        Subtask sub2 = new Subtask(3, "Sub2", "Desc", Status.DONE, epic.getId());
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        assertEquals(Status.DONE, manager.getEpicById(epic.getId()).getStatus(),
                "Эпик со всеми подзадачами DONE должен иметь статус DONE");
    }

    @Test
    void epicStatus_shouldBeInProgressWhenMixedNewAndDone() {
        Epic epic = new Epic(1, "Epic", "Desc", Status.NEW);
        manager.createEpic(epic);

        // ИСПОЛЬЗУЙ КОНСТРУКТОР С 5 ПАРАМЕТРАМИ
        Subtask sub1 = new Subtask(2, "Sub1", "Desc", Status.NEW, epic.getId());
        Subtask sub2 = new Subtask(3, "Sub2", "Desc", Status.DONE, epic.getId());
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus(),
                "Эпик с подзадачами NEW и DONE должен иметь статус IN_PROGRESS");
    }

    @Test
    void epicStatus_shouldBeInProgressWhenAnySubtaskInProgress() {
        Epic epic = new Epic(1, "Epic", "Desc", Status.NEW);
        manager.createEpic(epic);

        // ИСПОЛЬЗУЙ КОНСТРУКТОР С 5 ПАРАМЕТРАМИ
        Subtask sub1 = new Subtask(2, "Sub1", "Desc", Status.IN_PROGRESS, epic.getId());
        Subtask sub2 = new Subtask(3, "Sub2", "Desc", Status.NEW, epic.getId());
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        assertEquals(Status.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus(),
                "Эпик с любой подзадачей IN_PROGRESS должен иметь статус IN_PROGRESS");
    }
}