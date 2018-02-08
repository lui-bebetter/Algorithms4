/**********************************************************************
 *FileName: LazyPrimMST
 *Author:   luibebetter
 *Date:     2018/2/123:47
 *Description:The LazyPrimMST class represents a data type for computing
  a minimum spanning tree in an edge-weighted graph. The edge weights can
  be positive, zero, or negative and need not be distinct. If the graph
  is not connected, it computes a minimum spanning forest, which is the
  union of minimum spanning trees in each connected component.
  The weight() method returns the weight of a minimum spanning tree and
  the edges() method returns its edges.
 **********************************************************************/

package algs4.EdgeWeightedGraph;

import algs4.PriorityQueue.MinPQ;
import algs4.queue.LinkedQueue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/************************************************************************
 *@author luibebetter
 *@create 2018/2/1
 *Description:This implementation uses a lazy version of Prim's algorithm
  with a binary heap of edges. The constructor takes time proportional to
  E log E and extra space (not including the graph) proportional to E,
  where V is the number of vertices and E is the number of edges.
  Afterwards, the weight() method takes constant time and the edges() method
  takes time proportional to V.
 ************************************************************************/
public class LazyPrimMST {
	private LinkedQueue<Edge> mst;
	private boolean []marked;//has been in the mst ?
	private double weight;//total weight of the mst
	private MinPQ<Edge> pq;

	//computing the mst of a specific edge-weighted-graph
	public LazyPrimMST(EdgeWeightedGraph G){
		if(G==null) throw new IllegalArgumentException("Initialize LazyPrimMST with null arguments");
		mst=new LinkedQueue<>();
		marked=new boolean[G.V()];
		weight=0.0;
		pq=new MinPQ<>();

		for (int i = 0; i < G.V(); i++) {
			if(!marked[i]) prim(G,i);
		}
	}

	//prim algorithms:starting from a vertex and add the min weight edge to the mst with only one
	//endpoint in the mst.
	private  void prim(EdgeWeightedGraph G, int s ){
		visit(G,s);
		while(!pq.isEmpty()&&mst.size()<G.V()-1) {
			Edge e = pq.delMin();
			int v=e.either();
			int w=e.other(v);
			if(marked[v]&&marked[w]) continue;
			mst.enqueue(e);
			weight+=e.weight();
			if(!marked[v]) visit(G,v);
			if(!marked[w]) visit(G,w);
		}
	}

	private void visit(EdgeWeightedGraph G,int v){
		/************************************************************
		 * Description: marked the vertex and add all the edges
		   (with only one endpoint in the mst) adjacent to the vertex
		   to the MinPQ
		 *
		 * @param G
		 * @param v
		 * @return: void
		 * @Author: luibebetter
		 ***********************************************************/
		marked[v]=true;
		for(Edge e:G.adj(v)){
			int w=e.other(v);
			if(!marked[w]) pq.insert(e);
		}
	}

	public Iterable<Edge> edges(){
		return mst;
	}

	public double weight(){
		return weight;
	}

	/**
	 * Unit tests the {@code KruskalMST} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		InputStream in;
		try{
			in=new FileInputStream(args[0]);
		}catch(IOException e){
			throw new IllegalArgumentException("can't open the file "+args[0]);
		}
		EdgeWeightedGraph G = new EdgeWeightedGraph(in);
		LazyPrimMST mst = new LazyPrimMST(G);
		for (Edge e : mst.edges()) {
			System.out.println(e);
		}
		System.out.printf("%.5f\n", mst.weight());
	}

}
