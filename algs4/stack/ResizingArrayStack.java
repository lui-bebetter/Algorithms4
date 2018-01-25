/****************
*ResizingArrayStack: implements Stack using resizing array.

*when the array is full,double the current size.when the array is one-quarter full,halves the array.

*default size:10
***************/
package algs4.stack;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class ResizingArrayStack<Item> implements Iterable<Item> {
    private Item[] array;
    private int size;
    private static final int  DE_size=10;//default size of the array

    //default constructor
    public ResizingArrayStack(){
        array=(Item [])new Object[DE_size];
        size=0;
    }

    //
    public boolean isEmpty(){
        return size==0;
    }

    //size
    public int size(){
        return size;
    }

    //push
    public void push(Item item){
        int len=array.length;
        if(size==len) resize(len*2);
        array[size++]=item;
    }

    //pop
    public Item pop(){
        if(isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item temp=array[size-1];
        array[size-1]=null;
        size--;
        //shrink
        if(size>0&&size==array.length/4&&array.length>DE_size) resize(array.length/2);
        return temp;
    }

    //peek:return the item most recently added
    public Item peek(){
        if(isEmpty()) throw new NoSuchElementException();
        return array[size-1];
    }

    public String toString(){
        StringBuilder s=new StringBuilder();
        for(Item item:this){
            s.append(item);
            s.append(" ");
        } 
        return s.toString();
    }
    //Iterable implements
    public Iterator<Item> iterator(){
        return new ResizingArrayStackIterator();
    }

    //Iterator implements
    private class ResizingArrayStackIterator implements Iterator<Item>{
        private int current;

        public ResizingArrayStackIterator(){
            current=size-1;
        }

        public boolean hasNext(){
            return current>=0;
        }

        public void remove(){
            throw new UnsupportedOperationException();
        }

        public Item next(){
            if(!hasNext()) throw new NoSuchElementException("Stack underflow");
            return array[current--];
        }
    }

    //resize the array 
    private void resize(int capacity){
        assert capacity>size;
        Item[] temp=(Item[])new Object[capacity];
        for(int i=0;i<size;i++){
            temp[i]=array[i];
        }
        array=temp;
    }

    //test client
    public static void main(String [] args){
        ResizingArrayStack<String> stack=new ResizingArrayStack<String>();
        Scanner in=new Scanner(System.in);
        while(in.hasNext()){
            String s=in.next();
            if(s.equals("-")&&!stack.isEmpty()) System.out.print(" "+stack.pop());
            else if(s.equals("q")) break;
            else stack.push(s);
        }
        System.out.println("("+stack.size()+" left)");
        System.out.println(stack);
    }
}