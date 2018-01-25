package algs4.ThreeSum;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class ThreeSum{
    private ArrayList<Integer> ts=new ArrayList<Integer>();//integer array without duplication
    private final int target;
    private int count;

    public ThreeSum(int [] a, int target){
        this.target=target;
        Arrays.sort(a);
        for(int i=0;i<a.length;i++){
            if(i==0) {
                ts.add(a[i]);
            }else{
                if(a[i]==a[i-1]) continue;
                ts.add(a[i]);
            }
        }
        System.out.println(" "+ts);
    }


    public void getTriplets(){
        int lo,hi;
        for(int i=0;i<ts.size()-2;i++){
            lo=i+1;
            hi=ts.size()-1;
            while(lo<hi){
                if(ts.get(i)+ts.get(lo)+ts.get(hi)>target){
                    hi--;
                }else if (ts.get(i)+ts.get(lo)+ts.get(hi)<target){
                    lo++;
                }else{
                    System.out.println("Find triplets:"+ts.get(i)+" "+ts.get(lo)+" "+ts.get(hi));
                    lo++;
                    hi--;
                    count++;
                }
            }
        }
    }

    public int count(){
        return count;
    }


    public static void main(String [] args){
        String line;
        int target;
        int []a;
        Scanner in=new Scanner(System.in);
        System.out.println("please enter a set of numbers:");
        line=in.nextLine();
        System.out.println("please enter the taeget integer:");
        target=in.nextInt();
        Pattern p=Pattern.compile("\\p{javaWhitespace}+");
        String []list=p.split(line.trim());
        a=new int [list.length];
        for(int i=0;i<list.length;i++){
            a[i]=Integer.parseInt(list[i]);
        }
        ThreeSum t=new ThreeSum(a,target);
        t.getTriplets();
        System.out.println("total count of triplets:"+t.count());
    }
}