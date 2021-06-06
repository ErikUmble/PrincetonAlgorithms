/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Iterator;

public class PointSET {
    private int size;
    private SET<Point2D> ps;


    // construct an empty set of points
    public PointSET() {
        size = 0;
        ps = new SET<Point2D>();
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
        if (ps.contains(p)) return;
        else {
            size++;
            ps.add(p);
        }
    }
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return ps.contains(p);
    }
    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);

        Iterator<Point2D> all_points = ps.iterator();
        while (all_points.hasNext()) {
            all_points.next().draw();

        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> points_in_range = new Stack<Point2D>();
        Iterator<Point2D> all_points = ps.iterator();
        while (all_points.hasNext()) {
            Point2D this_point = all_points.next();
            if (rect.contains(this_point)) {
                points_in_range.push(this_point);
            }
        }
        return points_in_range;
    }
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        Iterator<Point2D> all_points = ps.iterator();
        Point2D nearest = all_points.next();
        double least_distance = nearest.distanceSquaredTo(p);
        while (all_points.hasNext()) {
            Point2D this_point = all_points.next();
            if (this_point.distanceSquaredTo(p) < least_distance) {
                least_distance = this_point.distanceSquaredTo(p);
                nearest = this_point;
            }
        }
        return nearest;

    }


    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET test = new PointSET();
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                Point2D this_point = new Point2D(.1*j, 0.5);
                test.insert(this_point);
                System.out.println(test.size());
            }
        }
        test.draw();

    }
}