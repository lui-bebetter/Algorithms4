/**********************************************************************
 *FileName: FlowEdge
 *Author:   lui1993
 *Date:     2018/2/1717:04
 *Description:The FlowEdge class represents a capacitated edge with a
  flow in a FlowNetwork.
 **********************************************************************/

package algs4.MaxFlow_MinCut;

/*************************************************************************
 *@author lui1993
 *@create 2018/2/17
 *Description:Each edge consists of two integers (naming the two vertices),
  a real-valued capacity, and a real-valued flow. The data type provides
  methods for accessing the two endpoints of the directed edge and the weight.
  It also provides methods for changing the amount of flow on the edge and
  determining the residual capacity of the edge.
 *************************************************************************/
public class FlowEdge {
	// to deal with floating-point roundoff errors.
	private static final double FLOATING_POINT_EPSILON=1E-10;
	private final int v;//vertex the edge incident from
	private final int w;//vertex the edge incident to
	private final double capacity;
	private double flow;

	//Initialize the edge
	public FlowEdge(int v, int w, double capacity){
		if(v<0||w<0||capacity<0) throw new IllegalArgumentException("Initialize FlowEdge with illegal arguments.");
		this.v=v;
		this.w=w;
		this.capacity=capacity;
		this.flow=flow;
	}

	//Initializing with specified flow
	public FlowEdge(int v, int w, double capacity, double flow){
		if(v<0||w<0||capacity<0.0||flow<0.0) throw new IllegalArgumentException("Initialize FlowEdge with illegal arguments.");
		if(flow>capacity) throw new IllegalArgumentException("Illegal flow");
		this.v=v;
		this.w=w;
		this.capacity=capacity;
		this.flow=flow;
	}

	//copy constructor
	public FlowEdge(FlowEdge e){
		if(e==null) throw new IllegalArgumentException("Initialize FlowEdge with null arguments");
		this.v=e.v;
		this.w=e.w;
		this.capacity=e.capacity;
		this.flow=e.flow;
	}

	//the edge incident from
	public int from(){
		return v;
	}

	//the edge incident to
	public int to(){
		return w;
	}

	//the other endpoint in the edge
	public int other(int v){
		if(v==this.v) return this.w;
		else if(v==this.w) return this.v;
		else throw new IllegalArgumentException(v+" not in the edge.");
	}

	//return the capacity of the edge
	public double capacity(){
		return capacity;
	}

	//current flow added to the edge
	public double flow(){
		return flow;
	}

	//the residual capacity to the {@code v} vertex
	public double residualCapacityTo(int v){
		if(v==this.v) return flow;//backward edge
		else if(v==this.w) return capacity-flow;//forward edge
		else throw new IllegalArgumentException("calls residualCapacityTo() with illegal arguments." );
	}

	//Increases the flow on the edge in the direction to the given vertex.
	public void addResidualFlowTo(int v,double delta){
		if(!(delta>=0)) throw new IllegalArgumentException("Delta must be non-negative.");
		if(v==this.v) flow-=delta;//backward edge
		else if(v==this.w) flow+=delta;//forward edge
		else throw new IllegalArgumentException("vertex "+v+"not in the edge.");

		// round flow to 0 or capacity if within floating-point precision
		if(Math.abs(flow)<FLOATING_POINT_EPSILON) flow=0.0;
		if(Math.abs(flow-capacity)<FLOATING_POINT_EPSILON) flow=capacity;

		if(!(flow>=0)) throw new IllegalArgumentException("Flow is negative.");
		if(!(flow<=capacity)) throw new IllegalArgumentException("Flow exceeds capacity.");
	}

	public String toString(){
		return v + "->" + w + " " + flow + "/" + capacity;
	}

	/**
	 * Unit tests the {@code FlowEdge} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		FlowEdge e = new FlowEdge(12, 23, 4.56,0.0000000000000002);
		System.out.println(e);
		System.out.println(e.residualCapacityTo(23));

	}
}
