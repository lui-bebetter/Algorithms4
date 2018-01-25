/*******************
*implements queue using resizing array

*one should be noting:wrapping
******************/
package algs4.queue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ResizingArrayQueue<Item> implements Iterable<Item>{

    private Item[] queue;
    private int first,last;//first:the index of the least recently added.last:the index of most recently added plus one.
    private static final int DE_SIZE=10;

    //constructor
    public ResizingArrayQueue(){
        queue=(Item[])new Object[DE_SIZE];
        first=last=0;
    }

    public int size(){
        return last-first;
    }

    public boolean isEmpty(){
        return last==first;
    }

    public void enqueue(Item item){
        if(last-first==queue.length) resize(2*queue.length);
        queue[last%queue.length]=item;//wrap around
        last++;
    }

    public Item dequeue(){
        if(isEmpty()) throw new NoSuchElementException("queue underflow");
        Item item =queue[first%queue.length];
        queue[first%queue.length]=null;//invalidate data set to null
        first++;

        //shrink:length>10,25%full
        if(queue.length>DE_SIZE&&last-first==queue.length/4) resize(queue.length/2);
        return item;
    }

    public Item peek(){
        if(isEmpty()) throw new NoSuchElementException("queue underflow");
        return queue[(last-1)%queue.length];
    }

    private void resize(int capacity){
        assert capacity>last-first;
        Item [] temp=(Item [])new Object[capacity];
        for(int i=0;i<last-first;i++){
            temp[i]=queue[(first+i)%queue.length];
        }
        first=0;
        last=last-first;
        queue=temp;
    }

    public String toString(){
        StringBuilder s=new StringBuilder();
        for(Item item:this){
            s.append(item);
            s.append(" ");
        } 
        return s.toString();
    }

    public Iterator<Item> iterator(){
        return new ResizingArrayQueueIterator();
    }

    private class ResizingArrayQueueIterator implements Iterator<Item>{
        private int current;

        public ResizingArrayQueueIterator(){
            current=first;
        }

        public boolean hasNext(){
            return current<last;
        }

        public Item next(){
            if(!hasNext()) throw new NoSuchElementException("queue underflow");
            Item item=queue[current%queue.length];
            current++;
            return item;
            
        }
        public void remove(){
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args){
        ResizingArrayQueue<String> queue=new ResizingArrayQueue<String>();
        Scanner in =new Scanner(System.in);
        while(in.hasNext()){
            String s=in.next();
            if(!s.equals("-")&&!s.equals("q")) queue.enqueue(s);
            else if(s.equals("q")) break;
            else if(!queue.isEmpty()) System.out.print(" "+queue.dequeue());
        }
        System.out.println("("+queue.size()+"left)");
        System.out.println(queue);
    }

}