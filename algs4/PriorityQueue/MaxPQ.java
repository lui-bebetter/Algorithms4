/***************************************************************
 *  Generic max priority queue implementation with a binary heap.
 *  Can be used with a comparator instead of the natural order,
 *  but the generic Key type must still be Comparable.
*****************************************************************/
package algs4.PriorityQueue;

import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public  class MaxPQ <Key> implements Iterable<Key>{

    private  Key [] a;
    private static final int DEFAULT_SIZE=10;//the default size of the array
    private int n;//the number of element in MaxPQ
    private Comparator<Key> comparator;

    /*****************************************
    *constructors for variable situation
    *****************************************/
    //initialize with natutal orderand default size
    public MaxPQ(){
        a=(Key[])new Object[DEFAULT_SIZE];
        n=0;
    }

    //initialize using a comparator
    public MaxPQ(Comparator<Key> comparator){
        a=(Key []) new Object[DEFAULT_SIZE];
        n=0;
        this.comparator=comparator;
    }

    //initialize with a array
    public MaxPQ(Key [] keys){
        n=keys.length;
        a=(Key[]) new Object[n+1];
        for (int i=0;i<n;i++)
            a[i+1]=keys[i];
        for (int k=n/2;k>=1;k--)
            sink(k);

        assert isMaxHeap();
    }

    public MaxPQ(Key [] keys , Comparator<Key> comparator){
        n=keys.length;
        a=(Key[]) new Object[n+1];
        this.comparator=comparator;
        for (int i=0;i<n;i++)
            a[i+1]=keys[i];
        for (int k=n/2;k>=1;k--)
            sink(k);

        assert isMaxHeap();
    }

    /************************************************
    *utilities:insert delMax size isEmpty
    ************************************************/
    //to string in a descending order
    public String toString(){
        StringBuilder s=new StringBuilder();
        for(Key item:this){
            s.append(item);
            s.append(" ");
        }
        return s.toString();
    }

    //is empty?
    public boolean isEmpty(){
        return n==0;
    }

    //return the size of the MaxPQ
    public int size(){
        return n;
    }

    //insert a key into the MaxPQ
    public void insert(Key key){
        if(n==a.length-1) resize(2*a.length);
        a[++n]=key;
        swim(n);

        assert isMaxHeap();
    }

    public Key delMax(){
        if(isEmpty()) throw new NoSuchElementException("MaxPQ underflow");
        exch(a,1,n);
        Key tmp=a[n];
        a[n--]=null;
        sink(1);

        if(a.length>DEFAULT_SIZE&&n==a.length/4) resize(a.length/2); 
        assert isMaxHeap();

        return tmp;
    }

    /*****************************************
    *Iterable implements
    *****************************************/
    public Iterator<Key> iterator(){
        return new MaxPQIterator();
    }

    //iterating over the keys on this priority queue in a descending order.
    private class MaxPQIterator implements Iterator<Key>{

        //the copy of the MaxHeap
        private MaxPQ<Key> copy;

        public MaxPQIterator(){
            if(comparator==null) copy=new MaxPQ<Key>();
            else copy=new MaxPQ<Key>(comparator);
            for (int i=1;i<=n;i++){
                copy.insert(a[i]);
            }
        }

        public boolean hasNext(){
            return !copy.isEmpty();
        }

        public void remove(){
            throw new UnsupportedOperationException();
        }

        public Key next(){
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMax();
        }
    }


    /**********************
    *put the smaller parent to the right position

    *parent with index k, having two child 2k,2k+1
    **********************/
    private void sink(int k){
        while(2*k<=n){
            int max=2*k;
            if(max<n &&less(a[max],a[max+1])) max++;
            if(!less(a[k],a[max])) break; 
            exch(a,max,k);
            k=max;
        }
    }

    /***************
    *put the larger child to the right position

    *child with index k, then the parent's index is k/2
    ***************/
    private void swim(int k){
        while(k>1 &&less(a[k/2],a[k])){
            exch(a, k/2, k);
            k=k/2;
        }
    }

    /****************************************
    *helper function:less and exch
    *****************************************/

    private void resize(int capacity){
        assert capacity>n;
        Key [] tmp=(Key []) new Object[capacity];
        for(int i=1;i<=n;i++)
            tmp[i]=a[i];
        a=tmp;
    }

    //is v<w?
    private boolean less(Key v, Key w){
        if (comparator!=null) return comparator.compare(v,w)<0;
        return ((Comparable<Key>)v).compareTo(w)<0; 
    }

    //exchange a[i] with a[j]
    private void exch(Key []a, int i, int j){
        Key tmp=a[i];
        a[i]=a[j];
        a[j]=tmp;
    }
    
    //is the array a subtree?
    private boolean isMaxHeap(){
        return isMaxHeap(1);
    }

    //is the subtree rooted at k a MAXHEAP
    private boolean isMaxHeap(int k){
        if(2*k<=n && (less(a[k],a[2*k]) || !isMaxHeap(2*k)) ) return false;
        if(2*k<n && (less(a[k],a[2*k+1]) || !isMaxHeap(2*k+1)) ) return false;
        return true;
    } 

    public static void main(String [] args){
        MaxPQ<String> heap=new MaxPQ<String>();
        Scanner in=new Scanner(System.in);
        while(in.hasNext()){
            String s=in.next();
            if (s.equals("q")) break;
            else if(!s.equals("-")) heap.insert(s);
            else if(!heap.isEmpty()) System.out.print(" "+heap.delMax());
        }
        System.out.println("("+heap.size()+" left)");
        System.out.println(heap);
    }
    
}