/**********************************************************************
 *FileName: Cycle
 *Author:   Dell
 *Date:     2018/1/321:08
 *Description:The Cycle class represents a data type for determining
  whether an undirected graph has a cycle.

 *The hasCycle operation determines whether the graph has a cycle and,
  if so, the cycle operation returns one.
 **********************************************************************/

package algs4.UndirectedGraph;
import algs4.queue.LinkedQueue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**********************************************************************
 *@author: Dell
 *@create: 2018/1/3
 *Description:This implementation uses depth-first search.
  The constructor takes time proportional to V + E (in the worst case),
  where V is the number of vertices and E is the number of edges.
  Afterwards, the hasCycle operation takes constant time;
  the cycle operation takes time proportional to the length of the cycle.
 ************************************************************************/
public class Cycle {
	private boolean []marked;
	private int[] edgeTo;
	private LinkedQueue<Integer> cycle;
	private boolean hasCycle;

	public Cycle(Graph G){
		marked=new boolean[G.V()];
		edgeTo=new int[G.V()];
		hasCycle=false;
		if(hasParallelEdges(G)) return;

		for(int v=0;v<G.V()&&!hasCycle;v++){
			if(!marked[v]) dfs(G,-1,v);
		}
	}

	public boolean hasCycle(){
		return hasCycle;
	}

	public Iterable<Integer> cycle(){
		return cycle;
	}

	private void dfs(Graph G, int previous, int v){
		/********************************
		 * Description: do dfs.as long as find a cycle,return.
		 *
		 * @param G
		 * @param previous the previous vertex
		 * @param next the next vertex
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		marked[v]=true;
		for(int w:G.adjcent(v)){
			if(hasCycle) return;
			if(!marked[w]){
				edgeTo[w]=v;
				dfs(G,v,w);
			}else if(w!=previous){//w!=previous means the vertex w must be a vertex in the path of v
				hasCycle=true;
				cycle=new LinkedQueue<Integer>();
				int x=v;
				while(x!=w){
					cycle.enqueue(x);
					x=edgeTo[x];
				}
				cycle.enqueue(w);
				cycle.enqueue(v);
				return;
			}
		}
	}

	private boolean hasParallelEdges(Graph G){
		/********************************
		 * Description: do the ParallelEdges exception for dfs
		 *
		 * @param G
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		for(int v=0;v<G.V();v++){
			for(int w:G.adjcent(v)){
				if(marked[w]){
					hasCycle=true;
					cycle=new LinkedQueue<Integer>();
					cycle.enqueue(v);
					cycle.enqueue(w);
					cycle.enqueue(v);
					return true;
				}
				marked[w]=true;
			}

			for(int w:G.adjcent(v)) marked[w]=false;
		}
		return false;
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

		Cycle finder = new Cycle(G);
		if (finder.hasCycle()) {
			for (int v : finder.cycle()) {
				System.out.print(v + " ");
			}
			System.out.println();
		}
		else {
			System.out.println("Graph is acyclic");
		}

	}
}
