package es.datastructur.synthesizer;
import java.lang.reflect.Array;
import java.util.Iterator;

//TODO: Make sure to that this class and all of its methods are public
//TODO: Make sure to add the override tag for all overridden methods
//TODO: Make sure to make this class implement BoundedQueue<T>

public class ArrayRingBuffer<T> implements BoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        first = last = fillCount = 0;
        rb = (T[]) new Object[capacity];
    }

    @Override
    public int capacity() {
        return rb.length;
    }

    @Override
    public int fillCount() {
        return fillCount;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    @Override
    public void enqueue(T x) {
        if (!isFull()) {
            rb[last] = x;
            last = (last + 1) % capacity();
            ++fillCount;
        }
        return;
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T dequeue() {
        if (isEmpty()) {
            return null;
        } else {
            T res = rb[first];
            first = (first + 1) % capacity();
            --fillCount;
            return res;
        }
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    @Override
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        } else {
            return rb[first];
        }
    }

    public class BoundedQueueIterator implements Iterator<T>{
        private int index;
        public BoundedQueueIterator() {
            index = first;
        }

        public boolean hasNext() {
            return index != last;
        }

        public T next() {
            T returnItem = rb[index];
            index += 1;
            return returnItem;
        }

    }
    @Override
    public Iterator<T> iterator() {
        return new BoundedQueueIterator();
    }

    @Override
    public boolean equals(Object obj) {
        BoundedQueue<T> other = (BoundedQueue<T>) obj;
        if (this.capacity() != other.capacity()) { return false; }
        Iterator iterator = other.iterator();
        for (T item : this) {
            if (iterator.next() != item) {
                return false;
            }
        }
        return true;
    }
}
