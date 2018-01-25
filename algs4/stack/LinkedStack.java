/************************
*  A generic stack, implemented using a linked list. Each stack
*  element is of type Item.

************************/
package algs4.stack;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class LinkedStack<Item> implements Iterable<Item>{
    private Node<Item> first;
    private int size;
    //inner class 
    private static class Node<Item>{
        private Item item;
        private Node<Item> next;
    }

    public  LinkedStack(){
        first=null;
        size=0;
    }


    public boolean isEmpty(){
        return first==null;
    }


    public void push(Item item){
        Node<Item> oldNode=first;
        first=new Node<Item>();
        first.item=item;
        first.next=oldNode;
        size++;
    }

    public Item pop(){
        if(isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item=first.item;
        first=first.next;
        size--;
        return item;
    }

    public int size(){
        return size;
    }

    //return the item most recently added to the stack
    public Item peek(){
        if(isEmpty()) throw new NoSuchElementException("Stack underflow");
        return first.item;
    }

    public String toString(){
        StringBuilder s=new StringBuilder();
        for(Item item:this){
            s.append(item);
            s.append(" ");
        } 
        return s.toString();
    }

    //iterable implements
    public Iterator<Item> iterator(){
        return new LinkedStackIterator();
    }


    public class LinkedStackIterator implements Iterator<Item>{
        private Node<Item> current;

        public LinkedStackIterator(){
            current=first;
        }

        public boolean hasNext(){
            return current!=null;
        }

        public Item next(){
            if(!hasNext()) throw new NoSuchElementException("Stack underflow");
            Item item=current.item;
            current=current.next;
            return item;
        }

        public void remove(){
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[]args){
        Scanner in =new Scanner(System.in);
        LinkedStack<String> stack=new LinkedStack<String>();
        while(in.hasNext()){
            String s=in.next();
            if(!s.equals("-")) stack.push(s);
            else if(s.equals("q")) break;
            else if(!stack.isEmpty()) System.out.print(" "+stack.pop());
        }
        System.out.println("("+stack.size()+"left)");
    } 

}