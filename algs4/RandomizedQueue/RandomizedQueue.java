/*************************
*RandomizedQueue:implemented by using ResizingArray

************************/
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

public class RandomizedQueue<Item> implements Iterable<Item>{
    private Item[] randomArray;
    private int size;
    private static final int DE_SIZE=10;

    //constructor
    public RandomizedQueue(){
        randomArray=(Item [])new Object[DE_SIZE];
        size=0;
    }

    public boolean isEmpty(){
        return size==0;
    }

    public int size(){
        return size;
    }

    //add an item 
    public void enqueue(Item item){
        if(item==null) throw new IllegalArgumentException("the item can't be null");
        if(size==randomArray.length) resize(2*randomArray.length);
        randomArray[size++]=item;
    }

    //delete an item
    public Item dequeue(){
        if(isEmpty()) throw new NoSuchElementException("queue underflow");
        int len=randomArray.length;
        int del=StdRandom.uniform(size);
        Item item=randomArray[del];
        //swap the last one with the deleting one ,the delete the last
        randomArray[del]=randomArray[size-1];
        randomArray[size-1]=null;
        size--;

        if(size==len/4&&len>DE_SIZE) resize(len/2);
        return item;
    }

    //return a random item
    public Item sample(){
        if(isEmpty()) throw new NoSuchElementException("the queue was empty");
        int random=StdRandom.uniform(size);
        return randomArray[random];
    }

    //resize the array 
   private void resize(int capacity){
        assert capacity>size;
        Item [] temp=(Item[])new Object[capacity];
        for (int i=0;i<size;i++){
            temp[i]=randomArray[i];
        }
        randomArray=temp;
    }

    //string format
    public String toString(){
        StringBuilder s=new StringBuilder();
        for (Item item:this){
            s.append(item);
            s.append(" ");
        }
        return s.toString();
    }

    public Iterator<Item> iterator(){
        return new RandomizedQueueIterator();
    }

    //iterator
    private class RandomizedQueueIterator implements Iterator<Item>{
        private int [] randomlist;
        private int current;

        public RandomizedQueueIterator(){
            randomlist=StdRandom.permutation(size);
            current=0;
        }

        public boolean hasNext(){
            return current<size;
        }

        public Item next(){
            if(!hasNext()) throw new NoSuchElementException("queue underflow");
            Item item=randomArray[randomlist[current]];
            current++;
            return item;
        }

        public void remove(){
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[]args){
        RandomizedQueue<String> queue=new RandomizedQueue<String>();
        In in=new In();
        Out out=new Out();
        while(!in.isEmpty()){
            String s=in.readString();
            if(s.equals("-")&&!queue.isEmpty()) out.print(" "+queue.dequeue());
            else if(!s.equals("-")) queue.enqueue(s);
            else if(s.equals("q")) break;
        }
        out.println("("+queue.size()+"left)");
        out.println(queue);
        out.println(queue);
    }


}