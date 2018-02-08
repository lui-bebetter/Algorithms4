/**********************************************************************
 *FileName: DirectedPathsBFS
 *Author:   luibebetter
 *Date:     2018/1/2020:43
 *Description:The DirectedPathsBFS class represents a data type for
  finding shortest paths (number of edges) from a source vertex s
  (or set of source vertices) to every other vertex in the digraph.
 **********************************************************************/

package algs4.DirectedGraph;

import algs4.queue.LinkedQueue;
import algs4.stack.LinkedStack;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**************************************************************************
 *@author: luibebetter
 *@create: 2018/1/20
 *Description:This implementation uses breadth-first search.
 *The constructor takes time proportional to V + E,
  where V is the number of vertices and E is the number of edges.
 *Each call to distTo(int) and hasPathTo(int) takes constant time;
 *each call to pathTo(int) takes time proportional to the length of the path.
 *It uses extra space (not including the digraph) proportional to V.
 *****************************************************************************/
public class DirectedPathsBFS {
	private static final int INFINITY=Integer.MAX_VALUE;
	private boolean [] marked;//reachable from the source?
	private int [] distTo;//number of edges in the path from the source
	private int [] edgeTo;//the previous vertex int the path

	//find the shortest path from the  vertex {@code s}
	public DirectedPathsBFS(Digraph G, int s){
		if(G==null) throw new IllegalArgumentException("Initiate DirectedPathsBFS with null digraph");
		marked=new boolean[G.V()];
		distTo=new int[G.V()];
		edgeTo=new int[G.V()];
		for(int i=0;i<G.V();i++) distTo[i]=INFINITY;
		validateVertex(s);
		bfs(G,s);
	}

	//do bfs
	private void bfs(Digraph G, int s){
		LinkedQueue<Integer> queue=new LinkedQueue<>();
		marked[s]=true;
		distTo[s]=0;
		queue.enqueue(s);

		while(!queue.isEmpty()){
			int v=queue.dequeue();
			for(int w:G.adj(v)){
				if(!marked[w]){
					marked[w]=true;
					edgeTo[w]=v;
					distTo[w]=distTo[v]+1;
					queue.enqueue(w);
				}
			}
		}
	}

	//find the shortest path from a set of sources
	public DirectedPathsBFS(Digraph G, Iterable<Integer> sources){
		if(G==null) throw new IllegalArgumentException("Initiate DirectedPathsBFS with null digraph");
		marked=new boolean[G.V()];
		distTo=new int[G.V()];
		edgeTo=new int[G.V()];
		for(int i=0;i<G.V();i++) distTo[i]=INFINITY;
		validateVertices(sources);
		bfs(G,sources);
	}

	//do bfs
	private void bfs(Digraph G, Iterable<Integer> sources){
		LinkedQueue<Integer> queue=new LinkedQueue<>();
		for(int s:sources){
			marked[s]=true;
			distTo[s]=0;
			queue.enqueue(s);
		}

		while(!queue.isEmpty()){
			int v=queue.dequeue();
			for(int w:G.adj(v)){
				if(!marked[w]){
					marked[w]=true;
					edgeTo[w]=v;
					distTo[w]=distTo[v]+1;
					queue.enqueue(w);
				}
			}
		}
	}

	public boolean hasPathTo(int v){
		/********************************
		 * Description: has path to v from the {@code s} vertex
		 *
		 * @param v the vertex
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		validateVertex(v);
		return marked[v];
	}

	public Iterable<Integer> pathTo(int v){
		/********************************
		 * Description: return the path to v
		 *
		 * @param v
		 * @return: java.lang.Iterable<java.lang.Integer>
		 * @Author: luibebetter
		 *********************************/
		validateVertex(v);
		if(!hasPathTo(v)) return null;
		LinkedStack<Integer> stack=new LinkedStack<>();
		while(distTo[v]!=0){
			stack.push(v);
			v=edgeTo[v];
		}
		stack.push(v);
		return stack;
	}

	public int distTo(int v){
		/********************************
		 * Description: return the shortest distance to {@code v},return INFINITY if unreachable

		 * @param v
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		validateVertex(v);
		return distTo[v];
	}

	/********************************************************
	 * Helper function
	 ********************************************************/
	private void validateVertex(int v){
		int V=marked.length;
		if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	private void validateVertices(Iterable<Integer> sources){
		int V=marked.length;
		if(sources==null) throw new IllegalArgumentException("the sources is null");
		for (int v:sources){
			if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
		}
	}

	//unit test
	public static void main(String[] args) {
		InputStream in;
		try{
			in=new FileInputStream(args[0]);
		}catch(IOException e){
			throw new IllegalArgumentException("can't open the file "+args[0]);
		}

		Digraph G=new Digraph(in);

		int s = Integer.parseInt(args[1]);
		DirectedPathsBFS dfs = new DirectedPathsBFS(G, s);

		for (int v = 0; v < G.V(); v++) {
			if (dfs.hasPathTo(v)) {
				System.out.printf("%d to %d(%d):  ", s, v,dfs.distTo(v));
				for (int x : dfs.pathTo(v)) {
					if (x == s) System.out.print(x);
					else        System.out.print("-" + x);
				}
				System.out.println();
			}

			else {
				System.out.printf("%d to %d:  not connected\n", s, v);
			}

		}
	}

}
