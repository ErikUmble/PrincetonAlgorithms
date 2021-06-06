/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

//////////////////////////// using ancestor with iterable inputs and a cyclic graph can yield incorrect answers depending on the order
/////////////////////////// of the entries in the iterables
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class SAP {
    private final Digraph G;
    private int distance;
    //private int v1;
    //private int v2;
    private int currentAncestor;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G); // need to make a new copy so that this is immutable
        distance = Integer.MAX_VALUE;
        //v1 = -1;
        //v2 = -1;
        currentAncestor = -1;
    }

    private int ancestor_bfs(Digraph G, Queue<Integer> q, int[] marked, int[] distances) {
        // given a Digraph, a Queue with initial source vertices, and an array where each
        // queue item is marked with its set's id
        // performs bfs and returns the first vertex found by a path from one source set that
        // was marked by the other source set
        // returns -1 if no such vertex is found
        distance = Integer.MAX_VALUE; // reset distance
        currentAncestor = -1;
        while (!q.isEmpty()) {
            int p = q.dequeue();

            if (distances[p] > distance) continue; // end this search branch longer from its source than the shortest total path

            for (int r : G.adj(p)) {
                if (marked[r] == 0) { // unmarked
                    q.enqueue(r);
                    marked[r] = marked[p];
                    distances[r] = distances[p] + 1;
                }
                else if (marked[r] != marked[p]) {
                    // found a vertex that was visited from other source, which is the ancestor
                    if (distance > (distances[r] + distances[p] + 1)) {
                        distance = distances[r] + distances[p] + 1;
                        currentAncestor = r;

                    }

                }
            }
        }
        return currentAncestor;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        //if (!((v == v1 && w == v2) || (v == v2 && w == v2))) { // only recompute ancestor if not cached
         //   currentAncestor = ancestor(v, w);
        //}
        currentAncestor = ancestor(v,w);
        if (currentAncestor < 0) return -1;
        return distance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if ((v >= G.V()) || (w >= G.V()) || ((v < 0) || (w < 0))) throw new IllegalArgumentException();
        if (v == w) {
            // short cut if same vertex
            currentAncestor = v;
            distance = 0;
            return v;
        }
        int[] marked = new int[G.V()];
        int[] distances = new int[G.V()];
        Queue<Integer> q = new Queue<Integer>();

        q.enqueue(v);
        marked[v] = 1;
        q.enqueue(w);
        marked[w] = 2;

        ancestor_bfs(G, q, marked, distances);
        // temp (a way of checking if order in queue matters)
        // this same problem is in the other ancestor, but idk how to fix8
            int d = distance;
            int a = currentAncestor;
            q = new Queue<Integer>();
            marked = new int[G.V()];
            q.enqueue(w);
            marked[w] = 1;
            q.enqueue(v);
            marked[v] = 2;
            ancestor_bfs(G, q, marked, distances);
            if (d < distance) {
                distance = d;
                currentAncestor = a;
            }
        //v1 = v;
        //v2 = w;
        return currentAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if ((v == null) || (w == null)) throw new IllegalArgumentException();
        currentAncestor = ancestor(v, w);
        if (currentAncestor < 0) return -1;
        return distance;

    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if ((v == null) || (w == null)) throw new IllegalArgumentException();
        // check for out of bounds elements or same element from both sets
        int shortcut = -1;
        Iterator<Integer> vi = v.iterator();
        Iterator<Integer> wi = w.iterator();
        for (Integer i : v) {
            if (i == null) throw new IllegalArgumentException();
            for (Integer j : w) {
                if (j == null) throw new IllegalArgumentException();
                if ((i >= G.V()) || (j >= G.V()) || ((i < 0) || (j < 0)))
                    throw new IllegalArgumentException();
                if (i.equals(j)) {
                    shortcut = i;
                }
            }
        }
        if (shortcut >= 0) {
            // short cut if same vertex
            currentAncestor = shortcut;
            distance = 0;
            return currentAncestor;
        }
        int[] marked = new int[G.V()];
        int[] distances = new int[G.V()];
        Queue<Integer> q = new Queue<Integer>();

        // add initial sources to the queue, and mark them with their source set id
        for (int i: v) {
            q.enqueue(i);
            marked[i] = 1;
        }
        for (int i: w) {
            q.enqueue(i);
            marked[i] = 2;
        }
        return ancestor_bfs(G, q, marked, distances);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("digraph3.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
