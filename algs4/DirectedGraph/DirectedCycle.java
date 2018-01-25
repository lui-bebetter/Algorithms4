/**********************************************************************
 *FileName: DirectedCycle
 *Author:   luibebetter
 *Date:     2018/1/2022:18
 *Description:The DirectedCycle class represents a data type for determining
  whether a digraph has a directed cycle
 **********************************************************************/

package algs4.DirectedGraph;

import algs4.stack.LinkedStack;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/*************************************************************************
 *@author: luibebetter
 *@create: 2018/1/20
 *Description:This implementation uses depth-first search.
  The constructor takes time proportional to V + E (in the worst case),
  where V is the number of vertices and E is the number of edges.
  Afterwards, the hasCycle operation takes constant time;
  the cycle operation takes time proportional to the length of the cycle.
 *************************************************************************/
public class DirectedCycle {
	private boolean [] marked;
	private int [] edgeTo;
	private boolean [] onstack;//// onStack[v] = is vertex on the stack of vertices that have not been done?
	LinkedStack<Integer> cycle=null;

	/**
	 * Determines whether the digraph {@code G} has a directed cycle and, if so,
	 * finds such a cycle.
	 * @param G the digraph
	 */
	public DirectedCycle(Digraph G){
		if(G==null) throw new IllegalArgumentException("calls DirectedCycle() with null arguments");
		marked=new boolean[G.V()];
		edgeTo=new int [G.V()];
		onstack=new boolean[G.V()];
		for(int s=0;s<G.V();s++)
			if(!marked[s]&&cycle==null) dfs(G,s);
	}

	//do dfs
	private void dfs(Digraph G,int s){
		marked[s]=true;
		onstack[s]=true;//add to the processing stack
		for(int v:G.adjcent(s)){
			if(cycle!=null) return;
			if(!marked[v]){
				edgeTo[v]=s;
				dfs(G,v);
			}else if(onstack[v]){
				cycle=new LinkedStack<>();
				int x=s;
				while(x!=v){
					cycle.push(x);
					x=edgeTo[x];
				}
				cycle.push(x);
				cycle.push(s);
			}
		}
		onstack[s]=false;//delete from the processing stack
	}

	public boolean hasCycle(){
		/********************************
		 * Description: has directed cycle?
		 *
		 * @param
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		return cycle!=null;
	}

	public Iterable<Integer> cycle(){
		/********************************
		 * Description: return a directed cycle
		 *
		 * @param
		 * @return: java.lang.Iterable<java.lang.Integer>
		 * @Author: luibebetter
		 *********************************/
		return cycle;
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

		DirectedCycle finder = new DirectedCycle(G);
		if (finder.hasCycle()) {
			System.out.print("Directed cycle: ");
			for (int v : finder.cycle()) {
				System.out.print(v + " ");
			}
			System.out.println();
		}

		else {
			System.out.println("No directed cycle");
		}
		System.out.println();
	}


}
