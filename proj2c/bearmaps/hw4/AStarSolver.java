package bearmaps.hw4;

import edu.princeton.cs.algs4.Stopwatch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author yangbinbin
 * @param <Vertex>
 * 2019.06.28
 */
public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    private ArrayHeapMinPQ<Vertex> pq;
    private SolverOutcome outcome;
    private double solutionWeight;
    private List<Vertex> solution;
    private int numStatesExplored;
    private HashMap<Vertex, DistAndParent> distToAndParentMap;
    private double timeSpent;


    private class DistAndParent {
        double distTo;
        Vertex parent;

        DistAndParent(double d, Vertex p) {
            this.distTo = d;
            this.parent = p;
        }
    }

    /**  Constructor which finds the solution, computing everything necessary for all
     *   other methods to return their results in constant time. Note that timeout passed in is in seconds.
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        pq = new ArrayHeapMinPQ();
        timeSpent = 0;
        distToAndParentMap = new HashMap<Vertex, DistAndParent>();

        Vertex p;
//        distToAndParentMap.put(p, new DistAndParent(0, p));
        pq.add(start, input.estimatedDistanceToGoal(start, end));
        distToAndParentMap.put(start, new DistAndParent(0, null));
        Stopwatch sw = new Stopwatch();
        while (pq.size() != 0 && !pq.getSmallest().equals(end) && timeSpent < timeout) {
            p = pq.removeSmallest();
            ++numStatesExplored;

            for (WeightedEdge e : input.neighbors(p)) {
                relax(e, input, end);
            }
            timeSpent = sw.elapsedTime();
        }
        if (pq.size() == 0) {
            outcome = SolverOutcome.UNSOLVABLE;
        } else if (timeSpent >= timeout) {
            outcome = SolverOutcome.TIMEOUT;
        } else {
            outcome = SolverOutcome.SOLVED;
            solution = getSolution(start, end);
            solutionWeight = distToAndParentMap.get(end).distTo;
        }

    }

    /** relax the neighbors.*/
    private void relax(WeightedEdge<Vertex> e, AStarGraph<Vertex> input, Vertex end) {
        Vertex p = e.from(), q = e.to();
        double w = e.weight();
        double distToP = distToAndParentMap.get(p).distTo;
        double distToQ;
        try {
            distToQ = distToAndParentMap.get(q).distTo;
        } catch (NullPointerException npe) {
            distToQ = Double.POSITIVE_INFINITY;
        }
        if (distToQ == Double.POSITIVE_INFINITY || distToP + w < distToQ) {
            distToQ = distToP + w;
            distToAndParentMap.put(q, new DistAndParent(distToQ, p));
            if (pq.contains(q)) {
                pq.changePriority(q, distToQ + input.estimatedDistanceToGoal(q, end));
            } else {
                pq.add(q, distToQ + input.estimatedDistanceToGoal(q, end));
            }
        }
    }

    private List<Vertex> getSolution(Vertex start, Vertex end) {
        LinkedList<Vertex> solution = new LinkedList<>();
        Vertex parent = end;
        while (parent != null) {
            solution.addFirst(parent);
            parent = distToAndParentMap.get(parent).parent;
        }
        return solution;
    }

    /**
     * @return  Returns one of SolverOutcome.SOLVED, SolverOutcome.
     * TIMEOUT, or SolverOutcome.UNSOLVABLE. Should be SOLVED if the
     * AStarSolver was able to complete all work in the time given.
     * UNSOLVABLE if the priority queue became empty. TIMEOUT if
     * the solver ran out of time.
     */
    public SolverOutcome outcome() {
        return outcome;
    }

    /**
     * @return A list of vertices corresponding to a solution.
     * Should be empty if result was TIMEOUT or UNSOLVABLE.
     */
    public List<Vertex> solution() {
        return solution;
    }

    /**
     * @return The total weight of the given solution, taking into account edge weights.
     * Should be 0 if result was TIMEOUT or UNSOLVABLE.
     */
    public double solutionWeight() {
        return solutionWeight;
    }

    /**
     * @return The total number of priority queue dequeue operations.
     */
    public int numStatesExplored() {
        return numStatesExplored;
    }

    /**
     * @return  The total time spent in seconds by the constructor.
     */
    public double explorationTime() {
        return timeSpent;
    }
}
