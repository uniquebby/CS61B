
package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author yangbinbin
 * 写测试水平有待提高
 */
public class KDTreeTest {

    public static void main(String[] args) {
        List<Point> l = new ArrayList<>();
        l.add(new Point(3889, 4651));
        l.add(new Point(491, 917));
        l.add(new Point(4372, 8617));
        l.add(new Point(215, 2763));
        l.add(new Point(3238, 6571));

        KDTree kd = new KDTree(l);
        NaivePointSet naive = new NaivePointSet(l);
        double x = 3648, y = 9663;
        Point goal = new Point(x, y);
        assertEquals(naive.nearest(x, y), kd.nearestRecursion(goal));
//        assertEquals(kd.nearest(kd.tree, goal, kd.tree).getP(), naive.nearest(x, y));
    }


    @Test
    public void testNearest() {
        List<Point> points = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100000; ++i) {
            double x = random.nextDouble(), y = random.nextDouble();
//            points.add(new Point(random.nextInt(10000), random.nextInt(10000)));
            points.add(new Point(x, y));
        }

        KDTree kd = new KDTree(points);
        NaivePointSet naive = new NaivePointSet(points);
        Stopwatch sw = new Stopwatch();
        double time1, time2;
        time1 = time2 = 0;

        ArrayList<Point> testPoints10000 = new ArrayList<>();
        for (int i = 0; i < 10000; ++i) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            Point goal = new Point(x, y);
            testPoints10000.add(goal);
        }


        Point p1, p2;
        for (int j = 0; j < 100; ++j) {
            sw = new Stopwatch();
            for (int i = 0; i < testPoints10000.size(); ++i) {
//        kd.nearest(kd.tree, goal, kd.tree);
//        Point p1 = kd.nearestRecursion(goal);
                p1 = testPoints10000.get(i);
                p1 = kd.nearest(p1.getX(), p1.getY());
            }
            time1 += sw.elapsedTime();

            sw = new Stopwatch();
            for (int i = 0; i < testPoints10000.size(); ++i) {
                p2 = testPoints10000.get(i);
                p2 = naive.nearest(p2.getX(), p2.getY());
            }
            time2 += sw.elapsedTime();
//        assertEquals(naive.nearest(x, y), kd.nearest(x, y));
//        assertEquals(naive.nearest(x, y), kd.nearest(kd.tree, goal, kd.tree).getP());
            System.out.println(time1);
            System.out.println(time2);
//        System.out.println(naive.time);
//        System.out.println(kd.time);
//        System.out.println(kd.times);
//        System.out.println(naive.times);
            System.out.println("No." + j + "times: " + "kd is " + time2 / time1 + "x faster than naive.");
            time1 = 0; time2 = 0;
        }
    }
}
