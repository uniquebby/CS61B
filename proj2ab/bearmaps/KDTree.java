package bearmaps;

import java.util.List;
import java.util.Stack;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * @author yangbinbin
 * 2019.06.25
 */
public class KDTree {
    public KDNode tree;
    private static final int LEFT = 0x1;
    private static final int RIGHT = 0x2;
    private static final int UP = 0x4;
    private static final int DOWN = 0x8;
    private static final int ALL_DONE = 0x0;
//    public static double time = 0;
    public static int times = 0;

    public class KDNode {
        Point p;
        KDNode side1;
        KDNode side2;

        KDNode(Point p) {
            this.p = p;
        }

        public double distance(Point goal) {
//            times++;
//            Stopwatch sw = new Stopwatch();
            double res =  Point.distance(this.p, goal);
//            time += sw.elapsedTime();
            return res;
        }

        public Point getP() {
            return p;
        }
    }

    public class Region {
        private double left;
        private double right;
        private double up;
        private double down;

        public Region() {
            this(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
                    Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        }

        public Region(double l, double r, double u, double d) {
            this.left = l;
            this.right = r;
            this.up = u;
            this.down = d;
        }

        /* Return the direction of goal compare to this. */
        public int directionOf(Point goal) {
            if (goal.getX() > right) {
                if (goal.getY() > up) {
                    return RIGHT|UP;
                } else if (goal.getY() < down){
                    return RIGHT|DOWN;
                } else {
                    return RIGHT;
                }
            } else if (goal.getX() < left){
                if (goal.getY() > up) {
                    return LEFT|UP;
                } else if (goal.getY() < down) {
                    return LEFT|DOWN;
                } else {
                    return LEFT;
                }
            } else {
                if (goal.getY() > up) {
                    return UP;
                } else if (goal.getY() < down) {
                    return DOWN;
                } else {
                    // contain
                    return 0;
                }
            }
        }


        /* Return the power 2 of distance of this region to goal.*/
        public double distance(Point goal) {
            int direction = this.directionOf(goal);
            switch (direction) {
                case UP:
                    return Math.pow(goal.getY() - up, 2);
                case DOWN:
                    return Math.pow(down - goal.getY(), 2);
                case LEFT:
                    return Math.pow(left - goal.getX() , 2);
                case RIGHT:
                    return Math.pow(goal.getX() - right, 2);
                case LEFT|UP:
                    return Point.distance(new Point(left, up), goal);
                case LEFT|DOWN:
                    return Point.distance(new Point(left, down), goal);
                case RIGHT|UP:
                    return Point.distance(new Point(right, up), goal);
                case RIGHT|DOWN:
                    return Point.distance(new Point(right, down), goal);
                default:
                    return 0;
            }
        }

        public void setDown(double down) {
            this.down = down;
        }

        public double getDown() {
            return down;
        }

        public void setLeft(double left) {
            this.left = left;
        }

        public double getLeft() {
            return left;
        }

        public void setRight(double right) {
            this.right = right;
        }

        public double getRight() {
            return right;
        }

        public void setUp(double up) {
            this.up = up;
        }

        public double getUp() {
            return up;
        }
    }


    /**
     * @param p Point to insert.
     * @param prev previou Side has inserted.
     */
    public void insert(Point p, int prev) {
        if (tree == null) {
            tree = new KDNode(p);
            return;
        }

        KDNode n = tree, parent = tree;
        while (n != null) {
            if (prev == UP || prev == DOWN) {
                if (p.getX() < n.p.getX()) {
                    parent = n;
                    n = n.side1;
                    prev = LEFT;
                } else {
                    parent = n;
                    n = n.side2;
                    prev = RIGHT;
                }
            } else {
                if (p.getY() > n.p.getY()) {
                    parent = n;
                    n = n.side1;
                    prev = UP;
                } else {
                    parent = n;
                    n = n.side2;
                    prev = DOWN;
                }
            }
        }

        if (prev == LEFT || prev == UP) {
            parent.side1 = new KDNode(p);
        } else {
            parent.side2 = new KDNode(p);
        }
    }

    public KDTree(List<Point> points) {
        buildTree(points);
    }

    private void buildTree(List<Point> points) {
        int i = 0;
        while (i < points.size()) {
            insert(points.get(i++), UP);
        }
    }

    public Point nearest(double x, double y) {
        if (tree == null) {
            return null;
        }
        Point goal = new Point(x, y);
        Point best = tree.p;
        double bestDistance = tree.distance(goal);
        /* stack of node. */
        Stack<KDNode> sn = new Stack<>();
        /* stack of region. */
        Stack<Region> sr = new Stack<>();
        /* stack of nextDirection. false for DOWN, true for RIGHT.*/
        Stack<Integer> sd = new Stack<>();
        Region curRegion = new Region();
        KDNode cur = tree;
        int curDirection = UP;
        boolean isCanBeSheared = false;

        while (!isCanBeSheared || !sn.empty()) {
            while (!isCanBeSheared) {
//                ++times;
                double d = cur.distance(goal);
                if (d == 0) {
                    return cur.p;
                } else if (d < bestDistance) {
                    best = cur.p;
                    bestDistance = d;
                }
                sn.push(cur);
                sr.push(curRegion);
                curRegion = new Region(curRegion.getLeft(), curRegion.getRight(),
                        curRegion.getUp(), curRegion.getDown());

                /* Decide where to go first and save the next direction to stack sd. */
                if (cur.side1 == null && cur.side2 == null) {
                    sd.push(ALL_DONE);
                    break;
                } else if (cur.side2 == null) {
                    if ((curDirection & (LEFT|RIGHT)) != 0) {
                        curRegion.setDown(cur.p.getY());
                        curDirection = UP;
                    } else {
                        curRegion.setRight(cur.p.getX());
                        curDirection = LEFT;
                    }
                    sd.push(ALL_DONE);
                    cur = cur.side1;
                } else if (cur.side1 == null) {
                    if ((curDirection & (LEFT|RIGHT)) != 0) {
                        curRegion.setUp(cur.p.getY());
                        curDirection = DOWN;
                    } else {
                        curRegion.setLeft(cur.p.getX());
                        curDirection = RIGHT;
                    }
                    sd.push(ALL_DONE);
                    cur = cur.side2;
                } else {
                    if (Point.distance(cur.side1.p, goal) <= Point.distance(cur.side2.p, goal)){
                        if ((curDirection & (LEFT|RIGHT)) != 0) {
                            curRegion.setDown(cur.p.getY());
                            curDirection = UP;
                            sd.push(DOWN);
                        } else {
                            curRegion.setRight(cur.p.getX());
                            curDirection = LEFT;
                            sd.push(RIGHT);
                        }
                        cur = cur.side1;
                    } else {
                        if ((curDirection & (LEFT|RIGHT)) != 0) {
                            curRegion.setUp(cur.p.getY());
                            curDirection = DOWN;
                            sd.push(UP);
                        } else {
                            curRegion.setLeft(cur.p.getX());
                            curDirection = RIGHT;
                            sd.push(LEFT);
                        }
                        cur = cur.side2;
                    }
                }

//                    Stopwatch sw = new Stopwatch();
                isCanBeSheared = isCanBeSheared(curRegion, bestDistance, goal);
//                    time += sw.elapsedTime();
            }

            cur = sn.pop();
            curRegion = sr.pop();
            int nextDirection = sd.pop();

            switch (nextDirection) {
                case LEFT:
                    curRegion.setRight(cur.p.getX());
                    cur = cur.side1;
                    break;
                case RIGHT:
                    curRegion.setLeft(cur.p.getX());
                    cur = cur.side2;
                    break;
                case UP:
                    curRegion.setDown(cur.p.getY());
                    cur = cur.side1;
                    break;
                case DOWN:
                    curRegion.setUp(cur.p.getY());
                    cur = cur.side2;
                    break;
                case ALL_DONE:
                    cur = null;
                default:
                    break;
            }
            curDirection = nextDirection;

            if (cur != null) {
//                Stopwatch sw = new Stopwatch();
                isCanBeSheared = isCanBeSheared(curRegion, bestDistance, goal);
//                time += sw.elapsedTime();
            } else {
                isCanBeSheared = true;
            }
        }
        return best;
    }

    private boolean isCanBeSheared(Region r, double d, Point goal) {
        return r.distance(goal) >= d;
    }

    public KDNode nearest(KDNode n, Point goal, KDNode best) {
        if (n == null) {
            return best;
        } else if (n.distance(goal) < best.distance(goal)) {
            best = n;
        }

        best = nearest(n.side1, goal, best);
        best = nearest(n.side2, goal, best);
        return best;
    }
}
