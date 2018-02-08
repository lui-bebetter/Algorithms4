/**********************************************************************
 *FileName: KruskalMST
 *Author:   luibebetter
 *Date:     2018/2/119:17
 *Description:The KruskalMST class represents a data type for computing a
  minimum spanning tree in an edge-weighted graph.
  The edge weights can be positive, zero, or negative and need not be distinct.
  If the graph is not connected, it computes a minimum spanning forest,
  which is the union of minimum spanning trees in each connected component.

 *The weight() method returns the weight of a minimum spanning tree and the
  edges() method returns its edges.
 *********************************************************************************/

package algs4.EdgeWeightedGraph;

import algs4.PriorityQueue.MinPQ;
import algs4.UnionFind.UF;
import algs4.queue.LinkedQueue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/************************************************************
 *@author: luibebetter
 *@create: 2018/2/1
 *Description:This implementation uses Kruskal's algorithm
  and the union-find data type.

 *The constructor takes time proportional to E log E and
  extra space (not including the graph) proportional to V,
  where V is the number of vertices and E is the number of edges.
  Afterwards, the weight() method takes constant time and
  the edges() method takes time proportional to V.
 **********************************************************/
public class KruskalMST {
  private LinkedQueue<Edge> mst;
  private double weight;//weight of the mst

  //computing the minimal spanning tree of the EdgeWeightedGraph
  public KruskalMST(EdgeWeightedGraph G){
    if(G==null) throw new IllegalArgumentException("Initialize KruskalMST() with null arguments");

    //Initialize the MinPQ and UF data type
	  mst=new LinkedQueue<>();
    MinPQ<Edge> pq=new MinPQ<>();
    for(Edge e:G.edges()) pq.insert(e);
    UF uf=new UF(G.V());

    //Kruskal's algorithm procedure
    while(!pq.isEmpty()&&mst.size()<G.V()-1){
      Edge e=pq.delMin();
      int v=e.either();
      int w=e.other(v);
      if(!uf.connected(v,w)){
        mst.enqueue(e);
        weight+=e.weight();
        uf.union(v,w);
      }
    }

  }

  //return an iterable object which iterate over edges belong to MST
  public Iterable<Edge> edges(){
    return mst;
  }

  //return sum of the weight of edges in the MST
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
		KruskalMST mst = new KruskalMST(G);
		for (Edge e : mst.edges()) {
			System.out.println(e);
		}
		System.out.printf("%.5f\n", mst.weight());
	}
}
