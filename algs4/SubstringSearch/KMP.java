/**********************************************************************
 *FileName: KMP
 *Author:   luibebetter
 *Date:     2018/3/923:08
 *Description:The KMP class finds the first occurrence of a pattern string
  in a text string.
 **********************************************************************/

package algs4.SubstringSearch;

/*****************************************************************************
 *@author: luibebetter
 *@create: 2018/3/9
 *Description:This implementation uses a version of the Knuth-Morris-Pratt
  substring search algorithm. The version takes time proportional to n + m R
  in the worst case, where n is the length of the text string, m is the length
  of the pattern, and R is the alphabet size. It uses extra space proportional
  to m R.
 *****************************************************************************/
public class KMP {
	private final int R;//radix of the string character
	private final int M;//length of pattern string or pattern characte array
	private int[][] dfa;//deterministic finite state automaton

	//Pre-processing the pattern string
	public KMP(String pattern){
		if(pattern==null) throw new IllegalArgumentException("Initialize KMP() with null pattern");
		M=pattern.length();

		R=256;
		dfa=new int[R][M];

		//Initialize the {@code dfs} array
		dfa[pattern.charAt(0)][0]=1;
		for (int j = 1,x=0; j < M; j++) {
			for (char c = 0; c < R; c++) {
				 dfa[c][j]=dfa[c][x];//copy mismatch case
			}
			dfa[pattern.charAt(j)][j]=j+1;//set match case
			x=dfa[pattern.charAt(j)][x];//update restart state
		}
	}

	//pre-process the pattern char array
	public KMP(char[] pattern, int R){
		if(pattern==null) throw new IllegalArgumentException("calls KMP() with null arguments");
		M=pattern.length;
		this.R=R;
		dfa=new int[R][M];

		dfa[pattern[0]][0]=1;
		for (int j = 1,x=0; j < M; j++) {
			for (char c = 0; c < R; c++) {
				dfa[c][j]=dfa[c][x];//copy the mismatch
			}
			dfa[pattern[j]][j]=j+1;//set match case
			x=dfa[pattern[j]][x];//update restart state
		}
	}

	//Returns the index of the first occurrrence of the pattern string in the text string
	public int search(String text){
		if(text==null) throw new IllegalArgumentException("calls search() with null arguments");
		// simulate operation of DFA on text
		int N=text.length();
		int i,j=0;
		for ( i = 0; i < N && j<M; i++) {
			j=dfa[text.charAt(i)][j];
		}

		if(j==M) return i-M;//found
		return N;//not found
	}

	public int search(char [] text){
		if(text==null) throw new IllegalArgumentException("calls search() with null arguments");
		int N=text.length;
		int i,j;
		for ( i = 0,j=0; i < N && j<M; i++) {
			j=dfa[text[i]][j];
		}

		if(j==M) return i-M;
		return N;
	}

	/**
	 * Takes a pattern string and an input string as command-line arguments;
	 * searches for the pattern string in the text string; and prints
	 * the first occurrence of the pattern string in the text string.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		String pat = args[0];
		String txt = args[1];
		char[] pattern = pat.toCharArray();
		char[] text    = txt.toCharArray();

		KMP kmp1 = new KMP(pat);
		int offset1 = kmp1.search(txt);

		KMP kmp2 = new KMP(pattern, 256);
		int offset2 = kmp2.search(text);

		// print results
		System.out.println("text:    " + txt);

		System.out.print("pattern: ");
		for (int i = 0; i < offset1; i++)
			System.out.print(" ");
		System.out.println(pat);

		System.out.print("pattern: ");
		for (int i = 0; i < offset2; i++)
			System.out.print(" ");
		System.out.println(pat);
	}
}
