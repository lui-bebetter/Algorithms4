/**********************************************************************
 *FileName: Bipartite
 *Author:   Dell
 *Date:     2018/1/319:43
 *Description:The Bipartite class represents a data type for determining
  whether an undirected graph is bipartite or whether it has an odd-length
  cycle.
 **********************************************************************/

package algs4.UndirectedGraph;

import  algs4.queue.LinkedQueue;
import algs4.stack.LinkedStack;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/******************************************************************************
 *@author: Dell
 *@create: 2018/1/3
 *Description:This implementation uses breadth-first search and is nonrecursive.
  The constructor takes time proportional to V + E (in the worst case), where V
  is the number of vertices and E is the number of edges.
  Afterwards, the isBipartite and color operations take constant time;
  the oddCycle operation takes time proportional to the length of the cycle.
 *******************************************************************************/
public class Bipartite {
  private static final boolean BLACK=true;
  private static final boolean RED=false;
  private boolean [] marked;//marked[v]={@code v}has been visited?
  private int [] edgeTo;//the previous vertex in the path from the source vertex
  private boolean []color;//the color of the corresponding vertex
  private boolean isBipartite;//is Bipartite?
  private LinkedQueue<Integer> cycle;//the odd length cycle

  public Bipartite(Graph G){
    isBipartite=true;
    marked=new boolean[G.V()];
    edgeTo=new int [G.V()];
    color=new boolean[G.V()];

    //do the bfs to determine whether the graph is Bipartite
    for(int i=0;i<G.V()&&isBipartite;i++){
      if(!marked[i]) bfs(G,i);
    }
  }

  /*the BreadthFirstSearch
   *Note:as long as two of the vertices satisfied distTo[w]=distTo[v] are connected, the graph is
     not a Bipartite.
   */
  private void bfs(Graph G, int s){
    marked[s]=true;
    LinkedQueue<Integer> queue=new LinkedQueue<Integer>();
    queue.enqueue(s);

    while(!queue.isEmpty()){
      int v=queue.dequeue();
      for(int w:G.adjcent(v)){
        if(!marked[w]){
          marked[w]=true;
          edgeTo[w]=v;
          color[w]=!color[v];
          queue.enqueue(w);
        }else if(color[w]==color[v]){//color[w]=color[v] means distTo[w]=distTo[v]
          isBipartite=false;

          cycle=new LinkedQueue<Integer>();
          LinkedStack<Integer> stack=new LinkedStack<Integer>();
          cycle.enqueue(w);
          int x=w,y=v;
          while(x!=y){
            stack.push(x);
            cycle.enqueue(y);
            x=edgeTo[x];
            y=edgeTo[y];
          }
          cycle.enqueue(x);

          while(!stack.isEmpty()) {
            cycle.enqueue(stack.pop());
          }
          return;
        }
      }
    }
  }

  //is Bipartite?
  public  boolean isBipartite(){
    return isBipartite;
  }

  public boolean color(int v){
    /********************************
     * Description:if isBipartite, return the color of the vertex {@code v}
     *
     * @param v
     * @return: boolean
     * @Author: luibebetter
     *********************************/
    validateVertex(v);
    if(!isBipartite()) throw new UnsupportedOperationException("Graph is not Bipartite");
    return color[v];
  }

  public Iterable<Integer> oddCycle(){
    /********************************
     * Description: if not Bipartite, return an an odd cycle
     *
     * @param
     * @return: java.lang.Iterable<java.lang.Integer>
     * @Author: luibebetter
     *********************************/
    return cycle;
  }

  private void validateVertex(int v){
    int V=marked.length;
    if(v<0||v>V-1) throw new IllegalArgumentException("vertex " + v +" is not between 0 and " + (V-1));
  }

  public static void main(String[] args) {
    Scanner in;
    try {
      File file = new File(args[0]);
      in=new Scanner(new BufferedInputStream(new FileInputStream(file)),"UTF-8");
    }catch(IOException E){
      throw new IllegalArgumentException("can't open file:"+args[0]);
    }

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

    Bipartite b = new Bipartite(G);

    if (b.isBipartite()) {
      System.out.println("Graph is bipartite");
      for (int v = 0; v < G.V(); v++) {
        System.out.println(v + ": " + b.color(v));
      }
    }
    else {
      System.out.print("Graph has an odd-length cycle: ");
      for (int x : b.oddCycle()) {
        System.out.print(x + " ");
      }
      System.out.println();
    }


  }

}
