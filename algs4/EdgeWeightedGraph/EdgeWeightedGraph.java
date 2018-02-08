/**********************************************************************
 *FileName: EdgeWeightedGraph
 *Author:   luibebetter
 *Date:     2018/2/115:20
 *Description:The EdgeWeightedGraph class represents an edge-weighted
  graph of vertices named 0 through V – 1, where each undirected edge
  is of type Edge and has a real-valued weight.
 *It supports the following two primary operations: add an edge to the
  graph, iterate over all of the edges incident to a vertex.
  It also provides methods for returning the number of vertices V and
  the number of edges E. Parallel edges and self-loops are permitted.
  By convention, a self-loop v-v appears in the adjacency list of v
  twice and contributes two to the degree of v.
 **********************************************************************/

package algs4.EdgeWeightedGraph;

import algs4.UndirectedGraph.Bag;
import algs4.stack.LinkedStack;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/************************************************************************
 *@author: luibebetter
 *@create: 2018/2/1
 *Description:This implementation uses an adjacency-lists representation,
  which is a vertex-indexed array of Bag objects.
  All operations take constant time (in the worst case) except iterating
  over the edges incident to a given vertex, which takes time proportional
  to the number of such edges.
 ******************************************************************************/
public class EdgeWeightedGraph {
	private final String NEWLINE=System.getProperty("line.separator");
	private Bag<Edge> [] adj;//vertex-indexed adjcency-lists
	private int E;//number of edges
	private int V;//number of vertices

	//Initialize a EdgeWeightedGraph with number of vertices
	public EdgeWeightedGraph(int V){
		if(V<0) throw new IllegalArgumentException("Initialize EdgeWeightedGraph with illegal argument");
		this.V=V;
		this.E=0;
		adj=(Bag<Edge>[]) new Bag[V];
		for (int i = 0; i < V; i++) {
			adj[i]=new Bag<>();
		}
	}

	//Initialize a EdgeWeightedGraph from an input stream
	public EdgeWeightedGraph(InputStream input){
		if(input==null) throw new IllegalArgumentException("Initialize EdgeWeightedGraph with null arguments");
		Scanner in=new Scanner(new BufferedInputStream(input));
		//Initialize with number of vertices
		int V=in.nextInt();
		if(V<0) throw new IllegalArgumentException("Initialize EdgeWeightedGraph with illegal argument");
		this.V=V;
		this.E=0;
		adj=(Bag<Edge>[]) new Bag[V];
		for (int i = 0; i < V; i++) {
			adj[i]=new Bag<>();
		}

		int E=in.nextInt();
		if(E<0) throw new IllegalArgumentException("Initialize EdgeWeightedGraph with illegal argument");
		for(int i=0;i<E;i++){
			int v=in.nextInt();
			int w=in.nextInt();
			validateVertex(v);
			validateVertex(w);
			double weight=in.nextDouble();
			Edge edge=new Edge(v,w,weight);
			addEdge(edge);
		}
	}

	//copy constructor
	public EdgeWeightedGraph(EdgeWeightedGraph G){
		this(G.V());
		this.E=G.E();
		for (int i = 0; i < G.V(); i++) {
			LinkedStack<Edge> reverse=new LinkedStack<>();
			for(Edge edge:G.adj(i)){
				reverse.push(edge);
			}
			for(Edge edge:reverse){
				adj[i].add(edge);
			}
		}
	}

	//return number of edges
	public int E(){
		return E;
	}

	//return number of vertices
	public int V(){
		return V;
	}

	//
	public int degree(int v){
		validateVertex(v);
		return adj[v].size();
	}

	//add edge v-w
	public void addEdge(Edge edge){
		if(edge==null) throw new IllegalArgumentException("calls addEdge() with null arguments");
		int v=edge.either();
		int w=edge.other(v);
		validateVertex(v);
		validateVertex(w);
		adj[v].add(edge);
		adj[w].add(edge);
		E++;
	}

	//return an iterable object which iterates over all the edges adjacent to
	public Iterable<Edge> adj(int v){
		validateVertex(v);
		return adj[v];
	}

	//return an iterable object which iterates over all the edges 
	public Iterable<Edge> edges(){
		Bag<Edge> list=new Bag<>();
		for (int i = 0; i < V; i++) {
			int selfloop=0;
			for(Edge e:adj(i)){
				int w=e.other(i);
				if(w>i) list.add(e);
				else if(w==i){
					if(selfloop%2==0) list.add(e);
					selfloop++;
				}
			}
		}
		return list;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(V + " " + E + NEWLINE);
		for (int v = 0; v < V; v++) {
			s.append(v + ": ");
			for (Edge e : adj[v]) {
				s.append(e + "  ");
			}
			s.append(NEWLINE);
		}
		return s.toString();
	}

	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertex(int v) {
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	public static void main(String[] args) {
		InputStream in;
		try {
			 in = new FileInputStream(args[0]);
		}catch (IOException e){
			throw new IllegalArgumentException("can't open the file "+args[0]);
		}
		EdgeWeightedGraph G = new EdgeWeightedGraph(in);
		EdgeWeightedGraph copyG=new EdgeWeightedGraph(G);
		System.out.println(G);
		System.out.println(copyG);
	}
}
