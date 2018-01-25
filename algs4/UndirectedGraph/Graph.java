/**********************************************************************
 *FileName: Graph
 *Author:   Dell
 *Date:     2018/1/123:09
 *Description:The Graph class represents an undirected graph of vertices
  named 0 through V-1
 **********************************************************************/

package algs4.UndirectedGraph;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

import algs4.stack.ResizingArrayStack;

/************************************************************************
 *@author: Dell
 *@create: 2018/1/1
 *Description:This implementation uses an adjacency-lists representation,
	which is a vertex-indexed array of Bag objects.
	All operations take constant time (in the worst case) except iterating over
	the vertices adjacent to a given vertex, which takes time proportional to
	the number of such vertices
 *************************************************************************/
public class Graph {
	private static final String NEWLINE=System.getProperty("line.separator");

	private final int vertices; //the number of vertices
	private int edges;//number of edges
	private Bag<Integer> [] adjcent;//vertex indexed array of {@code Bag }

	//Initializes an empty graph with V vertices and 0 edges
	public Graph(int v){
		if(v<0) throw new IllegalArgumentException("calls Graph() with null arguments");
		vertices=v;
		edges=0;
		adjcent=(Bag<Integer>[]) new Bag[v];
		for (int i=0;i<v;i++)
			adjcent[i]=new Bag<Integer>();
	}

	//Initializes a new graph that is a deep copy of G
	public Graph(Graph G) {
		this(G.V());
		this.edges=G.edges;
		for (int i = 0; i < G.V(); i++) {
			ResizingArrayStack<Integer> stack = new ResizingArrayStack<Integer>();
			for (int k : G.adjcent(i)) stack.push(k);
			for (int v : stack) adjcent[i].add(v);
		}
	}

	//add edge between vertex v and w
	public void addEdge(int v, int w){
		if(v<0||v>vertices-1||w<0||w>vertices-1) throw new IllegalArgumentException("calls addEdge() with illegal arguments");
		adjcent[v].add(w);
		adjcent[w].add(v);
		edges++;
	}

	public int E(){
		/********************************
		 * Description: return the number of edges
		 *
		 * @param
		 * @return: int the number of edges
		 * @Author: luibebetter
		 *********************************/
		return edges;
	}

	public int V(){
		/********************************
		 * Description: return the number of vertices
		 *
		 * @param
		 * @return: int the number of vertices
		 * @Author: luibebetter
		 *********************************/
		return vertices;
	}

	public int degree(int v){
		/********************************
		 * Description: return the degree of the vertex v
		 *
		 * @param v the vertex
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		if(v<0||v>vertices-1) throw new IllegalArgumentException("calls degree() with illegal arguments");
		return adjcent[v].size();
	}

	public Iterable<Integer> adjcent(int v){
		/********************************
		 * Description: return the adjcent vertex of v in the form of Iterable
		 *
		 * @param
		 * @return: java.lang.Iterable<java.lang.Integer>
		 * @Author: luibebetter
		 *********************************/
		if(v<0||v>vertices-1) throw new IllegalArgumentException("calls adjcent() with illegal arguments");
		return adjcent[v];
	}

	public String toString(){
		/********************************
		 * Description:the number of vertices <em>V</em>,
		   followed by the number of edges <em>E</em>,
		   followed by the <em>V</em> adjacency lists
		 *
		 * @param
		 * @return: java.lang.String
		 * @Author: luibebetter
		 *********************************/
		StringBuilder s=new StringBuilder();
		s.append(vertices+" vertices, "+edges+" edges"+NEWLINE);
		for(int i=0;i<vertices;i++){
			s.append(i+": ");
			for(int v:adjcent[i]) s.append(v+" ");
			s.append(NEWLINE);
		}
		return s.toString();
	}

	public static void main(String[] args) {
		try {
			Scanner in =new Scanner(System.in);
			int V= in.nextInt();
			if (V < 0) throw new IllegalArgumentException("number of vertices in a Graph must be nonnegative");
			Graph G=new Graph(V);

			int E = in.nextInt();
			if (E < 0) throw new IllegalArgumentException("number of edges in a Graph must be nonnegative");
			for (int i = 0; i < E; i++) {
				int v = in.nextInt();
				int w = in.nextInt();
				G.addEdge(v, w);
			}
			Graph copyG=new Graph(G);
			System.out.println(copyG);
			System.out.println(G);
		}
		catch (NoSuchElementException e) {
			throw new IllegalArgumentException("invalid input format in Graph constructor", e);
		}

	}


}
