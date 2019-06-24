package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ArrayHeapMinPQTest {
    private static final int N = 10000000;
    private static ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();

    /*
    @Test
    public void testAdd() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();

        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; ++i) {
            heap.add(i,  i);
//            assertTrue(heap.getSmallest() == i);
//            assertTrue(heap.size() == i + 1);
        }
        double time = sw.elapsedTime();
        TreePrintUtil.print(heap.tree);
        System.out.println("Total time elapsed for add: " + time +  " seconds.");
    }

    @Test
    public void testContain() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; ++i) {
            heap.add(i,  i);
            assertTrue(heap.contains(i));
            assertFalse(heap.contains(i + 1));
        }
        System.out.println("Total time elapsed for contain: " + sw.elapsedTime() +  " seconds.");
    }

    @Test
    public void testRemoveSmallest() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; ++i) {
            heap.add(i,  i);
        }
        for (int i = 0; i < N; ++i) {
            int item = heap.removeSmallest();
//            assertTrue(heap.size() == N - i - 1);
//            assertTrue(item ==  i);
        }
//        heap.add(0, -1);
        System.out.println("Total time elapsed for remove: " + sw.elapsedTime() +  " seconds.");
    }

    @Test
    public void testGetSmallest() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; ++i) {
            heap.add(i,  i);
        }
        int item = heap.getSmallest();
        assertTrue(item == 0);
        System.out.println("Total time elapsed for get: " + sw.elapsedTime() +  " seconds.");
    }

    @Test
    public void testChangePriority() {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < N; ++i) {
            heap.add(i, i);
        }
        heap.changePriority(8, -1.5);
        int item = heap.getSmallest();
        assertTrue(item == 8);
        System.out.println("Total time elapsed for change: " + sw.elapsedTime() +  " seconds.");
    }

     */


    public static void main(String[] args) {
        ArrayHeapMinPQ<Integer> heap = new ArrayHeapMinPQ<>();
        NaiveMinPQ<Integer> naive = new NaiveMinPQ<>();
        Stopwatch sw = new Stopwatch();

        double time = 0;
        for (int i = 0, n = 1; i < N; ++i) {
            sw = new Stopwatch();
            heap.add(i, i);
            time += sw.elapsedTime();
            if (i == n) {
                System.out.println("n  = " + i + "<--------->" + time + " seconds.");
                time = 0;
                n *= 2;
//                System.out.println("Total time elapsed for heap-add: n  = " + i + sw.elapsedTime() + " seconds.");
            }
        }

        sw = new Stopwatch();
        for (int i = 0; i < N; ++i) {
            naive.add(i, i);
        }
        System.out.println("Total time elapsed for naive-add: " + sw.elapsedTime() +  " seconds.");


        double time1 = 0;
        double time2 = 0;
        Random random = new Random();
        int item = random.nextInt(N);
        for (int i = 0; i < 1000; ++i) {
            sw = new Stopwatch();
            heap.contains(item);
            time1 += sw.elapsedTime();
            sw = new Stopwatch();
            naive.contains(item);
            time2 += sw.elapsedTime();
        }
        System.out.println("Total time elapsed for heap-contain: " + time1 +  " seconds.");
        System.out.println("Total time elapsed for naive-contain: " + time2 +  " seconds.");
        System.out.println("times X: " + time2/time1 +  " .");

        sw = new Stopwatch();
        int heaps = heap.getSmallest();
        System.out.println(heaps);
        System.out.println("Total time elapsed for heap-get: " + sw.elapsedTime() +  " seconds.");
        sw = new Stopwatch();
        int navis = naive.getSmallest();
        System.out.println(navis);
        System.out.println("Total time elapsed for naive-get: " + sw.elapsedTime() +  " seconds.");

        sw = new Stopwatch();
        heap.removeSmallest();
        System.out.println("Total time elapsed for heap-remove: " + sw.elapsedTime() +  " seconds.");
        sw = new Stopwatch();
        naive.removeSmallest();
        System.out.println("Total time elapsed for naive-remove: " + sw.elapsedTime() +  " seconds.");

        sw = new Stopwatch();
        heap.changePriority(2, -1);
        System.out.println("Total time elapsed for heap-change: " + sw.elapsedTime() +  " seconds.");
        sw = new Stopwatch();
        naive.changePriority(2, -1);
        System.out.println("Total time elapsed for naive-change: " + sw.elapsedTime() +  " seconds.");
    }
}
