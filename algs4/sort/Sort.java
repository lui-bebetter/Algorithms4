
package algs4.sort;

import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Sort{

    //cutoff for mergeSort:when hi-lo+1<=7, sort it using insertion sort.
    private static final int CUTOFF=7; 

    //this class should not be instantiated.
    private Sort(){}

    /*************************************************************************
    *Selection sort algorithm:during each i iteration
    from 0 to n-1,swap a[i] with the min one of  Objects whoese index are from 
     i to n-1.
    *n exchanges,n*n/2 compares,memory~n
    *************************************************************************/

    //Rearrange the array in a ascending order, using the natural order. 
    public static void selectionSort(Comparable[]a){
        int n=a.length;
        for (int i=0;i<n;i++){
            int min=i;
            //find the min one of i to n-1
            for (int j=i+1;j<n;j++){
                if (less(a[j],a[min])) min=j;
            }
            exch(a,i,min);
        }
        assert isSorted(a);
    }

    //Rearrange the array in a ascending order, using a comparator
    public static void selectionSort(Object[] a,Comparator comparator){
        int n=a.length;
        for (int i=0;i<n;i++){
            int min=i;
            //find the min one of i to n-1
            for (int j=i+1;j<n;j++){
                if (less(a[j],a[min],comparator)) min=j;
            }
            exch(a,i,min);
        }
        assert isSorted(a,comparator); 
    }
    /******************************************************************
    ******************************************************************/


    /*********************************************************************
    *Insertion sort:during each i iteration form 0 to n-1,putting the a[i]
    to the right position.

    *compares~n*n/2 at most,exchanges=compares+n-1,memory~n
    **********************************************************************/
    //using natural order for sorting
    public static void insertionSort(Comparable [] a){
        int n=a.length;

        //put the min to the first, as a sentinel
        int exchanges=0;
        for (int i=n-1;i>0;i--){
            if(less(a[i],a[i-1])){
                exch(a,i,i-1);
                exchanges++;
            }
        }

        if (exchanges==0) return;

        //insertion sort
        for (int i=2;i<n;i++){
            Comparable tmp=a[i];
            int j=i;
            while(less(tmp,a[j-1])){
                a[j]=a[j-1];
                j--;
            }
            a[j]=tmp;
        }

        //for debugging
        assert isSorted(a);

    }

    //sorting the array from lo to hi
    public static void insertionSort(Comparable [] a, int lo, int hi){

        //put the min to the first, as a sentinel
        int exchanges=0;
        for (int i=hi;i>lo;i--){
            if(less(a[i],a[i-1])){
                exch(a,i,i-1);
                exchanges++;
            }
        }

        if (exchanges==0) return;

        //insertion sort
        for (int i=lo+1;i<hi+1;i++){
            Comparable tmp=a[i];
            int j=i;
            while(less(tmp,a[j-1])){
                a[j]=a[j-1];
                j--;
            }
            a[j]=tmp;
        }

        //for debugging
        assert isSorted(a,lo,hi);

    }

    //using a comparator for sorting
    public static void insertionSort(Object [] a,Comparator comparator){
        int n=a.length;

        //put the min to the first, as a sentinel
        int exchanges=0;
        for (int i=n-1;i>0;i--){
            if(less(a[i],a[i-1],comparator)){
                exch(a,i,i-1);
                exchanges++;
            }
        }

        if (exchanges==0) return;

        //insertion sort
        for (int i=2;i<n;i++){
            Object tmp=a[i];
            int j=i;
            while(less(tmp,a[j-1],comparator)){
                a[j]=a[j-1];
                j--;
            }
            a[j]=tmp;
        }

        //for debugging
        assert isSorted(a,comparator);

    }

    //sorting the array from lo to hi
    public static void insertionSort(Object [] a, int lo, int hi, Comparator comparator){

        //put the min to the first, as a sentinel
        int exchanges=0;
        for (int i=hi;i>lo;i--){
            if(less(a[i],a[i-1],comparator)){
                exch(a,i,i-1);
                exchanges++;
            }
        }

        if (exchanges==0) return;

        //insertion sort
        for (int i=lo+1;i<hi+1;i++){
            Object tmp=a[i];
            int j=i;
            while(less(tmp,a[j-1],comparator)){
                a[j]=a[j-1];
                j--;
            }
            a[j]=tmp;
        }

        //for debugging
        assert isSorted(a,lo,hi,comparator);
    }

    /*********************************************************************
    **********************************************************************/


    /********************************************************************
    *Shell sort:increment sequence 3*x+1

    *in general, better than insertion sort.
    ********************************************************************/

    //using natural order
    public static void shellSort(Comparable [] a){
        int n=a.length;
        //h-sort
        int h=1;
        while(h<n/3) h=3*h+1;
        while(h>=1){
            for (int i=h;i<n;i++){
                Comparable tmp=a[i];
                int j=i;
                while(j>=h&&less(tmp,a[j-h])){
                    a[j]=a[j-h];
                    j-=h;
                }
                a[j]=tmp;
            }
            assert isHsorted(a,h);
            h=h/3;
        }
        assert isSorted(a);
    }

    //using a comparator
    public static void shellSort(Object [] a, Comparator comparator){
        int n=a.length;
        //h-sort
        int h=1;
        while(h<n/3) h=3*h+1;
        while(h>=1){
            for (int i=h;i<n;i++){
                Object tmp=a[i];
                int j=i;
                while(j>=h&&less(tmp,a[j-h],comparator)){
                    a[j]=a[j-h];
                    j-=h;
                }
                a[j]=tmp;
            }
            assert isHsorted(a,h,comparator);
            h=h/3;
        }
        assert isSorted(a,comparator);
    }
    /******************************************************************
    *******************************************************************/


    /******************************************************
    *Merge sort:divide and conquer
    *compares~nlogn at most, array acess~6nlogn, memory~2n
    *******************************************************/

    /****************
    using natural order
    ******************/

    //merge sort API
    public static void mergeSort(Comparable []a){
        Comparable [] aux=a.clone();
        mergeSort(a,aux,0,a.length-1);
        assert isSorted(a);
    } 

    //sort from li to hi
    public static void mergeSort(Comparable [] a, int lo, int hi){
        Comparable [] aux=a.clone();
        mergeSort(a,aux,lo,hi);
        assert isSorted(a,lo,hi);
    }

    //aux:auxiliary array
    private static void merge(Comparable[]a, Comparable [] aux, int lo, int mid, int hi){
        assert isSorted(aux,lo,mid);
        assert isSorted(aux,mid+1,hi);

        //increment i,j,k
        int i=lo, j=mid+1;
        for (int k=lo;k<=hi;k++){
            if (i>mid) a[k]=aux[j++];
            else if (j>hi) a[k]=aux[i++];
            else if (less(aux[j],aux[i])) a[k]=aux[j++];
            else a[k]=aux[i++];
        }

        assert isSorted(a,lo,hi);
    }

    //helper function in recursive form
    private static void mergeSort(Comparable []a, Comparable []aux, int lo, int hi){
        if(hi-lo+1<=CUTOFF){
            insertionSort(a,lo,hi);
            return;
        } 
        int mid=(lo+hi)/2;
        mergeSort(aux,a,lo,mid);
        mergeSort(aux,a,mid+1,hi);
        if(!less(aux[mid+1],aux[mid])){
            System.arraycopy(aux,lo,a,lo,hi-lo+1);
            return;
        }
        merge(a,aux,lo,mid,hi);
    }
    /****************
    *****************/


    /****************
    using a comparator
    *****************/

    public static void mergeSort(Object []a, Comparator comparator){
        Object [] aux=a.clone();
        mergeSort(a,aux,0,a.length-1,comparator);
        assert isSorted(a,comparator);
    } 

    //sort from li to hi
    public static void mergeSort(Object [] a, int lo, int hi, Comparator comparator){
        Object [] aux=a.clone();
        mergeSort(a,aux,lo,hi,comparator);
        assert isSorted(a,lo,hi,comparator);
    }

    //aux:auxiliary array
    private static void merge(Object[]a, Object [] aux, int lo, int mid, int hi, Comparator comparator){
        assert isSorted(aux,lo,mid, comparator);
        assert isSorted(aux,mid+1,hi,comparator);

        //increment i,j,k
        int i=lo, j=mid+1;
        for (int k=lo;k<=hi;k++){
            if (i>mid) a[k]=aux[j++];
            else if (j>hi) a[k]=aux[i++];
            else if (less(aux[j],aux[i],comparator)) a[k]=aux[j++];
            else a[k]=aux[i++];
        }

        assert isSorted(a,lo,hi,comparator);
    }

    //helper function in recursive form
    private static void mergeSort(Object []a, Object []aux, int lo, int hi, Comparator comparator){
        if(hi-lo+1<=CUTOFF){
            insertionSort(a,lo,hi,comparator);
            return;
        } 
        int mid=(lo+hi)/2;
        mergeSort(aux,a,lo,mid,comparator);
        mergeSort(aux,a,mid+1,hi,comparator);
        if(!less(aux[mid+1],aux[mid],comparator)){
            System.arraycopy(aux,lo,a,lo,hi-lo+1);
            return;
        }
        merge(a,aux,lo,mid,hi,comparator);
    }

    /***********
    ************/

    /***************************************************************************************
    ***************************************************************************************/

    

    /*************************************
    Helper sorting function:less,exchange.
    *************************************/

    //is v<w?
    private static boolean less(Comparable v,Comparable w){
        return v.compareTo(w)<0;
    }

    private static boolean less(Object v,Object w,Comparator comparator){
        return comparator.compare(v,w)<0;
    }

    //exchange a[i] with a[j]
    private static void exch(Object[]a, int i, int j){
        Object tmp=a[i];
        a[i]=a[j];
        a[j]=tmp;
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

    private static boolean isSorted(Object[] a,Comparator comparator){
        for(int i=1;i<a.length;i++){
            if (less(a[i],a[i-1],comparator)) return false;
        }
        return true;
    }

    private static boolean isSorted(Comparable [] a, int lo, int hi){
        for(int i=lo+1;i<hi+1;i++){
            if (less(a[i],a[i-1])) return false;
        }
        return true;
    }

    private static boolean isSorted(Object[] a,int lo, int hi, Comparator comparator){
        for(int i=lo+1;i<hi+1;i++){
            if (less(a[i],a[i-1],comparator)) return false;
        }
        return true;
    }

    //Debugging for shell sort
    private static boolean isHsorted(Comparable[]a, int h){
        for (int i=h;i<a.length;i++){
            if(less(a[i],a[i-h])) return false;
        }
        return true;
    }

      private static boolean isHsorted(Object[]a, int h, Comparator comparator){
        for (int i=h;i<a.length;i++){
            if(less(a[i],a[i-h],comparator)) return false;
        }
        return true;
    }


    /****************************************************************
    test client
    ****************************************************************/
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
        Sort.mergeSort(a);
        Sort.show(a);
    }
} 