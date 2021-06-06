/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class TRIESET {
    /*
    TrieSet with radix 26 (handles capital letters and shifts them by 65)
     */
    private static final int SHIFT = 65;
    private static final int R = 26;
    private Node root;


    public class Node {
        private boolean filled;
        private Node[] links = new Node[R];

        public Node next(char c) {
            return links[c - SHIFT];
        }

        public boolean isFilled() {
            return filled;
        }
    }

    // constructs a TRIE of radix size R
    public TRIESET() {
        this.root = new Node();
    }

    public void add(String key) { root = add(root, key, 0); }

    private Node add(Node current, String key, int d) {
        if (current == null) current = new Node(); // if the previous Node sent us down a null link
        if (d == key.length()) { // check to see if we have reached the Node associated with the key
            current.filled = true;
        }
            // otherwise, go down the next link (whether it exists or will be created) and save it as current link in case it gets created
        else {
            int index = key.charAt(d);
            index -= SHIFT; // prepare index to fit in radix size
            current.links[index] = add(current.links[index], key, d + 1);
        }
        return current; // pass this Node's reference back to get saved by its parent
    }

    public boolean contains(String key) {
        Node node = get(root, key, 0);
        if (node == null) return false;
        return node.filled;

    }
    public Node getRoot() {
        return root;
    }

    public Node get(Node current, String key, int d) {
        if (current == null) return null; // null link means the key does not exist in the TRIE
        if (d == key.length()) return current; // found the Node associated with the key
        return get(current.links[key.charAt(d) - SHIFT], key, d+1); // recursively search down the link
    }
}
