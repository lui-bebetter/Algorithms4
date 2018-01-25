/**********************************************************************
 *FileName: DeluxeBFS
 *Author:   luibebetter
 *Date:     2018/1/2219:40
 *Description:The DeluxeBFS class represents a data type for finding
  shortest ancestral path for two or two set of vertices.
 **********************************************************************/


import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
/*************************************************************************
 *@author: luibebetter
 *@create: 2018/1/22
 *Description:An ancestral path between two vertices v and w in a digraph
  is a directed path from v to a common ancestor x, together with a directed
  path from w to the same ancestor x. A shortest ancestral path is an ancestral
  path of minimum total length.

 *The length() and ancestor() take constant time, the constructor takes time
  proportional to E+V in the worst case.
 *****************************************************************************/
public class DeluxeBFS {
	private static final int INIFINITY=Integer.MAX_VALUE;
	private static final int VMARKED=1;//visited by v marked as VMARKED
	private static final int WMARKED=2;//visited by w marked as WMARKED
	private static final int VWMARKED=3;//visited by w and v as VWMARKED
	private  int shortestLength=INIFINITY;
	private  int ancestor=-1;
	private int [] marked;//visited by v or w or neither?
	private int [][] distTo;//number of edges from sources in the SAP:distTo[0][i]=distTov,distTo[1][i]=distTow
	//private int []edgeTo;

	//find the shortest ancestral path for two vertices
	public DeluxeBFS(Digraph G, int v, int w ) {
		if (G == null) throw new IllegalArgumentException("Initiate DeluxeBFS with null digraph");
		marked = new int[G.V()];
		distTo = new int[2][G.V()];
		//edgeTo=new int[G.V()];
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < G.V(); j++)
				distTo[i][j] = INIFINITY;
		validateVertex(v);
		validateVertex(w);

		//handler the corner case
		if (v == w) {
			shortestLength = 0;
			ancestor = v;
			return;
		}

		//do bfs
		LinkedQueue<Integer> queue = new LinkedQueue<>();
		queue.enqueue(v);
		queue.enqueue(w);
		//initiate v and w
		marked[v] = VMARKED;
		marked[w] = WMARKED;
		distTo[marked[v] - 1][v] = 0;
		distTo[marked[w] - 1][w] = 0;

		while (!queue.isEmpty()) {
			int j = queue.dequeue();

			//preprocessing
			if (marked[j] == VWMARKED) {
				if (distTo[0][j] > shortestLength) {
					if (distTo[1][j] > shortestLength) return;
					else marked[j] = WMARKED;
				} else if (distTo[1][j] > shortestLength) marked[j] = VMARKED;
			} else if (distTo[marked[j] - 1][j] > shortestLength) {
				return;
			}

			//visit adjacent vertices
			for (int k : G.adj(j)) {
				if (marked[k] == 0) {
					marked[k] = marked[j];

					//set the distance
					if (marked[k] == VWMARKED) {
						distTo[0][k] = distTo[0][j] + 1;
						distTo[1][k] = distTo[1][j] + 1;
					} else distTo[marked[k] - 1][k] = distTo[marked[j] - 1][j] + 1;
					queue.enqueue(k);
				} else if (marked[k] != marked[j]) {//find the ancestor
					if (marked[j] == VWMARKED) {
						int fromV, fromW;
						if (marked[k] == VMARKED) {
							fromW = distTo[WMARKED - 1][j] + 1;
							fromV = distTo[VMARKED - 1][k];
						} else {
							fromW = distTo[marked[k] - 1][k];
							fromV = distTo[VMARKED - 1][j] + 1;
						}
						if (fromV + fromW < shortestLength) {
							ancestor = k;
							shortestLength = fromV + fromW;
							marked[k] = VWMARKED;
							distTo[VMARKED - 1][k] = fromV;
							distTo[WMARKED - 1][k] = fromW;
							queue.enqueue(k);
						}
					} else if(marked[k]!=VWMARKED){
						int length = distTo[marked[k]-1][k] + distTo[marked[j]-1][j] + 1;
						if (length < shortestLength) {
							ancestor = k;
							shortestLength = length;
							marked[k]=VWMARKED;
							distTo[marked[j]-1][k]=distTo[marked[j]-1][j] + 1;
							queue.enqueue(k);
						}
					}
				}
			}
		}
	}

	//find the shortest ancestral path for two sets of vertices
	public DeluxeBFS(Digraph G,Iterable<Integer> v, Iterable<Integer> w){
		if(G==null) throw new IllegalArgumentException("Initiate DeluxeBFS with null digraph");
		marked=new int[G.V()];
		distTo=new int[2][G.V()];
		//edgeTo=new int[G.V()];
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < G.V(); j++)
				distTo[i][j] = INIFINITY;
		validateVertices(v);
		validateVertices(w);
		for(int r:v){
			for(int s:w){
				if(r==s){
					shortestLength=0;
					ancestor=r;
					return;
				}
			}
		}

		//do bfs
		LinkedQueue<Integer> queue=new LinkedQueue<>();
		//initiate
		for(int i:v){
			queue.enqueue(i);
			marked[i]=VMARKED;
			distTo[VMARKED-1][i]=0;
		}
		for(int i:w){
			queue.enqueue(i);
			marked[i]=WMARKED;
			distTo[WMARKED-1][i]=0;
		}

		while (!queue.isEmpty()) {
			int j = queue.dequeue();

			//preprocessing
			if (marked[j] == VWMARKED) {
				if (distTo[0][j] > shortestLength) {
					if (distTo[1][j] > shortestLength) return;
					else marked[j] = WMARKED;
				} else if (distTo[1][j] > shortestLength) marked[j] = VMARKED;
			} else if (distTo[marked[j] - 1][j] > shortestLength) {
				return;
			}

			//visit adjacent vertices
			for (int k : G.adj(j)) {
				if (marked[k] == 0) {
					marked[k] = marked[j];

					//set the distance
					if (marked[k] == VWMARKED) {
						distTo[0][k] = distTo[0][j] + 1;
						distTo[1][k] = distTo[1][j] + 1;
					} else distTo[marked[k] - 1][k] = distTo[marked[j] - 1][j] + 1;
					queue.enqueue(k);
				} else if (marked[k] != marked[j]) {//find the ancestor
					if (marked[j] == VWMARKED) {
						int fromV, fromW;
						if (marked[k] == VMARKED) {
							fromW = distTo[WMARKED - 1][j] + 1;
							fromV = distTo[VMARKED - 1][k];
						} else {
							fromW = distTo[marked[k] - 1][k];
							fromV = distTo[VMARKED - 1][j] + 1;
						}
						if (fromV + fromW < shortestLength) {
							ancestor = k;
							shortestLength = fromV + fromW;
							marked[k] = VWMARKED;
							distTo[VMARKED - 1][k] = fromV;
							distTo[WMARKED - 1][k] = fromW;
							queue.enqueue(k);
						}
					} else if(marked[k]!=VWMARKED){
						int length = distTo[marked[k]-1][k] + distTo[marked[j]-1][j] + 1;
						if (length < shortestLength) {
							ancestor = k;
							shortestLength = length;
							marked[k]=VWMARKED;
							distTo[marked[j]-1][k]=distTo[marked[j]-1][j] + 1;
							queue.enqueue(k);
						}
					}
				}
			}
		}

	}

	public int length(){
		/********************************
		 * Description:length of shortest ancestral path between v and w; -1 if no such path
		 *
		 * @param
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		if(shortestLength==INIFINITY) return -1;
		return shortestLength;
	}

	public int ancestor(){
		/********************************
		 * Description:a common ancestor of v and w that participates in
		   a shortest ancestral path; -1 if no such path
		 *
		 * @param
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		return ancestor;
	}

	/********************************************************
	 * Helper function
	 ********************************************************/
	private void validateVertex(int v){
		int V=marked.length;
		if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	private void validateVertices(Iterable<Integer> sources){
		int V=marked.length;
		if(sources==null) throw new IllegalArgumentException("the sources is null");
		for (int v:sources){
			if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
		}
	}

	/***********************************************************
	 *Unit Test
	 */
	public static void main(String[] args) {
		In in =new In(args[0]);
		Digraph G=new Digraph(in);

		while (!StdIn.isEmpty()){
			int v = StdIn.readInt();
			int w = StdIn.readInt();
			DeluxeBFS sap=new DeluxeBFS(G,v,w);
			int length   = sap.length();
			int ancestor = sap.ancestor();
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}
}
