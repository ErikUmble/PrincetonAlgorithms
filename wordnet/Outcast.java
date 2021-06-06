/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;

public class Outcast {
    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int[] distances = new int[nouns.length];
        for (int n = 0; n < nouns.length; n++) {
            for (String m : nouns) {
                distances[n] += wordnet.distance(nouns[n], m);
            }
        }

        int largest = 0;
        for ( int i = 1; i < distances.length; i++ )
        {
            if ( distances[i] > distances[largest] ) largest = i;
        }
        return nouns[largest];
    }
    // see test client below
    public static void main(String[] args)   {
        WordNet tester = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast test = new Outcast(tester);
        In in = new In("outcast29.txt");
        String[] nouns = in.readAllLines();
        System.out.println(test.outcast(nouns));
    }
}
