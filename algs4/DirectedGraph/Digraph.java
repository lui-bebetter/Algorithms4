/**********************************************************************
 *FileName: Digraph
 *Author:   Dell
 *Date:     2018/1/1916:07
 *Description:The Digraph class represents a directed graph of vertices
  named 0 through V - 1.
  It supports the following two primary operations: add an edge to the
  digraph, iterate over all of the vertices adjacent from a given vertex.
  Parallel edges and self-loops are permitted.
 **********************************************************************/

package algs4.DirectedGraph;

import algs4.UndirectedGraph.Bag;
import algs4.UndirectedGraph.Graph;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**************************************************************************
 *@author: Dell
 *@create: 2018/1/19
 *Description:This implementation uses an adjacency-lists representation,
  which is a vertex-indexed array of Bag objects.
  All operations take constant time (in the worst case) except iterating
  over the vertices adjacent from a given vertex, which takes time
   proportional to the number of such vertices.
 ****************************************************************************/
public class Digraph {
  private static final String NEWLINE = System.getProperty("line.separator");

  private int V;//number of vertices
  private int E;//number of edges
  private Bag<Integer> [] adjcent;
  private int [] indegree;

  //initiate with number of vertices
  public Digraph(int V){
    if(V<0) throw new IllegalArgumentException("calls Digraph() with null arguments");
    this.V=V;
    E=0;
    adjcent=(Bag<Integer>[]) new Bag[V];
    for(int i=0;i<V;i++) adjcent[i]=new Bag<>();
    indegree=new int[V];
  }

  //initiate from InputStream
  public Digraph(InputStream input){
    try {
      Scanner in =new Scanner(new BufferedInputStream(input));
      this.V= in.nextInt();
      if (V < 0) throw new IllegalArgumentException("number of vertices in a Graph must be nonnegative");

      int E = in.nextInt();
      if (E < 0) throw new IllegalArgumentException("number of edges in a Graph must be nonnegative");

      adjcent=(Bag<Integer>[]) new Bag[V];
      for(int i=0;i<V;i++) adjcent[i]=new Bag<>();
      indegree=new int[V];

      for (int i = 0; i < E; i++) {
        int v = in.nextInt();
        int w = in.nextInt();
        addEdge(v, w);
      }
    }
    catch (NoSuchElementException e) {
      throw new IllegalArgumentException("invalid input format in Graph constructor", e);
    }
  }

  //copy constructor:Initializes a digraph that is a deep copy of the specified digraph
  public Digraph(Digraph G){
    this(G.V());
    for(int i=0;i<G.V();i++){
      for(int j:G.adjcent(i))
        addEdge(i,j);
    }
  }

  public void addEdge(int v, int w){
    /********************************
     * Description: add an edge from {@code v} to {@code w}
     *
     * @param v
     * @param w
     * @return: void
     * @Author: luibebetter
     *********************************/
    validateVertex(v);
    validateVertex(w);
    adjcent[v].add(w);
    indegree[w]++;
    E++;
  }

  public int E(){
    /********************************
     * Description: the number of edges
     *
     * @param
     * @return: int
     * @Author: luibebetter
     *********************************/
    return E;
  }

  public int V(){
    /********************************
     * Description: return the number of vertices
     *
     * @param
     * @return: int
     * @Author: luibebetter
     *********************************/
    return V;
  }

  public Iterable<Integer> adjcent(int v){
    /********************************
     * Description: return a iterable object that iterating over all the
       adjcent vertices for {@code v}

     * @param v the vertex
     * @return: java.lang.Iterable<java.lang.Integer>
     * @Author: luibebetter
     *********************************/
    validateVertex(v);
    return adjcent[v];
  }

  public int outdegree(int v){
    /********************************
     * Description: return number of vertices pointing from v
     *
     * @param v the vertex
     * @return: int
     * @Author: luibebetter
     *********************************/
    validateVertex(v);
    return adjcent[v].size();
  }

  public int indegree(int v){
    /********************************
     * Description: return number of vertices pointing to v
     *
     * @param v
     * @return: int
     * @Author: luibebetter
     *********************************/
    validateVertex(v);
    return indegree[v];
  }

  public Digraph reverse(){
    /********************************
     * Description: return the reverse Digraph
     *
     * @param
     * @return: algs4.DirectedGraph.Digraph
     * @Author: luibebetter
     *********************************/
    Digraph reverse=new Digraph(V);
    for(int i=0;i<V;i++){
      for(int w:adjcent(i)){
        reverse.addEdge(w,i);
      }
    }
    return reverse;
  }

  public String toString(){
    /********************************
     * Description: the string format of this class
     *
     * @param
     * @return: java.lang.String
     * @Author: luibebetter
     *********************************/
    StringBuilder s = new StringBuilder();
    s.append(V + " vertices, " + E + " edges " + NEWLINE);
    for (int v = 0; v < V; v++) {
      s.append(String.format("%d: ", v));
      for (int w : adjcent[v]) {
        s.append(String.format("%d ", w));
      }
      s.append(NEWLINE);
    }
    return s.toString();
  }

  private void validateVertex(int v){
    if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
  }

  public static void main(String[] args) {
    InputStream in;
    try {
      in=new FileInputStream(args[0]);
    }
    catch (IOException e) {
      throw new IllegalArgumentException("can't open the file "+args[0]);
    }
    Digraph G = new Digraph(in);
    System.out.println(G);
  }
}
