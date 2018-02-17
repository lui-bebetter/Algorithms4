/**********************************************************************
 *FileName: DijkstraSP
 *Author:   lui1993
 *Date:     2018/2/1110:55
 *Description:The DijkstraSP class represents a data type for solving
  the single-source shortest paths problem in edge-weighted digraphs
  where the edge weights are non-negative.
 **********************************************************************/

package algs4.EdgeWeightedDigraph;

import algs4.PriorityQueue.IndexMinPQ;
import algs4.stack.LinkedStack;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/******************************************************************************
 *@author lui1993
 *@create 2018/2/11
 *Description:This implementation uses Dijkstra's algorithm with a binary heap.
  The constructor takes time proportional to E log V, where V is the number of
  vertices and E is the number of edges. Each call to distTo(int) and hasPathTo
  (int) takes constant time; each call to pathTo(int) takes time proportional
  to the number of edges in the shortest path returned.
 ******************************************************************************/
public class DijkstraSP {
	private double[] distTo;//distTo[v]=sum of weight of the shortest-path from s to v
	private DirectedEdge []edgeTo;//edgeTo[v]=the edge incident to v in the shortest path
	private IndexMinPQ<Double> pq;//the priority queue of vertex (distTo[v] as the priority)

	//the constructor:computing the shortest path of edge-weighted graph with specific source
	public DijkstraSP(EdgeWeightedDigraph G, int s){
		if(G==null) throw new IllegalArgumentException("Initializes DijkstraSP with null arguments");
		//only for non-negative weight digraph
		for (DirectedEdge e : G.edges()) {
			if (e.weight() < 0)
				throw new IllegalArgumentException("edge " + e + " has negative weight");
		}
		distTo=new double[G.V()];
		edgeTo=new DirectedEdge[G.V()];
		for (int i = 0; i < G.V(); i++) {
			distTo[i]=Double.POSITIVE_INFINITY;
		}

		//Initialize the priority queue
		validateVertex(s);
		pq=new IndexMinPQ<>(G.V());
		distTo[s]=0;
		pq.insert(s,distTo[s]);

		while(!pq.isEmpty()){
			int v=pq.delMin();
			for(DirectedEdge e:G.adj(v)){
				relax(e);
			}
		}
	}

	// relax edge e and update pq if decreased
	private void relax(DirectedEdge e){
		int w=e.to(),v=e.from();
		if(distTo[w]>distTo[v]+e.weight()){
			distTo[w]=distTo[v]+e.weight();
			edgeTo[w]=e;
			if(pq.contains(w)) pq.decreaseKey(w,distTo[w]);
			else pq.insert(w,distTo[w]);
		}
	}

	//Returns true if there is a path from the source vertex s to vertex v
	public boolean hasPathTo(int v){
		validateVertex(v);
		return distTo[v]<Double.POSITIVE_INFINITY;
	}

	//Returns the length of a shortest path from the source vertex s to vertex v
	public double distTo(int v){
		validateVertex(v);
		return distTo[v];
	}

	//Returns a shortest path from the source vertex s to vertex v.
	public Iterable<DirectedEdge> pathTo(int v){
		validateVertex(v);
		if(!hasPathTo(v)) return null;
		LinkedStack<DirectedEdge> stack=new LinkedStack<>();
		while(distTo[v]!=0){
			stack.push(edgeTo[v]);
			v=edgeTo[v].from();
		}
		return stack;
	}

	private void validateVertex(int v){
		int V=distTo.length;
		if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}


	/**
	 * Unit tests the {@code DijkstraSP} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		InputStream in;
		try {
			in = new FileInputStream(args[0]);
		}catch (IOException e){
			throw new IllegalArgumentException("can't open the file "+args[0]);
		}
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
		int s = Integer.parseInt(args[1]);

		// compute shortest paths
		DijkstraSP sp = new DijkstraSP(G, s);


		// print shortest path
		for (int t = 0; t < G.V(); t++) {
			if (sp.hasPathTo(t)) {
				System.out.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
				for (DirectedEdge e : sp.pathTo(t)) {
					System.out.print(e + "   ");
				}
				System.out.println();
			}
			else {
				System.out.printf("%d to %d         no path\n", s, t);
			}
		}
	}
}
