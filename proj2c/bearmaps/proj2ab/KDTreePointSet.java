package bearmaps.proj2ab;

import java.util.List;
import java.util.Stack;

/**
 * @author yangbinbin
 * 2019.06.25
 */
public class KDTreePointSet implements PointSet{
    public KDNode tree;
    private static final int LEFT = 0x1;
    private static final int RIGHT = 0x2;
    private static final int UP = 0x4;
    private static final int DOWN = 0x8;
    private static final int ALL_DONE = 0x0;

    public class KDNode {
        Point p;
        KDNode side1;
        KDNode side2;

        KDNode(Point p) {
            this.p = p;
        }

        public double distance(Point goal) {
            double res =  Point.distance(this.p, goal);
            return res;
        }

        public double compareTo(Point goal, boolean isLR) {
            if (isLR) {
                return this.p.getX() - goal.getX();
            } else {
                return this.p.getY() - goal.getY();
            }
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

    public KDTreePointSet(List<Point> points) {
        buildTree(points);
    }

    private void buildTree(List<Point> points) {
        int i = 0;
        while (i < points.size()) {
            insert(points.get(i++), UP);
        }
    }

    public Point nearestRecursion(Point goal) {
       Region r = new Region();
       return nearest(tree, goal, tree, true, r).p;
    }

    public KDNode nearest(KDNode n, Point goal, KDNode best, boolean isLR, Region r) {
        if (n == null) {
            return best;
        }
        KDNode goodSide, badSide;
        Region badRegion;
        if (n.distance(goal) < best.distance(goal)) {
            best = n;
        }
        if (n.compareTo(goal, isLR) >= 0) {
            goodSide = n.side1;
            badSide = n.side2;
            if (isLR) {
                badRegion = new Region(n.p.getX(), r.getRight(), r.getUp(), r.getDown());
                r.setRight(n.p.getX());
            } else {
                badRegion = new Region(r.getLeft(), r.getRight(), n.p.getY(), r.getDown());
                r.setDown(n.p.getY());
            }
        } else {
            goodSide = n.side2;
            badSide = n.side1;
            if (isLR) {
                badRegion = new Region(r.getLeft(), n.p.getX(), r.getUp(), r.getDown());
                r.setLeft(n.p.getX());
            }  else {
                badRegion = new Region(r.getLeft(), r.getRight(), r.getUp(), n.p.getY());
                r.setUp(n.p.getY());
            }
        }
        best = nearest(goodSide, goal, best, !isLR, r);
        if (badRegion.distance(goal) < best.distance(goal)) {
            best = nearest(badSide, goal, best, !isLR, badRegion);
        }
        return best;
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
                double d = cur.distance(goal);
                if (d == 0) {
                    return cur.p;
                } else if (d < bestDistance) {
                    best = cur.p;
                    bestDistance = d;
                }
                sn.push(cur);
                sr.push(curRegion);

                /* Decide where to go first and save the next direction to stack sd. */
                if (cur.side1 == null && cur.side2 == null) {
                    sd.push(ALL_DONE);
                    break;
                } else if (cur.side2 == null) {
                    if ((curDirection & (LEFT|RIGHT)) != 0) {
                        curRegion = new Region(curRegion.getLeft(), curRegion.getRight(),
                                        curRegion.getUp(), cur.p.getY());
                        curDirection = UP;
                    } else {
                        curRegion = new Region(curRegion.getLeft(), cur.p.getX(),
                                        curRegion.getUp(), curRegion.getDown());
                        curRegion.setRight(cur.p.getX());
                        curDirection = LEFT;
                    }
                    sd.push(ALL_DONE);
                    cur = cur.side1;
                } else if (cur.side1 == null) {
                    if ((curDirection & (LEFT|RIGHT)) != 0) {
                        curRegion = new Region(curRegion.getLeft(), curRegion.getRight(),
                                        cur.p.getY(), curRegion.getDown());
                        curDirection = DOWN;
                    } else {
                        curRegion = new Region(cur.p.getX(), curRegion.getRight(),
                                        curRegion.getUp(), curRegion.getDown());
                        curDirection = RIGHT;
                    }
                    sd.push(ALL_DONE);
                    cur = cur.side2;
                } else {
                    double distance1,distance2;
                    if ((curDirection & (LEFT|RIGHT)) != 0) {
                        Region ru = new Region(curRegion.getLeft(), curRegion.getRight(),
                                        curRegion.getUp(), cur.p.getY());

                        Region rd = new Region(curRegion.getLeft(), curRegion.getRight(),
                                        cur.p.getY(), curRegion.getDown());
                        distance1 = ru.distance(goal);
                        distance2 = rd.distance(goal);

                        if (distance1 <= distance2) {
                            curRegion = ru;                           //up
                            curDirection = UP;
                            sd.push(DOWN);
                            cur = cur.side1;
                        } else {
                            curRegion = rd;
                            curDirection = DOWN;
                            sd.push(UP);
                            cur = cur.side2;
                        }

                    } else {
                        Region rl = new Region(curRegion.getLeft(), cur.p.getX(),
                        curRegion.getUp(), curRegion.getDown());

                        Region rr = new Region(cur.p.getX(), curRegion.getRight(),
                        curRegion.getUp(), curRegion.getDown());
                        distance1 = rl.distance(goal);
                        distance2 = rr.distance(goal);

                        if (distance1 <= distance2) {
                            curRegion = rl;
                            curDirection = LEFT;
                            sd.push(RIGHT);
                            cur = cur.side1;
                        } else {
                            curRegion = rr;
                            curDirection = RIGHT;
                            sd.push(LEFT);
                            cur = cur.side2;
                        }
                    }
                    if (distance1 == 0 || distance2 == 0) {
                        isCanBeSheared = false;
                        continue;
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
//        Stopwatch sw = new Stopwatch();
        boolean res = r.distance(goal) >= d;
//        time += sw.elapsedTime();
        return res;
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
