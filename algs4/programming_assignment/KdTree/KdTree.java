/****************************************************************
KdTree implementation for geometric data processing
****************************************************************/

import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class KdTree{
    private Node root;
    private int size;
    private static final RectHV UNIT=new RectHV(0,0,1,1);

    private static class Node{
        private final Point2D point;
        private RectHV rect;
        private Node lb;//the reference to the left/bottom node
        private Node rt;//the reference to the right/top node
        private final int level;

        public Node(Point2D p, RectHV rect, int level){
            this.point=p;
            this.rect=rect;
            this.level=level;
        }

        public Node(Point2D p, Node parent){
            this.point=p;
            this.level=parent.level+1;
            if(parent.level%2==0){
                if(p.x()<parent.point.x()) this.rect=new RectHV(parent.rect.xmin(),parent.rect.ymin(),parent.point.x(),parent.rect.ymax());
                else this.rect=new RectHV(parent.point.x(),parent.rect.ymin(),parent.rect.xmax(),parent.rect.ymax());
            }else{
                if(p.y()<parent.point.y()) this.rect=new RectHV(parent.rect.xmin(),parent.rect.ymin(),parent.rect.xmax(),parent.point.y());
                else this.rect=new RectHV(parent.rect.xmin(),parent.point.y(),parent.rect.xmax(),parent.rect.ymax()); 
            }
        }
    }

    public KdTree(){}

    public boolean isEmpty(){
        return size==0;
    }

    public int size(){
        return size;
    }

    public void insert(Point2D p){
        if(p==null) throw new IllegalArgumentException("calls insert() with null point");
        if(contains(p)) return;
        if(isEmpty()) {
            root=new Node(p,UNIT,0);
            size++;
            return;
        }
        Node node=root;
        while(true){
            int cmp=compare(p,node);
            if(cmp<0) {
                if(node.lb==null){
                    node.lb=new Node(p,node);
                    size++;
                    return;
                }else node=node.lb;
            }else{
                if(node.rt==null){
                    node.rt=new Node(p,node);
                    size++;
                    return;
                }else node=node.rt;
            }
        }
    }

    public boolean contains(Point2D p){
        Node node=root;
        while(node!=null){
            int cmp=compare(p,node);
            if(cmp<0) node=node.lb;
            else if(cmp>0) node=node.rt;
            else {
                if(node.level%2==0&&node.point.y()==p.y()){
                    return true;
                }
                if(node.level%2!=0&&node.point.x()==p.x()){
                    return true;
                }
                node=node.rt;
            }
        }
        return false;
    }

    public Iterable<Point2D> range(RectHV rect){
        if(rect==null) throw new IllegalArgumentException("calls range() with null rectanlge");
        Queue<Point2D> q=new Queue<Point2D>();
        range(root,rect,q);
        return q;
    }

    private void range(Node node, RectHV rect,Queue<Point2D> q){
        if(node==null) return;
        if(node.lb!=null&&rect.intersects(node.lb.rect)) range(node.lb,rect,q);
        if(rect.contains(node.point)) q.enqueue(node.point);
        if(node.rt!=null && rect.intersects(node.rt.rect)) range(node.rt,rect,q);
    }

    public Point2D nearest(Point2D p){
        if(isEmpty()) return null;
        if(p==null) throw new IllegalArgumentException("calls nearest() with null point");
        Point2D minp=root.point;
        nearest(root,p,minp);
        return minp;
    }

    private void nearest(Node node, Point2D p,Point2D minp){
        if(node==null) return;
        double dis=node.point.distanceSquaredTo(p);
        double min=p.distanceSquaredTo(minp);
        if(dis<min) {
            minp=node.point;
        }
        int cmp=compare(p,node);
        if(cmp<0){
            if(node.lb!=null && node.lb.rect.distanceSquaredTo(p)<p.distanceSquaredTo(minp)) nearest(node.lb,p,minp);
            if(node.rt!=null && node.rt.rect.distanceSquaredTo(p)<p.distanceSquaredTo(minp)) nearest(node.rt,p,minp);
        }else{
            if(node.rt!=null && node.rt.rect.distanceSquaredTo(p)<p.distanceSquaredTo(minp)) nearest(node.rt,p,minp);
            if(node.lb!=null && node.lb.rect.distanceSquaredTo(p)<p.distanceSquaredTo(minp)) nearest(node.lb,p,minp);
        }
    }

    public void  draw(){
        UNIT.draw();
        if(isEmpty()) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        root.point.draw();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius();
        StdDraw.line(root.point.x(),UNIT.ymin(),root.point.x(),UNIT.ymax());
        draw(root.lb,root);
        draw(root.rt,root);
    }

    private void draw(Node node,Node parent){
        if(node==null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();
        if(parent.level%2==0){
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            if(node.point.x()<parent.point.x()) StdDraw.line(parent.rect.xmin(),node.point.y(),parent.point.x(),node.point.y());
            else StdDraw.line(parent.point.x(),node.point.y(),parent.rect.xmax(),node.point.y());
        }else{
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            if(node.point.y()<parent.point.y()) StdDraw.line(node.point.x(),parent.rect.ymin(),node.point.x(),parent.point.y());
            else StdDraw.line(node.point.x(),parent.point.y(),node.point.x(),parent.rect.ymax());
        }
        draw(node.lb,node);
        draw(node.rt,node); 
    }

    private int compare(Point2D p, Node node){
        double cmp1,cmp2;
        if(node.level%2==0){
            cmp1=p.x();
            cmp2=node.point.x();
        }else{
            cmp1=p.y();
            cmp2=node.point.y();
        }
        if(cmp1<cmp2) return -1;
        else if(cmp1>cmp2) return 1;
        else return 0;
    }

    public static void main(String[] args) {

        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        StdOut.println(kdtree.size());
        kdtree.draw();
        StdDraw.show();
    }
}
