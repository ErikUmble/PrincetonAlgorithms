/* *****************************************************************************
 *  Name:              Erik
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {


    private double[] thresholds;
    private double CONFIDENCE_95;
    //private int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if ((n <= 0) || (trials <= 0)) {
            throw new IllegalArgumentException();
        } else {

            this.thresholds = new double[trials];
            this.CONFIDENCE_95 = 1.96;
            //this.trials = trials;
            for (int i = trials-1; i >= 0; i--) {
                Percolation perc;
                perc = new Percolation(n);
                while (!perc.percolates()) {
                    int row = StdRandom.uniform(1,n+1);
                    int col = StdRandom.uniform(1,n+1);
                    if (!perc.isOpen(row,col)) {
                        perc.open(row,col);

                    }

                }

                double open = perc.numberOfOpenSites();
                this.thresholds[i] = open/(n*n);
            }

        }
    }

    // sample mean of percolation threshold
    public double mean() {

        return StdStats.mean(this.thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double colo = this.mean() - (CONFIDENCE_95*this.stddev()/Math.sqrt(this.thresholds.length));
        return colo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double cohi = this.mean() + (CONFIDENCE_95*this.stddev()/Math.sqrt(this.thresholds.length));
        return cohi;
    }
    /*
    // test client (see below)
    public static void main(String[] args) {
        In input = new In(args[0]);
        int n = input.readInt();
        int trials = input.readInt();

        PercolationStats test = new PercolationStats(n, trials);
        System.out.printf("mean                             %f \n", test.mean());
        System.out.printf("stddev                           %f \n", test.stddev());
        System.out.print("95 confidence                   ");
        System.out.print(test.confidenceLo());
        System.out.println(test.confidenceHi());
        System.out.print("Time took");

    }*/
    public static void main(String[] args) {
        int gridSize = 10;
        int trialCount = 10;
        if (args.length >= 2) {
            gridSize = Integer.parseInt(args[0]);
            trialCount = Integer.parseInt(args[1]);
        }
        PercolationStats ps = new PercolationStats(gridSize, trialCount);

        String confidence = "[" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]";
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + confidence);
    }

}
