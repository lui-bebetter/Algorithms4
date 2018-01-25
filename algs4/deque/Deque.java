/******************************
*Deque:double-ended queue supports adding and removing items from either 
 the front or the back of the data structure.

*implements Deque using linked list.
*****************************/

package algs4.deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Deque<Item> implements Iterable<Item>{

	private Node<Item> first;//the front element
	private Node<Item> last;//the end element
	private int size;

	//static inner class for node containing two references
	private static class Node<Item>{
		private Item item;
		private Node<Item> previous;
		private Node<Item> next; 
	}

	//constructor
	public Deque(){
		first=last=null;
		size=0;
	}

	public boolean isEmpty(){
		return size==0;
	}

	//return the size
	public int size(){
		return size;
	}

	//add item to the front
	public void addFirst(Item item){
		if(item==null) throw new IllegalArgumentException("can't call addFirst() with a null argument.");
		Node<Item> oldFirst=first;
		first=new Node<Item>();
		first.item=item;
		first.previous=null;
		first.next=oldFirst;
		if(isEmpty()) last=first;
		else oldFirst.previous=first;
		size++;
	}

	//add item to the end
	public void addLast(Item item){
		if(item==null) throw new IllegalArgumentException("can't call addLast() with a null argument.");
		Node<Item> oldLast=last;
		last=new Node<Item>();
		last.item=item;
		last.previous=oldLast;
		last.next=null;
		if(isEmpty()) first=last;
		else oldLast.next=last;
		size++;
	}

	//remove from the front
	public Item removeFirst(){
		if(isEmpty()) throw new NoSuchElementException("Deque underflow");
		Node<Item> oldFirst=first;
		Item item=oldFirst.item;
		first=oldFirst.next;
        oldFirst.next=null;
        size--;

        //check whether the deque is empty
        if(isEmpty()) last=first;
        else first.previous=null;
		return item;
	}

	//remove from the end
	public Item removeLast(){
		if(isEmpty()) throw new NoSuchElementException("Deque underflow");
		Node<Item> oldLast=last;
		Item item=oldLast.item;
		last=oldLast.previous;
		oldLast.previous=null;
		size--;

        //check whether the deque is empty
        if(isEmpty()) first=last;
        else last.next=null;
		return item;
	}

	//
	public Iterator<Item> iterator(){
		return new DequeIterator();
	}

	//implements Iterator class
	private class DequeIterator implements Iterator<Item>{
		private Node<Item> current=first;

		public boolean hasNext(){
			return current!=null;
		}

		public Item next(){
			if(!hasNext()) throw new NoSuchElementException("Deque underflow.");
			Item item=current.item;
			current=current.next;
			return item; 
		}

		public void remove(){
			throw new UnsupportedOperationException();
		}
	}

    public static void main(String []args){
        Deque<String> deque=new Deque<String>();
        Scanner in =new Scanner(System.in);
        while(in.hasNext()){
            String s=in.next();
            if(s.equals("<")&&!deque.isEmpty()) System.out.print(" "+deque.removeFirst());
            else if(s.equals(">")&&!deque.isEmpty()) System.out.print(" "+deque.removeLast());
            else if(s.equals("q")) break;
            else deque.addFirst(s);
        }
        while(in.hasNext()){
            String s=in.next();
            if(s.equals("<")&&!deque.isEmpty()) System.out.print(" "+deque.removeFirst());
            else if(s.equals(">")&&!deque.isEmpty()) System.out.print(" "+deque.removeLast());
            else if(s.equals("q")) break;
            else deque.addLast(s);
        }
    } 
}