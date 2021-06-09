/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description: MSD, but applied to sorting implicitly defined circular suffixes
 **************************************************************************** */

public class CircularSuffixMSD {
    private static int R;
    private static String s;
    private static int n;
    private static final int CUTOFF        =  15;   // cutoff to insertion sort

    public static void sort(int[] index, String s, int R) {
        // sorts index, based on the circular rotations of s of radix size R
        CircularSuffixMSD.R = R;
        CircularSuffixMSD.s = s;
        n = index.length;
        int[] aux = new int[n];
        sort(index, 0, n-1, 0, aux);
    }

    // return dth character of the circular suffix of shift shift,
    private static int charAt(int shift, int d) {
        assert d >= 0 && d <= s.length();
        return s.charAt((shift + d) % n);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private static void sort(int[] a, int lo, int hi, int d, int[] aux) {

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        // compute frequency counts
        int[] count = new int[R+2];
        for (int i = lo; i <= hi; i++) {
            int c = charAt(a[i], d);
            count[c+2]++;
        }

        // transform counts to indicies
        for (int r = 0; r < R+1; r++)
            count[r+1] += count[r];

        // distribute
        for (int i = lo; i <= hi; i++) {
            int c = charAt(a[i], d);
            aux[count[c+1]++] = a[i];
        }

        // copy back
        for (int i = lo; i <= hi; i++)
            a[i] = aux[i - lo];


        // recursively sort for each character (excludes sentinel -1)
        for (int r = 0; r < R; r++)
            sort(a, lo + count[r], lo + count[r+1] - 1, d+1, aux);
    }


    // insertion sort a[lo..hi], starting at dth character
    private static void insertion(int[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1], d); j--)
                exch(a, j, j-1);
    }

    // exchange a[i] and a[j]
    private static void exch(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // is v less than w, starting at character d
    private static boolean less(int v, int w, int d) {
        // assert v.substring(0, d).equals(w.substring(0, d));
        for (int i = d; i < s.length(); i++) {
            if (charAt(v, i) < charAt(w, i)) return true;
            if (charAt(v, i) > charAt(w, i)) return false;
        }
        return false;
    }
    public static void main(String[] args) {

    }
}
