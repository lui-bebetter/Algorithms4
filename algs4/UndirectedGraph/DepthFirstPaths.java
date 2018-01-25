/**********************************************************************
 *FileName: DepthFirstPaths
 *Author:   Dell
 *Date:     2018/1/216:55
 *Description:The DepthFirstPaths class represents a data type for finding
  paths from a source vertex s to every other vertex in an undirected graph
 **********************************************************************/

package algs4.UndirectedGraph;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import algs4.stack.LinkedStack;
import algs4.queue.LinkedQueue;

/***********************************************************************************
 *@author: Dell
 *@create: 2018/1/2
 *Description:This implementation uses depth-first search.

 *The constructor takes time proportional to V + E, where V is the number of vertices
  and E is the number of edges.

 *Each call to hasPathTo(int) takes constant time;

 *each call to pathTo(int) takes time proportional to the length of the path.

 *It uses extra space (not including the graph) proportional to V.
 *************************************************************************************/
public class DepthFirstPaths {
	private boolean [] marked;//{@code true} means has been visited
	private int [] edgeTo;//edge[v]=last edge on s-v path
	private final int s;

	public DepthFirstPaths(Graph G, int s){
		if(s<0||s>G.V()-1) throw new IllegalArgumentException("calls DepthFirstPaths() with illegal arguments");
		this.s=s;
		marked=new boolean[G.V()];
		edgeTo=new int[G.V()];
		for(int i=0;i<G.V();i++){
			marked[i]=false;
		}
		dfs(G,s);
	}

	private void dfs(Graph G, int v){
		/********************************
		 * Description:Computes a path between {@code v} and
		   every other vertex in graph {@code G}

		 * @param v
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		marked[v]=true;
		for(int w:G.adjcent(v))
		{
			if(!marked[w]){
				edgeTo[w]=v;
				dfs(G,w);
			}
		}
	}

	//s has path to v?
	public boolean hasPathTo(int v){
		if(v<0||v>marked.length-1) throw new IllegalArgumentException("calls DepthFirstPaths() with illegal arguments");
		return marked[v];
	}

	public Iterable<Integer> pathTo(int v){
		/********************************
		 * Description: find the path from s to vertex v
		 *
		 * @param v
		 * @return: java.lang.Iterable<java.lang.Integer>
		 * @Author: luibebetter
		 *********************************/
		if(v<0||v>marked.length-1) throw new IllegalArgumentException("calls DepthFirstPaths() with illegal arguments");
		LinkedStack<Integer> stack=new LinkedStack<Integer>();
		if(!hasPathTo(v)) return stack;
		while(v!=s){
			stack.push(v);
			v=edgeTo[v];
		}
		stack.push(s);
		return stack;
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
		DepthFirstPaths dfs = new DepthFirstPaths(G, s);

		for (int v = 0; v < G.V(); v++) {
			if (dfs.hasPathTo(v)) {
				System.out.printf("%d to %d:  ", s, v);
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
