/**********************************************************************
 *FileName: PrimMST
 *Author:   luibebetter
 *Date:     2018/2/311:22
 *Description:The PrimMST class represents a data type for computing
 a minimum spanning tree in an edge-weighted graph. The edge weights can
 be positive, zero, or negative and need not be distinct. If the graph
 is not connected, it computes a minimum spanning forest, which is the
 union of minimum spanning trees in each connected component.
 The weight() method returns the weight of a minimum spanning tree and
 the edges() method returns its edges.
 **********************************************************************/

package algs4.EdgeWeightedGraph;

import algs4.PriorityQueue.IndexMinPQ;
import algs4.queue.LinkedQueue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/***********************************************************************
 *@author: luibebetter
 *@create: 2018/2/3
 *Description:This implementation uses Prim's algorithm with an indexed
  binary heap. The constructor takes time proportional to E log V and
  extra space (not including the graph) proportional to V, where V is
  the number of vertices and E is the number of edges. Afterwards,
  the weight() method takes constant time and the edges() method takes
  time proportional to V.
 **************************************************************************/
public class PrimMST {
	private double weight;//total weight of the MST
	private LinkedQueue<Edge> mst;
	private boolean [] marked;//in the mst?
	private Edge [] edgeTo;//edgeTo[v]= the edge connecting v to the MST

	//Maintain a PQ of vertices connected by an edge to MST,
	// where priority of vertex v = weight of shortest edge connecting v to MST.
	private IndexMinPQ<Double> pq;

	//the constructor to compute the MST of the EdgeWeightedGraph
	public PrimMST(EdgeWeightedGraph G){
		if(G==null) throw new IllegalArgumentException("Initialize PrimMST with null arguments");
		weight=0.0;
		mst=new LinkedQueue<>();
		marked=new boolean[G.V()];
		edgeTo=new Edge[G.V()];
		pq=new IndexMinPQ<>(G.V());
		for (int i = 0; i < G.V(); i++) {
			if(!marked[i]) prim(G,i);
		}
	}

	//do the eager prim algorithms
	private void prim(EdgeWeightedGraph G, int s){
		visit(G,s);

		while(!pq.isEmpty()&&mst.size()<G.V()-1){
			int min=pq.delMin();//the min weight vertex
			assert !marked[min];
			Edge e=edgeTo[min];
			mst.enqueue(e);
			weight+=e.weight();
			visit(G,min);
		}
	}

	private void visit(EdgeWeightedGraph G, int v){
		marked[v]=true;
		for(Edge e:G.adj(v)){
			int w=e.other(v);
			if(!marked[w]) {
				if(!pq.contains(w)) {
					pq.insert(w, e.weight());
					edgeTo[w]=e;
				}
				else if(e.weight()<pq.keyOf(w)) {
					pq.decreaseKey(w, e.weight());
					edgeTo[w]=e;
				}
			}
		}
	}

	//return the total weight of the MST
	public double weight(){
		return weight;
	}

	public Iterable<Edge> edges(){
		return mst;
	}

	public static void main(String[] args) {
		InputStream in;
		try{
			in=new FileInputStream(args[0]);
		}catch(IOException e){
			throw new IllegalArgumentException("can't open the file "+args[0]);
		}
		EdgeWeightedGraph G = new EdgeWeightedGraph(in);
		PrimMST mst = new PrimMST(G);
		for (Edge e : mst.edges()) {
			System.out.println(e);
		}
		System.out.printf("%.5f\n", mst.weight());
	}
}
