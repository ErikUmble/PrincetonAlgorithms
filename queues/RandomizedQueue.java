/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] s;
    private int count;
    // construct an empty randomized queue
    public RandomizedQueue() {
        s = (Item[]) new Object[2];
        count = 0;

    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return count == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return count;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (count == s.length -1) s = resize(s.length * 2);
        s[count++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size() == 0) throw new NoSuchElementException();
        int index = StdRandom.uniform(count);
        Item rand_item = s[index];
        s[index] = s[count - 1];
        s[count-1] = null;
        count--;
        if (count < s.length/4 && s.length > 4) s = resize(s.length/2);
        return rand_item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size() == 0) throw new NoSuchElementException();
        return s[StdRandom.uniform(count)];
    }
    private Item[] resize(int capacity) {
        Item[] new_array = (Item[]) new Object[capacity];
        for (int i = 0; i < count; i++) {
            new_array[i] = s[i];
        }
        return new_array;
    }
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();

    }
    private class RandomIterator implements Iterator<Item> {
        private Item[] items;
        private int current = 0;
        private RandomIterator() {
             items = resize(count);
             StdRandom.shuffle(items);
        }
        public boolean hasNext() {
            return current < items.length;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return items[current++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> test = new RandomizedQueue<String>();
        test.enqueue("1");
        test.enqueue("2");
        test.enqueue("3");
        test.enqueue("4");
        StdOut.print(test.dequeue());
        StdOut.print(test.dequeue());
        test.enqueue("1");
        test.enqueue("2");
        test.enqueue("3");
        test.enqueue("4");test.enqueue("1");
        test.enqueue("2");
        test.enqueue("3");
        test.enqueue("4");
        StdOut.print(test.size());
        Iterator<String> testiter = test.iterator();
        while (testiter.hasNext()) {
            StdOut.print(testiter.next());
            StdOut.print(test.sample());
        }
        }
    }


