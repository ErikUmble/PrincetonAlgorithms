/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int size;

    private class Node {
        Item item;
        Node next; //pointers go from first to last
        Node previous;
    }
    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {return size == 0;}

    // return the number of items on the deque
    public int size() {return size;}

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw  new IllegalArgumentException();
        Node new_first = new Node();
        new_first.item = item;
        if (first == null) { // this is the first entry
            first = new_first;
            first.next = null;
            first.previous = null;
            last = first;
        } else { //not the first entry
            new_first.next = first;
            new_first.previous = null;
            first.previous = new_first;
            first = new_first;
        }
        size++;

    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw  new IllegalArgumentException();
        Node new_last = new Node();
        new_last.item = item;
        if (last == null) { // first addition
            last = new_last;
            last.next = null;
            last.previous = null;
            first = last;
        } else { //not the first
            new_last.previous = last;
            new_last.next = null;
            last.next = new_last;
            last = new_last;
        }
        size++;

    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size == 0) throw new NoSuchElementException();
        Node first_item = first;
        first = first_item.next;


        if (--size == 0) last = null;
        else first.previous = null;
        return first_item.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size == 0) throw new NoSuchElementException();
        Node last_item = last;
        last = last_item.previous;
        size--;
        if (last != null) last.next = null;
        else { //removed the last item
            first = null;
        }

        return last_item.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new FrontToBackIterator();
    }
    private class FrontToBackIterator implements Iterator<Item> {
        Node current = first;
        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (hasNext()) {
                Item current_item = current.item;
                current = current.next;
                return current_item;
            } else throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args){
        Deque<String> test = new Deque<String>();
        test.addFirst("1");
        test.addLast("2");
        test.addFirst("3");
        test.addLast("4");
        Iterator<String> testiter = test.iterator();
        while (testiter.hasNext()) {
            StdOut.print(testiter.next());
        }
        test.addFirst("1");
        test.addLast("2");
        StdOut.print(test.removeFirst());
        StdOut.print(test.removeLast());
        StdOut.print(test.size());

    }

}
