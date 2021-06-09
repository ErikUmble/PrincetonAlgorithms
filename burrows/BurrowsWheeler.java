/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);

        // output the sorted position of the original string
        for (int i = 0, n = csa.length(); i < n; i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        // output the last column of the sorted circular suffix array
        for (int i = 0, n = csa.length(); i < n; i++) {
            BinaryStdOut.write(s.charAt((csa.index(i) + n - 1) % n));
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int n = s.length();
        char[] t = s.toCharArray();
        char[] firstCol = new char[n];
        int[] next = new int[n];
        int[] count = new int[R + 1];
        // sort t into firstCol by key-counted indexing
        // count frequencies of occurance for each character in t
        for (int i = 0; i < n; i++) {
            count[t[i] + 1]++;
        }
        for (int i = 2; i <= R; i++) {
            count[i] += count[i-1];
        }
        for (int i = 0; i < n; i++) {
            firstCol[count[t[i]]] = t[i]; // use index info to put t[i] into firstCol in sorted order
            next[count[t[i]]] = i; // use the same info to remember which index rotates once to get here
            count[t[i]]++;
        }
        int current = first;
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(firstCol[current]);
            current = next[current];
        }
        BinaryStdOut.flush();

    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
    }

}
