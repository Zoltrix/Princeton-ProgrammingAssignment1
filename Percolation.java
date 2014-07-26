public class Percolation {

    private boolean[][] grid;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF topUF;
    
    private int N;
    
    private int topVirtual;
    private int bottomVirtual;

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        
        if (N <= 0)
            throw new IllegalArgumentException();
        
        this.N = N;
        grid = new boolean[N][N];

        // initialize uf with 2 extra nodes
        // for the top and bottom virtual nodes
        uf = new WeightedQuickUnionUF(N*N + 2);
        topUF = new WeightedQuickUnionUF(N*N + 1);

        // 0-based index
        topVirtual = N * N;
        bottomVirtual = N * N + 1;

        // connect top row with virtual top node
        for (int i = 0; i < N; i++) {
            topUF.union(i, topVirtual);
            uf.union(i, topVirtual);
        }

        // connect bottom row with virtual bottom node
        for (int i = N * N - N; i < N * N; i++)
            uf.union(i, bottomVirtual);

    }

    private int xyTo1D(int i, int j) {
        return i * N + j;
    }

    private boolean validate(int i, int j) {
        return i >= 0 && i < N && j >= 0 && j < N;
    }

    // open site (row i, column j) if it is not already
    public void open(int i, int j) {
        // convert to 0-based indices
        int row = i-1;
        int col = j-1;
        
        // validate indices
        if (!validate(row, col))
            throw new java.lang.IndexOutOfBoundsException();
        
        // already open
        if (grid[row][col])
            return;
        
        grid[row][col] = true;
        int thisSite = xyTo1D(row, col);
        
        /*look at the four neighbors of this cell*/
        
        // right
        if (validate(row, col+1)) { 
            int right = xyTo1D(row, col+1);
            if (grid[row][col+1]) {
                uf.union(thisSite, right);
                topUF.union(thisSite, right);
            }
        }
        
        // left
        if (validate(row, col-1)) { 
            int left = xyTo1D(row, col-1);
            if (grid[row][col-1]) {
                uf.union(thisSite, left);
                topUF.union(thisSite, left);
            }
        }
        
        // up
        if (validate(row-1, col)) { 
            int up = xyTo1D(row-1, col);
            if (grid[row-1][col]) {
                uf.union(thisSite, up);
                topUF.union(thisSite, up);
            }
        }
        
        // down
        if (validate(row+1, col)) { 
            int down = xyTo1D(row+1, col);
            if (grid[row+1][col]) {
                uf.union(thisSite, down);
                topUF.union(thisSite, down);
            }
        }
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        int row = i-1;
        int col = j-1;
        
        // validate indices
        if (!validate(row, col))
            throw new java.lang.IndexOutOfBoundsException();
        
        return grid[row][col];
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        int row = i-1;
        int col = j-1;
        
        // validate indices
        if (!validate(row, col))
            throw new java.lang.IndexOutOfBoundsException();
        
        if (!grid[row][col])
            return false;
        
        int thisSite = xyTo1D(row, col);
        
        return topUF.connected(thisSite, topVirtual);
    }

    // does the system percolate?
    public boolean percolates() {
        if (N == 1)
            return grid[0][0];
        
        return uf.connected(topVirtual, bottomVirtual);
    }
}
