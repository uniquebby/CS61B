package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private int times;
    private Percolation percolation;
    /* the fractions of open sites. */
    private int[] x;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N or T should not less or equal than zero");
        }
        this.times = T;

        for (int i = 0; i < T; ++i) {
            this.percolation = pf.make(N);
            while (!percolation.percolates()) {
                percolation.open(StdRandom.uniform(N), StdRandom.uniform(N));
            }
            x[i] = percolation.numberOfOpenSites();
        }
    }

    public double mean() {
        return StdStats.mean(x);
    }

    public double stddev() {
        return StdStats.stddev(x);
    }

    public double confidenceLow() {
        return mean() - stddev() / Math.sqrt((double)times);
    }

    public double confidenceHigh() {
        return mean() + stddev() / Math.sqrt((double)times);
    }

}
