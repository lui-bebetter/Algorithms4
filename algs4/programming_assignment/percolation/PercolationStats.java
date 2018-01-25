import  edu.princeton.cs.algs4.*;


public class PercolationStats{
    private double [] threshold;
    private static final double CONFIDENCE_95=1.96;
    public PercolationStats(int n,int trials){
        threshold=new double[trials];
        for(int i=0;i<trials;i++){
            Percolation p=new Percolation(n);
            int [] randomList=StdRandom.permutation(n*n);
            for(int j=0;j<n*n;j++){
                if(p.percolates()) break;
                int row=(randomList[j]/n)+1;
                int col=(randomList[j]%n)+1;
                p.open(row,col);
            }
            threshold[i]=p.numberOfOpenSites()/(double)(n*n);
        }
    }

    public double mean(){
        return StdStats.mean(threshold);
    }


    public double stddev(){
        return StdStats.stddev(threshold);
    }

    public double confidenceLo(){
        double lo=mean()-CONFIDENCE_95*stddev()/Math.sqrt(threshold.length);
        return lo;
    }


    public double confidenceHi(){
        double hi=mean()+CONFIDENCE_95*stddev()/Math.sqrt(threshold.length);
        return hi;
    }

    public static void main(String [] args){
        int n=Integer.parseInt(args[0]);
        int trials=Integer.parseInt(args[1]);
        PercolationStats ps=new PercolationStats(n,trials);
        StdOut.println("mean                    ="+ps.mean());
        StdOut.println("stddev                  ="+ps.stddev());
        StdOut.println("95% confidence interval ="+"["+ps.confidenceLo()+","+ps.confidenceHi()+"]");
    }
}