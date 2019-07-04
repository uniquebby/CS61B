import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

/**
 *
 *  A data structure that uses a hash table to store pairs of keys and values.
 *  Any key must appear at most once in the dictionary, but values may appear multiple
 *  times. Key operations are get(key), put(key, value), and contains(key) methods. The value
 *  associated to a key is the value in the last call to put with that key.
 * @param <K>
 * @param <V>
 */
public class MyHashBSTMap<K , V> implements Map61B<K, V> {
    private static int initialSize = 2;//2;//2;//2;//4;
    private static int subInitialSize = 2;//1;
    private static double loadFactor = 0.75;//5.58
    private static double subLoadFactor = 0.75;//4.0;//2.0;//4.0;//6.75;

    private static int sizeToBST = 8;

    /** the resize times for every resize. */
    private static int resizeOff = 1;
    /** the resize times for every subrsize. */
    private static int subResizeOff = 1;

    /** the number of Key-value mapping in the map of hashHeads'.*/
    private int headSize;

    /** the number of Key-value mapping in the map of subarrays'.*/
    private int size[];

    /** the threshold to resize */
//    private int  headThreshold;
    /** the threshold to subresize */
//    private int threshold[];

    /** hash head list */
    private MyHashMapEntry<K, V>[][] hashHeads;

    /** key set of all pairs. */
    private Set<K> keySet= new HashSet<>();

    /** Represent the Entry that to store pairs of keys and values. */
    private static class MyHashMapEntry<K, V> {
        K key;
        V value;
        int hash;
        int subHash;
        boolean isBST;
        int size;
        MyHashMapEntry<K, V> left;
        MyHashMapEntry<K, V> right;

        MyHashMapEntry(K k, V v) {
            this.key = k;
            this.value = v;
            this.hash = k.hashCode();
            this.subHash = (int) ((this.hash * 0x5DEECE66DL + 0xBL) & ((1L << 31) - 1));
            hash ^= (hash >>> 20) ^ (hash >>>12);
            this.left = this.right = null;
            isBST = false;
            size = 1;
        }

        MyHashMapEntry(MyHashMapEntry<K, V> e) {
            this.key = e.key;
            this.value = e.value;
            this.hash = e.hash;
            this.subHash = e.subHash;
            this.left = this.right = null;
            isBST = false;
            size = 1;
        }

        MyHashMapEntry addFirst(K k, V v) {
            MyHashMapEntry<K, V> res =  new MyHashMapEntry<K, V>(k, v);
            res.right = this;
            res.size = this.size + 1;
            return res;
        }

        void addToBST(K k, V v) {
            MyHashMapEntry<K, V> node = this;
            while (true) {
                if (node.compareTo(k) >= 0) {
                    if (node.left == null) {
                        node.left = new MyHashMapEntry<K, V>(k, v);
                        break;
                    } else {
                        node = node.left;
                    }
                } else {
                    if (node.right == null) {
                        node.right = new MyHashMapEntry<K, V>(k, v);
                        break;
                    } else {
                        node = node.right;
                    }
                }
            }
        }

        void addToBST(MyHashMapEntry<K, V> e) {
            MyHashMapEntry<K, V> node = this;
            while (true) {
                if (node.compareTo(e.key) >= 0) {
                    if (node.left == null) {
                        node.left = e;
                        break;
                    } else {
                        node = node.left;
                    }
                } else {
                    if (node.right == null) {
                        node.right = e;
                        break;
                    } else {
                        node = node.right;
                    }
                }
            }
        }
        MyHashMapEntry addFirst(MyHashMapEntry<K, V> e) {
            e.right = this;
            e.left = null;
            e.size = this.size + 1;
            return e;
        }

        public int compareTo(MyHashMapEntry<K, V> e) {
            return (subHash - e.subHash);
        }

        public int compareTo(K k) {
             int h = (int) ((k.hashCode() * 0x5DEECE66DL + 0xBL) & ((1L << 31) - 1));
             return (this.subHash - h);
        }
    }

    /** construct a new MyHashMap with default initialSize and loadFactor. */
    public MyHashBSTMap() {
        this(initialSize, loadFactor);
    }

    /** construct a new MyHashMap with given initialSize and default loadFactor. */
    public MyHashBSTMap(int initialSize) {
        this(initialSize, loadFactor);
    }

    /** construct a new MyHashMap with given initialSize and loadFactor. */
    public MyHashBSTMap(int initialSize, double loadFactor) {
        this(initialSize, loadFactor, subLoadFactor, subInitialSize, resizeOff, subResizeOff, sizeToBST);
    }

    public MyHashBSTMap(int initialSize, double loadFactor, double subLoadFactor, int subInitialSize,
                        int resizeOff, int subResizeOff, int sizeToBST) {
        this.size = new int[initialSize];
        this.loadFactor = loadFactor;
        this.subLoadFactor = subLoadFactor;
        this.resizeOff = resizeOff;
        this.subResizeOff = subResizeOff;
        this.hashHeads = (MyHashMapEntry<K, V>[][]) new MyHashMapEntry[initialSize][subInitialSize];
        this.sizeToBST = sizeToBST;
//        this.headThreshold = initialSize * hashHeads.length;
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
        MyHashMapEntry<K, V> p = e;
        while (e != null) {
            if (e.key.equals(key)) {
                return e;
            }
            if (p.isBST) {
                if (e.compareTo(key) >= 0) {
                    e = e.left;
                } else {
                    e = e.right;
                }
            } else {
                e = e.right;
            }
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
        MyHashMapEntry<K, V> root = hashHeads[subArrayIndex][bucketNum];
        if (root == null) {
            hashHeads[subArrayIndex][bucketNum] = new MyHashMapEntry<K, V>(key, value);
        } else {
            if (root.isBST) {
                root.addToBST(key, value);
            } else {
                hashHeads[subArrayIndex][bucketNum] = root.addFirst(key, value);
                if (root.size >= sizeToBST) {
                    MyHashMapIterator iterator = new MyHashMapIterator(subArrayIndex, bucketNum);
                    while (iterator.hasNextOfAt()) {
                        MyHashMapEntry<K, V> e = iterator.nextOfAt();
                        putToBST(e, hashHeads[subArrayIndex], bucketNum);
                    }
                }
            }
        }
        ++headSize;
        ++size[subArrayIndex];
        if (size[subArrayIndex] > hashHeads[subArrayIndex].length * subLoadFactor) {
            resize(subArrayIndex);
        }
        /** add the key to the keySet. */
        keySet.add(key);
    }

    public void putToBST(MyHashMapEntry<K,V> e, MyHashMapEntry<K, V>[] subArray, int bucketNum) {
        e.isBST = true;
        e.left = e.right = null;
        MyHashMapEntry<K, V> root = subArray[bucketNum];
        if (root == null || root.equals(e)) {
            subArray[bucketNum] = e;
        } else {
            root.addToBST(e);
        }
    }

    /**
     * put with a exist entry.
     * @param e entry to put.
     * @param subArray subarray to put in.
     */
    public void putOnly(MyHashMapEntry<K, V> e, MyHashMapEntry<K, V>[] subArray, int bucketNum) {
        e.isBST = false;
        e.left = e.right = null;
        MyHashMapEntry<K, V> root = subArray[bucketNum];
        if (root == null) {
            subArray[bucketNum] =  e;
        } else {
            subArray[bucketNum] = root.addFirst(e);
        }
    }

    /** return the hash of entry. */
    public int hash(K key) {
        int hash = key.hashCode();
        hash ^= (hash >>> 20) ^ (hash >>> 12);
        return (hash & (this.hashHeads.length - 1));
    }

    public int hash(MyHashMapEntry<K, V> e) {
        int hash = e.hash;
        return (hash & (this.hashHeads.length - 1));
    }

    /** return the hash of entry for subarray.*/
    public int subHash(K key, int subArrayIndex) {
        int hash = (int) ((key.hashCode()* 0x5DEECE66DL + 0xBL) & ((1L << 31) - 1));
        return (hash & (this.hashHeads[subArrayIndex].length - 1));
    }

    /** return the hash of entry for a temp array with length.*/
    public int subHash(MyHashMapEntry<K, V> e, int length) {
        int hash = e.subHash;
        return (hash & (length - 1));
    }

    /** double the maxSize of hashHeads */
    public void resize() {
        MyHashBSTMap<K, V> temp = new MyHashBSTMap<>((int)((this.hashHeads.length << resizeOff)));
        MyHashMapIterator iterator = new MyHashMapIterator();
        MyHashMapEntry<K, V> e;
        int subArrayIndex, bucketNum;
        while (iterator.hasNext()) {
            e = iterator.nextEntry();
            e = new MyHashMapEntry<>(e);
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
        MyHashMapEntry<K, V>[] temp = new MyHashMapEntry[(int)((this.hashHeads[subArrayIndex].length << subResizeOff))];
        MyHashMapIterator iterator = new MyHashMapIterator(subArrayIndex);
        MyHashMapEntry<K, V> e;
        int bucketNum;
        while (iterator.hasNextOf()) {
            e = iterator.nextOf();
            e = new MyHashMapEntry<>(e);
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
        private Stack<MyHashMapEntry<K, V>> stack, subStack;

        MyHashMapIterator() {
            this(0);
        }

        MyHashMapIterator(int subIndex) {
            this.ArrayIndex = buckets = subBuckets = 0;
            this.subIndex = subIndex;
            this.curEntry = hashHeads[0][0];
            this.subCur = hashHeads[subIndex][0];
            this.stack = new Stack();
            this.subStack = new Stack();
            getNextNotNull();
            getSubNextNotNull();
            getSubNextNotNullAt();
        }

        MyHashMapIterator(int subIndex, int bucketNum) {
            this.ArrayIndex = buckets = subBuckets = 0;
            this.subIndex = subIndex;
            this.curEntry = hashHeads[0][0];
            this.subCur = hashHeads[subIndex][bucketNum];
            this.stack = new Stack();
            this.subStack = new Stack();
            getNextNotNull();
            getSubNextNotNull();
            getSubNextNotNullAt();
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
             stack.push(p);
             curEntry = curEntry.left;
             getNextNotNull();
             return p;
         }

        /**
         * get next not null entry for next().
         * @return
         */
        public void getNextNotNull() {
             while (curEntry == null) {
                 if (stack.size() != 0) {
                     curEntry = stack.pop().right;
                 } else {
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
         }

        public boolean hasNextOf() {
            return subCur != null;
        }

        public boolean hasNextOfAt() {
            return subCur != null;
        }

        public MyHashMapEntry<K, V> nextOfAt() {
            p = subCur;
            subStack.push(p);
            subCur = subCur.left;
            getSubNextNotNullAt();
            return p;
        }

        public MyHashMapEntry<K, V> nextOf() {
            p = subCur;
            subStack.push(p);
            subCur = subCur.left;
            getSubNextNotNull();
            return p;
        }

        /**
         * get next not null entry for nextOf().
         * @return next not null entry in hashHeads[subIndex], null if no such entry.
         */
        public void getSubNextNotNull() {
            while (subCur == null) {
                if (subStack.size() != 0) {
                    subCur = subStack.pop().right;
                } else {
                    if (subBuckets < hashHeads[subIndex].length - 1) {
                        ++subBuckets;
                        subCur = hashHeads[subIndex][subBuckets];
                    } else {
                        return;
                    }
                }
            }
        }

        public void getSubNextNotNullAt() {
            while (subCur == null) {
                if (subStack.size() != 0) {
                    subCur = subStack.pop().right;
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















