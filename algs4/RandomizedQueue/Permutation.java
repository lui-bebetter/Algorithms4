/**********************
* a client program  that takes an integer k as a command-line argument; 

*reads in a sequence of strings from standard input ;

* and prints exactly k of them, uniformly at random;

*Print each item from the sequence at most once
********************/
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation{
    public static void main(String []args){
        RandomizedQueue<String> queue=new RandomizedQueue<String>();
        int k=Integer.parseInt(args[0]);
        while(!StdIn.isEmpty()){
            queue.enqueue(StdIn.readString());
        }
        if(k<0||k>queue.size()) throw new IllegalArgumentException("k should between 0 and the size of the input.");
        while(k>0){
            StdOut.println(queue.dequeue());
            k--;
        }
    }
}