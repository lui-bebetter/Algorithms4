/**********************************************************************
 *FileName: IndexMinPQ
 *Author:   luibebetter
 *Date:     2018/2/215:51
 *Description:The IndexMinPQ class represents an indexed priority queue
  of generic keys. It supports the usual insert and delete-the-minimum
  operations, along with delete and change-the-key methods.
  In order to let the client refer to keys on the priority queue, an
  integer between 0 and maxN - 1 is associated with each key—the client
  uses this integer to specify which key to delete or change. It also
  supports methods for peeking at the minimum key, testing if the
  priority queue is empty, and iterating through the keys.
 **********************************************************************/

package algs4.PriorityQueue;

import java.util.Iterator;
import java.util.NoSuchElementException;

/************************************************************************
 *@author: luibebetter
 *@create: 2018/2/2
 *Description:This implementation uses a binary heap along with an array
  to associate keys with integers in the given range. The insert, delete-
  the-minimum, delete, change-key, decrease-key, and increase-key operations
  take logarithmic time. The is-empty, size, min-index, min-key, and key-of
  operations take constant time. Construction takes time proportional to the
  specified capacity.
 ***********************************************************************/
public class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer>{
	private Key[] keys;//keys[i]=the key/priority of the associated-index i
	private int [] pq;//the priority queue array of the index associated with the key
	private int [] heapPosition;//the heap position of the index in the pq array
	private int size;//the size of the priority queue

	//Initializes an empty indexed priority queue with indices between 0 and maxN - 1
	public IndexMinPQ(int maxN){
		if(maxN<0) throw new IllegalArgumentException("Initialize IndexMinPQ with illegal arguments");
		keys=(Key[]) new Comparable[maxN+1];
		heapPosition=new int[maxN+1];
		pq=new int [maxN+1];
		size=0;
		for (int i = 0; i <=maxN; i++) {
			heapPosition[i]=-1;
		}
	}

	public int size(){
		return size;
	}

	public boolean isEmpty(){
		return size()==0;
	}

	public boolean contains(int i){
		/********************************
		 * Description:
		 *
		 * @param i the index
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		validateIndex(i);
		return heapPosition[i]!=-1;
	}

	//insert the key-index pair
	//if index i already in the MinPQ, throw an illegalArgumentException
	public void insert (int i, Key key){
		validateIndex(i);
		if(contains(i)) throw new IllegalArgumentException("index "+i+" already in the priority queue");
		if(key==null) throw new IllegalArgumentException("calls insert() with null arguments");
		keys[i]=key;
		pq[++size]=i;
		heapPosition[i]=size;
		swim(size);
	}


	public int  delMin(){
		/********************************
		 * Description: delete the min key-index and return the index
		 *
		 * @param
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		if(isEmpty()) throw new NoSuchElementException("IndexMinPQ underflow");
		int min=pq[1];
		exch(1,size--);
		sink(1);
		keys[min]=null;
		heapPosition[min]=-1;
		return min;
	}

	public void delete(int i){
		/********************************
		 * Description: remove the specific index
		 *
		 * @param i the index
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if(!contains(i))  throw new IllegalArgumentException("index "+i+" not in the IndexMinPQ.");
		int pos=heapPosition[i];
		keys[i]=null;
		heapPosition[i]=-1;
		exch(pos,size--);
		sink(pos);
	}

	//return the key associated withe the specific index
	public Key keyOf(int i){
		if(!contains(i)) throw new IllegalArgumentException("index "+i+" not in the IndexMinPQ.");
		return keys[i];
	}

	public int minIndex(){
		if(isEmpty()) throw new IllegalArgumentException("IndexMinPQ underflow.");
		return pq[1];
	}

	public Key minKey(){
		return keys[minIndex()];
	}

	/*************************************************************************
	 * Operation to change the key of a specific index.
	 **************************************************************/
	public void changeKey(int i, Key key){
		/********************************
		 * Description: change the key of index i to the specific value
		 *
		 * @param i the index
		 * @param key the key
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if(!contains(i)) throw new IllegalArgumentException("index "+i+" not in the IndexMinPQ.");
		if(key==null) throw new IllegalArgumentException("calls changeKey() with null arguments");
		keys[i]=key;
		swim(heapPosition[i]);
		sink(heapPosition[i]);
	}

	public void decreaseKey(int i, Key key){
		/********************************
		 * Description: decrease the key of index i to the specific value
		 *
		 * @param i the index
		 * @param key index associated key
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if(!contains(i)) throw new IllegalArgumentException("index "+i+" not in the IndexMinPQ.");
		if(key==null) throw new IllegalArgumentException("calls decreaseKey() with null arguments");
		if(!(key.compareTo(keys[i])<0)) throw new IllegalArgumentException("calls changeKey() with null arguments");
		keys[i]=key;
		swim(heapPosition[i]);
	}

	public void increaseKey(int i, Key key){
		/********************************
		 * Description: increase the key of index i to the specific value
		 *
		 * @param i the index
		 * @param key index associated key
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if(!contains(i)) throw new IllegalArgumentException("index "+i+" not in the IndexMinPQ.");
		if(key==null) throw new IllegalArgumentException("calls decreaseKey() with null arguments");
		if(!(key.compareTo(keys[i])>0)) throw new IllegalArgumentException("calls changeKey() with null arguments");
		keys[i]=key;
		sink(heapPosition[i]);
	}



	@Override
	public Iterator<Integer> iterator() {
		return new HeapIterator();
	}

	private class HeapIterator implements Iterator<Integer>{
		private IndexMinPQ<Key> copy;

		public HeapIterator(){
			copy=new IndexMinPQ<>(keys.length-1);
			for (int i = 1; i <= size; i++) {
				copy.insert(pq[i],keys[pq[i]]);
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
		public Integer next() {
			if(!hasNext()) throw new NoSuchElementException("IndexMinPQ underflow.");
			return copy.delMin();
		}
	}

	/**********************************************
	 * Helper function
	 ***********************************************/
	private void swim(int k){
		/********************************
		 * Description:
		 *
		 * @param k the heap position
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		while(k/2>=1){
			if(!less(k,k/2)) break;
			exch(k,k/2);
			k=k/2;
		}
	}

	private void sink(int k){
		/********************************
		 * Description:
		 *
		 * @param k the heap position
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		while(2*k<=size){
			int min=2*k;
			if(min<size&&less(min+1,min)) min=min+1;
			if(less(k,min)) break;
			exch(k,min);
			k=min;
		}
	}

	//is keys[v]<keys[w]?
	private boolean less(int  i, int  j){
		/********************************
		 * Description:
		 *
		 * @param v the heap position
		 * @param w the heap position
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		return keys[pq[i]].compareTo(keys[pq[j]])<0;
	}


	private void exch(int i, int j){
		/********************************
		 * Description:
		 *
		 * @param i the heap position
		 * @param j the heap position
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		int tmp=pq[i];
		pq[i]=pq[j];
		pq[j]=tmp;
		heapPosition[pq[i]]=i;
		heapPosition[pq[j]]=j;
	}

	private void validateIndex(int i){
		int n=keys.length-1;
		if(i<0||i>=n) throw new IllegalArgumentException("index "+i+" not between 0 and "+(n-1));
	}

	/**
	 * Unit tests the {@code IndexMinPQ} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		// insert a bunch of strings
		String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

		IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);
		for (int i = 0; i < strings.length; i++) {
			pq.insert(i, strings[i]);
		}

		// delete and print each key
		while (!pq.isEmpty()) {
			int i = pq.delMin();
			System.out.println(i + " " + strings[i]);
		}
		System.out.println();

		// reinsert the same strings
		for (int i = 0; i < strings.length; i++) {
			pq.insert(i, strings[i]);
		}

		pq.delete(8);
		// print each key using the iterator
		for (int i : pq) {
			System.out.println(i + " " + strings[i]);
		}
		while (!pq.isEmpty()) {
			pq.delMin();
		}

	}
}
