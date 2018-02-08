/**********************************************************************
 *FileName: Edge
 *Author:   luibebetter
 *Date:     2018/2/112:04
 *Description:The Edge class represents a weighted edge in an EdgeWeightedGraph
 **********************************************************************/

package algs4.EdgeWeightedGraph;

/*************************************************************************
 *@author: luibebetter
 *@create: 2018/2/1
 *Description: Each edge consists of two integers (naming the two vertices)
  and a real-value weight. The data type provides methods for accessing the
  two endpoints of the edge and the weight.
  The natural order for this data type is by ascending order of weight
 **************************************************************************/
public class Edge implements Comparable<Edge> {
	private final int v;
	private final int w;
	private final double weight;

	//Initialize an edge
	public Edge(int v, int w, double weight){
		if(v<0) throw new IllegalArgumentException("vertex index must be a nonnegative integer");
		if(w<0) throw new IllegalArgumentException("vertex index must be a nonnegative integer");
		if(Double.isNaN(weight)) throw new IllegalArgumentException("weight is NaN");
		this.v=v;
		this.w=w;
		this.weight=weight;
	}

	//return either endpoint of the edge
	public int either(){
		return v;
	}

	//return other endpoint
	public int other(int v){
		if(v==this.v) return this.w;
		else if(v==this.w) return this.v;
		else throw new IllegalArgumentException("illegal endpoint");
	}

	public double weight(){
		return weight;
	}

	@Override
	public String toString() {
		return String.format("%d-%d %.5f", v, w, weight);
	}

	public int compareTo(Edge that){
		if(this.weight<that.weight) return -1;
		else if(this.weight>that.weight) return 1;
		else return 0;
	}

	public static void main(String[] args) {
		Edge e = new Edge(12, 34, 5.67);
		System.out.println(e);
	}

}
