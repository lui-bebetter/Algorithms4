/**********************************************************************
 *FileName: RabinKarp
 *Author:   luibebetter
 *Date:     2018/3/1014:42
 *Description:The RabinKarp class finds the first occurrence of a pattern
  string in a text string
 **********************************************************************/

package algs4.SubstringSearch;

import java.math.BigInteger;
import java.util.Random;

/********************************
 *@author: luibebetter
 *@create: 2018/3/10
 *Description:This implementation uses the Rabin-Karp algorithm:using hash function.
 ********************************/
public class RabinKarp {
	private String pattern;//the string pattern
	private long patHash;//modular hash value of the pattern
	private int R;//radix
	private long Q;//a large prime, small enough to avid overflow
	private long RM;//R^(M-1) %Q

	//pre-process the pattern string
	public RabinKarp(String pattern){
		if(pattern==null) throw new IllegalArgumentException("Initialize RabinKarp with null pattern");
		this.pattern=pattern;
		R=256;
		Q=longRandomPrime();

		int m=pattern.length();
		RM=1;
		for (int i = 0; i < m-1; i++) {
			RM=(RM*R)%Q;
		}
		patHash=hash(pattern,pattern.length());
	}

	public int search(String text){
		if(text==null) throw new IllegalArgumentException("calls search() with null arguments");
		int n=text.length();
		int m=pattern.length();
		if(n<m) return n;
		long textHash=hash(text,m);
		if(textHash==patHash) return 0;
		for(int i=1;i<=n-m;i++){
			textHash=(textHash+Q-RM*text.charAt(i-1) % Q ) % Q;
			textHash=(textHash*R+text.charAt(i+m-1)) %Q;

			if(textHash==patHash) return i;
		}
		return n;
	}

	private long hash(String pattern, int m){
		long hash=0;
		for (int i = 0; i < m; i++) {
			hash=(hash*R+pattern.charAt(i)) % Q;
		}

		return hash;
	}

	private static long longRandomPrime(){
		BigInteger prime=BigInteger.probablePrime(31,new Random());
		return prime.longValue();
	}

	public static void main(String[] args) {
		String pat = args[0];
		String txt = args[1];

		RabinKarp searcher = new RabinKarp(pat);
		int offset = searcher.search(txt);

		// print results
		System.out.println("text:    " + txt);

		// from brute force search method 1
		System.out.print("pattern: ");
		for (int i = 0; i < offset; i++)
			System.out.print(" ");
		System.out.println(pat);
	}
}
