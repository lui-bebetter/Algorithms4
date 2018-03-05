/**********************************************************************
 *FileName: MSD
 *Author:   luibebetter
 *Date:     2018/2/2720:21
 *Description:The MSD class provides static methods for sorting an array
  of extended ASCII strings or integers using MSD radix sort.
 **********************************************************************/

package algs4.RadixSort;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**********************************************************************
 *@author: luibebetter
 *@create: 2018/2/27
 *Description:for array of variable length strings, linear time performance
  using extra space~N+DR, D-the depth of the recursive call
 **********************************************************************/
public class MSD {
	private static final int CUTOFF_FOR_INSERTIONSORT=15;
	private static final int R_FOR_STRING=256;
	private static final int R_FOR_INT=256;//each byte
	private static final int BITS_PER_INT=32;
	private static final int BITS_PER_BYTE=8;
	private static final int MASK=0xFF;

	//do not instantiate
	private MSD(){}

	public static void sort(String [] a){
		if(a==null) throw new IllegalArgumentException("calls sort() with null arguments");
		String [] aux=new String[a.length];
		sort(a,aux,0,a.length-1,0);
	}

	private static void sort(String [] a, String [] aux, int lo, int hi, int d){
		/********************************
		 * Description:
		 *
		 * @param a the string array
		 * @param aux
		 * @param lo
		 * @param hi
		 * @param d
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if(hi-lo+1<=CUTOFF_FOR_INSERTIONSORT){
			insertionSort(a,lo,hi,d);
			return;
		}

		int []count=new int[R_FOR_STRING+2];//include -1 which means the end of the string
		//computing the frequency count
		for(int i=lo;i<=hi;i++) {
			count[charAt(a[i],d)+2]++;
		}

		//computing the cumulates
		for(int r=0;r<R_FOR_STRING+1;r++) {
			count[r+1]+=count[r];
		}

		//move data
		for(int i=lo;i<=hi;i++){
			aux[count[charAt(a[i],d)+1]++]=a[i];
		}

		//copy back
		for(int i=lo;i<=hi;i++){
			a[i]=aux[i-lo];
		}

		//lo+count[r]:start index of entry r
		//lo+count[r+1]-1:end index
		for(int r=0;r<R_FOR_STRING;r++) {
			sort(a,aux,lo+count[r],lo+count[r+1]-1,d+1);
		}
	}

	public static void sort(int [] a){
		int []aux=new int[a.length];
		sort(a,aux,0,a.length-1,0);
	}

	private static void sort(int []a, int [] aux, int lo, int hi, int d){

		if(hi-lo+1<=CUTOFF_FOR_INSERTIONSORT){
			insertionSort(a,lo,hi,d);
			return;
		}

		int shift=BITS_PER_INT-BITS_PER_BYTE-BITS_PER_BYTE*d;
		int []count=new int[R_FOR_INT+1];
		//computing the frequency count
		for(int i=lo;i<=hi;i++) {
			int c=a[i]>>shift & MASK;
			count[c+1]++;
		}

		//computing cumulates
		for(int r=0;r<R_FOR_INT;r++){
			count[r+1]+=count[r];
		}

		// for most significant byte, 0x80-0xFF(negative number) comes before 0x00-0x7F
		if(d==0){
			int shift1=count[R_FOR_INT/2];//number of positive number
			int shift2=count[R_FOR_INT]-count[R_FOR_INT/2];//number of negative number

			//put negative number before the positive number
			for(int r=0;r<R_FOR_INT/2;r++) {
				count[r]+=shift2;
			}

			for(int r=R_FOR_INT/2;r<R_FOR_INT;r++){
				count[r]-=shift1;
			}

		}

		//move data
		for(int i=lo;i<=hi;i++){
			int c=a[i]>>shift & MASK;
			aux[count[c]++]=a[i];
		}

		//copy back
		for(int i=lo;i<=hi;i++){
			a[i]=aux[i-lo];
		}

		if(d==3) return;
		if (d == 0) {
			//1~127
			for(int r=0;r<R_FOR_INT/2-1;r++) {
				sort(a,aux,lo+count[r],lo+count[r+1]-1,d+1);
			}
			//129~255(-127~-1)
			for(int r=R_FOR_INT/2;r<R_FOR_INT-1;r++) {
				sort(a,aux,lo+count[r],lo+count[r+1]-1,d+1);
			}
			//0
			sort(a,aux,lo+count[R_FOR_INT-1],lo+count[0]-1,d+1);
			//-128
			sort(a,aux,lo,lo+count[R_FOR_INT/2]-1,d+1);
		} else {
			sort(a,aux,lo,lo+count[0]-1,d+1);
			for(int r=0;r<R_FOR_INT-1;r++){
				sort(a,aux,lo+count[r],lo+count[r+1]-1,d+1);
			}
		}

	}

	/*********************************************************************************
	 * Helper Function
	 *********************************************************************************/

	private static int charAt(String s, int d){
		/********************************
		 * Description: return the integer value of the string {@CODE S} at
		   specific index {@code d}, if d>s.length-1, return -1
		 * @param s the string
		 * @param d string index
		 * @return: int
		 * @Author: luibebetter
		 *********************************/
		assert d<=s.length();
		if(d<s.length()) return s.charAt(d);
		else return -1;
	}

	//sort the substring using insertion sort for small subarray
	private static void insertionSort(String [] a, int lo, int hi, int d){
		/********************************
		 * Description:
		 *
		 * @param a the string array
		 * @param lo the lower bound
		 * @param hi the upper bound
		 * @param d  the string index
		 * @return: void
		 * @Author: luibebetter
		 *********************************/
		if(hi<=lo) return;
		for(int i=lo+1;i<=hi;i++) {
			for(int j=i;j>lo && less(a[j],a[j-1],d);j--) {
				exch(a,j,j-1);
			}
		}
	}

	private static void insertionSort(int []a, int lo, int hi, int d){
		if(hi<=lo) return;
		for(int i=lo+1;i<=hi;i++){
			for(int j=i;j>lo && a[j]<a[j-1];j--){
				exch(a,j,j-1);
			}
		}
	}

	//if the substring of {@code s1} begins at index {@code d} less than {@code s2}
	private static boolean less(String s1, String s2, int d){
		assert d<=s1.length() &&d <=s2.length();
		return s1.substring(d).compareTo(s2.substring(d))<0;
	}

	private static  void exch(Object [] a, int i, int j){
		Object tmp=a[i];
		a[i]=a[j];
		a[j]=tmp;
	}

	private static void exch(int []a, int i, int j){
		int tmp=a[i];
		a[i]=a[j];
		a[j]=tmp;
	}


	/******************************************************************
	 * Reads in a sequence of fixed-length strings from standard input;
	 * LSD radix sorts them;
	 * and prints them to standard output in ascending order.
	 *
	 * @param args the command-line arguments
	 *********************************************************************/
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


		int []a=new int[s.length];
		for (int i = 0; i < s.length; i++) {
			a[i]=Integer.parseInt(s[i]);
		}
		sort(a);

		// print results
		for (int i = 0; i < a.length; i++)
			System.out.println(a[i]);
	}
}
