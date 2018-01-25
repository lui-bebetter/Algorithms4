/***********************************************************
*Fast collinear points algorithm based on sorting algorithm

*  Think of p as the origin.
*  For each other point q, determine the slope it makes with p.
*  Sort the points according to the slopes they makes with p.
*  Check if any 3 (or more) adjacent points in the sorted order 
   have equal slopes with respect to p. If so, these points, 
   together with p, are collinear.

**************************************************************/
import java.util.Arrays;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Comparator;


public class FastCollinearPoints{
    //the stack of line segment containing 4 or more points(may have duplicates)
    private LineSegment[] lineSegmentArray;
    private final Point [] points;

    
    //finds all line segments containing 4 or more points
    public FastCollinearPoints(Point [] inpoints){
        if(!validate(inpoints)) throw new IllegalArgumentException("inpoints should not be null");
        points=Arrays.copyOf(inpoints,inpoints.length);

        //sort the array in a natural order
        Arrays.sort(points);

        //find the two end points of one segment to a queue
        LinkedQueue<EndPoints> endPointsList=findEndPoints(points);

        int n=endPointsList.size();
        EndPoints [] endPointsArray=new EndPoints[n];
        for (int i=0;i<n;i++){
            endPointsArray[i]=endPointsList.dequeue();
        }

        //sort the endPointsArray use EndPoints' natural order
        Arrays.sort(endPointsArray);
        // a queue to store the line segment 
        LinkedQueue<LineSegment> lineList=new LinkedQueue<LineSegment> ();
        for (int i=0;i<n;i++){
            lineList.enqueue(endPointsArray[i].toLineSegment());
            int sub=0;//number of subsegments
            while(i+1+sub<n && endPointsArray[i+sub].compareTo(endPointsArray[i+1+sub])==0) sub++;
            i+=sub; 
        }

       toArray(lineList);//copy to linesegment array
    }

    // return the number of line segments
    public int numberOfSegments(){
        return lineSegmentArray.length;
    }

    public LineSegment[] segments(){
        
        return lineSegmentArray;
    }

    //find the two end points of one segment
    private LinkedQueue<EndPoints> findEndPoints(Point[] points){
        LinkedQueue<EndPoints> endPointsList=new LinkedQueue<EndPoints>();
        int n=points.length;
        double [] slope =new double[n]; 
        for (int i=0;i<n;i++){
            /********************
            *for each point in points, sort the clone  based on the slope

            *do not sort the points itself ,this way can't ensure the natrual 
             order will be resume after slope order sorting
            ********************/
            Point [] pointsBySlope=points.clone();
            Arrays.sort(pointsBySlope, i+1, n, points[i].slopeOrder());
            for (int k=i+1;k<n;k++){
                if ((slope[k]=points[i].slopeTo(pointsBySlope[k]))==Double.NEGATIVE_INFINITY)
                    throw new IllegalArgumentException("points should not duplicate.");
            }
            for (int k=i+1;k<n-2;k++){
                if (slope[k]!=slope[k+1]) continue;
                else if (slope[k+1]!=slope[k+2]){
                    k++;
                    continue;
                } else{
                    int more=0;//number of subsegments
                    while(k+more+3<n&&slope[k+2+more]==slope[k+3+more]){
                        more++;
                    }

                    //2-dim array stores the end points of the line segments
                    endPointsList.enqueue(new EndPoints(points[i],pointsBySlope[k+2+more]));
                    k+=more+2;
                }
            }
        }
        return endPointsList;
    }

    /************************************************************************
    *the private EndPoints class stores the two end points of the line segment, 
     which implement the comparable interface, return 0 if and only if the two 
     EndPoints belong to the same lineSegment. 

    *************************************************************************/
    private class EndPoints implements Comparable<EndPoints>{
        private  final Point p; //one end point
        private final Point q;//other end point

        public EndPoints(Point p, Point q){
            this.p=p;
            this.q=q;
        }

        //return 0 if and only if the two EndPoints belong to the same lineSegment
        public int compareTo(EndPoints that){
            if(this.slope()>that.slope()) return 1;
            else if(this.slope()<that.slope()) return -1;
            return this.q.compareTo(that.q);
        } 

        //transfer endpoints to line segment
        public LineSegment toLineSegment(){
            return new LineSegment(p,q);
        }

        //return the slope
        private double slope(){
            return p.slopeTo(q);
        }
    }

    //transfer linelist to linearrray
    private void toArray(LinkedQueue<LineSegment> lineList){
        int n=lineList.size();
        lineSegmentArray=new LineSegment[n];
        for (int i=0;i<n;i++)
            lineSegmentArray[i]=lineList.dequeue();
    }

    //check the input points 
    private boolean validate(Point [] points){
        if (points==null) return false;
        for (int i=0;i<points.length;i++){
            if (points[i]==null) return false;
        }
        return true;
    }

     //test client
    public static void main(String [] args){
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