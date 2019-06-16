/**
 * Double-ended queue(sequence containers with dynamic sizes that can
 * beexpanded or contracted on both ends).
 *
 * @author yangbinbin
 */
public class LinkedListDeque<T> implements Deque<T>{
    private ItemNode sentinel;
    private int size;

    /**
     * Itemnode of the double-ended queue.
     */
    public class ItemNode {
        public T item;
        public ItemNode prev;
        public ItemNode next;

        public ItemNode(T item) {
           this.item = item;
        }
    }

    public LinkedListDeque() {
        this.sentinel = new ItemNode(null);
        this.sentinel.prev = this.sentinel.next = this.sentinel;
        this.size = 0;
    }

    /**
     * Create a deep copy of other.
     * @param other
     */
    public LinkedListDeque(LinkedListDeque other) {
        this.sentinel = new ItemNode(null);
        this.size = other.size();
        ItemNode pNode = this.sentinel;
        T item;
        for (int i = 0; i < other.size(); ++i) {
            item = (T)other.get(i);
            pNode.next = new ItemNode(item);
            pNode = pNode.next;
        }
    }

    /**
     * Adds an item of type T to the front of the deque.
     * @param item
     */
    @Override
    public void addFirst(T item) {
        ItemNode pNode = new ItemNode(item);
        pNode.next = this.sentinel.next;
        this.sentinel.next = pNode;
        pNode.prev = this.sentinel;
        pNode.next.prev = pNode;
        ++this.size;
    }

    /**
     * Adds an item of type T to the back of the deque.
     * @param item
     */
    @Override
    public void addLast(T item) {
        ItemNode pNode = new ItemNode(item);
        pNode.prev = this.sentinel.prev;
        this.sentinel.prev = pNode;
        pNode.next = this.sentinel;
        pNode.prev.next = pNode;
        ++this.size;
    }

    /**
     * @return  Returns the number of items in the deque.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    @Override
    public void printDeque() {
        ItemNode pNode = this.sentinel;
        while (pNode.next != this.sentinel) {
            pNode = pNode.next;
            System.out.print(pNode.item);
            System.out.print(' ');
        }
        System.out.println();
    }

    /**
     *  Removes and returns the item at the front of the deque.
     * @return Returns the item at the front of the deque. If no such item exits, return null.
     */
    @Override
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        } else {
            ItemNode pNode = this.sentinel.next;
            this.sentinel.next = pNode.next;
            pNode.next.prev = this.sentinel;
            --this.size;
            return pNode.item;
        }
    }

    /**
    *  Removes and returns the item at the back of the deque.
    * @return Returns the item at the back of the deque. If no such item exits, return null.
    */
    @Override
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        } else {
            ItemNode pNode = this.sentinel.prev;
            this.sentinel.prev = pNode.prev;
            pNode.prev.next = this.sentinel;
            --this.size;
            return pNode.item;
        }
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     * @param index the index of the item to get.
     * @return the type T item with index.
     */
    @Override
    public T get(int index) {
        if (index >= this.size || index < 0) {
            return null;
        } else {
            ItemNode pNode = this.sentinel.next;
            for (int i = 0; i < index; ++i) {
                pNode = pNode.next;
            }
            return pNode.item;
        }
    }

    public T getRecursive(int index) {
        LinkedListDeque<T> DDList= this;
        if (DDList.sentinel.next == DDList.sentinel) {
            return null;
        } else if (index == 0) {
            return DDList.sentinel.next.item;
        } else {
            DDList.sentinel = DDList.sentinel.next;
            return DDList.getRecursive(index - 1);
        }
    }

}





























