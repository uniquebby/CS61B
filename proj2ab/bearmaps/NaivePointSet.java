package bearmaps;

import java.util.List;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * @author yangbinbin
 * 2019.06.24
 */
public class NaivePointSet implements PointSet{
    private List<Point> points;
//    public static int times = 0;
//    public static double time = 0;

    public NaivePointSet(List<Point> points) {
        this.points = points;
    }

    /* Returns the closest point to the inputted coordinates.*/
    public Point nearest(double x, double y) {
        Point p = new Point(x, y);
        double nearestDist = Point.distance(points.get(0), p);
        int i = 1, nearestPt = 0;
//        Stopwatch sw = new Stopwatch();
        while (i < points.size()) {
            double dis = Point.distance(points.get(i), p);
//            times++;
            if (dis  < nearestDist) {
                nearestPt = i;
                nearestDist = dis;
            }
            ++i;
        }
//        time += sw.elapsedTime();
        return new Point(points.get(nearestPt).getX(), points.get(nearestPt).getY());
    }
}
