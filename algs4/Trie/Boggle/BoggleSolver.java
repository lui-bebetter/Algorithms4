/**********************************************************************
 *FileName: BoggleSolver
 *Author:   luibebetter
 *Date:     2018/3/1019:43
 *Description:A data type for solving the Boggle problem.
 **********************************************************************/

package algs4.Trie.Boggle;

import algs4.Trie.TrieSET;
import algs4.queue.LinkedQueue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

/********************************
 *@author: luibebetter
 *@create: 2018/3/10
 *Description:
 ********************************/
public class BoggleSolver {
	private static final int R = 26;//the 26 letters A through Z
	private int OFFSET=65;
	private Node root;//the root
	private int size;//number of strings in the dictionary Trie
	private boolean [][]marked;


	// Initializes the data structure using the given array of strings as the dictionary.
	// (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
	public BoggleSolver(String [] dictionary) {
	  if(dictionary==null) throw new IllegalArgumentException("Initialize BoggleSolver() with null dictionary.");
	  root=new Node();
	  for(String key:dictionary) add(key);
	}


	// Returns the set of all valid words in the given Boggle board, as an Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		if(board==null) throw new IllegalArgumentException("calls getAllValidWords() with null arguments");
	  int n=board.rows(),m=board.cols();
	  marked=new boolean[n][m];
		HashSet<String> validWords=new HashSet<>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				dfs(board,root,new StringBuilder(),i,j,validWords);
			}
		}

		return validWords;
	}

	private void dfs(BoggleBoard board, Node node, StringBuilder prefix, int row, int col, HashSet<String> set) {
		marked[row][col]=true;
		char letter=board.getLetter(row,col);
		prefix.append(letter);
		String s=String.valueOf(letter);
		if(letter=='Q') {
			prefix.append('U');// the letter Q is  followed by the letter U
			s="QU";
		}
		Node next=get(node,s,0);
		if (next != null) {
			if (next.isString && prefix.length()>2) set.add(prefix.toString());
			for (int i = row - 1; i <= row + 1; i++) {
				for (int j = col - 1; j <= col + 1; j++) {
					if (i == row && j == col) continue;
					else if( (i>=0 && i<board.rows() && j>=0 && j <
							board.cols()) && !marked[i][j]){
						dfs(board, next, prefix, i, j, set);
					}
				}
			}
		}

		prefix.deleteCharAt(prefix.length()-1);
		if(letter=='Q') prefix.deleteCharAt(prefix.length()-1);
		marked[row][col]=false;
	}


	// Returns the score of the given word if it is in the dictionary, zero otherwise.
	// (You can assume the word contains only the uppercase letters A through Z.)
	public int scoreOf(String  word) {
		if(word==null) throw new IllegalArgumentException("calls scoreOf() with null arguments");
		if(!contains(word)) return 0;
		int len=word.length();
		if(len<3) return 0;
		else if(len<5) return 1;
		else if(len==5) return 2;
		else if(len==6) return 3;
		else if(len==7) return 5;
		else return 11;
	}

	/********************************************************************************
	 * Implementing the 26-way-Trie: include add, contains ,get operation.
	 ***************************************************************************/


	private static class Node {
		private boolean isString;
		private Node[] next = new Node[R];//implicitly store the R-char
	}

	//return number of key-value pair in the TrieST
	private int size() {
			return size;
	}

	//is empty?
	private boolean isEmpty() {
			return size() == 0;
	}

	//put operation
	private void add(String key) {
			//argument check
			if (key == null) throw new IllegalArgumentException("calls put() with null first arguments");
			root = add(root, key, 0);
	}

	private Node add(Node node, String key, int d) {
			/*****************************************************************
			 * Description: add the key to the subtree rooted at{@code node}
			 *
			 * @param node the subtree root
			 * @param key
			 * @param value
			 * @param d  the index of character in the string to be compared
			 * @return: algs4.Trie.TrieST.Node
			 * @Author: luibebetter
			 ****************************************************************/
			if (node == null) node = new Node();//
			if (d == key.length()) {
				if (!node.isString) size++;
				node.isString = true;
				return node;
			}
			char c = key.charAt(d);
			node.next[c-OFFSET] = add(node.next[c-OFFSET], key, d + 1);
			return node;
	}

	//does the SET contain {@code key}
	private boolean contains(String key) {
			if (key == null) throw new IllegalArgumentException("calls contains() with null arguments");
			Node node = get(root, key, 0);
			if (node == null) return false;
			else return node.isString;
	}

	private Node get(Node node, String key, int d) {
			/************************************************************************
			 * Description: get the value of the string @{code key} in the subtree rooted at
			 {@code node} based on the dth character
			 *
			 * @param node the subtree root
			 * @param key
			 * @param d  the index of character in the string to be compared
			 * @return: algs4.Trie.TrieST.Node
			 * @Author: luibebetter
			 **********************************************************************/
			if (node == null) return null;
			if (d == key.length()) return node;
			char c = key.charAt(d);
			return get(node.next[c-OFFSET], key, d + 1);
	}

	public static void main(String[] args) {
		final Pattern EVERYTHING_PATTERN=Pattern.compile("\\A");
		final Pattern WHITESPACE_PATTERN=Pattern.compile("\\p{javaWhitespace}+");
		Scanner in;
		try {
			in = new Scanner(new BufferedInputStream(new FileInputStream(args[0])));
		} catch (IOException e) {
			throw new IllegalArgumentException("Can't open the file "+args[0]);
		}
		String all=in.useDelimiter(EVERYTHING_PATTERN).next();
		String[] dictionary = WHITESPACE_PATTERN.split(all.trim());
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard(args[1]);
		int score = 0;
		for (String word : solver.getAllValidWords(board)) {
			System.out.println(word);
			score += solver.scoreOf(word);
		}
		System.out.println("Score = " + score);
	}

}
