/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class CircularSuffixArray {
    private static final int R = 256; // radix size
    private int n;
    private int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        n = s.length();
        index = new int[n];

        // initialize index with unsorted, circular order
        for (int i = 0; i < n; i++) {
            index[i] = i;
        }
        // shortcut if unary string
        boolean unary = true;
        for (int i = 0; i < n - 1; i++) {
            unary = unary && (s.charAt(i) == s.charAt(i + 1));
            if (!unary) break;
        }
        // avoid sorting if string is unary
        if (!unary) CircularSuffixMSD.sort(index, s, R);
    }

    // length of s
    public int length() { return n; }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException();
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            System.out.println(csa.index(i));
        }
        System.out.println(csa.index(csa.length() - 1));
    }

}
