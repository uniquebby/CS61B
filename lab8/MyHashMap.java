import java.util.*;

/**
 *
 *  A data structure that uses a hash table to store pairs of keys and values.
 *  Any key must appear at most once in the dictionary, but values may appear multiple
 *  times. Key operations are get(key), put(key, value), and contains(key) methods. The value
 *  associated to a key is the value in the last call to put with that key.
 * @param <K>
 * @param <V>
 */
public class MyHashMap<K , V> implements Map61B<K, V> {
    private static int initialSize = 16;
    private static double loadFactor = 0.75;

    /** the number of buckets */
    private int maxSize = initialSize;

    /** the number of Key-value mapping in the map. */
    private int size;

    /** hash head list */
    private MyHashMapEntry<K, V>[] hashHeads;

    /** key set of all pairs. */
    private Set<K> keySet= new HashSet<>();

    /** Represent the Entry that to store pairs of keys and values. */
    private static class MyHashMapEntry<K, V> {
        K key;
        V value;
        MyHashMapEntry<K, V> next;

        MyHashMapEntry(K k, V v, MyHashMapEntry<K, V> next) {
            this.key = k;
            this.value = v;
            this.next = next;
        }


        MyHashMapEntry addFirst(K k, V v) {
            return new MyHashMapEntry<K, V>(k, v, this);
        }

    }

    /** construct a new MyHashMap with default initialSize and loadFactor. */
    public MyHashMap() {
        this(initialSize, loadFactor);
    }

    /** construct a new MyHashMap with given initialSize and default loadFactor. */
    public MyHashMap(int initialSize) {
        this(initialSize, loadFactor);
    }

    /** construct a new MyHashMap with given initialSize and loadFactor. */
    public MyHashMap(int initialSize, double loadFactor) {
        this.maxSize = initialSize;
        this.loadFactor = loadFactor;
        this.hashHeads = (MyHashMapEntry<K, V>[]) new MyHashMapEntry[maxSize];
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        size = 0;
        hashHeads = null;
        keySet = null;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        if (keySet == null || keySet.size() == 0) {
            return false;
        }
        return keySet.contains(key);
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        MyHashMapEntry<K, V> e = getEntry(key);
        if (e == null) {
            return null;
        } else {
            return e.value;
        }
    }

    /**
     * Returns the MyHashMapEntry specified key is mapped, or null if this
     * map contains no mapping for the key.
     */

    public MyHashMapEntry<K, V> getEntry(K key) {
        if (!containsKey(key)) {
            return null;
        } else {
            int bucketNum = hash(key);
            MyHashMapEntry<K, V> e = hashHeads[bucketNum];
            while (e != null) {
                if (e.key.equals(key)) {
                    return e;
                }
                e = e.next;
            }
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value) {
        MyHashMapEntry e = getEntry(key);
        if (e != null) {
            e.value = value;
        } else {
            if (size > maxSize * loadFactor) {
                resize();
            }
            putOnly(key, value);
        }
    }

    /** put only, without regard to repetition.*/
    public void putOnly(K key, V value) {
         int bucketNum = hash(key);
         MyHashMapEntry<K, V> e = hashHeads[bucketNum];
         if (e == null) {
             hashHeads[bucketNum] = new MyHashMapEntry<K, V>(key, value, null);
         } else {
             hashHeads[bucketNum] = e.addFirst(key, value);
         }
         ++size;
         /** add the key to the keySet. */
         keySet.add(key);
    }

    /** return the hash of entry. */
    public int hash(K key) {
        int hash = key.hashCode();
        return  ((hash ^ (hash >>> 16)) & 0x7FFFFFFF) % maxSize;
    }

    /** double the maxSize of hashHeads.*/
    public void resize() {
        MyHashMap<K, V> temp = new MyHashMap<>(maxSize << 1);
        for (K key : keySet) {
            temp.putOnly(key, get(key));
        }
        this.maxSize = temp.maxSize;
        this.hashHeads = temp.hashHeads;
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    /* the iteraor for this class.
    private class MyHashMapIterator implements Iterator<K>{
       /* Stores the current key-value pair.
        private MyHashMapEntry cur;


        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public K next() {
            return null;
        }
    }*/

    @Override
    public Iterator<K> iterator() {
        return keySet.iterator();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
}















