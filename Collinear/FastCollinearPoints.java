/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private LineSegment[] segs = new LineSegment[4];
    private Point[] current_linear_points = new Point[4];
    private int next_seg = 0;
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        int N = points.length;
        Point[] points1 = new Point[N]; //
        try {
            for (int i = 0; i < N; i++) {
                points1[i] = points[i];
                for (int j = 0; j < N; j++) { //
                    if (points[j].equals(points[i]) && j != i) throw new IllegalArgumentException();
                }
            }
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException();
        }



        Arrays.sort(points1,0,N,Comparator.naturalOrder()); //now the endpoints are maximal



        for (int i = 0; i < N; i++) {
            if (points1[i] == null) throw new IllegalArgumentException(); //already tested for null
            Point[] tempPoints = new Point[N-1];
            for (int j = 0; j < N-1; j++) { //copy over to temp
                tempPoints[j] = points1[j];
            }
            if (i < N-1) tempPoints[i] = points1[N-1];


            Arrays.sort(tempPoints,0,N-1,points1[i].slopeOrder());


            int tempN = tempPoints.length;
            for (int j = 0; j < tempN-2; j++) {
                double current_slope = points1[i].slopeTo(tempPoints[j]);
                //int last_with_that_slope = j;
                current_linear_points[0] = points1[i];
                int collinear_points = 2;
                current_linear_points[collinear_points-1] = tempPoints[j];
                if (points1[i].slopeTo(tempPoints[j+1]) == Double.NEGATIVE_INFINITY) throw new IllegalArgumentException(); //duplicate check
                while (j < tempN-1 && points1[i].slopeTo(tempPoints[j+1]) == current_slope) {
                    j++;
                    //last_with_that_slope = j;
                    collinear_points++;
                    if (collinear_points == current_linear_points.length) resize_current_pts(current_linear_points.length*2);
                    current_linear_points[collinear_points-1] = tempPoints[j];


                }

                if (collinear_points >= 4) {
                    Arrays.sort(current_linear_points,0,collinear_points,Comparator.naturalOrder());
                    if (points1[i].equals(current_linear_points[0])){

                        //the point is a minimal in its line
                        LineSegment this_seg = new LineSegment(current_linear_points[0],current_linear_points[collinear_points-1]);
                        if (next_seg == segs.length) resize(segs.length*2);
                        segs[next_seg++] = this_seg;
                    }


                }
            }
        }

    }
    public int numberOfSegments() {
        return next_seg;
    }
    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[next_seg];
        int i = 0;
        while (i < segs.length && segs[i] != null) {
            segments[i] = segs[i];
            i++;
        }
        return segments;
    }
    private void resize(int capacity) {
        LineSegment[] new_array = new LineSegment[capacity];
        for (int i = 0; i < next_seg; i++) {
            new_array[i] = segs[i];
        }
        segs = new_array;
    }
    private void resize_current_pts(int capacity) {
        Point[] new_array = new Point[capacity];
        for (int i = 0; i < current_linear_points.length; i++) {
            new_array[i] = current_linear_points[i];
        }
        current_linear_points = new_array;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}