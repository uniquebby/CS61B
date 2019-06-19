import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * A BST-based implementation of the Map61B interface, which represents a basic tree-based map.
 * @author yangbinbin
 * @param <K> key type.
 * @param <V> value type.
 * 2019.06.19
 */
public class BSTMap<K extends Comparable, V> implements Map61B<K, V> {
    private int size = 0;

    /** Keys and values are stored in a BST of Entry objects.
     *  This variable stores the first pair in this tree. */
    private Entry tree = null;

    /** Represents one node in the BST that stores the key-value pairs
     *  in the dictionary. */
    private class Entry {
        private K key;
        private V value;
        private Entry leftEntry;
        private Entry rightEntry;

        Entry(K key, V value, Entry left, Entry right) {
            this.key = key;
            this.value = value;
            leftEntry = left;
            rightEntry = right;
        }

    }


    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        size = 0;
        tree = null;
    }

    @Override
    public int size() {
        return size;
    }

    /** Returns the value corresponding to KEY or null if no such value exists. */
    @Override
    public V get(K key) {
        return get(tree, key);
    }

    /** Inserts the key-value pair of KEY and VALUE into this dictionary,
     *  replacing the previous value associated to KEY, if any. */
    @Override
    public void put(K key, V value) {
        tree = put(tree, key, value);
    }

    /** Returns true if and only if this dictionary contains KEY as the
     *  key of some key-value pair. */
    @Override
    public boolean containsKey(K key) {
        if (tree == null) {
            return false;
        } else {
            return get(key) != null;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIter();
    }

    /** An iterator that iterates over the keys of the dictionary. */
    private class BSTMapIter implements Iterator<K> {

        /** Stores the current key-value pair. */
        private BSTMap.Entry cur;

        /** Stack of the first-order traverse. */
        private ArrayList<Entry> stack;

        /** Create a new BSTMapIter by setting cur to the first node in the
         *  tree that stores the key-value pairs. */
        public BSTMapIter() {
            cur = tree;
            stack = new ArrayList<Entry>();
            stack.add(tree);
        }

        @Override
        public boolean hasNext() {
            return  stack.size() != 0;
        }

        @Override
        public K next() {
            Entry res = cur;
            if (cur != null) {
                stack.add(res);
                cur = cur.leftEntry;
            }
            cur = stack.remove(stack.size() - 1);
            cur = cur.rightEntry;
            return res.key;
        }

    }
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    /** put an Entry with key and value to a tree which root node is e. */
    private Entry put(Entry subtree, K key, V value) {
        if (subtree == null) {
            ++size;
            return new Entry(key, value, null, null);
        }

        int cmp = key.compareTo(subtree.key);
        if (cmp < 0) {
            subtree.leftEntry = put(subtree.leftEntry, key, value);
        } else if (cmp > 0){
            subtree.rightEntry = put(subtree.rightEntry, key, value);
        } else {
            subtree.value = value;
        }
        return subtree;
    }

    /** return a Entry which KEY equals key, return null if no such Entry. */
    private V get(Entry subtree, K key) {
        if (subtree == null) {
            return null;
        }
        int cmp = key.compareTo(subtree.key);
        if (cmp == 0) {
            return subtree.value;
        } else if (cmp < 0 && subtree.leftEntry != null) {
            return get(subtree.leftEntry, key);
        } else if (cmp > 0 && subtree.rightEntry != null) {
            return get(subtree.rightEntry, key);
        } else {
            return null;
        }
    }
}
