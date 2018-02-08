/**********************************************************************
 *FileName: DepthFirstOrder
 *Author:   luibebetter
 *Date:     2018/1/2121:29
 *Description:The DepthFirstOrder class represents a data type for
  determining depth-first search ordering of the vertices in a digraph
  or edge-weighted digraph, including preorder, postorder, and reverse postorder.
 **********************************************************************/

package algs4.DirectedGraph;

import algs4.queue.LinkedQueue;
import algs4.stack.LinkedStack;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**********************************************************
 *@author: luibebetter
 *@create: 2018/1/21
 *Description:This implementation uses depth-first search.
  The constructor takes time proportional to V + E (in the worst case),
  where V is the number of vertices and E is the number of edges.
  Afterwards, the preorder, postorder, and reverse postorder operation
  takes take time proportional to V.
 ***************************************************************/
public class DepthFirstOrder {
	private boolean [] marked;
	private int [] pre;//pre[v]=the preorder number of vertex {@code v}
	private int [] post;//post[v]=the postorder number of vertex v
	private int precount;
	private int postcount;
	private LinkedQueue<Integer> postorder=new LinkedQueue<>();
	private LinkedQueue<Integer> preorder=new LinkedQueue<>();
	private LinkedStack<Integer> reversePostorder=new LinkedStack<>();

	//constructor
	public DepthFirstOrder(Digraph G){
		if(G==null) throw new IllegalArgumentException("Initiating DepthFirstOrder with null digraph");
		int V=G.V();
		pre=new int[V];
		post=new int[V];
		marked=new boolean[V];
		for(int i=0;i<V;i++) {
			if(!marked[i]) dfs(G,i);
		}
	}

	private void dfs(Digraph G,int v){
		marked[v]=true;
		preorder.enqueue(v);
		pre[v]=precount++;
		for(int w:G.adj(v)){
			if(!marked[w]){
				dfs(G,w);
			}
		}
		postorder.enqueue(v);
		reversePostorder.push(v);
		post[v]=postcount++;
	}

	public Iterable<Integer> post(){
		/********************************
		 * Description: return the post DFS order of the digraph
		 *
		 * @param
		 * @return: java.lang.Iterable<java.lang.Integer>
		 * @Author: luibebetter
		 *********************************/
		return postorder;
	}

	public int post(int v){
		/********************************
		 * Description: return the post order number of vertex {@code v}
		 *
		 * @param v the vertex
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		validateVertex(v);
		return post[v];
	}

	public Iterable<Integer> pre(){
		/********************************
		 * Description: return the pre DFS order
		 *
		 * @param
		 * @return: java.lang.Iterable<java.lang.Integer>
		 * @Author: luibebetter
		 *********************************/
		return preorder;
	}

	public int pre(int v){
		/********************************
		 * Description: return the pre order number
		 *
		 * @param v
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		validateVertex(v);
		return pre[v];
	}

	public Iterable<Integer> reversePost(){
		/********************************
		 * Description:
		 *
		 * @param
		 * @return: java.lang.Iterable<java.lang.Integer>
		 * @Author: luibebetter
		 *********************************/
		return reversePostorder;
	}

	private void validateVertex(int v){
		int V=marked.length;
		if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	/**
	 * Unit tests the {@code DepthFirstOrder} data type.
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

		DepthFirstOrder dfs = new DepthFirstOrder(G);
		System.out.println("   v  pre post");
		System.out.println("--------------");
		for (int v = 0; v < G.V(); v++) {
			System.out.printf("%4d %4d %4d\n", v, dfs.pre(v), dfs.post(v));
		}

		System.out.print("Preorder:  ");
		for (int v : dfs.pre()) {
			System.out.print(v + " ");
		}
		System.out.println();

		System.out.print("Postorder: ");
		for (int v : dfs.post()) {
			System.out.print(v + " ");
		}
		System.out.println();

		System.out.print("Reverse postorder: ");
		for (int v : dfs.reversePost()) {
			System.out.print(v + " ");
		}
		System.out.println();


	}


}
