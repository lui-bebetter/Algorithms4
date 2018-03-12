/**********************************************************************
 *FileName: BoyerMoore
 *Author:   luibebetter
 *Date:     2018/3/1014:06
 *Description:The BoyerMoore class finds the first occurrence of a pattern
  string in a text string.
 **********************************************************************/

package algs4.SubstringSearch;

/************************************************************************
 *@author: luibebetter
 *@create: 2018/3/10
 *Description:This implementation uses the Boyer-Moore algorithm (with the
  bad-character rule, but not the strong good suffix rule).
 ****************************************************************************/
public class BoyerMoore {
	private final int R;
	private String pattern;// store the pattern as a string or
	private char[] pat;//a character array
	private int [] right;//right[c]=the right-most index of char c

	//Preprocesses the pattern string.
	public BoyerMoore(String pattern){
		if(pattern==null) throw new IllegalArgumentException("Initializes BoyerMoore() with null arguments");
		this.pattern=pattern;
		int m=pattern.length();
		R=256;
		//Initiate the {@code right} array
		right=new int[R];
		for (char  c= 0; c < R; c++) {
			right[c]=-1;
		}
		for (int i = 0; i < m; i++) {
			right[pattern.charAt(i)]=i;
		}
	}

	// Preprocesses the pattern string.
	public BoyerMoore(char [] pattern, int R){
		if(pattern==null) throw new IllegalArgumentException("calls BoyerMoore() with null arguments");
		int m=pattern.length;
		pat=pattern;
		this.R=R;

		//Initialize the right-most array
		right=new int [R];
		for (char c = 0; c < R; c++) {
			right[c]=-1;
		}

		for (int i = 0; i < m; i++) {
			right[pattern[i]]=i;
		}
	}

	public int search(String text){
		if(text==null) throw new IllegalArgumentException("calls search() with null arguments");
		int n=text.length();
		int m=pattern.length();
		int skip;
		for(int i=0;i<=n-m;i+=skip){
			int j;
			skip=0;
			for(j=m-1;j>=0;j--) {
				if(text.charAt(i+j)!=pattern.charAt(j)) {
					skip=Math.max(1,j-right[text.charAt(i+j)]);
					break;
				}
			}
			if(skip==0) return i;//found
		}
		return n;//not found
	}

	public int search(char [] text){
		if(text==null) throw new IllegalArgumentException("calls search() with null arguments");
		int n=text.length;
		int m=pat.length;
		int skip;
		for(int i=0;i<=n-m;i+=skip){
			skip=0;
			int j;
			for(j=m-1;j>=0;j--){
				if(text[i+j]!=pat[j]){
					skip=Math.max(1,j-right[text[i+j]]);
					break;
				}
			}
			if(skip==0) return i;
		}

		return n;
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

		BoyerMoore boyermoore1 = new BoyerMoore(pat);
		BoyerMoore boyermoore2 = new BoyerMoore(pattern, 256);
		int offset1 = boyermoore1.search(txt);
		int offset2 = boyermoore2.search(text);

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
