/**********************************************************************
 *FileName: BreadthFirstPaths
 *Author:   Dell
 *Date:     2018/1/221:01
 *Description:The BreadthFirstPaths class represents a data type for
   finding shortest paths (number of edges) from a source vertex s
   (or a set of source vertices) to every other vertex in an undirected
   graph.
 **********************************************************************/
package algs4.UndirectedGraph;

import algs4.queue.LinkedQueue;
import algs4.stack.LinkedStack;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;


/******************************************************************************ssss
 *@author: Dell
 *@create: 2018/1/2
 *Description:This implementation uses breadth-first search.
   The constructor takes time proportional to V + E,
   where V is the number of vertices and E is the number of edges.
   Each call to distTo(int) and hasPathTo(int) takes constant time;
   each call to pathTo(int) takes time proportional to the length of the path.
   It uses extra space (not including the graph) proportional to V.
 ******************************************************************************/
public class BreadthFirstPaths {
	private static final int INFINITY=Integer.MAX_VALUE;
	private boolean [] marked;//has been visited
	private int [] edgeTo;//the previous vertex in the path from source to the current vertex
	private int [] distTo;//the number of edges in the path from the sources

	//Computes the shortest path between the source vertex {@code s}and every other vertex in the graph {@code G}
	public BreadthFirstPaths(Graph G, int s){
		marked=new boolean[G.V()];
		edgeTo=new int[G.V()];
		distTo=new int [G.V()];
		for(int i=0;i<G.V();i++){
			distTo[i]=INFINITY;
		}
		validateVertex(s);
		bfs(G,s);
	}

	// breadth-first search from a single source
	private void bfs(Graph G, int s){
		marked[s]=true;
		distTo[s]=0;
		LinkedQueue<Integer> queue=new LinkedQueue<Integer>();
		queue.enqueue(s);

		while(!queue.isEmpty()){
			int v=queue.dequeue();
			for (int w:G.adjcent(v)){
				if(!marked[w]){
					marked[w]=true;
					distTo[w]=distTo[v]+1;
					edgeTo[w]=v;
					queue.enqueue(w);
				}
			}
		}
	}

	public BreadthFirstPaths(Graph G, Iterable<Integer> sources){
		marked=new boolean[G.V()];
		edgeTo=new int[G.V()];
		distTo=new int [G.V()];
		for(int i=0;i<G.V();i++){
			distTo[i]=INFINITY;
		}
		validateVertices(sources);
		bfs(G,sources);
	}

	private void bfs(Graph G, Iterable<Integer> sources){
		/********************************
		 * Description:breadth-first search from multiple sources
		 *
		 * @param G
		 * @param sources
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		LinkedQueue<Integer> queue=new LinkedQueue<Integer>();
		for(int v:sources){
			marked[v]=true;
			distTo[v]=0;
			queue.enqueue(v);
		}

		while(!queue.isEmpty()){
			int v=queue.dequeue();
			for(int w:G.adjcent(v)){
				if(!marked[w]){
					marked[w]=true;
					distTo[w]=distTo[v]+1;
					edgeTo[w]=v;
					queue.enqueue(w);
				}
			}
		}

	}

	public boolean hasPathTo(int v){
		/********************************
		 * Description: is the v vertex connected to the source
		 *
		 * @param v the vertex
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		validateVertex(v);
		return marked[v];
	}

	public int distTo(int v){
		/********************************
		 * Description: number of edges between the source and the {@code v}
		 *
		 * @param v the vertex
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		validateVertex(v);
		return distTo[v];
	}

	public Iterable<Integer> pathTo(int v){
		/********************************
		 * Description: return the path from the sources to {@code v}
		 *
		 * @param v
		 * @return: java.lang.Iterable<java.lang.Integer>
		 * @Author: luibebetter
		 *********************************/
		LinkedStack<Integer> stack=new LinkedStack<Integer>();
		validateVertex(v);
		if(!hasPathTo(v)) return stack;
		int w;
		for(w=v;distTo[w]!=0;w=edgeTo[w]){
			stack.push(w);
		}
		stack.push(w);
		return stack;
	}

	//v between 0 and V-1?
  private void validateVertex(int v){
		int V=marked.length;
		if(v<0||v>V-1) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
  }

  private void validateVertices(Iterable<Integer> sources){
		if(sources==null) throw new IllegalArgumentException("calls with null arguments");
		int V=marked.length;
		for (int v:sources){
			if (v<0||v>V-1)  throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
		}
  }

	public static void main(String[] args) {
		Scanner in;
		try {
			File file = new File(args[0]);
			in=new Scanner(new BufferedInputStream(new FileInputStream(file)),"UTF-8");
		}catch(IOException E){
			throw new IllegalArgumentException("can't open file:"+args[0]);
		}

		int V= in.nextInt();
		if (V < 0) throw new IllegalArgumentException("number of vertices in a Graph must be nonnegative");
		Graph G=new Graph(V);
		int E = in.nextInt();
		if (E < 0) throw new IllegalArgumentException("number of edges in a Graph must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.nextInt();
			int w = in.nextInt();
			G.addEdge(v, w);
		}

		int s = Integer.parseInt(args[1]);
		BreadthFirstPaths bfs = new BreadthFirstPaths(G, s);

		for (int v = 0; v < G.V(); v++) {
			if (bfs.hasPathTo(v)) {
				System.out.printf("%d to %d(%d):  ", s, v,bfs.distTo(v));
				for (int x : bfs.pathTo(v)) {
					if (x == s) System.out.print(x);
					else        System.out.print("-" + x);
				}
				System.out.println();
			}

			else {
				System.out.printf("%d to %d(-):  not connected\n", s, v);
			}

		}
	}


}
