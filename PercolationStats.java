public class PercolationStats {

    // holds the percolation threshold of each experiment
    private double[] thresholds;
    
    // total number of conducted experiments
    private int T;
    
    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        
        if (N <= 0 || T <= 0)
            throw new java.lang.IllegalArgumentException();
        
        this.T = T;
        thresholds = new double[T];
        for (int t = 0; t < T; t++) {
            Percolation perc = new Percolation(N);
            
            int opened = 0;  // opened sites
            while (!perc.percolates()) {

                int row, col;
                do {
                    row = StdRandom.uniform(1, N+1);
                    col = StdRandom.uniform(1, N+1);
                } while (perc.isOpen(row, col));
                
                opened++;
                perc.open(row, col);
            }
            
            thresholds[t] = opened*1.0 / (N*N);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        double stddev = stddev();
        double mean = mean();
        
        return mean - (1.96*stddev / Math.sqrt(T*1.0));
    }

    // returns upper bound of the 95% confidence interval
    public double confidenceHi() {
        double stddev = stddev();
        double mean = mean();
        
        return mean + (1.96*stddev / Math.sqrt(T*1.0));
    }

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        
        PercolationStats percStats = new PercolationStats(N, T);
        
        System.out.printf("mean = %f\n", percStats.mean());
        System.out.printf("stddev = %f\n", percStats.stddev());
        
        System.out.printf("95%% confidence interval = %f, %f\n", 
                percStats.confidenceLo(), percStats.confidenceHi());
    }
}
