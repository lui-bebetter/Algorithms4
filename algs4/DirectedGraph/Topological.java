/**********************************************************************
 *FileName: Topological
 *Author:   lui1993
 *Date:     2018/2/1123:17
 *Description:The Topological class represents a data type for determining
  a topological order of a directed acyclic graph (DAG).
  Recall, a digraph has a topological order if and only if it is a DAG.
  The hasOrder operation determines whether the digraph has a topological
  order, and if so, the order operation returns one.
 **********************************************************************/

package algs4.DirectedGraph;

import algs4.EdgeWeightedDigraph.EdgeWeightedDigraph;


/********************************************************
 *@author lui1993
 *@create 2018/2/11
 *Description:This implementation uses depth-first search.
  The constructor takes time proportional to V + E (in the
  worst case), where V is the number of vertices and E is
  the number of edges.
  Afterwards, the hasOrder and rank operations takes constant
  time; the order operation takes time proportional to V.
 ********************************************************/
public class Topological {
	private Iterable<Integer> order;//the reversePost order
	private int [] rank;// rank[v] = rank of vertex v in topological order

	//computing the topological order in a digraph
	public Topological(Digraph G){
		if(G==null) throw new IllegalArgumentException("calls Topological() with null arguments");
		//is a DAG?
		DirectedCycle cycle=new DirectedCycle(G);
		if(cycle.hasCycle()) return;

		DepthFirstOrder dfs=new DepthFirstOrder(G);
		order=dfs.reversePost();
		rank=new int[G.V()];
		int count=0;
		for (Integer i : order) {
			rank[i]=count++;
		}
	}

	//computing the topological order in a edge-weighted digraph
	public Topological(EdgeWeightedDigraph G){
		if(G==null) throw new IllegalArgumentException("calls Topological() with null arguments");
		//is a DAG?
		DirectedCycle cycle=new DirectedCycle(G);
		if(cycle.hasCycle()) return;

		DepthFirstOrder dfs=new DepthFirstOrder(G);
		order=dfs.reversePost();
		rank=new int[G.V()];
		int count=0;
		for (Integer i : order) {
			rank[i]=count++;
		}
	}

	//is a DAG?
	public boolean hasOrder(){
		return order!=null;
	}

	public Iterable<Integer> order(){
		return order;
	}

	public int rank(int v){
		validateVertex(v);
		return rank[v];
	}

	private void validateVertex(int v){
		int V=rank.length;
		if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

}
