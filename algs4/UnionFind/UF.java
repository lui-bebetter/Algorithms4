/**********************************************************************
 *FileName: UF
 *Author:   luibebetter
 *Date:     2018/1/2823:15
 *Description:Weighted quick-union by rank with path compression by halving
 **********************************************************************/

package algs4.UnionFind;

import java.util.Scanner;

/**********************************************************************
 *@author: luibebetter
 *@create: 2018/1/28
 *Description:The UF class represents a union–find data type (also known
  as the disjoint-sets data type). It supports the union and find operations,
  along with a connected operation for determining whether two sites are in
  the same component and a count operation that returns the total number of
  components.
 ********************************************************************/
public class UF {
	private int count;//number of the different component
	private int [] parent;//the parent id of each site
	private int []rank;//rank[i]=height of the subtree rooted at i

	//Initialize the data structure for {@code n} objects
	public UF(int n){
		if(n<0) throw new IllegalArgumentException("Initiate UF with illegal argument");
		count=n;
		parent=new int[n];
		rank=new int[n];
		for (int i = 0; i < n; i++) {
			parent[i]=i;
		}
	}

	//return number of different components
	public int count(){
		return count;
	}

	//is v and w connected?
	public boolean connected(int v, int w){
		validate(v);
		validate(w);
		return find(v)==find(w);
	}

	//union v and w
	public void union(int v,int w){
		validate(v);
		validate(w);
		int rootv=find(v);
		int rootw=find(w);
		if(rootv==rootw) return;
		if(rank[rootv]<rank[rootw]) parent[rootv]=rootw;
		else if(rank[rootv]>rank[rootw]) parent[rootw]=rootv;
		else{
			parent[rootv]=rootw;
			rank[rootw]++;
		}
		count--;
	}

	//return the component identifier of v
	public int find(int v){
		validate(v);
		while(parent[v]!=v){
			parent[v]=parent[parent[v]];//path compression
			v=parent[v];
		}
		return v;
	}

	private void validate(int v){
		int n=parent.length;
		if(v<0||v>n-1) throw new IllegalArgumentException("index " + v + " is not between 0 and " + (n-1));
	}

	public static void main(String[] args) {
		Scanner in =new Scanner(System.in);
		int n = in.nextInt();
		UF uf = new UF(n);
		while (in.hasNext()) {
			int p = in.nextInt();
			int q = in.nextInt();
			if (uf.connected(p, q)) continue;
			uf.union(p, q);
			System.out.println(p + " " + q);
		}
		System.out.println(uf.count() + " components");
	}
}
