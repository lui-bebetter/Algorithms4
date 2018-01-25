/*****************************************************************
    *Quick sort:based on partitioning, performance guarantee
     extremely depends on the pre-shuffling.

    *average analysis:compares~2nlnn~1.39nlogn, exchanges~1/3nlnn,
     memory~n
******************************************************************/

package algs4.sort;
import java.util.Comparator;
import algs4.sort.Sort;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.Random;

public class QuickSort{
    //cut off for insertion sort
    private static final int CUTOFF=8;

    //public API:sort the whole array
    public static void sort(Comparable [] a){
        if(a==null) throw new IllegalArgumentException();
        shuffle(a);
        sort(a,0,a.length-1);
        assert isSorted(a);
    }

    //public API:select the kth(egg:k=0 means the first element) element from the array
    public static Comparable select (Comparable [] a, int k){
        if(a==null) throw new IllegalArgumentException();
        int n=a.length;
        if(k<0||k>n-1) throw new IllegalArgumentException("k must between 0 and the length of the array");
        shuffle(a);
        
        if(n<=CUTOFF){
            Sort.insertionSort(a);
            return a[k];
        }
        int lo=0,hi=n-1;
        int j;
        while(lo<hi){ 
            j=partition(a,lo,hi);
            if(k<j) hi=j-1;
            else if(k>j) lo=j+1;
            else return a[j];
        }
        return a[lo];
    }

    //partition:put the first element to the right place
    private static int partition(Comparable [] a, int lo, int hi){
       int i=lo, j=hi+1;
       while(true){
        //no larger to the left of the partition
        while(less(a[++i],a[lo])) if(i==hi) break;
        //no smaller to the right of the partition
        while(less(a[lo], a[--j])) ;

        if(i>=j) break;
        exch(a,i,j);
       }

       exch(a,lo,j);
       return j;
    }

    private static void sort(Comparable []a ,int lo, int hi){
        if(hi-lo+1<=CUTOFF){
            Sort.insertionSort(a,lo,hi);
            return;
        }
        int median=medianOf3(a,lo,hi);
        exch(a,lo,median);
        int j=partition(a,lo,hi);
        sort(a,lo,j-1);
        sort(a,j+1,hi);
        assert isSorted(a, lo,hi);
    } 

    /*************************************
    helper function:less exch
    ***************************************/
    private static void shuffle(Object [] a){
        Random random=new Random();
        for(int i=0;i<a.length;i++){
            int tmp=random.nextInt(i+1);
            exch(a,tmp,i);
        }
    }
    //is v<w?
    private static boolean less(Comparable v, Comparable w){
        return v.compareTo(w)<0;
    }

    //exchange a[i] with a[j]
    private static  void exch(Object [] a, int i, int j){
        Object tmp=a[i];
        a[i]=a[j];
        a[j]=tmp;
    }

    //median of a[lo],a[hi],a[(lo+hi)/2]
    private static int medianOf3(Comparable [] a, int lo, int hi){
        int mid=(lo+hi)/2;
        int min=lo, median=mid;
        if(less(a[mid],a[lo]))  {
            median=lo;
            min=mid;
        }
        if(less(a[hi],a[median])){
            if(less(a[hi],a[min])) median=min;
            else median=hi;
        } 
        return median;
    }

        /************************
    helper function for debugging
    ************************/

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

    private static boolean isSorted(Comparable [] a){
        for(int i=1;i<a.length;i++){
            if (less(a[i],a[i-1])) return false;
        }
        return true;
    }


    private static boolean isSorted(Comparable [] a, int lo, int hi){
        for(int i=lo+1;i<hi+1;i++){
            if (less(a[i],a[i-1])) return false;
        }
        return true;
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

        a=new Double[s.length];
        for (int i=0;i<s.length;i++) a[i]=Double.parseDouble(s[i]);
        QuickSort.sort(a);
        QuickSort.show(a);
    }

}