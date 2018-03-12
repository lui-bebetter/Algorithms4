/**********************************************************************
 *FileName: TrieSET
 *Author:   luibebetter
 *Date:     2018/3/1017:07
 *Description:The TrieSET class represents an ordered set of strings over
  the extended ASCII alphabet. It supports the usual add, contains, and
  delete methods. It also provides character-based methods for finding
  the string in the set that is the longest prefix of a given prefix,
  finding all strings in the set that start with a given prefix, and
  finding all strings in the set that match a given pattern.
 **********************************************************************/

package algs4.Trie;

import algs4.queue.LinkedQueue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

/************************************************************************
 *@author: luibebetter
 *@create: 2018/3/10
 *Description:his implementation uses a 256-way trie. The add, contains,
  delete, and longest prefix methods take time proportional to the length
  of the key (in the worst case). Construction takes constant time.
 ***********************************************************************/
public class TrieSET implements Iterable<String>{
	private static final int R=256;
	private Node root;//the root
	private int size;//number of string-value pairs in the Trie


	private static class Node{
		private boolean isString;
		private Node[]next=new Node[R];//implicitly store the R-char
	}

	public TrieSET(){
		root=new Node();
	}

	//return number of key-value pair in the TrieST
	public int size(){
		return size;
	}

	//is empty?
	public boolean isEmpty(){
		return size()==0;
	}

	//put operation
	public void add(String key){
		//argument check
		if(key==null) throw new IllegalArgumentException("calls put() with null first arguments");
		root=add(root,key,0);
	}

	private Node add(Node node,String key, int d){
		/*****************************************************************
		 * Description: add the key to the subtree rooted at{@code node}
		 *
		 * @param node the subtree root
		 * @param key
		 * @param value
		 * @param d  the index of character in the string to be compared
		 * @return: algs4.Trie.TrieST.Node
		 * @Author: luibebetter
		 ****************************************************************/
		if(node==null) node=new Node();//
		if(d==key.length()) {
			if(!node.isString) size++;
			node.isString=true;
			return node;
		}
		char c=key.charAt(d);
		node.next[c]=add(node.next[c],key,d+1);
		return node;
	}


	//does the SET contain {@code key}
	public boolean contains(String key){
		if(key==null) throw new IllegalArgumentException("calls contains() with null arguments");
		Node node=get(root,key,0);
		if(node==null) return false;
		else return node.isString;
	}

	private Node get(Node node,String key,int  d){
		/************************************************************************
		 * Description: get the value of the string @{code key} in the subtree rooted at
		 {@code node} based on the dth character
		 *
		 * @param node the subtree root
		 * @param key
		 * @param d  the index of character in the string to be compared
		 * @return: algs4.Trie.TrieST.Node
		 * @Author: luibebetter
		 **********************************************************************/
		if(node==null) return null;
		if(d==key.length()) return node;
		char c=key.charAt(d);
		return get(node.next[c],key,d+1);
	}

	//delete operation
	public void delete(String key) {
		if (key == null) throw new IllegalArgumentException("calls delete() with null arguments");
		if (!contains(key)) return ;
		root=delete(root,key,0);
		size--;
	}

	private Node delete(Node node, String key, int d){
		if(d==key.length()){
			node.isString=false;
			//check if the last node can be remove
			for (int c= 0; c < R; c++) {
				if(node.next[c]!=null) return node;
			}
			return null;
		}
		char c=key.charAt(d);
		node.next[c]=delete(node.next[c],key,d+1);

		//check if the node along the way up can be remove
		if(node.isString) return node;
		for (int c1 = 0; c1 < R; c1++) {
			if(node.next[c1]!=null) return node;
		}

		return null;
	}

	//Returns all keys in the symbol table as an Iterable.
	public Iterator<String> iterator(){
		return keysWithPrefix("").iterator();
	}

	private void collect(Node node, StringBuilder prefix, LinkedQueue<String> q){
		/**************************************************************************
		 * Description:Collecting all keys in the subtree rooted at {@code node}.
		 *Using StringBuilder instead of String can save memory, all the recursive call
		 * just use reference pointing to the same StringBuilder
		 * @param node
		 * @param prefix String corresponding to current node
		 * @param q
		 * @return: void
		 * @Author: luibebetter
		 **************************************************************************/
		if(node==null) return;
		if(node.isString) q.enqueue(prefix.toString());
		for (char c = 0; c < R; c++) {
			prefix.append(c);
			collect(node.next[c],prefix,q);
			prefix.deleteCharAt(prefix.length()-1);
		}
	}

	/*********************************************************************
	 * Character based operation
	 **************************************************************/
	//Does the TrieSET have string with specific prefix
	public boolean haveKeysWithPrefix(String prefix) {
		if(prefix==null) throw new IllegalArgumentException("calls haveStringWithPrefix() with null arguments");
		Node node=get(root,prefix,0);
		if(node==null) return false;
		return false;
	}

	//Returns all of the keys in the set that start with {@code prefix}.
	public Iterable<String> keysWithPrefix(String prefix){
		if(prefix==null) throw new IllegalArgumentException("calls keysWithPrefix() with null arguments");
		LinkedQueue<String> q=new LinkedQueue<>();
		Node node=get(root,prefix,0);
		StringBuilder s=new StringBuilder(prefix);
		collect(node,s,q);
		return q;
	}

	//Returns the string in the symbol table that is the longest prefix of {@code key}, or null, if no such string.
	public String longestPrefixOf(String key){
		if(key==null) throw new IllegalArgumentException("calls longestPrefixOf() with null arguments");
		int length=longestPrefixOf(root,key,0,0);
		return key.substring(0,length);
	}

	private int longestPrefixOf(Node node, String key, int d,int length){
		if(node==null) return length;
		if(node.isString) length=d;
		if(d==key.length()) return length;
		char c=key.charAt(d);
		return longestPrefixOf(node.next[c],key,d+1,length);

	}

	//Returns all of the keys in the symbol table that match {@code pattern},
	// where . symbol is treated as a wildcard character.
	public Iterable<String> keysThatMatch(String pattern){
		/********************************
		 * Description:
		 *
		 * @param pattern String pattern
		 * @return: java.lang.Iterable<java.lang.String>
		 * @Author: luibebetter
		 *********************************/
		if(pattern==null) throw new IllegalArgumentException("calls KeysThatMatch() with null arguments");
		LinkedQueue<String> q=new LinkedQueue<>();
		collect(root,pattern,new StringBuilder(),q);
		return q;
	}

	private void collect(Node node, String pattern, StringBuilder tmp, LinkedQueue<String> q){
		if(node==null) return;
		int d=tmp.length();
		if(d==pattern.length()) {
			if(node.isString) q.enqueue(tmp.toString());
			return;
		}

		char c=pattern.charAt(d);
		if(c=='.'){
			for (char i = 0; i < R; i++) {
				tmp.append(i);
				collect(node.next[i],pattern,tmp,q);
				tmp.deleteCharAt(tmp.length()-1);
			}
		}else{
			tmp.append(c);
			collect(node.next[c],pattern,tmp,q);
			tmp.deleteCharAt(tmp.length()-1);
		}
	}

	public static void main(String[] args) {

		// build symbol table from standard input
		TrieSET st = new TrieSET();

		Scanner in;
		try{
			in=new Scanner(new BufferedInputStream(new FileInputStream(args[0])));
		}catch(IOException e){
			throw new IllegalArgumentException();
		}
		for (int i = 0; in.hasNext(); i++) {
			String key = in.next();
			st.add(key);
		}

		// print results
		if (st.size() < 100) {
			System.out.println("keys(\"\"):"+st.size());
			for (String key : st) {
				System.out.println(key);
			}
			System.out.println();
		}

		System.out.println("longestPrefixOf(\"shellsort\"):");
		System.out.println(st.longestPrefixOf("shellsort"));
		System.out.println();

		System.out.println("longestPrefixOf(\"quicksort\"):");
		System.out.println(st.longestPrefixOf("quicksort"));
		System.out.println();

		System.out.println("keysWithPrefix(\"shor\"):");
		for (String s : st.keysWithPrefix("shor"))
			System.out.println(s);
		System.out.println();

		System.out.println("keysThatMatch(\".he.l.\"):");
		for (String s : st.keysThatMatch(".he.l."))
			System.out.println(s);
	}
}
