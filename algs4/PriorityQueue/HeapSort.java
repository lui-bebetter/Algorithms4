
package algs4.PriorityQueue;

import java.util.Scanner;
import java.util.regex.Pattern;

public class HeapSort{

    private HeapSort(){};

    //sort the array in a ascending order 
    public static <Key extends Comparable<Key> > void sort (Key [] a){
        int n=a.length;
        //rearrange  the array to a max heap
        for (int k=n/2;k>=1;k--) sink(a,k,n);
        for ( int i=0;i<a.length;i++){
            exch(a,1,n--);
            sink(a,1,n);
        }

    }

    public static void show(Object []a){
        StringBuilder s=new StringBuilder();
        s.append("[");
        for(int i=0;i<a.length;i++){
            s.append(a[i]);
            s.append(" ");
        }
        s.append("]");
        System.out.println(s.toString());
    }

    /**********************
    *put the smaller parent to the right position

    *parent with index k, having two child 2k,2k+1
    **********************/
    private static <Key extends Comparable<Key> > void sink(Key [] a,int k, int n){
        while(2*k<=n){
            int max=2*k;
            if(max<n &&less(a, max, max+1)) max++;
            if(!less(a,k,max)) break; 
            exch(a,max,k);
            k=max;
        }
    }

    /***************
    *put the larger child to the right position

    *child with index k, then the parent's index is k/2
    ***************/
    private static <Key extends Comparable<Key> > void swim(Key []a, int k){
        while(k>1 &&less(a, k/2, k)){
            exch(a, k/2, k);
            k=k/2;
        }
    }

    //is v<w?
    private static <Key extends Comparable<Key> > boolean less(Key [] a, int i, int j){
        return a[i-1].compareTo(a[j-1])<0; 
    }

    //exchange a[i] with a[j]
    private static <Key extends Comparable<Key> > void exch(Key []a, int i, int j){
        Key tmp=a[i-1];
        a[i-1]=a[j-1];
        a[j-1]=tmp;
    }

     public static void main(String[] args){
        final Pattern EVERYTHING_PATTERN=Pattern.compile("\\A");
        final Pattern WHITESPACE_PATTERN=Pattern.compile("\\p{javaWhitespace}+");
        Double []a;
        Scanner in=new Scanner(System.in);

        //read all input as a string
        String all=in.useDelimiter(EVERYTHING_PATTERN).next();

        //spilt the input and transfer it to double 
        String [] s=WHITESPACE_PATTERN.split(all.trim());

        HeapSort.sort(s);
        HeapSort.show(s);
    }
}