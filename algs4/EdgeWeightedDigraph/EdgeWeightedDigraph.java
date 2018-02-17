/**********************************************************************
 *FileName: EdgeWeightedDigraph
 *Author:   lui1993
 *Date:     2018/2/1022:00
 *Description:The EdgeWeightedDigraph class represents a edge-weighted
  digraph of vertices named 0 through V - 1, where each directed edge
  is of type DirectedEdge and has a real-valued weight.

 *It supports the following two primary operations: add a directed edge
  to the digraph and iterate over all of edges incident from a given vertex.
  It also provides methods for returning the number of vertices V and the
  number of edges E. Parallel edges and self-loops are permitted.
 **********************************************************************/

package algs4.EdgeWeightedDigraph;

import algs4.UndirectedGraph.Bag;
import algs4.stack.LinkedStack;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/********************************
 *@author lui1993
 *@create 2018/2/10
 *Description:
 ********************************/
public class EdgeWeightedDigraph {
	private static  final String NEWLINE=System.getProperty("line.separator");
	private final int V;
	private int E;
	private Bag<DirectedEdge>[] adj;
	private int [] indegree;

	//Initialize a edge-weighted-digraph with number of vertices
	public EdgeWeightedDigraph(int V){
		if(V<0) throw new IllegalArgumentException("Initializes EdgeWeightedDigraph with illegal arguments");
		this.V=V;
		this.E=0;
		adj=(Bag<DirectedEdge>[]) new Bag[V];
		for (int i = 0; i < V; i++) {
			adj[i]=new Bag<>();
		}
		indegree=new int[V];
	}

	//Initializing from an input stream
	public EdgeWeightedDigraph(InputStream in){
		if(in==null) throw new IllegalArgumentException("Initializes EdgeWeightedDigraph with null arguments");
		Scanner scan=new Scanner(new BufferedInputStream(in));
		int V=scan.nextInt();
		int E=scan.nextInt();

		//Initialize using V
		if(V<0) throw new IllegalArgumentException("Initializes EdgeWeightedDigraph with illegal arguments");
		this.V=V;
		this.E=0;
		adj=(Bag<DirectedEdge>[]) new Bag[V];
		for (int i = 0; i < V; i++) {
			adj[i]=new Bag<>();
		}
		indegree=new int[V];

		//add edge
		for (int i = 0; i < E; i++) {
			int v=scan.nextInt();
			int w=scan.nextInt();
			double weight=scan.nextDouble();
			DirectedEdge e=new DirectedEdge(v,w,weight);
			addEdge(e);
		}
	}

	//copy constructor
	public EdgeWeightedDigraph(EdgeWeightedDigraph G){
		this(G.V());
		LinkedStack<DirectedEdge> reverse=new LinkedStack<>();
		for (int i = 0; i < G.V(); i++) {
			for(DirectedEdge e:G.adj(i)) reverse.push(e);
		}
		for(DirectedEdge e:reverse){
			addEdge(e);
		}
	}

	//add a directed edge
	public void addEdge(DirectedEdge e){
		if(e==null) throw new IllegalArgumentException("calls addEdge() with null arguments");
		int v=e.from(), w=e.to();
		validateVertex(v);
		validateVertex(w);
		adj[v].add(e);
		E++;
		indegree[w]++;
	}

	//return number of edges in the graph
	public int E(){
		return E;
	}

	//return number of vertices of the graph
	public int V(){
		return V;
	}

	//return number of vertices incident to {@code v}
	public int indegree(int v){
		validateVertex(v);
		return indegree[v];
	}

	//return number of vertices incident from {@code v}
	public int outdegree(int v){
		validateVertex(v);
		return adj[v].size();
	}

	//an iterable object iterating over all the edges incident from @{code v}
	public Iterable<DirectedEdge> adj(int v){
		validateVertex(v);
		return adj[v];
	}

	//return all the edges in this graph
	public Iterable<DirectedEdge> edges(){
		Bag<DirectedEdge> queue=new Bag<>();
		for (int i = 0; i < V; i++) {
			for(DirectedEdge e:adj[i]){
				queue.add(e);
			}
		}
		return queue;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " " + E + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (DirectedEdge e : adj[v]) {
				s.append(e + "  ");
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

	private void validateVertex(int v){
		if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	/**
	 * Unit tests the {@code EdgeWeightedDigraph} data type.
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
		EdgeWeightedDigraph copyG=new EdgeWeightedDigraph(G);
		System.out.println(G);
		System.out.println(copyG);
	}

}
