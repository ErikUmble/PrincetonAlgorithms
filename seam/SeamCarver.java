
//*  Name: Erik Umble

//*  Date: May 31

//*  Description: A cool image resizing technique that preserves content

import edu.princeton.cs.algs4.Picture;
/*
To get color information from getRBG(x,y), use
  int rgb = getRBG(x,y);
  int r = (rgb >> 16) & 0xFF;
  int g = (rgb >>  8) & 0xFF;
  int b = (rgb >>  0) & 0xFF;
  // then to get back into a single int
  int rgb = (r << 16) + (g << 8) + (b << 0);

  We maintain a 2d int array of color information as well as the current int width and height values
  all values in the array to the right of width - 1 and below height -1 are garbage
  then, when picture is called, we construct a picture from those non-garbage color values

  to remove a seam, for each row of the seam, we swap pixels rightward until width - 1
  (same for horizontal seam but downwards and height -1)
 */
public class SeamCarver {
    private static final double BORDER_ENERGY = 1000.00;
    private int[][] colors;
    private double[][] energies;
    private int width;
    private int height;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        width = picture.width();
        height = picture.height();

        // copy over the color information from picture
        this.colors = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                colors[x][y] = picture.getRGB(x, y);
            }
        }
        // initialize and compute energies
        this.energies = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energies[x][y] = energy(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture picture = new Picture(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                picture.setRGB(x, y, colors[x][y]);
            }
        }
        return picture;
    }

    // width of current picture
    public int width() { return width; }

    // height of current picture
    public int height() { return height; }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if ((x < 0) || (x >= width) || (y < 0) || (y >= height)) throw new IllegalArgumentException();
        if ((x == 0) || (x == width - 1) || (y == 0) || (y == height - 1)) return BORDER_ENERGY; // edge case
        return Math.sqrt(energyFromRGB(colors[x-1][y], colors[x+1][y]) +
                                 energyFromRGB(colors[x][y-1], colors[x][y+1]));
    }
    private double energyFromRGB(int rgb0, int rgb1) {
        // decomposes color information and returns and sum of squared differences in color values
        int r0 = (rgb0 >> 16) & 0xFF;
        int g0 = (rgb0 >> 8) & 0xFF;
        int b0 = rgb0 & 0xFF;
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF;
        int b1 = rgb1 & 0xFF;

        return (Math.pow((r0 - r1), 2) + Math.pow((g0 - g1), 2) + Math.pow((b0 - b1), 2));
    }
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // keep track, for each vertex, the current distance from a left vertex
        // and the column index from the row above that has an edge to it
        double[][] distTo = new double[width][height];
        int[][] rowTo = new int[width][height];
        // initialize distTo to infinity for non-left edges and 1000 for left edges
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == 0) distTo[x][y] = BORDER_ENERGY;
                else distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }
        // since the graph is defined with all edges pointing to a right neighbor, we can
        // relax edges in cols from left to right for topological order
        for (int x = 0; x < width - 1; x++) { // the rightmost row has no edges from it
            for (int y = 1; y < height - 1; y++) { // do not consider paths from edge pixels ***
                // relax all edges pointing from (x,y): ie (x+1, y-1), (x+1, y), (x+1, y+1)
                // using i = x+1 and j = y-1, y, y+1
                int j = y - 1;
                for (int i = x + 1; j <= y + 1; j++) {
                    if (distTo[i][j] > distTo[x][y] + energies[i][j]) {
                        distTo[i][j] = distTo[x][y] + energies[i][j];
                        rowTo[i][j] = y;
                    }
                }
            }
        }
        // the best horizontal seam has the shortest distTo for a rightmost pixel
        // so we find this rightmost pixel, and trace the path back to find the seam
        int bestRow = 0;
        double shortestDistance = Double.POSITIVE_INFINITY;
        for (int y = 1; y < height - 1; y++) {
            if (distTo[width-1][y] < shortestDistance) {
                shortestDistance = distTo[width-1][y];
                bestRow = y;
            }
        }
        int[] bestRows = new int[width];
        for (int x = width-1; x >= 0; x--) {
            bestRows[x] = bestRow;
            bestRow = rowTo[x][bestRow];
        }
        return bestRows;

        // *** In the case where width < 3, and the picture is only made of edge pixels
        // the default column path of 0, 0, 0, ... 0 in colTo is fine.
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // keep track, for each vertex, the current distance from a top vertex
        // and the column index from the row above that has an edge to it
        double[][] distTo = new double[width][height];
        int[][] colTo = new int[width][height];
        // initialize distTo to infinity for non-top edges and 1000 for top edges
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (y == 0) distTo[x][y] = BORDER_ENERGY;
                else distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }
        // since the graph is defined with all edges pointing to a lower row, we can
        // relax edges in rows from top to bottom for topological order
        for (int y = 0; y < height - 1; y++) { // the bottom row has no edges from it
            for (int x = 1; x < width - 1; x++) { // do not consider paths from edge pixels ***
                // relax all edges pointing from (x,y): ie (x-1, y+1), (x, y+1), (x+1, y+1)
                // using i = x-1, x, x+1 and j = y+1
                int j = y + 1;
                for (int i = x - 1; i <= x + 1; i++) {
                    if (distTo[i][j] > distTo[x][y] + energies[i][j]) {
                        distTo[i][j] = distTo[x][y] + energies[i][j];
                        colTo[i][j] = x;
                    }
                }
            }
        }
        // the best vertical seam has the shortest distTo for a bottom pixel
        // so we find this bottom pixel, and trace the path back to find the seam
        int bestCol = 0;
        double shortestDistance = Double.POSITIVE_INFINITY;
        for (int x = 1; x < width - 1; x++) {
            if (distTo[x][height-1] < shortestDistance) {
                shortestDistance = distTo[x][height-1];
                bestCol = x;
            }
        }
        int[] bestColumns = new int[height];
        for (int y = height-1; y >= 0; y--) {
            bestColumns[y] = bestCol;
            bestCol = colTo[bestCol][y];
        }
        return bestColumns;

        // *** In the case where width < 3, and the picture is only made of edge pixels
        // the default column path of 0, 0, 0, ... 0 in colTo is fine.
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if ((seam == null) || (height <= 1) || (seam.length != width)) throw new IllegalArgumentException();
        if (!validSeam(seam, 0, height - 1)) throw new IllegalArgumentException();

        // for each col, starting with the seam[col] pixel and going downward until the second-to-last row,
        // copy the information of the downward neighboring pixel
        // this means the last row is simply a repeat of the second-to-last, but that is ok because it is garbage
        for (int x = 0; x < width; x++) {
            for (int y = seam[x]; y < height - 1; y++) {
                colors[x][y] = colors[x][y + 1];
                energies[x][y] = energies[x][y + 1];
            }
        }
        // update energies along the seam (now seam[col] and seam[col] - 1)
        for (int x = 0; x < width; x++) {
            energies[x][seam[x]] = energy(x, seam[x]);
            if (seam[x] > 0) energies[x][seam[x] - 1] = energy(x, seam[x] - 1);
        }
        // update height
        height -= 1;

        // update energies along new edge (height - 1)
        for (int x = 0; x < width; x++) {
            energies[x][height - 1] = BORDER_ENERGY;
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if ((seam == null) || (width <= 1) || (seam.length != height)) throw new IllegalArgumentException();
        if (!validSeam(seam, 0, width - 1)) throw new IllegalArgumentException();

        // for each row, starting with the seam[row] pixel and going rightward until the second-to-last column,
        // copy the information of the rightward neighboring pixel
        // this means the last column is simply a repeat of the second-to-last, but that is ok because it is garbage
        for (int y = 0; y < height; y++) {
            for (int x = seam[y]; x < width - 1; x++) {
                colors[x][y] = colors[x+1][y];
                energies[x][y] = energies[x+1][y];
            }
        }
        // update energies along the seam (now seam[row] and seam[row] - 1)
        for (int y = 0; y < height; y++) {
            energies[seam[y]][y] = energy(seam[y], y);
            if (seam[y] > 0) energies[seam[y] - 1][y] = energy(seam[y] - 1, y);
        }
        // update width
        width -= 1;

        // update energies along new edge (width - 1)
        for (int y = 0; y < height; y++) {
            energies[width - 1][y] = BORDER_ENERGY;
        }
    }

    // returns true if all seam values are within [lower, upper] and are within 1 of the previous
    private static boolean validSeam(int[] seam, int lower, int upper) {
        // check the first value
        if ((seam[0] < lower) || (seam[0] > upper)) return false;
        // check the others for value range and difference from previous
        for (int i = 1; i < seam.length; i++) {
            if ((seam[i] < lower) || (seam[i] > upper) || (Math.abs(seam[i] - seam[i-1]) > 1))
                return false;
        }
        return true;
    }

    //  unit testing (optional)
    public static void main(String[] args) {

        String[] a = {"HJocean.png", "400", "0"};
        ResizeDemo.main(a);

        /*
        int[] test_seam = { 0, 1, 1, 1, 1, 1, 0 };
        Picture im = new Picture("3x7.png");
        SeamCarver test = new SeamCarver(im);
        test.removeVerticalSeam(test_seam);
        //System.out.print(;*/
    }

}