package bearmaps;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T>{
    private static final int NO_NEED_TO_ADJUST = -1;
    private static final int NO_CHILD = -1;
    private ArrayList<Node> heap;
    /* BST tree root */
    private Node tree;

    private class Node implements Comparable<T>{
        private T item;
        private double priority;

        /** index in heap. */
        int index ;
        Node left, right, parent;

        Node(T item, double p) {
            this.item = item;
            this.priority = p;
            this.index = heap.size();
            this.left = this.right = this.parent = null;
        }

        @Override
        public int compareTo(T o) {
            int h1 = this.hashCode();
            int h2 = o.hashCode();
            if ((((h1 >>> 16) ^ h2) & 0x1) == 0) {
                return -1;
            } else {
                return 1;
            }
        }

        /* remove child node that '==' to n */
        void removeChild(Node n) {
            if (this.left == n) {
                this.left = null;
            } else if (this.right == n) {
                this.right = null;
            }
        }

        /* Replace child node old with given node newNd.*/
        void replaceChild(Node old, Node newNd) {
            if (this.left == old) {
                this.left = newNd;
            } else if (this.right == old) {
                this.right = newNd;
            }
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }

        T getItem() {
            return item;
        }

        void setItem(T item) {
            this.item = item;
        }
    }

    public ArrayHeapMinPQ() {
        this.heap = new ArrayList<Node>();
        this.tree = null;
    }

    /* Adds an item with the given priority value. Throws an
     * IllegalArgumentExceptionb if item is already present.
     * You may assume that item is never null. */
    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        Node node = new Node(item, priority);
        heap.add(node);
        swim(heap.size() - 1);

        addToBSTree(node);
    }

    /* Returns true if the PQ contains the given item. */
    @Override
    public boolean contains(T item) {
        return null != getBSTNode(item, true);
    }


    /* Returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T getSmallest() {
        if (heap.size() == 0) {
            throw new NoSuchElementException();
        }
        return heap.get(0).getItem();
    }

    /* Removes and returns the minimum item. Throws NoSuchElementException if the PQ is empty. */
    @Override
    public T removeSmallest() {
        if (heap.size() == 0) {
            throw new NoSuchElementException();
        }

        /* Remove the smallest node from the heap */
        swap(0, heap.size() - 1);
        Node smallest = heap.remove(heap.size() - 1);
        adjust(0);

        /* Remove the smallest node from the BST tree.*/
        removeFromBST(smallest);
        return smallest.getItem();
    }

    /* Returns the number of items in the PQ. */
    @Override
    public int size() {
        return heap.size();
    }

    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    @Override
    public void changePriority(T item, double priority) {
        Node node = getBSTNode(item, false);
        if (node == null) {
            throw new NoSuchElementException();
        }
        node.setPriority(priority);
        swim(node.index);
    }

    /* Return the index of parent of key. */
    private int parent(int key) {
        return (key - 1) / 2;
    }

    /* Return the index of left child of key. */
    private int leftChild(int key) {
        int l = 2 * key + 1;
        return l >= heap.size()? NO_CHILD : l;
    }

    /* Return the index of right child of key. */
    private int rightChild(int key) {
        int r = 2 * key + 2;
        return r >= heap.size()? NO_CHILD : r;
    }

    /* Adjust the node with key k to the correct position. */
    private void swim(int k) {
        while (k != 0) {
            if (heap.get(k).getPriority() < heap.get(parent(k)).getPriority()) {
                swap(k, parent((k)));
                k = parent(k);
            } else {
                return;
            }
        }
    }

    /* Swap nodes with key k1 and k2. */
    private void swap(int k1, int k2) {
        Node temp = heap.get(k1);
        heap.set(k1, heap.get(k2));
        heap.get(k2).index = k1;
        heap.set(k2, temp);
        temp.index = k2;
    }


    /**
     * Add node to BST tree.
     * @param n
     */
    private void addToBSTree(Node n) {
        if (tree == null) {
            tree = n;
            tree.parent = tree;
            return;
        }
        Node cur = tree;
        while (true) {
            int cmp = cur.compareTo(n.getItem());
            /* add to the left if n less than node tree. */
            if (cmp > 0) {
                if (cur.left == null) {
                    cur.left = n;
                    n.parent = cur;
                    return;
                }
                cur = cur.left;
            } else {
                if (cur.right == null) {
                    cur.right = n;
                    n.parent = cur;
                    return;
                }
                cur = cur.right;
            }
        }
    }

    /* Adjust the top node to correct position. */
    private void adjust(int k) {
        if (k >= heap.size()) {
            return;
        }
        Node t = heap.get(k);
        if (heap.size() == 0 || (t.left == null && t.right == null)) {
            return;
        }

        while (k < heap.size()) {
            int child = childToAdjust(k);
            if (child == NO_NEED_TO_ADJUST) {
                return;
            } else {
                swap(k, child);
                k = child;
            }
        }
    }

    /* Retrun the child node that will to be adjust, -1 represent left node,
       1 represent right node, 0 represent no need to adjust.
     */
    private int childToAdjust(int k) {
        if (k >= heap.size()) {
            return NO_NEED_TO_ADJUST;
        }
        Node t = heap.get(k);
        int leftChild = leftChild(k), rightChild = rightChild(k);
        if (leftChild == NO_CHILD && rightChild == NO_CHILD) {
            return NO_NEED_TO_ADJUST;
        } else if (rightChild == NO_CHILD) {
            if (t.getPriority() > heap.get(leftChild).getPriority()) {
                return leftChild;
            } else {
                return NO_NEED_TO_ADJUST;
            }
        } else if (leftChild == NO_CHILD) {
            if (t.getPriority() > heap.get(rightChild).getPriority()) {
                return rightChild;
            } else {
                return NO_NEED_TO_ADJUST;
            }
        }

        if (t.getPriority() <= heap.get(leftChild).getPriority()
           && t.getPriority() <= heap.get(rightChild).getPriority()) {
            return NO_NEED_TO_ADJUST;
        } else if (heap.get(leftChild).getPriority() <=
                heap.get(rightChild).getPriority()) {
            return leftChild;
        } else {
            return rightChild;
        }
    }

    /**
     * Get the node in BST by given item, return null if no such item.
     * @param item
     * @param justEqual true for just equals to item, false for '==' only.
     * @return
     */
    private Node getBSTNode(T item, boolean justEqual) {
        Node p = tree;
        if (justEqual) {
            while (p != null) {
                if (p.getItem().equals(item)) {
                    return p;
                }
                int cmp = p.compareTo(item);
                if (cmp > 0) {
                    p = p.left;
                } else {
                    p = p.right;
                }
            }
            return null;
        } else {
            while (p != null) {
                if (p.getItem() == item) {
                    return p;
                }
                int cmp = p.compareTo(item);
                if (cmp > 0) {
                    p = p.left;
                } else {
                    p = p.right;
                }
            }
            return null;
        }
    }

    /**
     * Get the parent node in BST by given item, return null if no such item.
     * @param item
     * @param justEqual true for just equals to item, false for '==' only.
     * @return
     */
    private Node getBSTParent(T item, boolean justEqual) {
        Node p = tree, parent = tree;
        while (p != null) {
            if (justEqual && p.getItem().equals(item)) {
                return parent;
            } else if (!justEqual && p.getItem() == item) {
                return parent;
            }
            int cmp = p.compareTo(item);
            parent = p;
            if (cmp > 0) {
                p = p.left;
            } else {
                p = p.right;
            }
        }
        return null;
    }

    /* Remove the node from BST tree.*/
    private void removeFromBST(Node n) {
        if (n.parent == null) {
            return;
        } else if (n.parent == tree) {
            tree = null;
        }
        if (n.left == null && n.left == null) {
            n.parent.removeChild(n);
        } else if (n.left != null) {
            Node l = getBSTLargest(n.left);
            l.parent.removeChild(l);
            n.parent.replaceChild(n, l);
        } else {
            Node r = getBSTSmallest(n.right);
            r.parent.removeChild(r);
            n.parent.replaceChild(n, r);
        }
    }

    /* Get the largest node from sub BST tree. */
    private Node getBSTLargest(Node subtree) {
        if(subtree == null) {
            return null;
        }
        while (subtree.right != null) {
            subtree = subtree.right;
        }
        return subtree;
    }
    /* Get the largest node from sub BST tree. */
    private Node getBSTSmallest(Node subtree) {
        if(subtree == null) {
            return null;
        }
        while (subtree.left != null) {
            subtree = subtree.left;
        }
        return subtree;
    }
}
