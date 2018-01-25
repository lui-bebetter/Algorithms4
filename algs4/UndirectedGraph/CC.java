/**********************************************************************
 *FileName: ComponentCount
 *Author:   Dell
 *Date:     2018/1/222:49
 *Description: Compute connected components using depth first search.
 **********************************************************************/

package algs4.UndirectedGraph;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import algs4.queue.LinkedQueue;
/********************************
 *@author: Dell
 *@create: 2018/1/2
 *Description:This implementation uses depth-first search.
   The constructor takes time proportional to V + E (in the worst case),
   where V is the number of vertices and E is the number of edges.
   Afterwards, the id, count, connected, and size operations take constant time.
 ********************************/
public class CC {
   private boolean [] marked;
   private int count;//number of components
   private int [] id;//id[i]=component id of vertex i
   private int [] size;//size[count]=number of vertices in the {@code count}component

   //Initializing with Graph G
   public CC(Graph G){
      if(G==null) throw new IllegalArgumentException("instantiate CC with null Graph");
      marked=new boolean[G.V()];
      id=new int [G.V()];
      size=new int [G.V()];
      count=0;

      //do the depth first search for all the vertices
      for(int i=0;i<G.V();i++){
         if(!marked[i]){
            dfs(G,i);
            count++;
         }
      }
   }

   private void dfs(Graph G, int v){
      marked[v]=true;
      id[v]=count;
      size[count]++;
      for(int w:G.adjcent(v)){
         if(!marked[w]){
            dfs(G,w);
         }
      }
   }

   public int id(int v){
      /********************************
       * Description: return the component id of the vertex v
       *
       * @param v the vertex
       * @return: int
       * @Author: luibebetter
       *********************************/
      validateVertex(v);
      return id[v];
   }

   public int size(int v){
      /********************************
       * Description: return the size of the component which the
         vertex {@code v} is belong to
       *
       * @param v
       * @return: int
       * @Author: luibebetter
       *********************************/
      validateVertex(v);
      return size[id[v]];
   }

   public int count(){
      /********************************
       * Description: return the number of components
       *
       * @param
       * @return: int
       * @Author: luibebetter
       *********************************/
      return count;
   }

   public boolean connected(int v, int w){
      /********************************
       * Description: is v connected to w
       *
       * @param v
       * @param w
       * @return: boolean
       * @Author: luibebetter
       *********************************/
      validateVertex(v);
      validateVertex(w);
      return id[v]==id[w];
   }

   private void validateVertex(int v){
      int V=marked.length;
      if(v<0||v>V-1) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
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

      CC cc = new CC(G);

      // number of connected components
      int m = cc.count();
      System.out.println(m + " components");

      // compute list of vertices in each connected component
      LinkedQueue<Integer>[] components = (LinkedQueue<Integer>[]) new LinkedQueue[m];
      for (int i = 0; i < m; i++) {
         components[i] = new LinkedQueue<Integer>();
      }
      for (int v = 0; v < G.V(); v++) {
         components[cc.id(v)].enqueue(v);
      }

      // print results
      for (int i = 0; i < m; i++) {
         for (int v : components[i]) {System.out.print(v + " ");
         }
         System.out.println();
      }
   }

}
