import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node {
    private Node next;
    private Float value;
    private Lock mutex;

    public Node(Float value) {
        this.value = value;
        this.next = null;
        this.mutex = new ReentrantLock();
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Float getValue() {
        return value;
    }

    public Lock getMutex() {
        return mutex;
    }

}
