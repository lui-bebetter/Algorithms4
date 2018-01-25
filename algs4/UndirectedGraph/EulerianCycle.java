/**********************************************************************
 *FileName: EulerianCycle
 *Author:   Dell
 *Date:     2018/1/415:38
 *Description:The EulerianCycle class represents a data type for finding
  an Eulerian cycle or path in a graph. An Eulerian cycle is a cycle
 (not necessarily simple) that uses every edge in the graph exactly once.
 **********************************************************************/

package algs4.UndirectedGraph;
import algs4.stack.LinkedStack;
import algs4.queue.LinkedQueue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/***********************************************************************
 *@author: Dell
 *@create: 2018/1/4
 *Description:This implementation uses a nonrecursive depth-first search.
  The constructor runs in O(E + V) time, and uses O(E + V) extra space,
  where E is the number of edges and V the number of vertices All other
  methods take O(1) time
 **************************************************************************/
public class EulerianCycle {
	private LinkedStack<Integer> cycle=null;

	// an undirected edge, with a field to indicate whether the edge has already been used
	private static class Edge{
		private final int v;
		private final int w;
		private boolean isUsed;

		public Edge(int v, int w){
			this.v=v;
			this.w=w;
			isUsed=false;
		}

		//return the other vertex of the edge as respect to {@code v}
		public int other(int s){
			if(this.v==s) return this.w;
			else if(this.w==s) return this.v;
			else throw new IllegalArgumentException("vertex "+v+"not in the Edge("+this.v+","+this.w+")");
		}
	}

	public EulerianCycle(Graph G){
		if(G==null) throw new IllegalArgumentException("calls EulerianCycle() with null arguments");
		if(G.E()==0) return;//a Eulerian cycle must have at least one edge.

		//the degree of vertex in graph that has a Eulerian cycle must be even\
		for(int v=0;v<G.V();v++){
			if(G.degree(v)%2 !=0) return;
		}


		// create local view of adjacency lists, to iterate one vertex at a time
		// the helper Edge data type is used to avoid exploring both copies of an edge v-w
		LinkedQueue<Edge> [] adj=(LinkedQueue<Edge>[]) new LinkedQueue[G.V()];
		for(int v=0;v<G.V();v++){
			adj[v]=new LinkedQueue<Edge>();
		}

		for(int v=0;v<G.V();v++){
			int selfLoop=0;//two vertices means one edge
			for(int w:G.adjcent(v)){
				if(w==v){
					if(selfLoop%2==0){
						Edge e=new Edge(v,w);
						adj[w].enqueue(e);
						adj[v].enqueue(e);
					}
					selfLoop++;
				}else if(v<w){
					Edge e=new Edge(v,w);
					adj[v].enqueue(e);
					adj[w].enqueue(e);
				}
			}
		}

		cycle=new LinkedStack<Integer>();

		//do the dfs :greedily search through edges in iterative DFS style
		LinkedStack<Integer> stack=new LinkedStack<Integer>();
		int s=nonIsolatedVertex(G);//Initialize the source vertex with non-isolated vertex
		if(s==-1) s=0;//all isolated vertex is a degenerate Eulerian cycle
		stack.push(s);

		while(!stack.isEmpty()){
			int v=stack.pop();
			while(!adj[v].isEmpty()){
				Edge e=adj[v].dequeue();
				if(e.isUsed) continue;
				e.isUsed=true;//Instead of  to mark the vertex, to mark the edge
				stack.push(v);//store the vertex that has not been done.
				v=e.other(v);//search the next vertex in the path
			}

			cycle.push(v);//put the vertex that all edges have been used
		}

		// check if all edges are used
		if(cycle.size()!=G.E()+1){
			cycle=null;
		}

	}

	// Returns the sequence of vertices on an Eulerian cycle.
	public Iterable<Integer> cycle(){
		return cycle;
	}

	public boolean hasEulerianCycle(){
		return cycle!=null;
	}


	private int nonIsolatedVertex(Graph G){
		/********************************
		 * Description: returns any non-isolated vertex; -1 if no such vertex
		 *
		 * @param G
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		for(int v=0;v<G.V();v++){
			if(G.degree(v)>0) return v;
		}
		return -1;
	}

	private static void unitTest(Graph G, String description) {
		System.out.println(description);
		System.out.println("-------------------------------------");
		System.out.print(G);

		EulerianCycle euler = new EulerianCycle(G);

		System.out.print("Eulerian cycle: ");
		if (euler.hasEulerianCycle()) {
			for (int v : euler.cycle()) {
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

		Scanner in;
		try {
			File file = new File(args[0]);
			in = new Scanner(new BufferedInputStream(new FileInputStream(file)), "UTF-8");
		} catch (IOException E) {
			throw new IllegalArgumentException("can't open file:" + args[0]);
		}

		int V = in.nextInt();
		if (V < 0) throw new IllegalArgumentException("number of vertices in a Graph must be nonnegative");
		Graph G = new Graph(V);
		int E = in.nextInt();
		if (E < 0) throw new IllegalArgumentException("number of edges in a Graph must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.nextInt()-1;
			int w = in.nextInt()-1;
			G.addEdge(v, w);
		}

		unitTest(G, "Eulerian cycle");

	}

}
