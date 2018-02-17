/**********************************************************************
 *FileName: DirectedEdge
 *Author:   lui1993
 *Date:     2018/2/1021:16
 *Description:The DirectedEdge class represents a weighted edge in an EdgeWeightedDigraph. Each edge consists of two integers (naming the two vertices) and a real-value weight. The data type provides methods for accessing the two endpoints of the directed edge and the weight.
 **********************************************************************/

package algs4.EdgeWeightedDigraph;

/********************************
 *@author lui1993
 *@create  2018/2/10
 *Description:
 ********************************/
public class DirectedEdge {
	private final int v;
	private final int w;
	private final double weight;

	//Initialize DirectedEdge:from v to w with specific weight
	public DirectedEdge(int from, int to, double weight){
		if (from < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
		if (to < 0) throw new IllegalArgumentException("Vertex names must be nonnegative integers");
		if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
		this.v=from;
		this.w=to;
		this.weight=weight;
	}

	//return the from vertex in the edge
	public int from(){
		return v;
	}

	//return the to vertex in the edge
	public int to(){
		return w;
	}

	//return the weight of  the edge
	public double weight(){
		return weight;
	}

	//the string representation of the directed edge
	public String toString(){
		return String.format("%d->%d,%5.2f",v, w,weight);
	}

	/**
	 * Unit tests the {@code DirectedEdge} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		DirectedEdge e = new DirectedEdge(12, 34, 5.67);
		System.out.println(e);
	}
}
