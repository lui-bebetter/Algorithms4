/**********************************************************************
 *FileName: LSD
 *Author:   luibebetter
 *Date:     2018/2/2711:01
 *Description:The LSD class provides static methods for sorting an array
  of w-character strings or 32-bit integers using LSD radix sort.
 **********************************************************************/

package algs4.RadixSort;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.IllformedLocaleException;
import java.util.Scanner;
import java.util.regex.Pattern;

/********************************
 *@author luibebetter
 *@create 2018/2/27
 *Description:For array of fixed length strings, linear time performance
 ********************************/
public class LSD {

	//do not instantiate
	private LSD(){}
	
	//Rearranges the array of W-character strings in ascending order.
	public static void sort(String [] a, int W){
		//check arguments
		if(a==null) throw new IllegalArgumentException("Initialize sort() with null arguments");
		if(W<0) throw new IllegalArgumentException("Length of String can't be negative.");
		for (String s : a) {
			if(s.length()!=W)throw new IllegalArgumentException("The length of String<"+s+"> is not "+W);
		}

		int N=a.length;
		int R=256;// extend ASCII alphabet size
		String [] aux=new String[N];
		for (int d = W-1; d >=0; d--) {
			// sort by key-indexed counting on dth character

			// compute frequency counts
			int [] count =new int[R+1];
			for (int i = 0; i < N; i++) {
				count[a[i].charAt(d)+1]++;
			}

			// compute cumulates
			for(int r=0;r<R;r++){
				count[r+1]+=count[r];
			}

			// move data
			//no using auxiliary array
			for (int i = 0; i < N; i++) {

			}
			for (int i = 0; i < N; i++) {
				aux[count[a[i].charAt(d)]++]=a[i];
			}

			// copy back
			for (int i = 0; i < N; i++) {
				a[i]=aux[i];
			}
		}
	}

	//Rearranges the array of 32-bit integers in ascending order.
	public static void sort(int [] a){
		if(a==null) throw new IllegalArgumentException("calls sort() with null arguments");
		//treating each integer as a sequence of w = 4 bytes (R = 256).
		final int R=256;//a byte
		final int W=4;// each integer is 4 bytes
		final int BITS_PER_BYTES=8;
		final int MASK=R-1;//0xFF

		int N=a.length;
		int [] aux=new int[N];
		for(int d=0;d<W;d++){
			//sort by key-indexed counting on dth byte
			int [] count=new int[R+1];

			//computing frequency counts
			for (int i = 0; i < N; i++) {
				int r=a[i]>>(BITS_PER_BYTES*d) & MASK;
				count[r+1]++;
			}

			//computing cumulates
			for(int r=0;r<R;r++){
				count[r+1]+=count[r];
			}

			// for most significant byte, 0x80-0xFF(negative number) comes before 0x00-0x7F
			if(d==W-1){
				int shift1=count[R/2];//number of positive number
				int shift2=count[R]-count[R/2];//number of negative number

				//put negative number before the positive number
				for(int r=0;r<R/2;r++) {
					count[r]+=shift2;
				}

				for(int r=R/2;r<R;r++){
					count[r]-=shift1;
				}

			}

			//move data
			/*********************************************************
			 * in-place but not stable key-index counting sort version
			 * NOTE:can't be used in LSD.
			 *********************************************************/

			/*for(int i=N-1;i>=0;i--){
				int r=a[i]>>(BITS_PER_BYTES*d) &MASK;
				while(count[r]<i){
					exch(a,i,count[r]++);
					r=a[i]>>(BITS_PER_BYTES*d) &MASK;
				}
			}*/

			/******************************************************
			 * extra n space but stable version, can be used in LSD.
			 ******************************************************/
			for (int i = 0; i < N; i++) {
				int r=a[i] >>(BITS_PER_BYTES*d) &MASK;
				aux[count[r]++]=a[i];
			}

			//copy back
			for (int i = 0; i < N; i++) {
				a[i]=aux[i];
			}

		}
	}

	private static void exch(int []a, int i, int j){
		int tmp=a[i];
		a[i]=a[j];
		a[j]=tmp;
	}

	/**
	 * Reads in a sequence of fixed-length strings from standard input;
	 * LSD radix sorts them;
	 * and prints them to standard output in ascending order.
	 *
	 * @param args the command-line arguments
	 */
	public static void main(String[] args) {
		final Pattern EVERYTHING_PATTERN=Pattern.compile("\\A");
		final Pattern WHITESPACE_PATTERN=Pattern.compile("\\p{javaWhitespace}+");
		Scanner in;
		try{
			in=new Scanner(new BufferedInputStream(new FileInputStream(args[0])));
		}catch(IOException e){
			throw new IllegalArgumentException();
		}
		//read all input as a string
		String all=in.useDelimiter(EVERYTHING_PATTERN).next();

		//spilt the input and transfer it to double
		String [] s=WHITESPACE_PATTERN.split(all.trim());

		/*a=new Double[s.length];
		for (int i=0;i<s.length;i++) a[i]=Double.parseDouble(s[i]);*/


		// check that strings have fixed length
		int n = s.length;
		/*int w = s[0].length();
		for (int i = 0; i < n; i++)
			assert s[i].length() == w : "Strings must have fixed length";*/

		// sort the strings
		int []a=new int[s.length];
		for (int i = 0; i < s.length; i++) {
			a[i]=Integer.parseInt(s[i]);
		}
		sort(a);

		// print results
		for (int i = 0; i < n; i++)
			System.out.println(a[i]);
	}

}
