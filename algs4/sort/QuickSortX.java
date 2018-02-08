/*****************************************************************
    *Quick sort X:based on partitioning, performance guaranteed by 
    Tukey's ninther

    *using the Bentley-McIlroy 3-way partitioning scheme

    *type-safe version that uses static generics
******************************************************************/

package algs4.sort;
import java.util.Comparator;
import algs4.sort.Sort;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.Random;

public class QuickSortX{
    //cut off for insertion sort
    private static final int INSERTION_SORT_CUTOFF=8;

    //cut off for median-of-3 partitioning
    private static final int MEDIAN_OF_3_CUTOFF=40;

    //this class should not be instantiated
    private QuickSortX(){}

    //public API:sort the whole array
    public static <Key extends Comparable<Key> > void sort(Key [] a){
        if(a==null) throw new IllegalArgumentException();
        sort(a,0,a.length-1);
        assert isSorted(a);
    }

    //public API:find the kth element
    //public static <Key extends Comparable<Key>> Key select(Key [] a, int k){



    //sort from lo to hi
    private static <Key extends Comparable<Key>> void sort(Key []a ,int lo, int hi){
        int n=hi-lo+1;//the subarray length
        if(n<=INSERTION_SORT_CUTOFF){
            Sort.insertionSort(a,lo,hi);
            return;
        }

        //guarantee the first element near the middle after partition depend on Tukey's ninther
        int median;
        if(n<=MEDIAN_OF_3_CUTOFF){
            median=medianOf3(a,lo,lo+n/2,hi);
        }else{
            int eps=n/8;
            int mid=lo+n/2;
            int median1=medianOf3(a,lo,lo+eps,lo+eps+eps);
            int median2=medianOf3(a,mid-eps,mid,mid+eps);
            int median3=medianOf3(a,hi-eps-eps,hi-eps,hi);
            median=medianOf3(a,median1,median2,median3);
        }
        exch(a,lo,median);

        //the Bentley-McIlroy 3-way partitioning scheme
        int i=lo, j=hi+1;
        int p=lo, q=hi+1;
        Key v=a[lo];
        while(true){
            while(less(a[++i],v)) {
                if(i==hi) break;
            }
            while(less(v,a[--j])) ;

            //check whether the last element equals v
            if(i==j&& v.compareTo(a[i])==0) exch(a,++p,i);
            if(i>=j) break;

            exch(a,i,j);
            if(v.compareTo(a[i])==0) exch(a,++p,i);
            if(v.compareTo(a[j])==0) exch(a,--q, j);
        }

        //put the partitioning element to the right place
        i=j+1;
        for(int k=lo;k<=p;k++) exch(a,k,j--);
        for(int k=hi;k>=q;k--) exch(a,k,i++);

        sort(a,lo,j);
        sort(a,i,hi);
        assert isSorted(a, lo,hi);
    } 

    /*************************************
    helper function:less exch
    ***************************************/
    //is v<w?
    private static <Key extends Comparable<Key>> boolean less(Key v, Key w){
        return v.compareTo(w)<0;
    }

    //exchange a[i] with a[j]
    private static  void exch(Object [] a, int i, int j){
        Object tmp=a[i];
        a[i]=a[j];
        a[j]=tmp;
    }

    //median of a[lo],a[hi],a[(lo+hi)/2]
    private static <Key extends Comparable<Key>> int medianOf3(Key [] a, int lo,int mid, int hi){
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

    private static <Key extends Comparable<Key>> boolean isSorted(Key [] a){
        for(int i=1;i<a.length;i++){
            if (less(a[i],a[i-1])) return false;
        }
        return true;
    }


    private static <Key extends Comparable<Key>> boolean isSorted(Key [] a, int lo, int hi){
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
        QuickSortX.sort(a);
        QuickSortX.show(a);
    }

}