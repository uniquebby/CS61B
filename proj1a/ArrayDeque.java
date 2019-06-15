/**
 * Array deque.
 * @author yangbinbin
 */
public class ArrayDeque<T> {
    private static int INIT_MAXSIZE = 8;

    private int size;
    private int maxSize;
    private int nextFirst;
    private int nextLast;
    private T[] items;

    /**
     * Creates an empty array deque.
     */
    public ArrayDeque() {
        this.size = 0;
        this.maxSize =  INIT_MAXSIZE;
        this.nextFirst = 3;
        this.nextLast = 4;
        this.items = (T[])new Object[INIT_MAXSIZE];
    }

    /**
     * Creates a deep copy of other.
     * @param other
     */
    public ArrayDeque(ArrayDeque other) {
        this.size = other.size;
    }

    /**
     * Adds an item of type T to the front of the deque.
     * @param item
     */
    public void addFirst(T item) {
        if (this.isFull()) {
            this.resize(this.maxSize * 2);
        }
        items[nextFirst] = item;
        nextFirst = (nextFirst - 1 + this.maxSize) % this.maxSize;
        ++this.size;
    }

    /**
     * Adds an item of type T to the back of the deque.
     * @param item
     */
    public void addLast(T item) {
        if (this.isFull()) {
            this.resize(this.maxSize * 2);
        }
        items[nextLast] = item;
        nextLast = (nextFirst + 1) % this.maxSize;
        ++this.size;
    }

    /**
     * @return Return true if the arrayDeq is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * @return Returns the number of items in the deque.
     */
    public int size() {
        return this.size;
    }

    /**
     * @return Returns the maxsize of the deque.
     */
    public int maxSize() {
        return this.maxSize;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    public void printDeque() {
        for (int i = (this.nextFirst + 1) % this.maxSize; i != this.nextLast; ++i) {
            System.out.print(this.items[i]);
            System.out.print(' ');
        }
        System.out.println();
    }

    /**
     * Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.
     * @return the first item or null if the deque is empty.
     */
    public T removlFirst() {
        if (this.isEmpty()) {
            return null;
        } else {
            this.nextFirst = (this.nextFirst + 1) % this.maxSize;
            T item = this.items[this.nextFirst];
            --this.size;
            return item;
        }
    }

    /**
     * Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.
     * @return the last item or null if the deque is empty.
     */
    public T removlLast() {
        if (this.isEmpty()) {
            return null;
        } else {
            this.nextLast = (this.nextLast - 1 + this.maxSize) % this.maxSize;
            T item = this.items[this.nextLast];
            --this.size;
            if (this.maxSize >= 16 && (this.maxSize / this.size) > 4) {
                this.resize(this.maxSize / 2);
            }
            return item;
        }
    }

    /**
     *  Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     *  If no such item exists, returns null. Must not alter the deque!
     * @param index
     * @return null or type T according to the index.
     */
    public T get(int index) {
        if (index >= this.size || index < 0) {
            return null;
        } else {
            return this.items[(this.nextFirst + 1 + index) % this.maxSize];
        }
    }

    /**
     * @return Return true if there is no space, false otherwise.
     */
    public boolean isFull() {
        return this.nextFirst == this.nextLast;
    }

    /**
     * Double the size of the array.
     */
    private void resize(int maxSize) {
        this.maxSize *= maxSize;
        T[] newItems = (T[])new Object[this.maxSize];
        int i = 0;
        while (!this.isEmpty()) {
            newItems[i] = this.removlFirst();
            ++i;
        }
        this.items = newItems;
        this.nextFirst = this.maxSize - 1;
        this.nextLast = i;
    }


}










