package algs4.ThreeSum;
import java.util.Scanner;
import algs4.stack.ResizingArrayStack;

public class Statistic{
    private static double mean(ResizingArrayStack<Double> a){
        double total=0;
        for(double item:a){
                total+=item;
        }
        return total/a.size();
    }

    private static double stddev(ResizingArrayStack<Double> a){
        double stddev=0;
        double mean=mean(a);
        for(double item:a){
                stddev+=(item-mean)*(item-mean);
        }
        return Math.sqrt(stddev/(a.size()-1));
    }

    public static void main(String[] agrs){
        Scanner in=new Scanner(System.in);
        ResizingArrayStack<Double> stack= new ResizingArrayStack<Double>();
        while(in.hasNextDouble()) stack.push(in.nextDouble());
        System.out.println("total number of data:"+stack.size());
        System.out.println("stddev: "+Statistic.stddev(stack));
        System.out.println("mean: "+Statistic.mean(stack));
    }
}