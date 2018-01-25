/*****************
limitation:th successive components of the array can't duplicates.
****************/
package algs4.BitonicSearch;
import java.util.Scanner;
import java.util.regex.Pattern;

public class BitonicSearch{

    //ordinary binary search in a from lo to hi for target
    public static boolean simpleSearch(int [] a,int lo,int hi,int target){
        while(lo<=hi){
            int mid=(lo+hi)/2;
            if (target>a[mid]) lo=mid+1;
            if (target<a[mid]) hi=mid-1;
            if (target==a[mid]) return true;
        }
        return false;
    }

    public static boolean bitonicSearch(int [] a,int lo, int hi, int target){
        while(lo<=hi){
            int mid=(lo+hi)/2;
            if (target>a[mid]){
                if(a[mid]<a[mid+1]){
                    lo=mid+1;
                }else{
                    hi=mid-1;
                }
            }else if(target<a[mid]){
                //有待改进
                if(a[mid]<a[mid+1]){
                    if(simpleSearch(a,lo,mid--,target)) return true;
                    else lo=mid+1;
                }else{
                    if (simpleSearch(a,mid++,hi,target)) return true;
                    else hi=mid-1;
                }
            }else{
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args){
        Scanner in=new Scanner(System.in);
        Pattern p=Pattern.compile("\\p{javaWhitespace}+");
        System.out.println("please enter a bitonic set of number:");
        String line=in.nextLine();
        String [] sList=p.split(line.trim());
        int []a=new int [sList.length];
        for(int i=0;i<a.length;i++){
            a[i]=Integer.parseInt(sList[i]);
        }
        System.out.println("please enter the target integer:");
        int target=in.nextInt();
        if(BitonicSearch.bitonicSearch(a,0,a.length-1,target)) System.out.println(target+"is found");

    }
}