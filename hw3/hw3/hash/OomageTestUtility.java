package hw3.hash;

import java.util.Iterator;
import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /*
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int[] buckets = new int[M];
        Iterator<Oomage> iterator= oomages.iterator();
        Oomage o;
        int bucketNum;
        while (iterator.hasNext()) {
            o = iterator.next();
            bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            buckets[bucketNum] += 1;
        }

        for (int i = 0; i < M; ++i) {
            if (buckets[i] < oomages.size() / 50 || buckets[i] > oomages.size() / 2.5) {
                return false;
            }
        }
        return true;
    }
}
