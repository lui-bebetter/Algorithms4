/**********************************************************************
 *FileName: WordNet
 *Author:   luibebetter
 *Date:     2018/1/2221:11
 *Description:
 **********************************************************************/

package algs4.DirectedGraph.WordNet;

import algs4.BinarySearchTree.RedBlackBST;
import algs4.DirectedGraph.Digraph;
import algs4.DirectedGraph.DirectedCycle;
import algs4.queue.LinkedQueue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/********************************
 *@author: luibebetter
 *@create: 2018/1/22
 *Description:The constructor takes time linearithmic (nlogn)in the input size,
  The methods distance() and sap()  run in time linear in the size of the WordNet digraph.
 ********************************/
public class WordNet {
	private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\p{javaWhitespace}+");
	private static final Pattern COMMAS_PATTERN = Pattern.compile(",");
	private RedBlackBST<String, LinkedQueue<Integer>> nounsST=new RedBlackBST<>();//a symbol table stores the <noun,synset-IDs Queue> pairs
	private String[] synsets;//synsets[i]=the synset nouns of the i synset id
	private final Digraph G;
	private final SAP sap;

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		readSynsets(synsets);//read in synsets, initiating the the {@code synsets} and {@code nounST} data structure
		Digraph G = readHpernyms(hypernyms);
		isRootedDAG(G);
		this.G = G;
		sap = new SAP(this.G);
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return nounsST.keys();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if (word == null) throw new IllegalArgumentException("calls isNoun() with null arguments");
		return nounsST.contains(word);
	}

	//  SAP distance between nounA and nounB
	public int distance(String nounA, String nounB) {
		if (!(isNoun(nounA) && isNoun(nounB)))
			throw new IllegalArgumentException("calls distance() with non-WordNet nouns");
		return sap.length(nounsST.get(nounA), nounsST.get(nounB));
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (!(isNoun(nounA) && isNoun(nounB)))
			throw new IllegalArgumentException("calls distance() with non-WordNet nouns");
		int ancestor = sap.ancestor(nounsST.get(nounA), nounsST.get(nounB));
		if(ancestor==-1) return null;
		return synsets[ancestor];
	}


	/*******************************************
	 * Helper function
	 **********************************************/
	private String[] queueToArray(LinkedQueue<String> queue) {
		int length = queue.size();
		String[] a = new String[length];
		for (int i = 0; i < length; i++) a[i] = queue.dequeue();
		return a;
	}


	private void readSynsets(String synsets) {
		/********************************
		 * Description:read in synsets, initiating the the {@code synsets} and {@code nounST} data structure
		 *
		 * @param synsets
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		Scanner synIn;
		try {
			synIn = new Scanner(new BufferedInputStream(new FileInputStream(synsets)));
		} catch (IOException e) {
			throw new IllegalArgumentException("can't open the file " + synsets );
		}

		//read,split and initiate the {@code synsets} and {@code nounST}
		LinkedQueue<String> queue = new LinkedQueue<>();
		while (synIn.hasNextLine()) {
			//split the line
			String[] line = COMMAS_PATTERN.split(synIn.nextLine());
			String synsetNouns = line[1];//the second field in the line of the synsets file
			queue.enqueue(synsetNouns);
			String[] nouns = WHITESPACE_PATTERN.split(synsetNouns.trim());

			int synsetId = Integer.parseInt(line[0]);
			for (int i = 0; i < nouns.length; i++) {
				if (!nounsST.contains(nouns[i])) {
					LinkedQueue<Integer> ids = new LinkedQueue<>();
					ids.enqueue(synsetId);
					nounsST.put(nouns[i], ids);
				} else {
					nounsST.get(nouns[i]).enqueue(synsetId);
				}
			}
		}

		this.synsets = queueToArray(queue);
	}

	private Digraph readHpernyms(String hypernyms) {
		/********************************
		 * Description: read in hypernyms and build the digraph
		 *
		 * @param hypernyms
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		Scanner hyperIn;
		try {
			hyperIn = new Scanner(new BufferedInputStream(new FileInputStream(hypernyms)));
		} catch (IOException e) {
			throw new IllegalArgumentException("can't open the file " + synsets + " or " + hypernyms);
		}

		Digraph G = new Digraph(synsets.length);
		//add edges
		while (hyperIn.hasNextLine()) {
			String[] id = COMMAS_PATTERN.split(hyperIn.nextLine());
			int synsetId = Integer.parseInt(id[0]);
			for (int i = 1; i < id.length; i++) {
				G.addEdge(synsetId, Integer.parseInt(id[i]));
			}
		}

		return G;
	}

	//is the Digraph a rooted DAG
	private void isRootedDAG(Digraph G) {
		DirectedCycle cycle = new DirectedCycle(G);
		if (cycle.hasCycle()) throw new IllegalArgumentException("the synsets corresponding digraph is not a rooted DAG");
		int root = 0;
		for (int i = 0; i < G.V(); i++) {
			if (G.outdegree(i) == 0) root++;
		}
		if (root != 1) throw new IllegalArgumentException("the synsets corresponding digraph is not a rooted DAG");
	}

	// do unit testing of this class
	public static void main(String[] args) {
		String synsets=args[0];
		String hypernyms=args[1];
		WordNet wd=new WordNet(synsets,hypernyms);
		Scanner in =new Scanner(new BufferedInputStream(System.in));
		int i=0;
		for(String s:wd.nouns()) i++;
		System.out.println(i);
		while(in.hasNext()){
			String nounA=in.next();
			String nounB=in.next();
			System.out.println(wd.distance(nounA,nounB));
			System.out.println(wd.sap(nounA,nounB));
		}
	}
}
