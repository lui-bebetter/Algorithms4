/************************************************************************************
 *FileName: RedBlackBST.java
 *Author:   luibebetter
 *Date:     2017/12/2311:40
 *Description: A symbol table implemented using a left-leaning red-black BST.
  This is the 2-3 version.

 *red-black tree:all path from root to leaf have same black links
 *Costs: search、delete、insert operations~2nlogn in worst case,1.00logn in general
 ***********************************************************************************/

package algs4.BinarySearchTree;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

import algs4.queue.LinkedQueue;

/********************************
 *@author luibebetter
 *@create: 2017/12/23
 *Description:The BST class represents an ordered symbol table
  of generic key-value pairs
 ********************************/
public class RedBlackBST<Key extends Comparable<Key>,Value> {
	private static final boolean BLACK=false;
	private static final boolean RED=true;

	private Node<Key,Value> root;

	/*************************************************************
	 *Node data structure
	 ************************************************************/
	private static class Node<Key extends Comparable<Key>, Value>{
		private Key key;
		private Value value;
		private int size;//the size of the subtree rooted at this node
		private Node<Key,Value> left, right;
		private boolean color;//is red?

		public Node(Key key, Value value, boolean color, int size){
			this.key=key;
			this.value=value;
			this.color=color;
			this.size=size;
		}
	}

	/*****************************************************************
	 * Node helper function
	 *****************************************************************/
	public int size(){
		/********************************
		 * Description: return the size of the BST
		 *
		 * @param
		 * @return: int the size of the BST
		 * @Author: luibebetter
		 *********************************/
		return size(root);
	}

	private int size(Node<Key,Value> node){
		if(node==null) return 0;
		return node.size;
	}

	//is empty?
	public boolean isEmpty(){
		return size()==0;
	}

	public boolean isRed(Node<Key,Value> node){
		if(node==null) return false;
		return node.color;
	}

	/**********************************************************************
	 * standard BST search operation
	 **********************************************************************/
	public Value get(Key key){
		/********************************
		 * Description: get the value of the key in the BST,return null if
		   the key is not in the BST
		 * @param key the key
		 * @return Value the value of the key
		 * @Author luibebetter
		 *********************************/
		if(key==null) throw new IllegalArgumentException("calls get() with null key");
		return get(root,key);
	}

	private Value get(Node<Key,Value> node,Key key){
		if(node==null) return null;
		int cmp=key.compareTo(node.key);
		if(cmp<0) return get(node.left,key);
		else if (cmp>0) return get(node.right,key);
		else return node.value;
	}

	public boolean contains(Key key){
		/********************************
		 * Description: does the BST contains the specific key
		 *
		 * @param key
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		if (key==null) throw new IllegalArgumentException("calls contains() with null key");
		return get(key)!=null;
	}

	/*******************************************************************************
	 * Red-black tree insertion and deletion
	 *******************************************************************************/

	public void put(Key key, Value value){
		/********************************
		 * Description: put the key-value pair to the BST, if value is null then delete the key
		 *
		 * @param key
		 * @param value
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if (key==null) throw new IllegalArgumentException("calls put() with null key");
		if (value==null){
			delete(key);
			return;
		}
		root=put(root,key,value);
		root.color=BLACK;//ensure the root is always black after the put operation

		assert check();
	}

	private Node<Key,Value> put(Node<Key,Value>node,Key key, Value value){
		if(node==null) return new Node<Key,Value>(key,value,RED,1);
		int cmp=key.compareTo(node.key);
		if (cmp<0) node.left=put(node.left,key,value);
		else if (cmp>0) node.right=put(node.right,key,value);
		else node.value=value;

		//fix up the BST:convert the situations to some specific situations
		if(isRed(node.right)&&!isRed(node.left)) node=rotateLeft(node);
		if(isRed(node.left)&&isRed(node.left.left)) node=rotateRight(node);
		if(isRed(node.left)&&isRed(node.right)) flipColors(node);

		node.size=1+size(node.left)+size(node.right);
		return node;
	}

	public void delMin(){
		/********************************
		 * Description:delete the min one of the BST
		 *
		 * @param
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if(isEmpty()) throw new NoSuchElementException("calls delMin() with empty BST.");
		if(!isRed(root.left)&&!isRed(root.right)) root.color=RED;//not important
		root=delMin(root);
		//ensure the root is always Black
		if(!isEmpty()) root.color=BLACK;

		assert check();
	}

	private Node<Key,Value> delMin(Node<Key,Value> node){
		if(node.left==null) return null;//null=min.right

		//ensure the next node is a 3 or 4 node
		if(!isRed(node.left)&&!isRed(node.left.left)) node=moveRedLeft(node);
		node.left=delMin(node.left);

		//fix up the left-leaning BST

		return balance(node);
	}

	public void delMax(){
		/********************************
		 * Description: delete the max one of the BST
		 *
		 * @param
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if(isEmpty()) throw new NoSuchElementException("calls delMax() with empty BST.");
		if(!isRed(root.left)&&!isRed(root.right)) root.color=RED;//not important
		root=delMax(root);
		//ensure the root is always Black
		if(!isEmpty()) root.color=BLACK;

		assert check();
	}

	private Node<Key,Value> delMax(Node<Key,Value> node){
		if(isRed(node.left)) node=rotateLeft(node);
		if(node.right==null) return null;//not return node.left because it do rotateLeft first.

		if(!isRed(node.right)&&!isRed(node.right.left)) node=moveRedRight(node);

		node.right=delMax(node.right);
		return balance(node);

	}

	public void delete(Key key){
		/********************************
		 * Description: delete the key from the BST
		 *
		 * @param key
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if (key==null) throw new IllegalArgumentException("calls delete() with null key");
		if(!contains(key)) return;
		if(isEmpty()) throw new NoSuchElementException("calls delMax() with empty BST.");
		if(!isRed(root.left)&&!isRed(root.right)) root.color=RED;//not important
		root=delete(root,key);
		//ensure the root is always Black
		if(!isEmpty()) root.color=BLACK;
		assert check();
	}

	private Node<Key,Value> delete(Node<Key,Value>node,Key key){
		assert contains(key);
		int cmp=key.compareTo(node.key);
		if(cmp<0){
			if(!isRed(node.left)&&!isRed(node.left.left)) node=moveRedLeft(node);
			node.left=delete(node.left,key);
		}
		else{
			if(isRed(node.left)) node=rotateRight(node);
			if(key.compareTo(node.key)==0 && (node.right==null)) return null;
			if(!isRed(node.right)&&!isRed(node.right.left))  node=moveRedRight(node);
			if(key.compareTo(node.key)==0){
				Node<Key,Value> x=min(node.right);
				node.key=x.key;
				node.value=x.value;
				node.right=delMin(node.right);
			}
			else node.right=delete(node.right,key);
		}
		return balance(node);
	}

	/**************************************************************************
	 * ordered symbol table methods
	 ****************************************************************************/

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


	/***************************************************************************
	 * Range count and range search
	 ***************************************************************************/
	public Iterable<Key> keys(){
		/********************************
		 * Description: return all keys in the BST as a Iterable.
		 *
		 * @param
		 * @return: java.lang.Iterable<Key>
		 * @Author: luibebetter
		 *********************************/
		LinkedQueue<Key> q=new LinkedQueue<Key>();
		inorder(root,q,min(),max());
		return q;
	}

	public Iterable<Key> keys(Key lo,Key hi){
		/********************************
		 * Description: return the keys in [lo,hi] in a ascending order
		 *
		 * @param lo the low bound
		 * @param hi the high nound
		 * @return: java.util.Iterator<Key>
		 * @Author: luibebetter
		 *********************************/
		if(lo==null||hi==null||lo.compareTo(hi)>=0) throw new IllegalArgumentException("call keys() with illegal arguments.");
		LinkedQueue<Key> q=new LinkedQueue<Key>();
		inorder(root,q,lo,hi);
		return q;
	}

	private void inorder(Node<Key,Value>node, LinkedQueue<Key>q, Key lo, Key hi){
		if(node==null) return;
		int cmplo=node.key.compareTo(lo);
		int cmphi=node.key.compareTo(hi);
		if (cmplo>0) inorder(node.left,q,lo,hi);
		if (cmplo>=0&&cmphi<=0) q.enqueue(node.key);
		if (cmphi<0) inorder(node.right,q,lo,hi);
	}


	/**************************************************************************
	 * Red black tree helper function
	 **************************************************************************/
	private Node<Key,Value> rotateRight(Node<Key,Value> node){
		/********************************
		 * Description:make a left-leaning link lean to the right
		 * if and only if the {@code node.left} is red that this rotation maintains
		   the perfect black length balance

		 *always used like this:h=rotateRight(h) to reset the node.

		 * @param node
		 * @return: RedBlackBST.Node<Key,Value>
		 * @Author: luibebetter
		 *********************************/
		assert isRed(node.left);
		Node<Key,Value> x=node.left;
		node.left=x.right;
		x.right=node;
		x.color=node.color;
		node.color=RED;//the color of the node x

		//reset the size of the two nodes
		x.size=node.size;
		node.size=1+size(node.left)+size(node.right);

		return x;//to reset the link :h=rotateRight(h)
	}

	private Node<Key,Value> rotateLeft(Node<Key,Value> node){
		/********************************
		 * Description: make a right-leaning link lean to left
		 *
		 * @param node
		 * @return: RedBlackBST.Node<Key,Value>
		 * @Author: luibebetter
		 *********************************/
		assert isRed(node.right);
		Node<Key,Value> x=node.right;
		node.right=x.left;
		x.left=node;
		x.color=node.color;
		node.color=RED;

		//reset the size of the two nodes
		x.size=node.size;
		node.size=1+size(node.left)+size(node.right);

		return x;
	}

	private void flipColors(Node<Key,Value>node){
		/*****************************************************************
		 * Description:flip the colors of the node and its two children
		   applicable condition:the node is black and its two children are
		   red or the node is red and its two children are black
		 * @param node
		 * @return: void
		 * @Author: luibebetter
		 ****************************************************************/
		assert (isRed(node)&&!isRed(node.left)&&!isRed(node.right)) || (!isRed(node)&&isRed(node.left)&&isRed(node.right));
		node.color=!node.color;
		node.left.color=!node.left.color;
		node.right.color=!node.right.color;
	}


	private Node<Key,Value> moveRedLeft(Node<Key,Value> node){
		/******************************************************
		 * Description: ensure node.left is part of 3 or 4 node
		 * applicable condition: node is red and its two children are black
		 * @param node
		 * @return: RedBlackBST.Node<Key,Value>
		 * @Author: luibebetter
		 ***********************************************************/
		assert isRed(node)&&!isRed(node.left)&&!isRed(node.right);
		flipColors(node);
    if (isRed(node.right.left)){
    	node.right=rotateRight(node.right);
    	node=rotateLeft(node);
    	flipColors(node);
    }
    return node;
	}

	private Node<Key,Value> moveRedRight(Node<Key,Value> node){
		/********************************
		 * Description: ensure node.right is part of 3 or 4 node
		 *
		 * @param node
		 * @return: RedBlackBST.Node<Key,Value>
		 * @Author: luibebetter
		 *********************************/
		flipColors(node);
		if(isRed(node.left.left)) {
			node=rotateRight(node);
			flipColors(node);
		}

		return node;
	}

	private Node<Key, Value> balance(Node<Key,Value> node){
		/********************************
		 * Description: fix up the left-leaning BST
		 *
		 * @param node
		 * @return: RedBlackBST.Node<Key,Value>
		 * @Author: luibebetter
		 *********************************/
		if(isRed(node.right)) node=rotateLeft(node);
		if(isRed(node.left)&&isRed(node.left.left)) node=rotateRight(node);
		if(isRed(node.left)&& isRed(node.right)) flipColors(node);

		node.size=1+size(node.left)+size(node.right);//fix up the size changed by delete operation
		return node;
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
		if (!is23()) System.out.println("Not a 2-3 tree");
		if (!isbalanced()) System.out.println("Not balanced");
		return isBST() && isSizeConsistent() && isRankConsistent()&&is23()&&isbalanced();
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
		if(node.size!=1+size(node.left)+size(node.right)) return false;
		return isSizeConsistent(node.left)&&isSizeConsistent(node.right);
	}

	private boolean isRankConsistent(){
		for (int i = 0; i < size(); i++)
			if (i != rank(select(i))) return false;
		for (Key key : keys())
			if (key.compareTo(select(rank(key))) != 0) return false;
		return true;
	}

	private boolean is23(){
		/********************************
		 * Description: 2-3tree:1、no right link is red
		   2、no two red links in a row
		 *
		 * @param
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		return is23(root);
	}

	private boolean is23(Node<Key,Value> node){
		if(node==null) return true;
		if(isRed(node.right)) return false;
		if(node!=root&&isRed(node)&&isRed(node.left)) return false;
		return is23(node.left)&&is23(node.right);
	}

	private boolean isbalanced(){
		/********************************
		 * Description: is all the path from root to leaf have same black links?
		 *
		 * @param
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		int black=0;
		Node<Key,Value> x=root;
		while(x!=null){
			if(!isRed(x)) black++;
			x=x.left;
		}

		return isbalanced(root,black);
	}

	private boolean isbalanced(Node<Key,Value>node, int black){
		if (node==null) return black==0;
		if(!isRed(node)) black--;
		return isbalanced(node.left,black)&&isbalanced(node.right,black);
	}


	/**
	 * Unit tests the {@code BST} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		RedBlackBST<String, Integer> st = new RedBlackBST<String, Integer>();
		Scanner in=new Scanner(System.in);
		for (int i = 0;in.hasNext(); i++) {
			String key = in.next();
			st.put(key, i);
		}
		System.out.println(st.size());
		for (String s : st.keys())
			System.out.println(s + " " + st.get(s));
		st.delete("E");
		for (String s : st.keys())
			System.out.println(s + " " + st.get(s));
	}

}
