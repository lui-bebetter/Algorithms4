
package algs4.BinarySearchTree;

import java.util.Comparator;
import java.util.NoSuchElementException;
import algs4.queue.LinkedQueue;
import java.util.Scanner;


public class BST<Key extends Comparable<Key>, Value> {

	//BST needs store the reference point to the root node
	private Node<Key, Value> root = null;


	public Value get(Key key) {
		/************************
		 * Description: get the value of a specific key
		 *
		 * @param key
		 * @return: Value
		 * @Author: Dell
		 ************************/
		if (key == null) throw new IllegalArgumentException("call get() with key be null");
		return get(root, key);
	}

	private Value get(Node<Key, Value> node, Key key) {
		if (node == null) return null;
		int cmp = key.compareTo(node.key);
		if (cmp < 0) return get(node.left, key);
		else if (cmp > 0) return get(node.right, key);
		else return node.value;
	}

	public boolean contains(Key key) {
		/********************************
		 * Description: whether the BST has the key
		 *
		 * @param key
		 * @return: boolean
		 * @Author: Dell
		 *********************************/
		return get(key) != null;
	}

	public int size() {
		/********************************
		 * Description: return the size of BST
		 *
		 * @param
		 * @return: int
		 * @Author: Dell
		 *********************************/
		return size(root);
	}

	private int size(Node node) {
		if (node == null) return 0;
		return node.count;
	}

	public void put(Key key, Value value) {
		/********************************
		 * Description:
		 *
		 * @param key
		 * @param value
		 * @return: void
		 * @Author: Dell
		 *********************************/
		if (key == null || value == null)
			throw new IllegalArgumentException("call put(key,value) with key or value be null");
		root = put(root, key, value);
		assert check();
	}

	private Node<Key, Value> put(Node<Key, Value> node, Key key, Value value) {

		if (node == null) return new Node<Key, Value>(key, value, 1);
		int cmp = key.compareTo(node.key);
		if (cmp < 0) node.left = put(node.left, key, value);
		else if (cmp > 0) node.right = put(node.right, key, value);
		else node.value = value;
		node.count = 1 + size(node.left) + size(node.right);
		return node;
	}

	public boolean isEmpty(){
		/********************************
		 * Description: Is empty?
		 *
		 * @param
		 * @return: boolean
		 * @Author: Dell
		 *********************************/
		return size()==0;
	}

	public Key min(){
		/********************************
		 * Description: return the min one of the BST
		 *
		 * @param
		 * @return: Value
		 * @Author: Dell
		 *********************************/
		if(isEmpty()) throw new NoSuchElementException("calls min() with empty BST.");
		Node<Key, Value> min=min(root);
    return min.key;
	}

	private Node<Key, Value> min(Node<Key, Value> node){
		if(node.left==null) return node;
		return min(node.left);
	}

	public Key max(){
		/********************************
		 * Description: return the max of the BST
		 *
		 * @param
		 * @return: Value
		 * @Author: Dell
		 *********************************/
		if(isEmpty()) throw new NoSuchElementException("calls max() with empty BST.");
		Node<Key,Value> max=max(root);
		return max.key;
	}

	private Node<Key, Value> max(Node<Key, Value> node){
		if (node.right==null) return node;
		return max(node.right);
	}

	public Key floor(Key key){
		/********************************
		 * Description: return the largest key that less than or equal to the key
		 *
		 * @param key
		 * @return: Key
		 * @Author: luibebetter
		 *********************************/
		if(isEmpty()) throw new NoSuchElementException("calls floor(Key) with empty BST");
		if(key==null) throw new IllegalArgumentException();
		Node<Key,Value> tmp=floor(root,key);
		if(tmp==null) return null;
		else return tmp.key;
	}

	private Node<Key, Value> floor(Node<Key, Value> node, Key key){
		if(node==null) return null;
		int cmp=key.compareTo(node.key);
		if(cmp<0) return floor(node.left,key);
		else if(cmp>0) {
			Node<Key,Value> tmp=floor(node.right,key);
			if (tmp==null) return node;
			else return tmp;
		}
		else return node;
	}

	public Key ceiling(Key key){
		/********************************
		 * Description: return the smallest key that larger than or equal to the key
		 *
		 * @param key
		 * @return: Key
		 * @Author: luibebetter
		 *********************************/
		if(isEmpty()) throw new NoSuchElementException("calls floor(Key) with empty BST");
		if(key==null) throw new IllegalArgumentException();
		Node<Key,Value> tmp=ceiling(root,key);
		if(tmp==null) return null;
		else return tmp.key;
	}

	private Node<Key, Value> ceiling(Node<Key, Value> node, Key key){
		if(node==null) return null;
		int cmp=key.compareTo(node.key);
		if(cmp>0) return floor(node.right,key);
		else if(cmp<0) {
			Node<Key,Value> tmp=floor(node.left,key);
			if (tmp==null) return node;
			else return tmp;
		}
		else return node;
	}


	public void  delMin(){
		/********************************
		 * Description: delete the min one
		 *
		 * @param
		 * @return: void
		 * @Author: Dell
		 *********************************/
		if(isEmpty()) throw new NoSuchElementException("calls delMin() with empty BST");
		root=delMin(root);
		assert check();
	}

	private Node<Key, Value> delMin(Node<Key, Value> node){
		if(node.left==null) return node.right;
		node.left= delMin(node.left);
		node.count=1+size(node.left)+size(node.right);
		return node;
	}

	public void delMax(){
		/********************************
		 * Description: delete the max one
		 *
		 * @param
		 * @return: void
		 * @Author: Dell
		 *********************************/
		if(isEmpty()) throw new NoSuchElementException("calls delMax() with empty BST");
		root=delMax(root);
		assert check();
	}

	private Node<Key,Value> delMax(Node<Key,Value> node){
		if(node.right==null) return node.left;
		node.right=delMax(node.right);
		node.count=1+size(node.left)+size(node.right);
		return node;
	}

	public void delete(Key key){
		/********************************
		 * Description:
		 *
		 * @param key
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if(isEmpty()) throw new NoSuchElementException("calls delete() with empty BST");
		if(key==null) throw new IllegalArgumentException();
		root=delete(root,key);
		assert check();
	}

	public Key select(int k){
		/********************************
		 * Description: return the kth key(the 0th=the first key ) in the BST
		 *
		 * @param k [0,n)
		 * @return: Key
		 * @Author: luibebetter
		 *********************************/
		return select(root,k).key;
	}

	private Node<Key, Value> select(Node<Key, Value> node, int k){
		if(k<0 ||k>=size()) throw new IllegalArgumentException("argument to select() is invalid: " + k);
		int kth=size(node.left);
		if(k<kth) return select(node.left,k);
		else if(k>kth) return select(node.right,k-kth-1);
		else return node;
	}

	public int rank(Key key){
		/********************************
		 * Description: return the number of keys less than the key
		 *
		 * @param key the key
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		if(key==null) throw new IllegalArgumentException();
		return rank(root,key);
	}

	private int rank(Node<Key, Value> node, Key key){
		if(node==null) return 0;
		int cmp=key.compareTo(node.key);
		if(cmp<0) return rank(node.left,key);
		else if(cmp>0) return 1+size(node.left)+rank(node.right,key);
		else return size(node.left);
	}

	private Node<Key, Value> delete(Node<Key, Value> node , Key key){
		if (node==null) return null;//when BST does not contains the key
		int cmp=key.compareTo(node.key);
		if (cmp<0) node.left=delete(node.left,key);
		else if (cmp>0) node.right=delete(node.right,key);
		else{
			if(node.left==null) return node.right;
			if(node.right==null) return node.left;

			Node<Key, Value> min=min(node.right);
			min.right=delMin(node.right);
			min.left=node.right;
			node=min;
		}
		node.count=1+size(node.left)+size(node.right);
		return node;
	}

	public Iterable<Key> keys(){
		/********************************
		 * Description: iterate over the BST key in ascending order
		 *
		 * @param
		 * @return: java.lang.Iterable<BST.Node<Key,Value>>
		 * @Author: luibebetter
		 *********************************/
		LinkedQueue<Key> q=new LinkedQueue<Key>();
		inorder(root,q,min(),max());
		return q;
	}

	public Iterable<Key> keys(Key lo, Key hi){
		/********************************
		 * Description:iterate over the keys that in [lo,hi]
		 *
		 * @param lo the smallest key
		 * @param hi the largest key
		 * @return: java.lang.Iterable<BST.Node<Key,Value>>
		 * @Author: luibebetter
		 *********************************/
		if(lo==null||hi==null) throw new IllegalArgumentException("calls keys(lo,hi) with lo or hi be null");
		LinkedQueue<Key> q=new LinkedQueue<Key>();
		inorder(root,q,lo,hi);
		return q;
	}

	private void inorder(Node<Key,Value> node, LinkedQueue<Key> q, Key lo,Key hi){
		/********************************
		 * Description:put the left,the node, the right in order
		 *
		 * @param node
		 * @param q  Queue to store the keys in a ascending order
		 * @param lo the smallest key
		 * @param hi the largest key
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if(lo.compareTo(hi)>0) throw new IllegalArgumentException("calls keys(lo,hi) with ("+lo+","+hi+")");
		if(node==null) return;
		int cmplo=node.key.compareTo(lo);
		int cmphi=node.key.compareTo(hi);
		if(cmplo>=0) inorder(node.left,q,lo,hi);
		if(cmplo>=0&&cmphi<=0) q.enqueue(node.key);
		if(cmphi<=0) inorder(node.right,q,lo,hi);
	}

	private static class Node<Key extends Comparable<Key>, Value> {
		/********************************
		 * Description:inner static class
		 *********************************/
		private Key key;
		private Value value;
		private int count;
		private Node<Key, Value> left;
		private Node<Key, Value> right;

		public Node(Key key, Value value, int count) {
			this.key = key;
			this.value = value;
			this.count = count;
		}
	}
	/***********************************************************************************************
	 * Helper function for debugging
	 ***********************************************************************************************/
	private boolean check() {
		/********************************
		 * Description: check integrity of the BST
		 *
		 * @param
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		if (!isBST())            System.out.println("Not in symmetric order");
		if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
		if (!isRankConsistent()) System.out.println("Ranks not consistent");
		return isBST() && isSizeConsistent() && isRankConsistent();
	}

	private boolean isBST(){
		/********************************
		 * Description: is the BST satisfy symmetric order?symmetric order ensure the data structure
		   a BST
		 * @param
		 * @return: boolean:
		 * @Author: luibebetter
		 *********************************/
		return isBST(root,null,null);
	}

	private boolean isBST(Node<Key,Value> node, Key min, Key max){
		if (node==null) return true;
		if(min!=null && node.key.compareTo(min)<=0) return false;
		if(max!=null && node.key.compareTo(max)>=0) return false;
		return isBST(node.left,min,node.key)&&isBST(node.right,node.key,max);
	}

	private boolean isSizeConsistent(){
		/********************************
		 * Description: is node.count setted right
		 *
		 * @param
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		return isSizeConsistent(root);
	}

	private boolean isSizeConsistent(Node<Key, Value> node){
		if (node==null) return true;
		if(node.count!=1+size(node.left)+size(node.right)) return false;
		return isSizeConsistent(node.left)&&isSizeConsistent(node.right);
	}

	private boolean isRankConsistent(){
		for (int i = 0; i < size(); i++)
			if (i != rank(select(i))) return false;
		for (Key key : keys())
			if (key.compareTo(select(rank(key))) != 0) return false;
		return true;
	}

	/**
	 * Unit tests the {@code BST} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		BST<String, Integer> st = new BST<String, Integer>();
		Scanner in=new Scanner(System.in);
		for (int i = 0;in.hasNext(); i++) {
			String key = in.next();
			st.put(key, i);
		}

		System.out.println();

		for (String s : st.keys())
			System.out.println(s + " " + st.get(s));
	}
}