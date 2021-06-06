/* *****************************************************************************
 *  Name:              Erik
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int n;
    private int size;
    private int openSites = 0;
    private boolean[] status; // 0:closed, > 0: open, 2:open and top, 3: open top and botton, 4: open and bottom
    private WeightedQuickUnionUF dataModel; // 0 starts (1,1) size=virtual top size +1 = vitual bottom
    private WeightedQuickUnionUF dataModel2; //same as first except without virtual bottom


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n >= 1) {
            this.n = n;
            this.size = n*n;
            dataModel = new WeightedQuickUnionUF(this.size + 2); // dont forget about virtual top and botton
            dataModel2 = new WeightedQuickUnionUF(this.size + 1); // no virutal bottom to avoid backwash
            status = new boolean[this.size];
        } else {
            throw new IllegalArgumentException();
        }


    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (validIndex(row, col)) {
            int id = xyTo1D(row, col);
            if (!status[id]) {
                status[id] = true;
                openSites ++;

                if (row == this.n) {
                    this.dataModel.union(id, this.size + 1); // virtual bottom at size + 1
                }
                if (row == 1) {
                    this.dataModel.union(id, this.size); //virtual top
                    this.dataModel2.union(id, this.size); //virtual top
                }
                if (!(row == 1) && status[xyTo1D(row-1, col)]) {
                    this.dataModel.union(id, xyTo1D(row - 1, col));
                    this.dataModel2.union(id, xyTo1D(row - 1, col));
                    if (!(row == this.n) && status[xyTo1D(row + 1, col)]) {
                        this.dataModel.union(id, xyTo1D(row + 1, col));
                        this.dataModel2.union(id, xyTo1D(row + 1, col));
                    }
                } else if (!(row == this.n)){ //we know that row == 1 so dont even check if it might equal n


                    if (status[xyTo1D(row + 1, col)]) {
                        this.dataModel.union(id, xyTo1D(row + 1, col));
                        this.dataModel2.union(id, xyTo1D(row + 1, col));
                    }


                }
                if (!(col == 1) && this.status[xyTo1D(row, col-1)]) {
                    this.dataModel.union(id, xyTo1D(row, col-1));
                    this.dataModel2.union(id, xyTo1D(row, col-1));

                }
            } else if ((col == 1) && status[xyTo1D(row, col+1)]) {
                this.dataModel.union(id, xyTo1D(row, col+1));
                this.dataModel2.union(id, xyTo1D(row, col+1));
            }
            if (!(col == this.n) && this.status[xyTo1D(row, col+1)]) {
                this.dataModel.union(id, xyTo1D(row, col+1));
                this.dataModel2.union(id, xyTo1D(row, col+1));



            }

        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {

        return (status[xyTo1D(row, col)]);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {

        return this.dataModel2.find(xyTo1D(row, col)) == this.dataModel2.find(this.size);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.dataModel.find(this.size) == this.dataModel.find(this.size + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(6);
        perc.open(1, 6);
        perc.open(2,6);
        perc.open(3,6);
        perc.open(4,6);
        perc.open(4,5);
        for (int i = 0; i < perc.status.length+2; i++) {
            System.out.print(i + " ");
            System.out.println(perc.dataModel.find(i));

        }

    }
    private boolean validIndex(int row, int column) {
        if (((row <= 0) || (column <= 0)) || ((row > n) || (column > n)))
            throw new IllegalArgumentException();
        else {
            return true;
        }
    }
    private int xyTo1D(int row, int column) {
        validIndex(row, column);
        int index = ((row - 1) * n) + column-1; // -1 since 0 based index
        return index;

    }

}

/*
public class Percolation {

    private int n;
    private int size;
    private int openSites = 0;
    private boolean[] status; // 0:closed, > 0: open, 2:open and top, 3: open top and botton, 4: open and bottom
    private WeightedQuickUnionUF dataModel; // 0 starts (1,1) size=virtual top size +1 = vitual bottom


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n >= 1) {
            this.n = n;
            this.size = n*n;
            dataModel = new WeightedQuickUnionUF(this.size + 2); // dont forget about virtual top and botton
            status = new boolean[this.size];
        } else {
            throw new IllegalArgumentException();
        }


    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (validIndex(row, col)) {
            int id = xyTo1D(row, col);
            if (!status[id]) {
                status[id] = true;
                openSites ++;

                if (row == this.n) {
                    this.dataModel.union(id, this.size + 1); // virtual bottom at size + 1
                }
                if (row == 1) {
                    this.dataModel.union(id, this.size); //virtual top
                }
                if (!(row == 1) && status[xyTo1D(row-1, col)]) {
                    this.dataModel.union(id, xyTo1D(row - 1, col));
                    if (!(row == this.n) && status[xyTo1D(row + 1, col)]) {
                        this.dataModel.union(id, xyTo1D(row + 1, col));
                    }
                } else if (!(row == this.n)){ //we know that row == 1 so dont even check if it might equal n


                    if (status[xyTo1D(row + 1, col)]) {
                        this.dataModel.union(id, xyTo1D(row + 1, col));
                }


                }
                if (!(col == 1) && this.status[xyTo1D(row, col-1)]) {
                    this.dataModel.union(id, xyTo1D(row, col-1));

                    }
                } else if ((col == 1) && status[xyTo1D(row, col+1)]) {
                    this.dataModel.union(id, xyTo1D(row, col+1));
                }
                if (!(col == this.n) && this.status[xyTo1D(row, col+1)]) {
                    this.dataModel.union(id, xyTo1D(row, col+1));



            }

        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {

        return (status[xyTo1D(row, col)]);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {

        return this.dataModel.find(xyTo1D(row, col)) == this.dataModel.find(this.size);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.dataModel.find(this.size) == this.dataModel.find(this.size + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation perc = new Percolation(6);
        perc.open(1, 6);
        perc.open(2,6);
        perc.open(3,6);
        perc.open(4,6);
        perc.open(4,5);
        for (int i = 0; i < perc.status.length+2; i++) {
            System.out.print(i + " ");
            System.out.println(perc.dataModel.find(i));

        }

    }
    private boolean validIndex(int row, int column) {
        if (((row <= 0) || (column <= 0)) || ((row > n) || (column > n)))
            throw new IllegalArgumentException();
        else {
            return true;
        }
    }
    private int xyTo1D(int row, int column) {
        validIndex(row, column);
        int index = ((row - 1) * n) + column-1; // -1 since 0 based index
        return index;

    }

}
*/