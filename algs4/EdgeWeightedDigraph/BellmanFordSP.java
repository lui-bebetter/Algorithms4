/**********************************************************************
 *FileName: BellmanFordSP
 *Author:   lui1993
 *Date:     2018/2/1212:31
 *Description:The BellmanFordSP class represents a data type for solving
 the single-source shortest paths problem in edge-weighted digraphs with
 no negative cycles. The edge weights can be positive, negative, or zero.
 This class finds either a shortest path from the source vertex s to every
 other vertex or a negative cycle reachable from the source vertex.
 **********************************************************************/

package algs4.EdgeWeightedDigraph;

import algs4.DirectedGraph.DirectedCycle;
import algs4.queue.LinkedQueue;
import algs4.stack.LinkedStack;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/********************************
 *@author lui1993
 *@create 2018/2/12
 *Description:This implementation uses the Bellman-Ford-Moore algorithm.
  The constructor takes time proportional to V (V + E) in the worst case,
  where V is the number of vertices and E is the number of edges.
  Each call to distTo(int) and hasPathTo(int), hasNegativeCycle takes
  constant time; each call to pathTo(int) and negativeCycle() takes time
  proportional to length of the path returned.
 ********************************/
public class BellmanFordSP {
	private DirectedEdge [] edgeTo;// edgeTo[v] = last edge on shortest s->v path
	private double [] distTo; // distTo[v] = distance  of shortest s->v path
	private boolean[] onQueue;// onQueue[v] = is v currently on the queue?
	private LinkedQueue<Integer> queue;// queue of vertices to relax
	private Iterable<Integer> cycle;// negative cycle (or null if no such cycle)
	private int cost;// number of calls to relax()

	//computing the SP from single source in edge-weighted digraph
	public BellmanFordSP(EdgeWeightedDigraph G, int s){
		if(G==null) throw new IllegalArgumentException("Initialize BellmanFordSP with null arguments");
		edgeTo=new DirectedEdge[G.V()];
		distTo=new double[G.V()];
		onQueue=new boolean[G.V()];
		for (int i = 0; i < G.V(); i++) {
			distTo[i]=Double.POSITIVE_INFINITY;
		}

		//maintain a queue of vertex whose distTo changed
		validateVertex(s);
		queue=new LinkedQueue<>();
		distTo[s]=0.0;
		queue.enqueue(s);
		onQueue[s]=true;
		//relax all the edges incident from the vertex in the queue
		while(!queue.isEmpty()&&!hasNegativeCycle()){
			int v=queue.dequeue();
			onQueue[v]=false;
			relax(G,v);
		}
	}

	//relax all the edges incident from the vertex and add the vertex
	// whose distTo changed to the queue
	private void relax(EdgeWeightedDigraph G, int v){
		for(DirectedEdge e:G.adj(v)){
			int w=e.to();
			if(distTo[w]>distTo[v]+e.weight()){
				distTo[w]=distTo[v]+e.weight();
				edgeTo[w]=e;
				if(!onQueue[w]){
					queue.enqueue(w);
					onQueue[w]=true;
				}
			}

			//check if the digraph has negative cycle
			if(cost++%G.V()==0){
				findNegativeCycle();
				if(hasNegativeCycle()) return;
			}
		}
	}

	private void findNegativeCycle(){
		int V=edgeTo.length;
		//constructor the shortest-path-tree
		EdgeWeightedDigraph G=new EdgeWeightedDigraph(V);
		for (int i = 0; i < V; i++) {
			if(edgeTo[i]!=null) G.addEdge(edgeTo[i]);
		}

		//if the shortest path tree has cycle,the negative cycle exists.
		DirectedCycle finder=new DirectedCycle(G);
		cycle=finder.cycle();
	}

	//has negative cycle?
	public  boolean hasNegativeCycle(){
		return cycle!=null;
	}

	//Returns a negative cycle reachable from the source vertex {@code s}
	public Iterable<Integer> negativeCycle(){
		return cycle;
	}

	//Is there a path from the source {@code s} to vertex {@code v}?
	public boolean hasPathTo(int v){
		validateVertex(v);
		return distTo[v]<Double.POSITIVE_INFINITY;
	}

	// Returns a shortest path from the source {@code s} to vertex {@code v}.
	public Iterable<DirectedEdge> pathTo(int v){
		validateVertex(v);
		if(!hasPathTo(v)) return null;
		if(hasNegativeCycle()) throw new UnsupportedOperationException("Negative cycle exists.");
		LinkedStack<DirectedEdge> path=new LinkedStack<>();
		while(edgeTo[v]!=null){
			path.push(edgeTo[v]);
			v=edgeTo[v].from();
		}

		return path;
	}

	public double distTo(int v){
		validateVertex(v);
		if(hasNegativeCycle()) throw new UnsupportedOperationException("Negative cycle exists.");
		return distTo[v];
	}

	private void validateVertex(int v){
		int V=edgeTo.length;
		if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	/**
	 * Unit tests the {@code BellmanFordSP} data type.
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
		int s = Integer.parseInt(args[1]);
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);

		BellmanFordSP sp = new BellmanFordSP(G, s);

		// print negative cycle
		if (sp.hasNegativeCycle()) {
			for (int e : sp.negativeCycle())
				System.out.println(e);
		}

		// print shortest paths
		else {
			for (int v = 0; v < G.V(); v++) {
				if (sp.hasPathTo(v)) {
					System.out.printf("%d to %d (%5.2f)  ", s, v, sp.distTo(v));
					for (DirectedEdge e : sp.pathTo(v)) {
						System.out.print(e + "   ");
					}
					System.out.println();
				}
				else {
					System.out.printf("%d to %d           no path\n", s, v);
				}
			}
		}

	}

}
