/**********************************************************************
 *FileName: reconstructeBST
 *Author:   luibebetter
 *Date:     2018/3/416:18
 *Description:Given preorder traversal of a binary search tree, construct the BST.
 *Tree order of BST:1、perorder:parent fisrt 2、inorder:parent second
  3、postorder:parent last.
 **********************************************************************/

package algs4.BinarySearchTree;

import algs4.stack.LinkedStack;

/********************************
 *@author: luibebetter
 *@create: 2018/3/4
 *Description:
 ********************************/
public class reconstructeBST<Key extends Comparable<Key>> {
	private Node<Key> root;//the root of the reconstructed BST

	private static class Node<Key>{
		Key key;
		Node<Key> left;
		Node<Key> right;

		public Node(Key key){
			this.key=key;
		}
	}

	//run time :O(n)
	public reconstructeBST(Key[] pre){
		if(pre==null) throw new IllegalArgumentException("calls reconstructeBST() with null arguments");
		int n=pre.length;
		root=new Node<Key>(pre[0]);

		//mantain a stack of the node, pop it only when all its subtree has been set
		LinkedStack<Node<Key>> s=new LinkedStack<>();
		s.push(root);
		for (int i = 1; i < n; i++) {
			Node<Key> tmp=null;

			//if the next key greater than the current node, find its parent and
			//be the left node of that node
			while(!s.isEmpty() && pre[i].compareTo(s.peek().key)>0){
				tmp=s.pop();
			}
			if(tmp==null) {
				tmp=s.peek();
				tmp.left=new Node<>(pre[i]);
				s.push(tmp.left);
			}else{
				tmp.right=new Node<>(pre[i]);
				s.push(tmp.right);
			}
		}
	}

	public void printInorder(Node<Key> node){
		if (node == null) {
			return;
		}
		printInorder(node.left);
		System.out.print(node.key + " ");
		printInorder(node.right);
	}

	// Driver program to test above functions
	public static void main(String[] args) {
		Integer pre[] = new Integer[]{10, 5, 1, 7, 40, 50};
		reconstructeBST<Integer> tree = new reconstructeBST<Integer>( pre);
		System.out.println("Inorder traversal of the constructed tree is ");
		tree.printInorder(tree.root);
	}
}
