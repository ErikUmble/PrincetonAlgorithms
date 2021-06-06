/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public final class Board {

    private final int[][] tiles;
    private final byte n;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = (byte) tiles[0].length;
        this.tiles = double_copy(tiles,n);
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (byte i = 0; i < n; i++) {
            for (byte j = 0; j < n ; j++) {
                if (!(tiles[i][j] == 0)) { //ignore the 0
                    if (! (((i * n) + j+1) == tiles[i][j])) count++; //the tile does not contain the value it should. Calculated with i*n + j

                }
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int count = 0;
        for (byte i = 0; i < n; i++) {
            for (byte j = 0; j < n ; j++) { //for computation, j starts at 1, for indexing use j-1
                int this_tile = tiles[i][j];
                if (this_tile == 0) continue; //ignore the 0
                byte correct_i = (byte) ((this_tile-1) / n); //integer divide for i
                byte correct_j = (byte) ((this_tile) % n); //remainder division for j
                if ((this_tile % n) == 0) correct_j = n;
                int distance = abs(i-correct_i) + abs((j+1)-correct_j);
                count = count + distance;
            }
        }
        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;

        return (Arrays.deepEquals(this.tiles, that.tiles));
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<Board>();
        //find the row, col of 0
        byte row = 0;
        byte col = 0;
        boolean con = true;
        for (byte i = 0; i < n && con; i++) {
            for (byte j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                    con = false;
                    break;

                }
            }
        }
        //create neighbors
        if (row > 0) {
            int [][] nr1_array = double_copy(tiles, n); //swap 0 with row - 1
            exchange(nr1_array, row, col, row-1, col);
            Board nr1_board = new Board(nr1_array);
            neighbors.push(nr1_board);
        }
        if (row < n - 1) {
            int [][] nr2_array = double_copy(tiles, n); //swap 0 with row + 1
            exchange(nr2_array, row, col, row+1, col);
            Board nr2_board = new Board(nr2_array);
            neighbors.push(nr2_board);
        }
        if (col > 0) {
            int [][] nc1_array = double_copy(tiles, n); //swap 0 with col - 1
            exchange(nc1_array, row, col, row, col-1);
            Board nr1_board = new Board(nc1_array);
            neighbors.push(nr1_board);
        }
        if (col < n -1) {
            int [][] nc2_array = double_copy(tiles, n); //swap 0 with col + 1
            exchange(nc2_array, row, col, row, col+1);
            Board nr1_board = new Board(nc2_array);
            neighbors.push(nr1_board);
        }
        return neighbors;

    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] new_tiles = double_copy(tiles, n);
        if ((new_tiles[0][0] != 0) && (new_tiles[0][1] != 0)) {
            exchange(new_tiles, 0,0,0,1);
        } else {
            exchange(new_tiles, n-1,0,n-1,1);
        }

        Board new_board = new Board(new_tiles);
        return new_board;
    }


    private void exchange(int[][] array,int row1, int col1, int row2, int col2) {
        int swap = array[row1][col1];
        array[row1][col1] = array[row2][col2];
        array[row2][col2] = swap;
    }
    private int[][] double_copy(int[][] array, int n) {
        int[][] new_array = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                new_array[i][j] = array[i][j];
            }
        }
        return new_array;
    }
    private int abs(int a) {
        if (a >= 0) return a;
        else return -a;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tiles[i][j] = (i*3) + (j+1);
            }
        }
        tiles[2][2] = 0;
        Board t1 = new Board(tiles);
        Board test = new Board(tiles);
        System.out.println(t1.equals(test));

    }

}
