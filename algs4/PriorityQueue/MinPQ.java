/**********************************************************************
 *FileName: MinPQ
 *Author:   luibebetter
 *Date:     2018/2/120:38
 *Description:The MinPQ class represents a priority queue of generic keys.
  It supports the usual insert and delete-the-minimum operations,
  along with methods for peeking at the minimum key, testing if the
  priority queue is empty, and iterating through the keys.
 **********************************************************************/

package algs4.PriorityQueue;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;

/*********************************************************
 *@author: luibebetter
 *@create: 2018/2/1
 *Description:This implementation uses a binary heap.
  The insert and delete-the-minimum operations take
  logarithmic amortized time. The min, size, and is-empty
  operations take constant time. Construction takes time
  proportional to the specified capacity or the number of
  items used to initialize the data structure.
 ***********************************************************/
public class MinPQ<Key extends Comparable<Key>> implements Iterable<Key>  {
	private static final int DEFAULT_CAPCITY=10;
	private Comparator<Key> comparator;
	private Key[] a;//using a array to store the keys,starts from index 1
	private int size;//number of keys in the MinPQ

	//Initialize an empty MinPQ
	public MinPQ(){
		this(DEFAULT_CAPCITY);
	}

	public MinPQ(int capacity){
		if(capacity<0) throw new IllegalArgumentException("Initialize MinPQ with illegal arguments");
		a=(Key[]) new Comparable[capacity+1];
		size=0;
	}

	public MinPQ(Comparator<Key> comparator){
		this();
		this.comparator=comparator;
	}

	public MinPQ(int capacity, Comparator<Key> comparator){
		this(capacity);
		this.comparator=comparator;
	}

	//return the size of the MinPQ
	public int size(){
		return size;
	}

	//is empty?
	public boolean isEmpty(){
		return size==0;
	}

	//insert
	public void insert(Key key){
		if(key==null) throw new IllegalArgumentException("calls insert() with null arguments");
		if(size==a.length-1) resize(2*a.length);
		a[++size]=key;
		swim(size);
	}

	//delete the min one
	public Key delMin(){
		if(isEmpty()) throw new NoSuchElementException("MinPQ underflow.");
		Key tmp=a[1];
		exch(1,size--);
		sink(1);
		a[size+1]=null;

		//resize the array
		if(a.length/2>DEFAULT_CAPCITY&&size==a.length/4) resize(a.length/2);
		return tmp;
	}


	@Override
	public Iterator<Key> iterator() {
		return new HeapIterator();
	}

	private class HeapIterator implements Iterator<Key>{
		private MinPQ<Key> copy;//the copy of the MinPQ

		//constructor
		public HeapIterator(){
			if(comparator!=null) copy=new MinPQ<>(size,comparator);
			else copy=new MinPQ<>(size);
			copy.size=size;
			for (int i = 1; i <= size; i++) {
				copy.a[i]=a[i];
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean hasNext() {
			return !copy.isEmpty();
		}

		@Override
		public Key next() {
			if(!hasNext()) throw new IllegalArgumentException("MinPQ underflow.");
			return copy.delMin();
		}
	}

	//to string in a descending order
	public String toString(){
		StringBuilder s=new StringBuilder();
		for(Key item:this){
			s.append(item);
			s.append(" ");
		}
		return s.toString();
	}

	/****************************************************
	 * Helper function
	 ***************************************************/
	//resize the array with specific capacity
	private void resize(int capacity){
		assert capacity>size;
		Key []tmp=(Key[]) new Comparable[capacity];
		for (int i = 1; i <= size; i++) {
			tmp[i]=a[i];
		}
		a=tmp;
	}

	//put the last element to the right position
	// prerequisite:except the k element, all elements in the right position
	private void swim(int k){
		while(k/2>=1){
			int parent=k/2;
			if(less(a[k],a[parent])) {
				exch(k, parent);
				k=parent;
			}else return;
		}
	}

	//maintain the subtree rooted at k a MinPQ
	private void sink(int k){
		while(2*k<=size){
			int min=2*k;
			if(min<size&&less(a[min+1],a[min])) min=min+1;
			if(!less(a[min],a[k])) break;
			exch(k,min);
			k=min;
		}
	}

	//is v<w?
	private boolean less(Key v, Key w){
		if(comparator!=null) return comparator.compare(v,w)<0;
		else return v.compareTo(w)<0;
	}

	private void exch(int i, int j){
		Key tmp=a[i];
		a[i]=a[j];
		a[j]=tmp;
	}

	public static void main(String [] args){
		MinPQ<String> heap=new MinPQ<String>();
		Scanner in=new Scanner(System.in);
		while(in.hasNext()){
			String s=in.next();
			if (s.equals("q")) break;
			else if(!s.equals("-")) heap.insert(s);
			else if(!heap.isEmpty()) System.out.print(" "+heap.delMin());
		}
		System.out.println("("+heap.size()+" left)");
		System.out.println(heap);
	}
}
