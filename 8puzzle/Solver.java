/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Iterator;

public final class Solver {
    private Stack<Board> solution;
    private int moves;
    private boolean is_solvable;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        solution = new Stack<Board>();
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        MinPQ<SearchNode> pqt = new MinPQ<SearchNode>();
        boolean solved = false;
        boolean solved_by_twin = false;
        SearchNode SN1 = new SearchNode(initial, 0, null); //initial search node
        SearchNode SNt = new SearchNode(initial.twin(),0,null); //initial twin search node
        SearchNode SNprevious = SN1;
        SearchNode SNtprevious = SNt;
        pq.insert(SN1);
        pqt.insert(SNt);
        while (!solved) {
            if (SNprevious.board.isGoal()) {
                solved = true;
                break; //is this correct? or should it finnish the loop anyways
            }
            if (SNtprevious.board.isGoal()) {
                solved = true;
                solved_by_twin = true;
                break; //is this correct? or should it finnish the loop anyways
            }

            SNprevious = pq.delMin();
            SNtprevious = pqt.delMin();

            Iterator<Board> Bneighbors = SNprevious.board.neighbors().iterator();
            Iterator<Board> Btneighbors = SNtprevious.board.neighbors().iterator();

            while (Bneighbors.hasNext()) {
                Board Bcurrent = Bneighbors.next();

                if (!(SNprevious.previous == null)) {
                    if (!(Bcurrent.equals(SNprevious.previous.board))) { //critical optimization
                        SearchNode SNcurrent = new SearchNode(Bcurrent, SNprevious.moves + 1, SNprevious);
                        pq.insert(SNcurrent);
                    }
                } else { //go ahead and do it, there have not been enough steps to return to a previous board
                    SearchNode SNcurrent = new SearchNode(Bcurrent, SNprevious.moves + 1, SNprevious);
                    pq.insert(SNcurrent);
                }
            }
            while (Btneighbors.hasNext()) {
                Board Bcurrent = Btneighbors.next();
                if (!(SNtprevious.previous == null)) {
                    if (!(Bcurrent.equals(SNtprevious.previous.board))) {
                        SearchNode SNtcurrent = new SearchNode(Bcurrent, SNtprevious.moves + 1, SNtprevious);
                        pqt.insert(SNtcurrent);
                    }

                } else {
                    SearchNode SNtcurrent = new SearchNode(Bcurrent, SNtprevious.moves + 1, SNtprevious);
                    pqt.insert(SNtcurrent);
                }
            }


            }
        if (solved_by_twin) {
            moves = -1;
            solution = null;
            is_solvable = false;
        } else {
            moves = SNprevious.moves;
            SearchNode backtrack = SNprevious;
            solution.push(backtrack.board);
            while (backtrack.previous != null) {
                backtrack = backtrack.previous;
                solution.push(backtrack.board);

            }

            is_solvable = true;


        }



    }
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int m_distance;
        private int priority;
        private int moves;
        private SearchNode previous = null;
        private SearchNode(Board board, int moves, SearchNode previous){
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.m_distance = board.manhattan();
            this.priority = this.m_distance + this.moves;
        }
        public int compareTo(SearchNode that) {
            if (this.priority > that.priority) return 1;
            else if (this.priority < that.priority) return -1;
            else return 0;

        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return is_solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {

    }

}
