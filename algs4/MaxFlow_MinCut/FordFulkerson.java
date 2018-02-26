/**********************************************************************
 *FileName: FordfulKerson
 *Author:   lui1993
 *Date:     2018/2/1722:42
 *Description:The FordFulkerson class represents a data type for computing
  a maximum st-flow and minimum st-cut in a flow network.
 **********************************************************************/

package algs4.MaxFlow_MinCut;

import algs4.queue.LinkedQueue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**********************************************************************
 *@author lui1993
 *@create 2018/2/17
 *Description:This implementation uses the Ford-Fulkerson algorithm
  with the shortest augmenting path heuristic. The constructor takes
  time proportional to E V (E + V) in the worst case and extra space
  (not including the network) proportional to V, where V is the number
  of vertices and E is the number of edges. In practice, the algorithm
  will run much faster. Afterwards, the inCut() and value() methods
  take constant time.

 *If the capacities and initial flow values are all integers, then this
  implementation guarantees to compute an integer-valued maximum flow.
  If the capacities and floating-point numbers, then floating-point
  roundoff error can accumulate.
 ************************************************************************/
public class FordFulkerson {
	private static final double FLOATING_POINT_EPSILON=1E-10;
	private double value;//the max-flow value
	private final int V;//number of vertices
	private boolean [] marked;
	private FlowEdge[] edgeTo;

	//computing the maxflow from source {@code s} to target {@code t}
	public FordFulkerson(FlowNetwork f, int s, int t){
		if(f==null) throw new IllegalArgumentException("calls FordfulKerson() with null arguments");
		this.V=f.V();
		validateVertex(s);
		validateVertex(t);

		//pre-check
		if(s==t) throw new IllegalArgumentException("Source equals sink");
		if(!isFeasible(f,s,t)) throw new IllegalArgumentException("Initial flow is infeasible");

		//initialize the max-flow value
		value=excess(f,t);
		while(hasAugmentingPath(f,s,t)){

			//find the minimal permitted flow
			double bottle=Double.POSITIVE_INFINITY;
			int tmp=t;
			while(edgeTo[tmp]!=null){
				FlowEdge e=edgeTo[tmp];
				if(bottle>e.residualCapacityTo(tmp)) bottle=e.residualCapacityTo(tmp);
				tmp=e.other(tmp);
			}

			//add the minimal flow to the edge along the augmenting path
			tmp=t;
			while(edgeTo[tmp]!=null){
				FlowEdge e=edgeTo[tmp];
				e.addResidualFlowTo(tmp,bottle);
				tmp=e.other(tmp);
			}
			value+=bottle;
		}

	}

	//Returns the value of the maximum flow
	public double value(){
		return value;
	}

	//Returns true if the specified vertex is on the {@code s} side of the mincut.
	public boolean inCut(int v){
		validateVertex(v);
		return marked[v];
	}

	//
	private boolean hasAugmentingPath(FlowNetwork f,int s,int t){

		marked=new boolean[f.V()];
		edgeTo=new FlowEdge[f.V()];

		//do bfs, find the shortest augmenting path
		LinkedQueue<Integer> queue=new LinkedQueue<>();
		marked[s]=true;
		queue.enqueue(s);

		while(!queue.isEmpty() && !marked[t]){
			int v=queue.dequeue();
			for(FlowEdge e:f.adj(v)){
				int w=e.other(v);
				if(!marked[w] && e.residualCapacityTo(w)>0){
					queue.enqueue(w);
					marked[w]=true;
					edgeTo[w]=e;
				}
			}
		}

		//is there a augmenting path
		return marked[t];
	}

	// return excess flow at vertex v:inflow-outflow
	private double excess(FlowNetwork f, int v){
		double excess=0.0;
		for(FlowEdge e:f.adj(v)){
			if(e.from()==v) excess-=e.flow();
			else if(e.to()==v) excess+=e.flow();
		}
		return excess;
	}

	//does the flow net work  have feasible max flow
	private boolean isFeasible(FlowNetwork f, int s ,int t){
		// check that capacity constraints are satisfied
		for(FlowEdge e:f.edges()){
			if(e.flow()<-FLOATING_POINT_EPSILON||e.flow()>e.capacity()+FLOATING_POINT_EPSILON) {
				System.err.println("Edge does not satisfy capacity constraints: " + e);
				return false;
			}
		}

		// check that net flow into a vertex equals zero, except at source and sink
		double maxFlow=excess(f,t);
		if(Math.abs(maxFlow+excess(f,s))>FLOATING_POINT_EPSILON) {
			System.err.println("Excess at source = " + excess(f, s));
			System.err.println("Max flow         = " + value);
			return false;
		}

		for (int i = 0; i < V; i++) {
			if(i==s||i==t) continue;
			if(Math.abs(excess(f,i))>FLOATING_POINT_EPSILON){
				System.err.println("Net flow out of " + i + " doesn't equal zero");
				return false;
			}
		}

		return true;
	}

	private void validateVertex(int v){
		if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	/**
	 * Unit tests the {@code FordFulkerson} data type.
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

		FlowNetwork G=new FlowNetwork(in);
		int s=0,t=G.V()-1;

		// compute maximum flow and minimum cut
		FordFulkerson maxflow = new FordFulkerson(G,s,t);
		System.out.println("Max flow from " + s + " to " + t);
		for (int v = 0; v < G.V(); v++) {
			for (FlowEdge e : G.adj(v)) {
				if ((v == e.from()) && e.flow() > 0)
					System.out.println("   " + e);
			}
		}

		// print min-cut
		System.out.print("Min cut: ");
		for (int v = 0; v < G.V(); v++) {
			if (maxflow.inCut(v)) System.out.print(v + " ");
		}
		System.out.println();

		System.out.println("Max flow value = " +  maxflow.value());
	}
}
