/**********************************************************************
 *FileName: Outcast
 *Author:   luibebetter
 *Date:     2018/1/2315:40
 *Description:Outcast detection
 **********************************************************************/

package algs4.DirectedGraph.WordNet;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/********************************
 *@author: luibebetter
 *@create: 2018/1/23
 *Description:
 ********************************/
public class Outcast {
	private  final WordNet wordnet;

	//construcor takes a WordNet Object
	public Outcast(WordNet wordnet){
		if(wordnet==null) throw new IllegalArgumentException("Initiate Outcast with null wordnet");
		this.wordnet=wordnet;
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String [] nouns){
		if(nouns==null) throw new IllegalArgumentException("calls outcast() with null arguments");
		int maxDis=-1;
		int maxId=-1;
		for(int i=0;i<nouns.length;i++){
			int dis=0;
			for(int j=0;j<nouns.length;j++){
				dis+=wordnet.distance(nouns[i],nouns[j]);
			}
			if(dis>maxDis) {
				maxDis=dis;
				maxId=i;
			}
		}
		return nouns[maxId];
	}

	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);

		final Pattern EVERYTHING_PATTERN=Pattern.compile("\\A");
		final Pattern WHITESPACE_PATTERN=Pattern.compile("\\p{javaWhitespace}+");
		for (int t = 2; t < args.length; t++) {
			Scanner in;
			try {
				in = new Scanner(new BufferedInputStream(new FileInputStream(args[t])));
			} catch (IOException e) {
				throw new IllegalArgumentException("can't open the file " + args[t] );
			}

			//read all input as a string
			String all=in.useDelimiter(EVERYTHING_PATTERN).next();

			//spilt the input and transfer it to double
			String [] nouns=WHITESPACE_PATTERN.split(all.trim());
			System.out.println(args[t] + ": " + outcast.outcast(nouns));
		}
	}
}
