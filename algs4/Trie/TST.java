/**********************************************************************
 *FileName: TST
 *Author:   luibebetter
 *Date:     2018/3/815:47
 *Description:The TST class represents an symbol table of key-value pairs,
  with string keys and generic values.
 *It supports the usual put, get, contains, delete, size, and is-empty methods.
  It also provides character-based methods for finding the string in the symbol
  table that is the longest prefix of a given prefix, finding all strings in the
  symbol table that start with a given prefix, and finding all strings in the
  symbol table that match a given pattern. A symbol table implements the
  associative array abstraction: when associating a value with a key that is
  already in the symbol table, the convention is to replace the old value with
  the new value. Unlike Map, this class uses the convention that values cannot
  be null—setting the value associated with a key to null is equivalent to
  deleting the key from the symbol table.
 **********************************************************************/

package algs4.Trie;

import algs4.queue.LinkedQueue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/***************************************************************
 *@author: luibebetter
 *@create: 2018/3/8
 *Description:This implementation uses a ternary search trie
 ***************************************************************/
public class TST<Value> {
	private Node root;
	private int size;//number of key-value pairs in the TST

	//constructor
	public TST(){}

	//inner Node class
	private static class Node{
		private Object value;//value associated with the key
		private char  c;//explicitly stored character
		private Node left;//pointing to the node whose character less than {@code c}
		private Node middle;//pointing to the node whose character be {@code c}
		private Node right;//pointing to the node whose character greater than {@code c}

		public Node(char c){
			this.c=c;
		}
	}

	//put operation implemented in recursive form
	public void put(String key, Value value){
		if(key==null || value==null) throw new IllegalArgumentException("calls put() with null arguments");
		root=put(root,key,value,0);
	}

	private Node put(Node node, String key, Value value, int d){
		/********************************
		 * Description:
		 *
		 * @param node
		 * @param key
		 * @param value
		 * @param d  the character index in the String {@code key}
		 * @return: algs4.Trie.TST.Node
		 * @Author: luibebetter
		 *********************************/
		char c=key.charAt(d);
		if(node==null) node=new Node(c);
		if(c<node.c){
			node.left=put(node.left,key,value,d);
		}else if(c>node.c){
			node.right=put(node.right,key,value,d);
		}else {
			if(d==key.length()-1) {
				if(node.value==null) size++;
				node.value=value;
			}else node.middle=put(node.middle,key,value,d+1);
		}

		return node;
	}

	//size of the TST
	public int size(){
		return size;
	}

	public boolean isEmpty(){
		return size()==0;
	}

	//get operation
	public Value get(String key){
		if(key==null) throw new IllegalArgumentException("calls get() with null arguments");
		if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
		Node node=get(root,key,0);
		if(node==null) return null;
		return (Value)node.value;
	}

	private Node get(Node node, String key, int d){
		if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
		char c=key.charAt(d);
		if(node==null) return null;
		if(c<node.c)  return get(node.left, key, d);
		else if(c>node.c) return get(node.right, key,d);
		else{
			if(d==key.length()-1) return node;
			return get(node.middle,key,d+1);
		}
	}

	//contains operation
	public boolean contains(String key){
		if(key==null) throw new IllegalArgumentException("calls contains() with null arguments");
		return get(key)!=null;
	}

	//Delete operation
	public void delete(String key){
		if(key==null) throw new IllegalArgumentException("calls delete() with null arguments");
		root=delete(root,key,0);
	}

	private Node delete(Node node, String key, int d){
		if(node==null) return null;
		char c=key.charAt(d);
		if(c<node.c) node.left=delete(node.left,key,d);
		else if(c>node.c) node.right=delete(node.right,key,d);
		else {
			if(d==key.length()-1) {
				if(node.value!=null) size--;
				node.value=null;
			}
			else node.middle=delete(node.middle,key,d+1);
		}

		if(node.value!=null) return node;
		if(node.left!=null || node.middle!=null||node.right!=null) return node;
		return null;
	}

	//Returns all keys in the symbol table as an Iterable.
	public Iterable<String> keys(){
		LinkedQueue<String> q=new LinkedQueue<>();
		collect(root,new StringBuilder(),q);
		return q;
	}

	private void collect(Node node, StringBuilder prefix, LinkedQueue<String> q){
		if(node==null) return;
		if(node.value!=null) {
			prefix.append(node.c);
			q.enqueue(prefix.toString());
			prefix.deleteCharAt(prefix.length()-1);
		}
		collect(node.left,prefix,q);
		prefix.append(node.c);
		collect(node.middle,prefix,q);
		prefix.deleteCharAt(prefix.length()-1);
		collect(node.right,prefix,q);
	}

	//Returns all of the keys in the set that start with prefix.
	public Iterable<String> keysWithPrefix(String prefix){
		if(prefix==null) throw new IllegalArgumentException("calls keysWithPrefix() with null arguments");
		Node node=get(root,prefix,0);
		LinkedQueue<String> q=new LinkedQueue<>();
		collect(node.middle,new StringBuilder(prefix),q);
		return q;
	}

	//Returns all of the keys in the symbol table that match {@code pattern},
	//where . symbol is treated as a wildcard character.
	public Iterable<String> keysThatMatch(String pattern){
		if(pattern==null) throw new IllegalArgumentException("calls keysThatMatch() with null arguments");
		LinkedQueue<String> q=new LinkedQueue<>();
		collect(root,pattern,new StringBuilder(),q);
		return q;
	}

	private void collect(Node node, String pattern, StringBuilder prefix, LinkedQueue<String> q){
		if(node==null) return;
		int d=prefix.length();
		char c=pattern.charAt(d);
		if(c=='.'){
			collect(node.left,pattern, prefix,q);
			prefix.append(node.c);
			if(d==pattern.length()-1) {
				if(node.value!=null) q.enqueue(prefix.toString());
			}else{
				collect(node.middle, pattern, prefix, q);
			}
			prefix.deleteCharAt(prefix.length()-1);
			collect(node.right,pattern,prefix,q);
		}else{
			if(c<node.c) collect(node.left,pattern,prefix,q);
			else if(c>node.c) collect(node.right,pattern,prefix,q);
			else {
				prefix.append(node.c);
				if(d==pattern.length()-1) {
					if(node.value!=null)q.enqueue(prefix.toString());
				}else {
					collect(node.middle, pattern, prefix, q);
				}
				prefix.deleteCharAt(prefix.length() - 1);
			}
		}
	}

	public String longestPrefixOf(String query){
		if(query==null) throw new IllegalArgumentException("calls longestPrefixOf() with null arguments");
		int index=longestPrefixOf(root,query,0,-1);
		return query.substring(0,index+1);
	}

	private int longestPrefixOf(Node node,String query,int d, int index){
		if(node==null) return index;
		char c=query.charAt(d);
		if(c<node.c) return longestPrefixOf(node.left, query,d,index);
		else if(c>node.c) return longestPrefixOf(node.right,query,d,index);
		else {
			index=d;
			if(d==query.length()-1) return index;
			else return longestPrefixOf(node.middle,query,d+1,index);
		}
	}


	/**
	 * Unit tests the {@code TrieST} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {

		// build symbol table from standard input
		TST<Integer> st = new TST<Integer>();

		Scanner in;
		try{
			in=new Scanner(new BufferedInputStream(new FileInputStream(args[0])));
		}catch(IOException e){
			throw new IllegalArgumentException();
		}
		for (int i = 0; in.hasNext(); i++) {
			String key = in.next();
			st.put(key, i);
		}

		st.put("shellss",10);
		st.delete("shells");
		// print results
		if (st.size() < 100) {
			System.out.println("keys(\"\"):"+st.size());
			for (String key : st.keys()) {
				System.out.println(key + " " + st.get(key));
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
