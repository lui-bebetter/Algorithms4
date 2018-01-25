/******************************************************************

*  examines 4 points at a time and checks whether they all lie on 
 the same line segment,returning all such line segments. 

*  To check whether the 4 points p, q, r, and s are collinear, 
 check whether the three slopes between p and q, between p and r,
 and between p and s are all equal.
 
 *****************************************************************/
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints{
    private  LineSegment[] lineSegmentArray;
    
    private final Point [] points;


    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + java.util.Arrays.hashCode(lineSegmentArray);
        result = 31 * result + java.util.Arrays.hashCode(points);
        return result;
    }

    // finds all line segments containing 4 points
    public  BruteCollinearPoints(Point [] inpoints){
        if(!validate(inpoints)) throw new IllegalArgumentException("points should not be null");
        int n=inpoints.length;
        points=Arrays.copyOf(inpoints,n);
        //a list of the lineSegment containing 4 points
        LinkedQueue<LineSegment> lineList=new LinkedQueue<LineSegment>();

        //sort the array in natural order
        Arrays.sort(points);

        for(int i=0;i<points.length-1;i++){
            if(points[i].compareTo(points[i+1])==0)
                throw new IllegalArgumentException("point in points shouldn't duplicate.");
        }
        
        //find the 4 points linesegment through the points  
        for (int i=0;i<n;i++)
            for (int j=i+1;j<n;j++)
                for (int k=j+1;k<n;k++)
                    for ( int l=k+1;l<n;l++ ){
                        Point p=points[i],q=points[j],r=points[k],s=points[l];
                        
                        double qslope=p.slopeTo(q);
                        double rslope=q.slopeTo(r);
                        double sslope=r.slopeTo(s);
                        if (qslope==rslope && qslope==sslope)
                            lineList.enqueue(new LineSegment(p,s));
                    }
        toArray(lineList);//copy to linesegment array
    }

    //return the number of line segment in lineList
    public int numberOfSegments(){
        return lineSegmentArray.length;
    }

    //return an array of the lineList
    public LineSegment[] segments(){     
        return lineSegmentArray;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
 }