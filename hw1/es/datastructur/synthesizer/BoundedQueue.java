package es.datastructur.synthesizer;

public interface BoundedQueue<T> extends Iterable<T>{
    /**
     * @return Return size of the buffer.
     */
    public abstract int capacity();

    /**
     * @return Return number of items currently in the buffer.
     */
    public abstract int fillCount();

    /**
     * Add item x to the end.
     * @param x
     */
    public abstract void enqueue(T x);

    /**
     * delete and return item from the front.
     * @return Return item from the front.
     */
    public abstract T dequeue();

    /**
     * @return Return (but do not delete) item from the front.
     */
    public abstract T peek();

    /**
     * Is the buffer empty?
     * @return
     */
    public default boolean isEmpty() {
        return fillCount() == 0;
    }

    /**
     * Is the buffer full?
     * @return
     */
    public default boolean isFull() {
        return fillCount() == capacity();
    }
}
