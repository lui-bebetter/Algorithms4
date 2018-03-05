package algs4.hello;
/**********************************************************************
 *FileName: Solution
 *Author:   luibebetter
 *Date:     2018/3/116:17
 *Description:leetcode template
 **********************************************************************/



import algs4.queue.LinkedQueue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/********************************
 *@author: luibebetter
 *@create: 2018/3/1
 *Description:
 ********************************/
public class Solution {
	private int preindex;//the subtree root index in {@code pre} array

	public TreeNode reConstructBinaryTree(int [] pre, int [] in) {
		int n1=pre.length,n2=in.length;
		if(n1!=n2||n1==0) throw new IllegalArgumentException();
		TreeNode root =subtree(pre,in,0,n1-1);
		return root;
	}

	private TreeNode subtree(int []pre,  int []in, int lo, int hi){
		/*******************************************************************************
		 * Description:return the root of the subtree in the {@code in} array from lo to hi
		 * @param preindex    the subtree root index in {@code pre} array
		 * @param in   the inorde of the BT
		 * @param lo   the lower index
		 * @param hi the higher index
		 * @return: Solution.TreeNode
		 * @Author: luibebetter
		 ***************************************************************************/
		if(hi<lo) return null;
		int i;
		for(i=lo;i<=hi;i++) {
			if(in[i]==pre[preindex]) break;
		}

		TreeNode node=new TreeNode(pre[preindex]);
		preindex++;
		node.left=subtree(pre,in,lo,i-1);
		node.right=subtree(pre,in,i+1,hi);
		return node;
	}
	public class TreeNode{
		int val;
		TreeNode left;
		TreeNode right;
		public TreeNode(int x){
			val=x;
		}
	}

	public void printLevelOrder(TreeNode root){
		if(root==null) throw new IllegalArgumentException("calls printLevelOrder() with null arguments");
		LinkedQueue<TreeNode> q=new LinkedQueue<>();
		q.enqueue(root);
		while(!q.isEmpty()){
			TreeNode node=q.dequeue();
			System.out.print(node.val);
			if(node.left!=null) {
				q.enqueue(node.left);

			}
			if(node.right!=null){
				q.enqueue(node.right);

			}
		}
	}

	public static void main(String[] args) {
		int []pre=new int[]{1,2,4,7,3,5,6,8};
		int []in=new int[]{4,7,2,1,5,3,8,6};
		Solution s=new Solution();
		s.printLevelOrder(s.reConstructBinaryTree(pre, in));
	}
}
