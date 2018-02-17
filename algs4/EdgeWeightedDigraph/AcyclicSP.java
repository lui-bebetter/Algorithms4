/**********************************************************************
 *FileName: AcyclicSP
 *Author:   lui1993
 *Date:     2018/2/1123:37
 *Description:The AcyclicSP class represents a data type for solving the
  single-source shortest paths problem in edge-weighted directed acyclic
  graphs (DAGs). The edge weights can be positive, negative, or zero.
 **********************************************************************/

package algs4.EdgeWeightedDigraph;

import algs4.DirectedGraph.Topological;
import algs4.PriorityQueue.IndexMinPQ;
import algs4.stack.LinkedStack;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/*********************************************************************
 *@author lui1993
 *@create 2018/2/11
 *Description:This implementation uses a topological-sort based algorithm.
  The constructor takes time proportional to V + E, where V is the number
  of vertices and E is the number of edges. Each call to distTo(int) and
  hasPathTo(int) takes constant time; each call to pathTo(int) takes time
  proportional to the number of edges in the shortest path returned.
 ********************************************************************/
public class AcyclicSP {
	private double []distTo;//sum of weight in the shortest path
	private DirectedEdge [] edgeTo;//the edge incident to v in the shortest path

	//computing the shortest path int the edge-weighted digraph from single source
	public AcyclicSP(EdgeWeightedDigraph G, int s){
		if(G==null) throw new IllegalArgumentException("Initializes AcyclicSP with null arguments");
		edgeTo=new DirectedEdge[G.V()];
		distTo=new double[G.V()];
		for (int i = 0; i < G.V(); i++) {
			distTo[i]=Double.POSITIVE_INFINITY;
		}

		validateVertex(s);
		distTo[s]=0;
		Topological topo=new Topological(G);
		if(!topo.hasOrder()) throw new IllegalArgumentException("The digraph is not a DAG");
		int rank=topo.rank(s);
		int count=0;
		for(int v:topo.order()){
			if(count++<rank) continue;
			for(DirectedEdge e:G.adj(v)){
				relax(e);
			}
		}
	}

	private void relax(DirectedEdge e){
		int w=e.to(),v=e.from();
		if(distTo[w]>distTo[v]+e.weight()){
			distTo[w]=distTo[v]+e.weight();
			edgeTo[w]=e;
		}
	}

	//path from s to v exist?
	public boolean hasPathTo(int v){
		validateVertex(v);
		return distTo[v]<Double.POSITIVE_INFINITY;
	}

	//sum of weight in the shortest path
	public double distTo(int v){
		validateVertex(v);
		return distTo[v];
	}

	//the shortest path from s to v
	public Iterable<DirectedEdge> pathTo(int v){
		validateVertex(v);
		if(!hasPathTo(v)) return null;

		LinkedStack<DirectedEdge> stack=new LinkedStack<>();
		while(edgeTo[v]!=null){
			stack.push(edgeTo[v]);
			v=edgeTo[v].from();
		}
		return stack;
	}

	private void validateVertex(int v){
		int V=distTo.length;
		if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	/**
	 * Unit tests the {@code DijkstraSP} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		InputStream in;
		try {
			in = new FileInputStream(args[0]);
		}catch (IOException e){
			throw new IllegalArgumentException("can't open the file "+args[0]);
		}
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
		int s = Integer.parseInt(args[1]);

		// compute shortest paths
		AcyclicSP sp = new AcyclicSP(G, s);


		// print shortest path
		for (int t = 0; t < G.V(); t++) {
			if (sp.hasPathTo(t)) {
				System.out.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
				for (DirectedEdge e : sp.pathTo(t)) {
					System.out.print(e + "   ");
				}
				System.out.println();
			}
			else {
				System.out.printf("%d to %d         no path\n", s, t);
			}
		}
	}
}
