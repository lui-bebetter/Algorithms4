/**********************************************************************
 *FileName: SeparateChainingHashST
 *Author:   Dell
 *Date:     2017/12/3022:49
 *Description: This implementation uses a separate chaining hash table
 **********************************************************************/

package algs4.HashTable;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

import algs4.queue.LinkedQueue;

/************************************************************************************
 *@author: Dell
 *@create: 2017/12/30
 *Description:This implementation uses a separate chaining hash table.

 *It requires that the key type overrides the {@code equals()} and
    {@code hashCode()} methods.

 *The expected time per <em>put</em>, <em>contains</em>, or <em>remove</em>
    operation is constant, subject to the uniform hashing assumption.

 *  The <em>size</em>, and <em>is-empty</em> operations take constant time.
    Construction takes constant time.
 ************************************************************************/
public class SeparateChainingHashST<Key,Value>{
	private static final int INIT_CAPACITY=4;//the default value of the number of the linked list
	private Node [] st;//arrray of the linked list ST
	private int size;//the number of key-value pairs
	private int m;//the number of the linked list

	public SeparateChainingHashST(){
		this(INIT_CAPACITY);
	}

	public SeparateChainingHashST(int capacity){
		this.m=capacity;
		this.size=0;
		st=(Node[]) Array.newInstance(Node.class,INIT_CAPACITY);
	}

	public void put(Key key, Value value){
		/********************************
		 * Description:put the key-value pair to the HashST
		 *
		 * @param key
		 * @param value
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if (key==null || value==null) throw new IllegalArgumentException("calls put(Key,Value) with null arguments");
		if(size>10*m) resize(2*m);
		int i=hash(key);
		for(Node node=st[i];node!=null;node=node.next){
			if(node.key.equals(key)){
				node.value=value;
				return;
			}
		}
		st[i]=new Node(key,value,st[i]);
		size++;

	}

	public Value get(Key key){
		/********************************
		 * Description: get hte value of the key, return null if not exists
		 *
		 * @param key
		 * @return: Value
		 * @Author: luibebetter
		 *********************************/
		if(key==null) throw new IllegalArgumentException("calls get(Key) with null key");
		int i=hash(key);
		for(Node node=st[i];node!=null;node=node.next){
			if(node.key.equals(key)) return node.value;
		}
		return null;
	}

	//return the size of the HashST
	public int size(){
		return size;
	}

	//is empty?
	public boolean isEmpty(){
		return size()==0;
	}

	//contains the {@code key}
	public boolean contains(Key key){
		if(key==null) throw new IllegalArgumentException("calls contains(Key) with null keys");
		return get(key)!=null;
	}

	public void delete(Key key){
		/********************************
		 * Description: delete the key,if size<10*m,resize with size m/2
		 *
		 * @param key
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if(key==null) throw new IllegalArgumentException("calls delete(Key) with null key");
		if(isEmpty()) throw new NoSuchElementException("HashST underflow.");
		int i=hash(key);
		st[i]=delete(st[i],key);
		if(m>2*INIT_CAPACITY&&size<2*m) resize(m/2);
	}

	private Node delete(Node node, Key key){
		if(node==null) return null;
		if(node.key.equals(key)){
			size--;
			return node.next;
		}
		node.next=delete(node.next,key);
		return node;
	}

  public Iterable<Key> keys(){
		LinkedQueue<Key> q=new LinkedQueue<Key>();
		for(int i=0;i<m;i++)
			for(Node node=st[i];node!=null;node=node.next)
				q.enqueue(node.key);
		return q;
  }


	private int hash(Key key){
		/********************************
		 * Description: return the hash function of the key,
		 * map it to 0~m-1
		 *
		 * @param key
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		return (key.hashCode() & 0x7fffffff)%m;
	}

	private void resize(int capacity){
		/********************************
		 * Description: resize the HashST by changing the number of linked listd
		    specified as {@code capacity}

		 * @param capacity the number of linked lists
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		SeparateChainingHashST<Key,Value> tmp=new SeparateChainingHashST<Key,Value>(capacity);
		for(int i=0;i<m;i++)
			for(Node node=st[i];node!=null;node=node.next)
				tmp.put(node.key,node.value);
		this.m=tmp.m;
		this.size=tmp.size;
		this.st=tmp.st;
	}

	private class Node{
		private Key key;
		private Value value;
		private Node next;

		public Node(Key key, Value value, Node next){
			this.key=key;
			this.value=value;
			this.next=next;
		}
	}

	/**
	 * Unit tests the {@code SequentialSearchST} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		SeparateChainingHashST<String, Integer> st = new SeparateChainingHashST<String, Integer>();
		Scanner in =new Scanner(System.in);
		for (int i = 0; in.hasNext(); i++) {
			String key = in.next();
			st.put(key, i);
		}
		for (String s : st.keys())
			System.out.println(s + " " + st.get(s));
	}
}
