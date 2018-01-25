/******************************************************
*Brute-force inplementation using a Red black tree
******************************************************/

import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class PointSET{
    private SET<Point2D> pset;//the BST stores Points

    //the constructor
    public PointSET(){
        pset=new SET<Point2D>();
    }

    //is empty?
    public boolean isEmpty(){
        return pset.isEmpty();
    }

    //return the size of the BST
    public int size(){
        return pset.size();
    }

    //inset a point into the BST
    public void insert(Point2D p){
        if(p==null) throw new IllegalArgumentException("calls insert() with null point.");
        pset.add(p);
    }

    public boolean contains(Point2D p){
        if(p==null) throw new IllegalArgumentException("calls insert() with null point.");
        return pset.contains(p);  
    }

    //draw the points to the StdDraw
    public void draw(){
        for (Point2D p:pset){
            p.draw();
        }
    }

    // find all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect){
        if (rect==null) throw new IllegalArgumentException("calls range() with null rectangle");
        Queue<Point2D> q=new Queue<Point2D>();
        for (Point2D p:pset){
            if (rect.contains(p)) q.enqueue(p);
        }
        return q;
    }

    //return the nearst neighbor Point 
    public Point2D nearest(Point2D p){
        if (p==null) throw new IllegalArgumentException("calls nearest() with null point");
        if (pset.contains(p)) return p;
        double min=Double.POSITIVE_INFINITY;
        Point2D minpoint=null;
        for (Point2D point:pset){
            double dis=p.distanceSquaredTo(point);
            if (dis<min) {
                min=dis;
                minpoint=point;
            }
        }
        return minpoint;
    }
}