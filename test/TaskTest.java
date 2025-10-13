import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testEquals_shouldReturnTrueForSameId() {
        Task task1 = new Task(1, "Задача 1", "Описание", Status.NEW);
        Task task2 = new Task(1, "Другая задача", "Другое описание", Status.DONE);

        assertEquals(task1, task2, "Задачи с одинаковым id должны считаться равными");
    }

    @Test
    void testEquals_shouldReturnFalseForDifferentId() {
        Task task1 = new Task(1, "Задача 1", "Описание", Status.NEW);
        Task task2 = new Task(2, "Задача 2", "Описание", Status.NEW);

        assertNotEquals(task1, task2, "Задачи с разными id не должны быть равны");
    }

    @Test
    void testHashCode_shouldBeSameForSameId() {
        Task task1 = new Task(1, "Задача 1", "Описание", Status.NEW);
        Task task2 = new Task(1, "Задача 2", "Описание другое", Status.IN_PROGRESS);

        assertEquals(task1.hashCode(), task2.hashCode(), "HashCode должен совпадать у задач с одинаковым id");
    }

    @Test
    void testToString_shouldContainAllFields() {
        Task task = new Task(5, "Название", "Описание", Status.NEW);
        String taskString = task.toString();

        assertTrue(taskString.contains("id=5"), "Строка должна содержать id");
        assertTrue(taskString.contains("Название"), "Строка должна содержать имя");
        assertTrue(taskString.contains("Описание"), "Строка должна содержать описание");
        assertTrue(taskString.contains("NEW"), "Строка должна содержать статус");
    }
}
