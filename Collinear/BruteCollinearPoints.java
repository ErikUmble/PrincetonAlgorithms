/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segs;
    private int next_seg = 0;
    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points
        FastCollinearPoints fun = new FastCollinearPoints(points);
        segs = fun.segments();
        next_seg = fun.numberOfSegments();
        /*segs =  new LineSegment[4];
        int N = points.length;
        //check for validness
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < N; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
        }
        //find segments
        for (int i = 0; i <= N-4; i++) { //first point

            for (int j = i + 1; j <= N-3; j++) { // second point
                Double s1 = points[i].slopeTo(points[j]);
                if (s1 == -Double.MAX_VALUE) throw new IllegalArgumentException(); //make sure not same point

                for (int k = j + 1; k <= N - 2; k++) { //third point
                    if (s1 == points[i].slopeTo(points[k])) { //only check 4th point if three are collinear

                        for (int l = k + 1; k <= N - 1; k++) { //fourth point
                            if (s1 == points[i].slopeTo(points[l])) {
                                Point[] points_in_the_line = {points[i],points[j],points[k],points[l]};
                                Arrays.sort(points_in_the_line);

                                LineSegment this_seg = new LineSegment(points_in_the_line[0], points_in_the_line[3]);
                                boolean unique = true;
                                for (int m = 0; m < next_seg; m++) {
                                    if (segs[m].toString().equals(this_seg.toString())) unique = false;
                                }
                                if (unique) {
                                    if (next_seg == segs.length -1) resize(segs.length * 2);
                                    segs[next_seg++] = this_seg;
                                }


                            } else if (points[i].slopeTo(points[l]) == -Double.MAX_VALUE) throw new IllegalArgumentException();

                        }
                    }

                }
            }
        }*/
    }
    private void resize(int capacity) {
        LineSegment[] new_array = new LineSegment[capacity];
        for (int i = 0; i < next_seg; i++) {
            new_array[i] = segs[i];
        }
        segs = new_array;
    }
    public int numberOfSegments() { // the number of line segments
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

    public static void main(String[] args) {
        Point[] test = {new Point(10000, 0),null,new Point(7000, 3000),new Point(20000, 21000),
                        new Point(3000, 4000),new Point(14000, 15000),new Point(6000, 7000),new Point(0, 1000)};
        FastCollinearPoints tester = new FastCollinearPoints(test);
        System.out.println(Arrays.toString(tester.segments()));
    }


}
