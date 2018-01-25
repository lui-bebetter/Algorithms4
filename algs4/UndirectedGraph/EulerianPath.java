/**********************************************************************
 *FileName: EulerianPath
 *Author:   Dell
 *Date:     2018/1/423:23
 *Description:The EulerianPath class represents a data type for finding
   an Eulerian path in a graph. An Eulerian path is a path (not necessarily
   simple) that uses every edge in the graph exactly once.
 **********************************************************************/

package algs4.UndirectedGraph;
import algs4.stack.LinkedStack;
import algs4.queue.LinkedQueue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/************************************************************************
 *@author: Dell
 *@create: 2018/1/4
 *Description:It has been proven that connected graphs with all vertices
  of even degree have an Eulerian circuit, and such graphs are called Eulerian.
  If there are exactly two vertices of odd degree, all Eulerian paths start at
  one of them and end at the other. A graph that has an Eulerian path but not
  an Eulerian circuit is called semi-Eulerian.

 *This implementation uses a nonrecursive depth-first search.
  The constructor runs in O(E + V) time, and uses O(E + V) extra space,
  where E is the number of edges and V the number of vertices All other
  methods take O(1) time.
 *************************************************************************/
public class EulerianPath {
	private LinkedStack<Integer> path=null;

	public EulerianPath(Graph G){
		if(G==null) throw new IllegalArgumentException("calls EulerianPath() with null arguments");

		// find vertex from which to start potential Eulerian path:
		// a vertex v with odd degree(v) if it exits;
		// otherwise a vertex with degree(v) > 0
		int s=nonIsolatedVertex(G);
		if(s==-1) s=0;//when all vertices are isolated, path={0}
		//check whether the number of odd degree vertex is 0 or 2
		int oddDegreeVertex=0;
		for(int v=0;v<G.V();v++){
			if(G.degree(v)%2!=0) {
				s=v;
				oddDegreeVertex++;
			}
		}
		if(!(oddDegreeVertex==0||oddDegreeVertex==2)) return;

		// create local view of adjacency lists, to iterate one vertex at a time
		// the helper Edge data type is used to avoid exploring both copies of an edge v-w
		LinkedQueue<Edge> []adj=(LinkedQueue<Edge>[]) new LinkedQueue[G.V()];
		for(int v=0;v<G.V();v++) adj[v]=new LinkedQueue<Edge>();
		for(int v=0;v<G.V();v++) {
			int selfLoop=0;
			for(int w:G.adjcent(v)){
				if(w==v){
					if(selfLoop%2==0){
						Edge e=new Edge(v,w);
						adj[v].enqueue(e);
						adj[w].enqueue(e);
					}
					selfLoop++;
				}else if(v<w){
					Edge e=new Edge(v,w);
					adj[v].enqueue(e);
					adj[w].enqueue(e);
				}
			}
		}

		path=new LinkedStack<Integer>();
		LinkedStack<Integer> stack=new LinkedStack<Integer>();
		stack.push(s);

		//do the dfs :greedily search through edges in iterative DFS style
		while(!stack.isEmpty()){
			int v=stack.pop();
			while(!adj[v].isEmpty()){
				Edge e=adj[v].dequeue();
				if(e.isUsed) continue;
				e.isUsed=true;
				stack.push(v);
				v=e.other(v);
			}

			// push vertex with no more leaving edges to path
			path.push(v);
		}

		if(path.size()!=G.E()+1){
			path=null;
		}
	}

	public Iterable<Integer> path(){
		/********************************
		 * Description: return the Eulerian path ,if not Eulerian return null
		 *
		 * @param
		 * @return: java.lang.Iterable<java.lang.Integer>
		 * @Author: luibebetter
		 *********************************/
		return path;
	}

	public boolean hasEulerianPath(){
		/********************************
		 * Description:
		 *
		 * @param
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		return path!=null;
	}

	private int nonIsolatedVertex(Graph G){
		for(int v=0;v<G.V();v++){
			if(G.degree(v)>0) return v;
		}
		return -1;
	}

	private static class Edge{
		private final int v;
		private final int w;
		private boolean isUsed;

		public Edge(int v, int w){
			this.v=v;
			this.w=w;
			isUsed=false;
		}

		public int other(int s){
			if(s==v) return w;
			else if(s==w) return v;
			else throw new IllegalArgumentException("vertex "+s+"not in Edge("+v+","+w+")");
		}
	}

	private static void unitTest(Graph G, String description) {
		System.out.println(description);
		System.out.println("-------------------------------------");
		System.out.print(G);

		EulerianPath euler = new EulerianPath(G);

		System.out.print("Eulerian path: ");
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
