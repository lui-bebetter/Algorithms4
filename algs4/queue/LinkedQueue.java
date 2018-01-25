/*************************
*implements queue data structure using singly-linked list
*contains two references:first point to the least recently added,last point to the most recently added

******************/
package algs4.queue;

import  java.util.Iterator;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class LinkedQueue<Item> implements Iterable<Item>{

    private Node<Item> first,last;
    private int size;

    //static inner class for saving memory
    public  static class Node<Item>{
        Item item;
        Node<Item> next;
    }

    //constructor
    public LinkedQueue(){
        first=last=null;
        size=0;
    } 

    //
    public boolean isEmpty(){
        return size==0;
    }

    public int size(){
        return size;
    }

    //corner case:when the queue is empty
    public void enqueue(Item item){
        Node<Item> oldlast=last;
        last=new Node<Item>();
        last.item=item;
        last.next=null;
        if(isEmpty()) first=last;
        else oldlast.next=last;
        size++;
    }

    //dequeue:corner case:after dequeue the queue become empty.
    public Item dequeue(){
        if(isEmpty()) throw new NoSuchElementException("queue underflow");
        Item item=first.item;
        first=first.next;
        size--;
        if(isEmpty()) last=null;
        return item;
    }

    //return the most reccently added element
    public Item peek(){
        if(isEmpty()) throw new NoSuchElementException("queue underflow");
        return last.item;
    }

    public String toString(){
        StringBuilder s=new StringBuilder();
        for(Item item:this){
            s.append(item);
            s.append(" ");
        }
        return s.toString();
    }

    //return an iterator
    public Iterator<Item> iterator(){
        return new LinkedQueueIterator();
    }

    //iterator class
    private class LinkedQueueIterator implements Iterator<Item>{
        private Node<Item> current;

        public LinkedQueueIterator(){
            current=first;
        }

        public boolean hasNext(){
            return current!=null;
        }

        public Item next(){
            if(!hasNext()) throw new NoSuchElementException("queue underflow");
            Item item=current.item;
            current=current.next;
            return item;
        }

        public void remove(){
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String [] args){
        LinkedQueue<String> queue=new LinkedQueue<String>();
        Scanner in =new Scanner(System.in);
        while(in.hasNext()){
            String s=in.next();
            if(!s.equals("-")) queue.enqueue(s);
            else if(s.equals("q")) break;
            else if(!queue.isEmpty()) System.out.print(" "+queue.dequeue());
        }
        System.out.println("("+queue.size()+"left)");
    }
    
}