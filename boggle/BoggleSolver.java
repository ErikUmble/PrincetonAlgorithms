/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver
{
    /*
        Notes: QU is treated as Q (ie, any words with QU get the combination 'substituted' with Q
    in the search table, and any words with Q not followed by a U are discarded. In score keeping,
    however, QU counts as two letters.
     */
    private final TRIESET st;
    private int M;
    private int N;
    private char[][] boardChar;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        st = new TRIESET();
        boolean include = true;
        for (String s: dictionary) {
            if (s.length() < 3) continue;
            for (int i = 0, n = s.length(); i < n; i++) {
                if (s.charAt(i) == 'Q') {
                    include = false;
                    if ((i == n - 1) || (s.charAt(i + 1) != 'U')) break;
                    else addQU(s);
                }
            }
            if (include) st.add(s);
            else include = true;
        }
    }
    private void addQU(String s) {
        // adds s to the st if s has valid Qs, but first removes all Us that follow Qs
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = s.length(); i < n; i++) {
            sb.append(s.charAt(i));
            if (s.charAt(i) == 'Q') {
                if ((i == n - 1) || (s.charAt(i + 1) != 'U')) return;
                i++;
            }
        }
        st.add(new String(sb));
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        HashSet<String> wordsFound = new HashSet<String>();
        // shortcut if no words were in the dictionary
        if (st.getRoot() == null) return wordsFound;
        M = board.rows();
        N = board.cols();
        boardChar = new char[M][N];
        // copy over board values
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                boardChar[i][j] = board.getLetter(i, j);
            }
        }
        // dfs from each starting position
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                char firstChar = board.getLetter(i, j);
                TRIESET.Node initial = st.getRoot().next(firstChar);
                // shortcut if no words start with this letter
                if (initial == null) continue;
                StringBuilder start = new StringBuilder();
                start.append(firstChar);
                if (start.charAt(0) == 'Q') start.append('U');

                boolean[][] marked = new boolean[M][N];
                dfs(marked, wordsFound, initial, start, i, j);
            }
        }
        return wordsFound;
    }
    // searches from (row i, col j) along all valid paths, stopping a path when no words contain it
    // as their prefix, and adding words to the wordsFound set when traversing filled nodes
    // expects the char at (i, j) to be the last in prefix
    private void dfs(boolean[][] marked, HashSet<String> wordsFound,
                        TRIESET.Node current, StringBuilder prefix, int i, int j) {
        // recursively searches each valid path, returning true if a word was found down the path
        // marks each tile it visits as true, but recurs to false if no word was found through it
        marked[i][j] = true; // don't use this tile again down this path
        for (int m = i - 1; m <= i + 1 && m < M; m++) {
            if (m < 0) continue;
            for (int n = j - 1; n <= j + 1 && n < N; n++) {
                if (n < 0) continue;
                // don't search the path if no prefix corresponds with it or if we have already used its letter
                if (marked[m][n] || (current.next(boardChar[m][n]) == null)) continue;
                boolean eraseTwo = false;
                prefix.append(boardChar[m][n]);
                if (boardChar[m][n] == 'Q') {
                    prefix.append('U'); // we know that any Q must be followed by a U
                    eraseTwo = true;
                }
                dfs(marked, wordsFound, current.next(boardChar[m][n]), prefix, m, n);
                prefix.deleteCharAt(prefix.length() - 1);
                if (eraseTwo) prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        marked[i][j] = false; // unwind

        if (current.isFilled()) {
            wordsFound.add(prefix.toString());
        }

    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        boolean checkQU = false;
        int len = 0;
        for (int i = 0, l = word.length(); i < l; i++, len++) {
            if (word.charAt(i) == 'Q') {
                checkQU = true;
            }

        }
        if (checkQU) {
            if (!containsQU(word)) return 0;
        }
        else if (!st.contains(word)) return 0;
        if (len < 5) return 1;
        if (len < 6) return 2;
        if (len < 7) return 3;
        if (len < 8) return 5;
        return 11;
    }
    private boolean containsQU(String s) {
        // returns whether or not the st contains s by first removing Us that follow Qs
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = s.length(); i < n; i++) {
            sb.append(s.charAt(i));
            if (s.charAt(i) == 'Q') {
                if ((i == n - 1) || (s.charAt(i + 1) != 'U')) return false;
                i++;
            }
        }
        return st.contains(new String(sb));
    }

    public static void main(String[] args) {
        /*
        args = new String[2];
        args[0] = "dictionary-yawl.txt";
        args[1] = "board-points4540.txt";
         */
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        System.out.print(solver.st.contains("QAT"));
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);


    }
}
