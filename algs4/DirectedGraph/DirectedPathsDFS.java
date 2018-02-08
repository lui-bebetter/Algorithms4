/**********************************************************************
 *FileName: DirectedPathsDFS
 *Author:   Dell
 *Date:     2018/1/2019:50
 *Description:The DirectedPathsDFS class represents a data type for
  finding directed paths from a source vertex s to every other vertex
  in the digraph.
 **********************************************************************/

package algs4.DirectedGraph;

import algs4.stack.LinkedStack;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/*********************************************************************
 *@author: Dell
 *@create: 2018/1/20
 *Description:This implementation uses depth-first search.

 *The constructor takes time proportional to V + E,
  where V is the number of vertices and E is the number of edges.

 *Each call to hasPathTo(int) takes constant time;

 *each call to pathTo(int) takes time proportional to the length
  of the path returned.

 *It uses extra space (not including the graph) proportional to V.
 *************************************************************************/
public class DirectedPathsDFS {
	private boolean [] marked;//reachable from the source {@code s}?
	private int []edgeTo;//edgeTo[v] =the previous vertex in the path
	private final int source;// source vertex

	//initiatinting with the Digraph and the source
	public DirectedPathsDFS(Digraph G,int s){
		if(G==null) throw new IllegalArgumentException("initiating DirectedPathsDFS with null Digraph");
		marked=new boolean[G.V()];
		edgeTo=new int [G.V()];
		validateVertex(s);
		source=s;
		dfs(G,s);
	}

	public boolean hasPathTo(int v){
		/********************************
		 * Description: the source {@code s} has directed path to {@code v}?
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
		 * Description: return the path from {@code s} to {@code v}
		 *
		 * @param v
		 * @return: java.lang.Iterable<java.lang.Integer>
		 * @Author: luibebetter
		 *********************************/
		validateVertex(v);
		if(!hasPathTo(v)) return null;
		LinkedStack<Integer> stack=new LinkedStack<>();
		while(v!=source){
			stack.push(v);
			v=edgeTo[v];
		}
		stack.push(v);
		return stack;
	}

	/********************************************************
	 * Helper function
	 *******************************************************/
	private void validateVertex(int v){
		int V=marked.length;
		if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	private void dfs(Digraph G,int s){
		/********************************
		 * Description: run dfs from the source {@code s}
		 *
		 * @param G the Digraph
		 * @param s the source
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		marked[s]=true;
		for(int w:G.adj(s)){
			if(!marked[w]){
				edgeTo[w]=s;
				dfs(G,w);
			}
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
		DirectedPathsDFS dfs = new DirectedPathsDFS(G, s);

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
