package bearmaps.proj2ab;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * @author yangbinbin
 * 2019.06.23
 * @param <T>
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T>{
    private static final int NO_NEED_TO_ADJUST = -1;
    private static final int NO_CHILD = -1;
    private ArrayList<Node> heap;
    /* BST tree root */
    public Node tree;

    private class Node implements Comparable<T>,TreePrintUtil.TreeNode{
        private T item;
        private double priority;

        /** index in heap. */
        int level;
        int index;
        long hash;
        Node left, right, parent;

        Node(T item, double p) {
            this.item = item;
            this.priority = p;
            this.index = heap.size();
            this.left = this.right = this.parent = null;
            this.hash = (this.item.hashCode() * 0x5DEECE66DL + 0xBL) & ((1L << 32) - 1);
        }

        @Override
        public int compareTo(T o) {
//            int h1 = this.item.hashCode();
            int h2 = o.hashCode();
//            long res1 = (h1 * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
            long res1 = this.hash;
            long res2 = ((h2) * 0x5DEECE66DL + 0xBL) & ((1L << 32) - 1);
//            h1 = (823543 * h1) & 0x00FFFFFF;
//            h2 = (823543 * h2) & 0x00FFFFFF;
            return (int)(res1 - res2);
//            return h1 - h2;
        }

        @Override
        public String toString() {
            return " [" + this.item + "] ";
        }

        @Override
        public String getPrintInfo() {
            return this.toString();
        }

        @Override
        public TreePrintUtil.TreeNode getLeftChild() {
            return this.left;
        }

        @Override
        public TreePrintUtil.TreeNode getRightChild() {
            return this.right;
        }

        @Override
        public int getLevel() {
            return level;
        }

        @Override
        public void setLevel(int level) {
            this.level = level;
        }

        /* remove child node that '==' to n, and adjust the chile node of
        *  n node.there is no situation that n has also left and right child.*/
        void removeChild(Node n) {
//            if (n.item.equals(476) || (n.right != null && n.right.item.equals(476))
//                || (n.left != null && n.left.item.equals(476))) {
//                System.out.println("fkkkkkkkkkkkk");
//            }
            n.parent = null;
            if (this.left == n) {
                if (n.right != null) {
                    this.left = n.right;
                    n.right.parent = this;
                } else if (n.left != null){
                    this.left = n.left;
                    n.left.parent = this;
                } else {
                    this.left = null;
                }
            } else if (this.right == n) {
                if (n.left != null) {
                    this.right = n.left;
                    n.left.parent = this;
                } else if (n.right != null) {
                    this.right = n.right;
                    n.right.parent = this;
                } else {
                    this.right = null;
                }
            } else if (n == tree) {
                tree = null;
            }
        }

        /* Replace child node old with given node newNd.*/
        void replaceChild(Node old, Node newNd) {
            if (this.left == old) {
                this.left = newNd;
                newNd.parent = this;
            } else if (this.right == old) {
                this.right = newNd;
                newNd.parent = this;
            } else if (old.parent == old) {
                old.parent = null;
                tree = newNd;
                newNd.parent = newNd;
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
        Node node = getBSTNode(item, true);
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
            n.parent = n;
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
        if (heap.size() == 0 || (leftChild(k) == NO_CHILD && rightChild(k) == NO_CHILD)) {
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
//       System.out.println(n.item + " n");
       if (n.left != null) {
            Node l = getBSTLargest(n.left);
            if (l.item.equals(476)) {
//                System.out.println(l.item + " l");
            }
            if (l.parent != n ) {
                l.parent.removeChild(l);
                l.left = n.left;
                n.left.parent = l;
            }
            if (n.right != null) {
                l.right = n.right;
                n.right.parent = l;
            }
            n.parent.replaceChild(n, l);
        } else if (n.right != null){
            Node r = getBSTSmallest(n.right);
//           System.out.println(r.item + " r");
            if (r.parent != n) {
                r.parent.removeChild(r);
                r.right = n.right;
                n.right.parent = r;
            }
            n.parent.replaceChild(n, r);
        } else {
           if (n == tree) {
               tree = null;
           }
           else if (n.parent.left == n) {
               n.parent.left = null;
           } else {
               n.parent.right = null;
           }
           n.parent = null;
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
