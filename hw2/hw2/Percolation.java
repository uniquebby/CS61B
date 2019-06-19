package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private enum site{BLOCKED, OPEN, FULL};
    private int openSites;
    private int size;
    private site[][] grid;
    WeightedQuickUnionUF wuf;

    /**
     * Create N-by-N grid, with all sites initially blocked.
     * @param N the rows and columns of grid.
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        size = N;
        grid = new site[N][N];
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; ++j) {
                grid[i][j] = site.BLOCKED;
            }
        }
        wuf = new WeightedQuickUnionUF(N * N);
    }

    public void isValidIndex(int row, int col) {
        if (row < size && col < size && row >= 0 && col >= 0) {
            return;
        } else {
            throw new IndexOutOfBoundsException("index row or col is not between 0 and " + (size - 1));
        }
    }

    /**
     * open the site (row, col) if it is not open already.
     * @param row
     * @param col
     */
    public void open(int row, int col) {
        isValidIndex(row, col);
        if (row == 0) {
            grid[row][col] = site.FULL;
        } else {
            grid[row][col] = site.OPEN;
        }
        ++openSites;
        update(row, col);
    }

    /* conputing the index with row and col. */
    public int indexOf(int row, int col) {
        return row * size + col;
    }

    /* conputing the index to row and col. */
    public int[] indexTo(int index) {
        int[] res = new int[2];
        res[0] = index / size;
        res[1] = index % size;
        return res;
    }

    /* update the union for each time to open. */
    public void update(int row, int col) {
        //up site.
        if (row > 0 && (isOpen(row - 1, col))) {
            if (isFull(row - 1, col) || isFull(row, col)) {
                int[] rc = indexTo(wuf.find(indexOf(row - 1, col)));
                grid[rc[0]][rc[1]] = site.FULL;
                grid[row][col] = site.FULL;
            }
            wuf.union(indexOf(row - 1, col), indexOf(row , col));
        }
        //down site.
        if (row < size - 1 && (isOpen(row + 1, col))) {
            if (isFull(row + 1, col) || isFull(row, col)) {
                int[] rc = indexTo(wuf.find(indexOf(row + 1, col)));
                grid[rc[0]][rc[1]] = site.FULL;
                grid[row][col] = site.FULL;
            }
            wuf.union(indexOf(row, col), indexOf(row + 1, col));
        }
        //left site.
        if (col > 0 && isOpen(row , col - 1)) {
            if (isFull(row , col - 1) || isFull(row, col)) {
               int[] rc = indexTo(wuf.find(indexOf(row, col - 1)));
                grid[rc[0]][rc[1]] = site.FULL;
                grid[row][col] = site.FULL;
            }
            wuf.union(indexOf(row, col), indexOf(row , col - 1));
        }
        //right site.
        if (col < size - 1 && isOpen(row , col + 1)) {
            if (isFull(row , col + 1) || isFull(row, col)) {
                int[] rc = indexTo(wuf.find(indexOf(row, col + 1)));
                grid[rc[0]][rc[1]] = site.FULL;
                grid[row][col] = site.FULL;
            }
            wuf.union(indexOf(row, col), indexOf(row , col + 1));
        }
    }

    /* is the site (row, col) open? */
    public boolean isOpen(int row, int col) {
        isValidIndex(row, col);
        return grid[row][col] != site.BLOCKED;
    }

    /* is the site (row, col) full? */
    public boolean isFull(int row, int col) {
        isValidIndex(row, col);
        int[] rc = indexTo(wuf.find(indexOf(row, col)));
        return grid[rc[0]][rc[1]] == site.FULL;
    }

    /* number of open sites. */
    public int numberOfOpenSites() {
        return openSites;
    }

    /* does the system percolate? */
    public boolean percolates() {
        for (int j = 0; j < size; ++j) {
            if (isFull(size - 1, j)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {

    }
}
