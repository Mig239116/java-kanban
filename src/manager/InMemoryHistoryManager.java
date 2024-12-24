package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> tasks;
    private HashMap<Integer, Node<Task>> taskTracker;
    private Node<Task> head;
    private Node<Task> tail;

    class Node<E> {
        public E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    public InMemoryHistoryManager() {
        tasks = new ArrayList<>();
        taskTracker = new HashMap<>();
    }

    private Node<Task> linkLast(Task element) {
       final Node<Task> oldTail = tail;
       final Node<Task> newNode = new Node<>(oldTail, element, null);
       tail = newNode;
       if (oldTail == null) {
           head = newNode;
       } else {
           oldTail.next = newNode;
       }
       return newNode;
    }

    private void removeNode(Node<Task> node) {
        Node<Task> previousNode = node.prev;
        Node<Task> nextNode = node.next;
        if (nextNode == null & previousNode != null) {
            tail = previousNode;
            previousNode.next = null;
        } else if (previousNode == null & nextNode != null) {
            head = nextNode;
            nextNode.prev = null;
        } else if (previousNode == null & nextNode == null) {
            tail = null;
            head = null;
        } else {
            previousNode.next = nextNode;
            nextNode.prev = previousNode;
        }
        node.prev = null;
        node.next = null;
        node.data = null;
    }

    private void getTasks() {
        tasks.clear();
        for (Node<Task> node: taskTracker.values()) {
            tasks.add(node.data);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        getTasks();
        return new ArrayList<>(tasks);
    }

    @Override
    public void add(Task task) {
        if (taskTracker.containsKey(task.getID())) {
            removeNode(taskTracker.get(task.getID()));
            remove(task.getID());
        }
        taskTracker.put(task.getID(), linkLast(task));

    }

    @Override
    public void remove(int id) {
        taskTracker.remove(id);
    }

}
