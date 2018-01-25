/**********************************************************************
 *FileName: Bag
 *Author:   Dell
 *Date:     2018/1/122:33
 *Description:Bag implementation uses a singly linked list with a static
  nested class Node
 **********************************************************************/
package algs4.UndirectedGraph;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**********************************************************************
 *@author: Dell
 *@create: 2018/1/1
 *Description:The add, isEmpty, and size operations take constant time.
  Iteration takes time proportional to the number of items.
 ***********************************************************************/
public class Bag<Item> implements Iterable<Item> {

	private Node<Item> first;//the reference point to the first node in the bag
	private int  size;//the size the bag

	private static class Node<Item>{
		/****************************************************
		 * Description: static inner class {@code Node} in a
		   single-linked list

		 * @Author: luibebetter
		 ****************************************************/
		private Item item;
		private Node<Item> next;
	}

	//initialize the private variables
	public Bag(){
		first=null;
		size=0;
	}

	public void add(Item item){
		/********************************
		 * Description: add a new item to the bag
		 *
		 * @param item
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if(item==null) throw new IllegalArgumentException("calls add() with null arguments");
		Node<Item> oldFirst=first;
		first=new Node<Item>();
		first.item=item;
		first.next=oldFirst;
		size++;
	}

	//return the size of the bag
	public int size(){
		return size;
	}

	public boolean isEmpty(){
		/********************************
		 * Description: is empty?
		 *
		 * @param
		 * @return: boolean
		 * @Author: luibebetter
		 *********************************/
		return size()==0;
	}

	@Override
	public Iterator<Item> iterator() {
		return new BagIterator();
	}

	private class BagIterator implements Iterator<Item>{
		private Node<Item> current;

		public BagIterator(){current=first;}

		@Override
		public boolean hasNext() {
			return current!=null;
		}

		@Override
		public Item next() {
			if(!hasNext()) throw new NoSuchElementException("Bag underflow.");
			Item tmp=current.item;
			current=current.next;
			return tmp;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Unit tests the {@code Bag} data type.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		Bag<String> bag = new Bag<String>();
		Scanner in=new Scanner(System.in);
		while (in.hasNext()) {
			String item = in.next();
			bag.add(item);
		}

		System.out.println("size of bag = " + bag.size());
		for (String s : bag) {
			System.out.println(s);
		}
	}
}
