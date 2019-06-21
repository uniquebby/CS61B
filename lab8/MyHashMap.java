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
    private static int initialSize = 1;//1;//4;
    private static int subInitialSize = 4;
    private static double loadFactor = 5.0;//5.0;//3.5;
    private static double subLoadFactor = 1.5;//1.25;//6.75;

    /** the number of Key-value mapping in the map of hashHeads'.*/
    private int headSize;

    /** the number of Key-value mapping in the map of subarrays'.*/
    private int size[];

    /** hash head list */
    private MyHashMapEntry<K, V>[][] hashHeads;

    /** key set of all pairs. */
    private Set<K> keySet= new HashSet<>();

    /** Represent the Entry that to store pairs of keys and values. */
    private static class MyHashMapEntry<K, V> {
        K key;
        V value;
        int hash;
        MyHashMapEntry<K, V> next;

        MyHashMapEntry(K k, V v, MyHashMapEntry<K, V> next) {
            this.key = k;
            this.value = v;
            this.hash = k.hashCode();
            this.hash *= this.hash;
            this.next = next;
        }

        MyHashMapEntry addFirst(K k, V v) {
            return new MyHashMapEntry<K, V>(k, v, this);
        }

        MyHashMapEntry addFirst(MyHashMapEntry<K, V> e) {
            e.next = this;
            return e;
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
        this(initialSize, loadFactor, subLoadFactor, subInitialSize);
    }

    public MyHashMap(int initialSize, double loadFactor, double subLoadFactor, int subInitialSize) {
        this.size = new int[initialSize];
        this.loadFactor = loadFactor;
        this.hashHeads = (MyHashMapEntry<K, V>[][]) new MyHashMapEntry[initialSize][subInitialSize];
    }




    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        headSize = 0;
        hashHeads = new MyHashMapEntry[initialSize][0];
        keySet = new HashSet<>();
        size = new int[initialSize];
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
        int subArrayIndex = hash(key);
        int bucketNum = subHash(key, subArrayIndex);
        MyHashMapEntry<K, V> e = getEntry(key, subArrayIndex, bucketNum);
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

    public MyHashMapEntry<K, V> getEntry(K key, int subArrayIndex, int bucketNum) {
        if (headSize == 0) {
            return null;
        }
        MyHashMapEntry<K, V> e = hashHeads[subArrayIndex][bucketNum];
        while (e != null) {
            if (e.key.equals(key)) {
                return e;
            }
            e = e.next;
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return headSize;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value) {
        if (headSize > hashHeads.length * loadFactor) {
            resize();
        }
        int subArrayIndex = hash(key);
        int bucketNum = subHash(key, subArrayIndex);
        MyHashMapEntry<K, V> e = getEntry(key, subArrayIndex, bucketNum);
        if (e != null) {
            e.value = value;
        } else {
           putOnly(key, value, subArrayIndex, bucketNum);
        }
    }

    /** put only, without regard to repetition.*/
    public void putOnly(K key, V value, int subArrayIndex, int bucketNum) {
        MyHashMapEntry<K, V> e = hashHeads[subArrayIndex][bucketNum];
        if (e == null) {
            hashHeads[subArrayIndex][bucketNum] = new MyHashMapEntry<K, V>(key, value, null);
        } else {
            hashHeads[subArrayIndex][bucketNum] = e.addFirst(key, value);
        }
        ++headSize;
        ++size[subArrayIndex];
        if (size[subArrayIndex] > hashHeads[subArrayIndex].length * subLoadFactor) {
            resize(subArrayIndex);
        }
        /** add the key to the keySet. */
        keySet.add(key);
    }

    /**
     * put with a exist entry.
     * @param e entry to put.
     * @param subArray subarray to put in.
     */
    public void putOnly(MyHashMapEntry<K, V> e, MyHashMapEntry<K, V>[] subArray, int bucketNum) {
        MyHashMapEntry<K, V> list = subArray[bucketNum];
        if (list == null) {
            subArray[bucketNum] =  e;
            e.next = null;
        } else {
            subArray[bucketNum] = list.addFirst(e);
        }
    }

    /** return the hash of entry. */
    public int hash(K key) {
        int hash = key.hashCode();
        hash = hash * hash;
        return  ((hash ^ (hash >>> 16)) & 0x7FFFFFFF) & (hashHeads.length - 1);
    }

    public int hash(MyHashMapEntry<K, V> e) {
        return (e.hash ^ (e.hash >>> 16)) & 0x7FFFFFFF & (hashHeads.length -1);
    }

    /** return the hash of entry for subarray.*/
    public int subHash(K key, int subArrayIndex) {
        int hash = key.hashCode();
        hash = hash * hash;
        return (hash ^ (hash >>> 8)) & (hashHeads[subArrayIndex].length - 1);
    }

    /** return the hash of entry for a temp array with length.*/
    public int subHash(MyHashMapEntry<K, V> e, int length) {
        return ((e.hash) ^ (e.hash >>> 8)) & (length - 1);
    }

    /** double the maxSize of hashHeads */
    public void resize() {
        MyHashMap<K, V> temp = new MyHashMap<>(hashHeads.length << 1);
        MyHashMapIterator iterator = new MyHashMapIterator();
        MyHashMapEntry<K, V> e;
        int subArrayIndex, bucketNum;
        while (iterator.hasNext()) {
            e = iterator.nextEntry();
            subArrayIndex = temp.hash(e);
            bucketNum = temp.subHash(e, temp.hashHeads[subArrayIndex].length);
            temp.putOnly(e, temp.hashHeads[subArrayIndex], bucketNum);
            ++temp.size[subArrayIndex];
        }
        this.hashHeads = temp.hashHeads;
        this.size = temp.size;
    }

    /** double the maxSize of subarray. */
    public void resize(int subArrayIndex) {
        MyHashMapEntry<K, V>[] temp = new MyHashMapEntry[hashHeads[subArrayIndex].length << 1];
        MyHashMapIterator iterator = new MyHashMapIterator(subArrayIndex);
        MyHashMapEntry<K, V> e;
        int bucketNum;
        while (iterator.hasNextOf()) {
            e = iterator.nextOf();
            bucketNum = subHash(e, temp.length);
            putOnly(e, temp, bucketNum);
        }
        hashHeads[subArrayIndex] = temp;
    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    /* the iteraor for this hashHeads. */
    private class MyHashMapIterator implements Iterator<K>{

       /* Stores the current key-value pair. */
        private MyHashMapEntry<K, V> subCur, curEntry, p;

        /* subArrayIndex represent the index of next,
           subIndex represent the index of nextOf.
         */
        private int ArrayIndex, buckets, subBuckets, subIndex;

        MyHashMapIterator() {
            this(0);
        }

        MyHashMapIterator(int subIndex) {
            this.ArrayIndex = buckets = subBuckets = 0;
            this.subIndex = subIndex;
            this.curEntry = hashHeads[0][0];
            this.subCur = hashHeads[subIndex][0];
            getNextNotNull();
            getSubNextNotNull();
        }



        @Override
        public boolean hasNext() {
            return curEntry != null;
        }

        @Override
        public K next() {
            return nextEntry().key;
        }

         public MyHashMapEntry<K, V> nextEntry() {
             p = curEntry;
             curEntry = curEntry.next;
             getNextNotNull();
             return p;
         }

        /**
         * get next not null entry for next().
         * @return
         */
        public void getNextNotNull() {
             while (curEntry == null) {
                 if (buckets < hashHeads[ArrayIndex].length - 1) {
                     ++buckets;
                     curEntry = hashHeads[ArrayIndex][buckets];
                 } else if (ArrayIndex < hashHeads.length - 1) {
                     ++ArrayIndex;
                     buckets = 0;
                     curEntry = hashHeads[ArrayIndex][buckets];
                 } else {
                     return;
                 }
             }
         }

        public boolean hasNextOf() {
            return subCur != null;
        }

        public MyHashMapEntry<K, V> nextOf() {
            p = subCur;
            subCur = subCur.next;
            getSubNextNotNull();
            return p;
        }

        /**
         * get next not null entry for nextOf().
         * @return next not null entry in hashHeads[subIndex], null if no such entry.
         */
        public void getSubNextNotNull() {
            while (subCur == null) {
                if (subBuckets < hashHeads[subIndex].length -1) {
                    ++subBuckets;
                    subCur = hashHeads[subIndex][subBuckets];
                } else {
                    return;
                }
            }
        }
    }

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















