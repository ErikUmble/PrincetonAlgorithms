/* *****************************************************************************
 *  Name: Erik
 *  Date: May 2021
 *  Description: WordNet assignment for Algorithms II
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.HashSet;

public class WordNet {
    private final HashMap<String, HashSet<Integer>> synsetMap;
    private final HashMap<Integer, String> idMap;
    private Digraph digraph;
    private final SAP sap;
    private int numIDs;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if ((synsets == null) || (hypernyms == null)) throw new IllegalArgumentException();
        synsetMap = new HashMap<String, HashSet<Integer>>();
        idMap = new HashMap<Integer, String>();
        numIDs = 0;
        HashSet<Integer> synsetIDs = new HashSet<Integer>(); // keep track of ids that have outgoing links
        loadSynsetMap(synsets);
        int nonRoots =loadHypernymsGraph(hypernyms, synsetIDs);//

        if ((nonRoots + 1) != numIDs) throw new IllegalArgumentException("multiple roots");

        // check to make sure graph is acyclic
        DirectedCycle dcycle = new DirectedCycle(digraph);
        if (dcycle.hasCycle()) throw new IllegalArgumentException("graph has a directed cycle");
        sap = new SAP(digraph);

    }
    private int loadHypernymsGraph(String hypernyms, HashSet<Integer> synsetIDs) {
        // creates and builds the directed graph
        // keeps track of the synsetIDs that have outgoing links and returns the number of them that do
        In input = new In(hypernyms);
        digraph = new Digraph(numIDs);
        while (input.hasNextLine()) {
            String line = input.readLine();
            String[] fields = line.split(",");
            int v = Integer.parseInt(fields[0]);

            if (fields.length > 1) synsetIDs.add(v);

            for (int i=1; i < fields.length; i++) {
                digraph.addEdge(v, Integer.parseInt(fields[i]));
            }
        }
        return synsetIDs.size();
    }
    private boolean loadSynsetMap(String synsets) {
        In input = new In(synsets);
        while (input.hasNextLine()) {
            numIDs++;
            String line = input.readLine();
            String[] fields = line.split(",");
            idMap.put(Integer.parseInt(fields[0]), fields[1]);
            for (String word : fields[1].split(" ")) {
                if (synsetMap.containsKey(word)) {
                    synsetMap.get(word).add(Integer.parseInt(fields[0]));
                }
                else {
                    HashSet<Integer> set = new HashSet<Integer>();
                    set.add(Integer.parseInt(fields[0]));
                    synsetMap.put(word, set);
                }
            }
        }
        return true;
    }
    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return synsetMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if ((nounA == null) || (nounB == null)) throw new IllegalArgumentException();
        return sap.length(synsetMap.get(nounA), synsetMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if ((nounA == null) || (nounB == null)) throw new IllegalArgumentException();
        int ancestorID = sap.ancestor(synsetMap.get(nounA), synsetMap.get(nounB));
        return idMap.get(ancestorID);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet test = new WordNet("synsets100-subgraph.txt", "hypernyms100-subgraph.txt");
        System.out.print(test.nouns());
    }

}
