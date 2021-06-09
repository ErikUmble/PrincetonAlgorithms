/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        int[] index = new int[R];
        // initialize index array for each character in radix
        for (int i = 0; i < R; i++) {
            index[i] = i;
        }
        int maxIndex = 0; // keep track of the upper bound for indices that might need to be updated

        // encode each character from input
        while (!BinaryStdIn.isEmpty()) {
            int c = BinaryStdIn.readChar();
            BinaryStdOut.write((char) index[c]);
            maxIndex = Math.max(c, maxIndex);

            // for each index that might have been in front of the current,
            // if it was, its place gets move by one to the right
            // but cutoff when we have changed as many indices as could possibly be in
            // front of c
            for (int j = 0, changed = 0; j <= maxIndex && changed < index[c]; j++) {
                if (index[j] < index[c]) {
                    index[j]++;
                    changed++;
                }
            }
            // set this character to the first in the order
            index[c] = 0;
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] chars = new char[R];
        // initialize index array for each character in radix
        for (char i = 0; i < R; i++) {
            chars[i] = i;
        }
        // encode each character from input
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            BinaryStdOut.write(chars[index]);

            // for each char in front of this one, move it to the right
            for (int j = index; j > 0; j--) {
                exch(chars, j, j-1);
            }
        }
        BinaryStdOut.flush();
    }
    private static void exch(char[] a, int i, int j) {
        char temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
    }

}