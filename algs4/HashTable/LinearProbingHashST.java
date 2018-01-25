/**********************************************************************
 *FileName: LinearProbingHashST
 *Author:   Dell
 *Date:     2017/12/3115:13
 *Description:Symbol-table implementation with linear-probing hash table
 **********************************************************************/

package algs4.HashTable;


import java.util.NoSuchElementException;
import java.util.Scanner;

import algs4.queue.LinkedQueue;
/**********************************************************************************
 *@author: Dell
 *@create: 2017/12/31
 *Description:This implementation uses a linear probing hash table. It requires that
 *  the key type overrides the {@code equals()} and {@code hashCode()} methods.
 *  The expected time per <em>put</em>, <em>contains</em>, or <em>remove</em>
 *  operation is constant, subject to the uniform hashing assumption.
 *  The <em>size</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes constant time.
 **********************************************************************************/
public class LinearProbingHashST<Key,Value> {
	private static final int INIT_CAPACITY=4;
	private int size;//the number of key-value pair
	private int length;//the length of the key,value array

	private Key[] keys;
	private Value[] values;

	public LinearProbingHashST(){
		this(INIT_CAPACITY);
	}

	public LinearProbingHashST(int capacity){
		keys=(Key[])new Object[capacity];
		values=(Value[])new Object[capacity];
		this.length=capacity;
		this.size=0;
	}

	public void put(Key key, Value value){
		/********************************
		 * Description: put the key-value to the HashST
		 *
		 * @param key
		 * @param value
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if(key==null||value==null) throw new IllegalArgumentException("calls put(Key,Value) with null arguments");

		// double table size if 50% full
		if(size>length/2) resize(2*length);
		int i=hash(key);
		while(keys[i]!=null){
			if(keys[i].equals(key)){
				values[i]=value;
				return;
			}
			i=++i%length;
		}
		keys[i]=key;
		values[i]=value;
		size++;
	}

	public void delete(Key key){
		/********************************
		 * Description: delete the key-value pair specified by {@code key}
				do the rehash operation for the key-value of same cluster
		 * @param key
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if(key==null) throw new IllegalArgumentException("calls delete() with null arguments");
		if(isEmpty()) throw new NoSuchElementException("HashST undeflow");
		for(int i=hash(key);keys[i]!=null;i=(i+1)%length) {
			if (keys[i].equals(key)) {
				keys[i] = null;
				values[i] = null;
				size--;
				i=(i+1)%length;
				while(keys[i]!=null){
					Key rehashKey=keys[i];
					Value rehashValue=values[i];
					keys[i]=null;
					values[i]=null;
					size--;
					put(rehashKey,rehashValue);
					i=(i+1)%length;
				}
			}
		}
		if(length>2*INIT_CAPACITY&&size<length/8) resize(length/2);
	}

	public Value get(Key key){
		/********************************
		 * Description: return the value of the key
		 *
		 * @param key
		 * @return: Value
		 * @Author: luibebetter
		 *********************************/
		if(key==null) throw new IllegalArgumentException("calls get() with null arguments");
		for(int i=hash(key);keys[i]!=null;i=(i+1)%length){
			if(keys[i].equals(key)) return values[i];
		}
		return null;
	}

	//return the size of the HashST
	public int size(){
		return size;
	}

	public boolean isEmpty(){
		return size()==0;
	}

	public boolean iscontains(Key key){
		if(key==null) throw new IllegalArgumentException("calls iscontains() with null arguments");
		return get(key)!=null;
	}

	public Iterable<Key> keys() {
		LinkedQueue<Key> queue = new LinkedQueue<Key>();
		for (int i = 0; i < length; i++)
			if (keys[i] != null) queue.enqueue(keys[i]);
		return queue;
	}

	private void resize(int capacity){
		/********************************
		 * Description: reize the HashST to the length {@code capacity}
		 *
		 * @param capacity
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		assert capacity>size;
		LinearProbingHashST<Key,Value> tmp=new LinearProbingHashST<Key,Value>(capacity);
		for(int i=0;i<length &&keys[i]!=null;i++)
			tmp.put(keys[i],values[i]);
		this.keys=tmp.keys;
		this.values=tmp.values;
		this.size=tmp.size;
		this.length=tmp.length;
	}

	private int hash(Key key){
		/********************************
		 * Description: hash the key to 0~length-1
		 *
		 * @param key
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		return (key.hashCode()&0x7fffffff)%length;
	}

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
