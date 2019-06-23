package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayHeapMinPQTest {
    private static final int N = 100000;
    /*
    @Test
    public void testAdd() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();

        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; ++i) {
            heap.add(i, N - i);
            assertTrue(heap.getSmallest() == i);
            assertTrue(heap.size() == i + 1);
        }
        System.out.println("Total time elapsed for add: " + sw.elapsedTime() +  " seconds.");
    }

    @Test
    public void testContain() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; ++i) {
            heap.add(i, N - i);
            assertTrue(heap.contains(i));
            assertFalse(heap.contains(i + 1));
        }
        System.out.println("Total time elapsed for add: " + sw.elapsedTime() +  " seconds.");
    }

    @Test
    public void testRemoveSmallest() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; ++i) {
            heap.add(i, N - i);
        }
        for (int i = 0; i < N; ++i) {
            int item = heap.removeSmallest();
            assertTrue(heap.size() == N - i - 1);
            assertTrue(item == N - i - 1);
        }
        heap.add(0, -1);
        System.out.println("Total time elapsed for add: " + sw.elapsedTime() +  " seconds.");
    }

    @Test
    public void testGetSmallest() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; ++i) {
            heap.add(i, N - i);
        }
        int item = heap.getSmallest();
        assertTrue(item == N - 1);
        System.out.println("Total time elapsed for add: " + sw.elapsedTime() +  " seconds.");
    }

    @Test
    public void testChangePriority() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; ++i) {
}
            heap.add(i, N - i);
        }
        heap.changePriority(8, -1.5);
        int item = heap.getSmallest();
        assertTrue(item == 8);
        System.out.println("Total time elapsed for add: " + sw.elapsedTime() +  " seconds.");
    }

     */

    public static void main(String[] args) {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        NaiveMinPQ<Integer> naive = new NaiveMinPQ<>();

        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; ++i) {
            heap.add(i, N - i);
        }
        System.out.println("Total time elapsed for heap-add: " + sw.elapsedTime() +  " seconds.");
        sw = new Stopwatch();
        for (int i = 0; i < N; ++i) {
            naive.add(i, N - i);
        }
        System.out.println("Total time elapsed for naive-add: " + sw.elapsedTime() +  " seconds.");

        sw = new Stopwatch();
        for (int i = 0; i < 1000; ++i) {
            heap.contains(i);
        }
        System.out.println("Total time elapsed for heap-contain: " + sw.elapsedTime() +  " seconds.");
        sw = new Stopwatch();
        for (int i = 0; i < 1000; ++i) {
            naive.contains(i);
        }
        System.out.println("Total time elapsed for naive-contain: " + sw.elapsedTime() +  " seconds.");

        sw = new Stopwatch();
        heap.getSmallest();
        System.out.println("Total time elapsed for heap-get: " + sw.elapsedTime() +  " seconds.");
        sw = new Stopwatch();
        naive.getSmallest();
        System.out.println("Total time elapsed for naive-get: " + sw.elapsedTime() +  " seconds.");

        sw = new Stopwatch();
        heap.removeSmallest();
        System.out.println("Total time elapsed for heap-remove: " + sw.elapsedTime() +  " seconds.");
        sw = new Stopwatch();
        naive.removeSmallest();
        System.out.println("Total time elapsed for naive-remove: " + sw.elapsedTime() +  " seconds.");

        sw = new Stopwatch();
        heap.changePriority(8, -1);
        System.out.println("Total time elapsed for heap-change: " + sw.elapsedTime() +  " seconds.");
        sw = new Stopwatch();
        naive.changePriority(8, -1);
        System.out.println("Total time elapsed for naive-change: " + sw.elapsedTime() +  " seconds.");
    }
}
