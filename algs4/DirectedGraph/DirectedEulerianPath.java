/**********************************************************************
 *FileName: DirectedEulerianPath
 *Author:   luibebetter
 *Date:     2018/1/2120:26
 *Description:The DirectedEulerianPath class represents a data type for
  finding an Eulerian path in a digraph.
  An Eulerian path is a path (not necessarily simple) that uses every
  edge in the digraph exactly once.
 **********************************************************************/

package algs4.DirectedGraph;

import algs4.StdRandom.StdRandom;
import algs4.stack.LinkedStack;

import java.util.Iterator;

/***********************************************************************
 *@author: luibebetter
 *@create: 2018/1/21
 *Description:This implementation uses a nonrecursive depth-first search.
  The constructor runs in O(E + V) time, and uses O(V) extra space,
  where E is the number of edges and V the number of vertices.
  All other methods take O(1) time.
 **********************************************************************/
public class DirectedEulerianPath {
	private LinkedStack<Integer> path=null;

	//find the Eulerian path of the specific digraph
	public DirectedEulerianPath(Digraph G){
		if(G==null) throw new IllegalArgumentException("Initiating DirectedEulerianPath with null digraph");
		if(G.V()==0) return;

		/****************************
		*A directed graph has an Eulerian trail if and only if at most one vertex has (out-degree) − (in-degree) = 1,
		 **at most one vertex has (in-degree) − (out-degree) = 1
		 *************************/
		//initiate the source using:non-isolated vertex if no vertex whose out degree>in degree
		int s=nonIsolatedVertex(G);
		int deficit=0;
		for(int i=0;i<G.V();i++){
			if(G.outdegree(i)>G.indegree(i)) {
				deficit = G.outdegree(i) - G.indegree(i);
				s=i;
			}
		}
		if(deficit>1) return;
		if(s==-1) s=0;

		// create local view of adjacency lists, to iterate one vertex at a time
		Iterator<Integer> []adj=(Iterator<Integer>[]) new Iterator[G.V()];
		for(int i=0;i<G.V();i++) adj[i]=G.adjcent(i).iterator();

		//do dfs
		LinkedStack<Integer> stack=new LinkedStack<>();
		path=new LinkedStack<>();
		stack.push(s);

		while(!stack.isEmpty()){
			int v=stack.pop();
			while(adj[v].hasNext()){
				stack.push(v);
				v=adj[v].next();
			}

			// add vertex with no more leaving edges to PATH
			path.push(v);
		}

		if(path.size()!=G.E()+1) path=null;
	}

	public boolean hasEulerianPath(){
		/********************************
		 * Description: has Eulerian Path?
		 *
		 * @param
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		return path!=null;
	}

	//return the Eulerian path,if non-exist return null
	public Iterable<Integer> path(){
		return path;
	}

	private int nonIsolatedVertex(Digraph G){
		for(int i=0;i<G.V();i++){
			if(G.outdegree(i)>0) return i;
		}
		return -1;
	}

	/*******************************************
	 * Unit test
	 */
	private static void unitTest(Digraph G, String description) {
		System.out.println(description);
		System.out.println("-------------------------------------");
		System.out.print(G);

		DirectedEulerianPath euler = new DirectedEulerianPath(G);

		System.out.print("Eulerian cycle: ");
		if (euler.hasEulerianPath()) {
			for (int v : euler.path()) {
				System.out.print(v + " ");
			}
			System.out.println();
		}
		else {
			System.out.println("none");
		}
		System.out.println();
	}


	/**
	 * Unit tests the {@code EulerianCycle} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		int V = Integer.parseInt(args[0]);
		int E = Integer.parseInt(args[1]);

		// Eulerian cycle
		Digraph G1 = DigraphGenerator.eulerianCycle(V, E);
		unitTest(G1, "Eulerian cycle");

		// Eulerian path
		Digraph G2 = DigraphGenerator.eulerianPath(V, E);
		unitTest(G2, "Eulerian path");

		// empty digraph
		Digraph G3 = new Digraph(V);
		unitTest(G3, "empty digraph");

		// self loop
		Digraph G4 = new Digraph(V);
		int v4 = StdRandom.uniform(V);
		G4.addEdge(v4, v4);
		unitTest(G4, "single self loop");

		// union of two disjoint cycles
		Digraph H1 = DigraphGenerator.eulerianCycle(V/2, E/2);
		Digraph H2 = DigraphGenerator.eulerianCycle(V - V/2, E - E/2);
		int[] perm = new int[V];
		for (int i = 0; i < V; i++)
			perm[i] = i;
		StdRandom.shuffle(perm);
		Digraph G5 = new Digraph(V);
		for (int v = 0; v < H1.V(); v++)
			for (int w : H1.adjcent(v))
				G5.addEdge(perm[v], perm[w]);
		for (int v = 0; v < H2.V(); v++)
			for (int w : H2.adjcent(v))
				G5.addEdge(perm[V/2 + v], perm[V/2 + w]);
		unitTest(G5, "Union of two disjoint cycles");

		// random digraph
		Digraph G6 = DigraphGenerator.simple(V, E);
		unitTest(G6, "simple digraph");
	}

}
