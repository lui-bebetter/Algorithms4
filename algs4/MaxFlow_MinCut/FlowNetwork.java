/**********************************************************************
 *FileName: FlowNetwork
 *Author:   lui1993
 *Date:     2018/2/1721:58
 *Description:The FlowNetwork class represents a capacitated network with
  vertices named 0 through V - 1, where each directed edge is of type
  FlowEdge and has a real-valued capacity and flow. It supports the
  following two primary operations: add an edge to the network, iterate
  over all of the edges incident to or from a vertex. It also provides
  methods for returning the number of vertices V and the number of edges
  E. Parallel edges and self-loops are permitted.
 **********************************************************************/

package algs4.MaxFlow_MinCut;

import algs4.UndirectedGraph.Bag;
import algs4.stack.LinkedStack;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/*************************************************************************
 *@author lui1993
 *@create 2018/2/17
 *Description:This implementation uses an adjacency-lists representation,
  which is a vertex-indexed array of Bag objects.
  All operations take constant time (in the worst case) except iterating
  over the edges incident to a given vertex, which takes time proportional
  to the number of such edges.
 ************************************************************************/
public class FlowNetwork {
	private static final String NEWLINE=System.getProperty("line.separator");
	private final int V;//number of vertices in the flow net work
	private  int E;//number of edges in the flow net work
	private Bag<FlowEdge> [] adj;//adj[v]=the edges incident to or from vertex {@code v}

	//Initialize the flow net work with number of vertices
	public FlowNetwork(int V){
		if(V<0) throw new IllegalArgumentException("Initialize FlowNetwork() with null arguments");
		this.V=V;
		this.E=0;
		adj=(Bag<FlowEdge>[]) new Bag[V];
		for (int i = 0; i < V; i++) {
			adj[i]=new Bag<>();
		}
	}

	//Initialize the flow net work from a input stream
	public FlowNetwork(InputStream input) {
		if(input==null) throw new IllegalArgumentException("calls FlowNetwork() with null arguments");
		Scanner scan=new Scanner(new BufferedInputStream(input));
		int V=scan.nextInt();
		if(V<0) throw new IllegalArgumentException("Initialize FlowNetwork() with null arguments");
		this.V=V;
		this.E=0;
		adj=(Bag<FlowEdge>[]) new Bag[V];
		for (int i = 0; i < V; i++) {
			adj[i]=new Bag<>();
		}

		int E=scan.nextInt();
		if(E<0) throw new IllegalArgumentException();
		for (int i = 0; i < E; i++) {
			int v=scan.nextInt();
			int w=scan.nextInt();
			validateVertex(v);
			validateVertex(w);
			double capacity=scan.nextDouble();
			addEdge(new FlowEdge(v,w,capacity));
		}
	}
	//copy constructor
	public FlowNetwork(FlowNetwork fn){
		this(fn.V());

		for (int i = 0; i < fn.V(); i++) {
			LinkedStack<FlowEdge> reverse=new LinkedStack<>();
			for(FlowEdge e:fn.adj(i)){
				reverse.push(e);
			}

			for(FlowEdge e:reverse){
				adj[i].add(e);
			}
		}
	}

	//return the number of edges in the FN
	public int E(){
		return E;
	}

	//number of vertices in the flow net work
	public int V(){
		return V;
	}

	//add an edge
	public void addEdge(FlowEdge e){
		if(e==null) throw new IllegalArgumentException("calls addEdge() with null arguments");
		int v=e.from(), w=e.to();
		validateVertex(v);
		validateVertex(w);
		adj[v].add(e);
		adj[w].add(e);
		E++;
	}

	//Returns the edges incident on vertex v (includes both edges pointing to and from v)
	public Iterable<FlowEdge> adj(int v){
		validateVertex(v);
		return adj[v];
	}

	//return all the edges in the flow net work
	public Iterable<FlowEdge> edges(){
		Bag<FlowEdge> edges=new Bag<>();
		for (int i = 0; i < V; i++) {
			for(FlowEdge e:adj(i)){
				if(e.from()==i) edges.add(e);
			}
		}
		return edges;
	}

	private void validateVertex(int v){
		if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	/**
	 * Returns a string representation of the flow network.
	 * This method takes time proportional to <em>E</em> + <em>V</em>.
	 * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
	 *    followed by the <em>V</em> adjacency lists
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " " + E + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(v + ":  ");
			for (FlowEdge e : adj[v]) {
				if (e.to() != v) s.append(e + "  ");
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

	/**
	 * Unit tests the {@code FlowNetwork} data type.
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

		FlowNetwork G = new FlowNetwork(in);
		System.out.println(G);
	}

}
