/**********************************************************************
 *FileName: Quick3string
 *Author:   luibebetter
 *Date:     2018/3/111:06
 *Description:The Quick3string class provides static methods for sorting
  an array of strings using 3-way radix quicksort.
 **********************************************************************/

package algs4.RadixSort;

import algs4.StdRandom.StdRandom;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/********************************
 *@author: luibebetter
 *@create: 2018/3/1
 *Description:
 ********************************/
public class Quick3string {
	private static final int CUTOFF_FOR_INSERTIONSORT=15;

	//do not instantiate
	private Quick3string(){}

	// Rearranges the array of strings in ascending order
	public static void sort(String[]a){
		sort(a,0,a.length-1,0);
	}

	//sort the subarray from lo to hi using dth character
	private static void sort(String[]a, int lo, int hi, int d){
		if(hi-lo+1<=CUTOFF_FOR_INSERTIONSORT) {
			insertionSort(a,lo,hi,d);
			return;
		}
		int v=charAt(a[lo],d);
		int lt=lo, gt=hi;
		int i=lo+1;
		while(i<=gt){
			if(charAt(a[i],d)<v) exch(a,i++,lt++);
			else if(charAt(a[i],d)>v) exch(a,i,gt--);
			else i++;
		}
		sort(a,lo,lt-1,d);
		if(v>=0) sort(a,lt,gt,d+1);//exclude -1 which means string have no character
		sort(a,gt+1,hi,d);
	}

	/**********************************************************
	 * Helper Function
	 **********************************************************/
	//insertion sort for small subarray
	private static void insertionSort(String []a, int lo, int hi, int d){
		if(hi<=lo) return;
		for(int i=lo+1;i<=hi;i++){
			for(int j=i;j>lo && less(a[j],a[j-1],d);j--){
				exch(a,j,j-1);
			}
		}
	}

	// return the dth character of s, -1 if d = length of s
	private static int charAt(String a, int d){
		assert d<=a.length();
		if(d<a.length()) return a.charAt(d);
		else return -1;
	}

	private static void exch(Object []a, int i, int j){
		Object tmp=a[i];
		a[i]=a[j];
		a[j]=tmp;
	}

	private static boolean less(String s1, String s2, int d){
		return s1.substring(d).compareTo(s2.substring(d))<0;
	}

	public static void main(String[] args) {
		/*final Pattern EVERYTHING_PATTERN=Pattern.compile("\\A");
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

		sort(s);

		// print results
		for (int i = 0; i < s.length; i++)
			System.out.println(s[i]);*/
		int [] []a=new int[2][3];
		System.out.println(a.length+" "+a[0].length);
	}
}
