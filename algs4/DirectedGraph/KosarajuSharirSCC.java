/**********************************************************************
 *FileName: KosarajuSharirSCC
 *Author:   luibebetter
 *Date:     2018/1/2122:29
 *Description:The KosarajuSharirSCC class represents a data type for
  determining the strongly-connected components in a digraph
 **********************************************************************/

package algs4.DirectedGraph;

import algs4.queue.LinkedQueue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/*************************************************************************
 *@author: luibebetter
 *@create: 2018/1/21
 *Description:This implementation uses the Kosaraju-Sharir algorithm.
  The constructor takes time proportional to V + E (in the worst case),
  where V is the number of vertices and E is the number of edges.
  Afterwards, the id, count, and areStronglyConnected operations take constant time.
 ****************************************************************************/
public class KosarajuSharirSCC {
	private int count;//number of components
	private int [] id;//component id
	private boolean [] marked;

	//the constructor
	public KosarajuSharirSCC(Digraph G){
		if(G==null)throw new  IllegalArgumentException("Initiates KosarajuSharirSCC with null digraph");
		count=0;
		id=new int[G.V()];
		marked=new boolean[G.V()];

		Digraph reverse=G.reverse();
		DepthFirstOrder order=new DepthFirstOrder(reverse);
		for(int v:order.reversePost()){
			if(!marked[v]){
				dfs(G,v);//do dfs in the original Digraph
				count++;
			}
		}
	}

	//do dfs in original digraph
	private void dfs(Digraph G, int v){
		marked[v]=true;
		id[v]=count;
		for(int w:G.adjcent(v)){
			if(!marked[w]){
				dfs(G,w);
			}
		}
	}

	public int count(){
		/********************************
		 * Description: return the number of strongly-connected components
		 *
		 * @param
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		return count;
	}

	public int id(int v){
		/********************************
		 * Description: return the component id of the vertex
		 *
		 * @param v the vertex
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		validateVertex(v);
		return id[v];
	}

	public boolean stronglyConnected(int v, int w){
		/********************************
		 * Description: is {@code v) and {@code w} strongly connected?
		 *
		 * @param v
		 * @param w
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		validateVertex(v);
		validateVertex(w);
		return id[v]==id[w];
	}

	private void validateVertex(int v){
		int V=marked.length;
		if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	/**
	 * Unit tests the {@code KosarajuSharirSCC} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		InputStream in;
		try{
			in=new FileInputStream(args[0]);
		}catch(IOException e){
			throw new IllegalArgumentException("can't open the file "+args[0]);
		}

		Digraph G=new Digraph(in);
		KosarajuSharirSCC scc = new KosarajuSharirSCC(G);

		// number of connected components
		int m = scc.count();
		System.out.println(m + " strong components");

		// compute list of vertices in each strong component
		LinkedQueue<Integer>[] components = (LinkedQueue<Integer>[]) new LinkedQueue[m];
		for (int i = 0; i < m; i++) {
			components[i] = new LinkedQueue<Integer>();
		}
		for (int v = 0; v < G.V(); v++) {
			components[scc.id(v)].enqueue(v);
		}

		// print results
		for (int i = 0; i < m; i++) {
			for (int v : components[i]) {
				System.out.print(v + " ");
			}
			System.out.println();
		}

	}
}
