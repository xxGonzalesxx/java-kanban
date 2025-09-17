import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    // Вложенный класс узла двусвязного списка
    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    // HashMap: id -> Node (быстрый доступ)
    private final Map<Integer, Node> nodes = new HashMap<>();

    // Голова и хвост двусвязного списка
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (task == null) return;

        int id = task.getId();

        // Если задача уже в истории — удаляем старый узел (константно)
        Node existing = nodes.get(id);
        if (existing != null) {
            removeNode(existing);
        }

        // Вставляем новую запись в конец списка
        Node newNode = linkLast(task);
        // Обновляем/пишем ссылку в HashMap
        nodes.put(id, newNode);
    }

    @Override
    public void remove(int id) {
        // Берём узел из map и удаляем его из списка
        Node node = nodes.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    // --- Вспомогательные методы двусвязного списка ---

    // Добавить задачу в конец списка, вернуть созданный Node
    private Node linkLast(Task task) {
        Node node = new Node(task);
        if (tail == null) {
            // список пуст
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        return node;
    }

    // Удалить конкретный узел из списка (O(1))
    private void removeNode(Node node) {
        if (node == null) return;

        Node prev = node.prev;
        Node next = node.next;

        if (prev != null) {
            prev.next = next;
        } else {
            // node был головой
            head = next;
        }

        if (next != null) {
            next.prev = prev;
        } else {
            // node был хвостом
            tail = prev;
        }

        // "обнуляем" ссылки для GC
        node.prev = null;
        node.next = null;
        node.task = null;
    }

    // Собрать задачи в ArrayList в порядке от головы к хвосту
    private List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        Node current = head;
        while (current != null) {
            result.add(current.task);
            current = current.next;
        }
        return result;
    }
}
