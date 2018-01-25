/**********************************************************************
 *FileName: SAP
 *Author:   luibebetter
 *Date:     2018/1/2220:39
 *Description:Finding shortest ancestral path for two or two set of vertices.
 **********************************************************************/

package algs4.DirectedGraph.WordNet;

import algs4.DirectedGraph.Digraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**************************************************************
 *@author: luibebetter
 *@create: 2018/1/22
 *Description:The length() and ancestor() takes time proportional
  to E+V in the worst case.
 ***************************************************************/
public class SAP {
	private final Digraph G;//the digraph

	//Initiate SAP
	public SAP(Digraph G){
		if(G==null) throw new IllegalArgumentException("Initiate SAP with null digraph");
		this.G=new Digraph(G);
	}

	public int length(int v, int w){
		/********************************
		 * Description:length of shortest ancestral path
		 between v and w; -1 if no such path
		 *
		 * @param v
		 * @param w
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		validateVertex(v);
		validateVertex(w);
		DeluxeBFS deluxe=new DeluxeBFS(G,v,w);
		return deluxe.length();
	}

	//a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w){
		validateVertex(v);
		validateVertex(w);
		DeluxeBFS deluxe=new DeluxeBFS(G,v,w);
		return deluxe.ancestor();
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w){
		validateVertices(v);
		validateVertices(w);
		DeluxeBFS deluxe=new DeluxeBFS(G,v,w);
		return deluxe.length();
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
		validateVertices(v);
		validateVertices(w);
		DeluxeBFS deluxe=new DeluxeBFS(G,v,w);
		return deluxe.ancestor();
	}

	/********************************************************
	 * Helper function
	 ********************************************************/
	private void validateVertex(int v){
		int V=G.V();
		if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

	private void validateVertices(Iterable<Integer> sources){
		int V=G.V();
		if(sources==null) throw new IllegalArgumentException("the sources is null");
		for (int v:sources){
			if(v<0||v>=V) throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
		}
	}

	/*******************************************************
	 * Unit Test
	 ********************************************/
	public static void main(String[] args) {
		InputStream in;
		try{
			in=new FileInputStream(args[0]);
		}catch(IOException e){
			throw new IllegalArgumentException("can't open the file "+args[0]);
		}

		Digraph G=new Digraph(in);
		SAP sap = new SAP(G);

		Scanner scanner=new Scanner(System.in);
		while (scanner.hasNext()) {
			int v = scanner.nextInt();
			int w = scanner.nextInt();
			int length   = sap.length(v, w);
			int ancestor = sap.ancestor(v, w);
			System.out.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}
}
