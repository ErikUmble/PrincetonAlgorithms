/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Iterator;

public class KdTree {
    private int size;
    private Node root;
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;

    // construct an empty set of points
    public KdTree() {
        size = 0;
    }
    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }
    // number of points in the set
    public int size() {
        return size;
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!contains(p)) {

            root = put(root, p, root);
            size++;
        }
    }
    private Node put(Node x, Point2D p, Node parent) {
        if (isEmpty()) { // first node
            Node root_node = new Node(p, HORIZONTAL);
            root_node.rect = new RectHV(0,0,1,1);
            return root_node;
        }
        if (x == null) {
            Node new_node = new Node(p, parent.orientation); //end of a line
            if (parent.orientation == VERTICAL && compareX(p, parent.p) >= 0) { //right side of a vertical line
                new_node.rect = new RectHV(parent.p.x(),parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
            }
            else if (parent.orientation == VERTICAL) { //left side of a vertical line
                new_node.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(),parent.p.x(),parent.rect.ymax());
            }
            else if (compareY(p, parent.p) >= 0) { //above a horizontal line
                new_node.rect = new RectHV(parent.rect.xmin(), parent.p.y(),parent.rect.xmax(),parent.rect.ymax());
            }
            else { //below a horizontal line
                new_node.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(),parent.rect.xmax(),parent.p.y());
            }
            return new_node;
        }
        int comparison;
        if (x.orientation == VERTICAL) comparison = compareX(p, x.p);
        else comparison = compareY(p, x.p);

        if (comparison < 0) x.lb = put(x.lb, p, x);
        else x.rt = put(x.rt, p, x); //default to greater
        return x;


    }
    private int compareX(Point2D a, Point2D b) {
        if (a.x() > b.x()) return 1;
        else if (a.x() < b.x()) return -1;
        else return 0;
    }
    private int compareY(Point2D a, Point2D b) {
        if (a.y() > b.y()) return 1;
        else if (a.y() < b.y()) return -1;
        else return 0;

    }
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(root, p) != null;
    }
    private Node get(Node x, Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (x == null) return null;
        int comparison;
        if (x.orientation == VERTICAL) comparison = compareX(p, x.p);
        else comparison = compareY(p, x.p);

        if (comparison < 0) return get(x.lb, p);
        else if (x.p.equals(p)) return x; //
        else return get(x.rt, p); //default to right
    }
    // draw all points to standard draw
    public void draw() {
        Iterator<Node> all_nodes = allNodes().iterator();
        while (all_nodes.hasNext()) {
            Node this_node = all_nodes.next();
            if (this_node.orientation == VERTICAL) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(this_node.p.x(), this_node.rect.ymin(), this_node.p.x(), this_node.rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(this_node.rect.xmin(), this_node.p.y(), this_node.rect.xmax(), this_node.p.y());
            }
        }
    }
    private Iterable<Node> allNodes() {
        Queue<Node> queue = new Queue<Node>();
        searchSubtree(root, queue);
        return queue;
    }
    private Node searchSubtree(Node x, Queue<Node> queue) {
        if (x == null) return null;
        queue.enqueue(x);

        searchSubtree(x.rt, queue);
        searchSubtree(x.lb, queue);
        return x;
    }
    /*private boolean checkOrientations() {
        Iterator<Node> all_nodes = allNodes().iterator();
        while (all_nodes.hasNext()) {
            Node this_node = all_nodes.next();
            if (this_node.lb != null) {
                if (this_node.lb.orientation == this_node.orientation) return false;
            }
            if (this_node.rt != null) {
                if (this_node.rt.orientation == this_node.orientation) return false;
            }
        }
        return true;
    }*/
    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> points_in_range = new Queue<Point2D>();
        rangeSearch(root, rect, points_in_range);
        return points_in_range;
    }
    private Node rangeSearch(Node x, RectHV rect, Queue<Point2D> queue) {
        if (x == null) return null;
        if (rect.contains(x.p)) queue.enqueue(x.p);
        if (x.rt != null) {
            if (rect.intersects(x.rt.rect)) rangeSearch(x.rt, rect, queue);
        }
        if (x.lb != null) {
            if (rect.intersects(x.lb.rect)) rangeSearch(x.lb, rect, queue);
        }
        return x;
    }
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) return null;
        Point2D closest = nearestSearch(root, p, root.p);
        return closest;
    }
    private Point2D nearestSearch(Node x, Point2D p, Point2D current_closest) {
        if (x == null) return null;
        if (x.p.distanceSquaredTo(p) < current_closest.distanceSquaredTo(p)) current_closest = x.p;

        boolean right_has = false;
        boolean right_checked = true;
        boolean left_has = false;
        boolean left_checked = true;
        if (x.lb != null) left_has = true;
        if (x.rt != null) right_has = true;

        if (right_has && x.orientation == VERTICAL && compareX(p, x.p) >= 0) current_closest = nearestSearch(x.rt, p, current_closest);
        else if (right_has && x.orientation == HORIZONTAL && compareY(p, x.p) >= 0) current_closest = nearestSearch(x.rt, p, current_closest);
        else right_checked = false;


        if (left_has && x.orientation == VERTICAL && compareX(p, x.p) < 0) current_closest = nearestSearch(x.lb, p, current_closest);
        else if (left_has && x.orientation == HORIZONTAL && compareY(p, x.p) < 0) current_closest = nearestSearch(x.lb, p, current_closest);
        // check the other side of the boundary
        else left_checked = false;

        //check on the other side of the boundary
        if (!right_checked && right_has && current_closest.distanceSquaredTo(p) > x.rt.rect.distanceSquaredTo(p)) current_closest = nearestSearch(x.rt, p, current_closest);
        if (!left_checked && left_has && current_closest.distanceSquaredTo(p) > x.lb.rect.distanceSquaredTo(p)) current_closest = nearestSearch(x.lb, p, current_closest);

        return current_closest;
    }
    private static class Node {
        private Point2D p;
        private Node lb;
        private Node rt;
        private RectHV rect;
        private boolean orientation;

        private Node(Point2D point, boolean parent_orientation) {
            this.p = point;
            this.orientation = !parent_orientation;
            this.lb = null;
            this.rt = null;

        }


    }
    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree test = new KdTree();
        test.insert(new Point2D(0.5,0.5));
        test.insert(new Point2D(0.5,0.4));
        test.insert(new Point2D(0.3,0.3));
        test.insert(new Point2D(0.2,0.2));
        test.insert(new Point2D(0.1,0.1));
        System.out.println(test.size());
        Iterator<Node> all_points = test.allNodes().iterator();
        while (all_points.hasNext()) {
            System.out.println(all_points.next().p.toString());
        }
        test.draw();
        //System.out.println(test.checkOrientations());
    }
}
