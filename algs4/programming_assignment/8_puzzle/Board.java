/**************************************************
*   A board class of the 8-Puzzle problem
***************************************************/
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import edu.princeton.cs.algs4.ResizingArrayStack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board{

    /*
    *  the reason for using 1d array but not 2d array:a[i] is immutable ,
       but a[i][j] is mutable
    *  blocks[i] =blocks in row i/dim col i%dim
    */
    private final int [] blocks;
    private final int n;//the length of the board
    private final int dim;
    private static final int BLANK=0;


    /*construct a board from an n-by-n array
    * a[i][j] means row i column j blocks
    */
    public Board(int [][] inBlocks){
        if (inBlocks==null) throw new IllegalArgumentException("the input of the Board class constructor should not be null");
        dim=inBlocks.length;
        n=dim*dim;
        blocks=new int[n];
        for (int i=0;i<dim;i++)
            for(int j=0;j<dim;j++)
                blocks[i*dim+j]=inBlocks[i][j];
    } 

    //return the dimension of the board
    public int dimension(){
        return dim;
    }

    //return priority level calculated by the hamming priority function
    public int hamming(){
        int hamming=0;
        for (int i=0;i<n-1;i++)
            if (blocks[i]!=i+1) hamming++; 
        return hamming;
    }

    //return priority level calculated by the manhattan priority function
    public int manhattan(){
        int manhattan=0;
        for (int i=0;i<n;i++){
            int value=blocks[i];
            if(value==BLANK) continue;
            manhattan+=Math.abs((value-1)%dim-i%dim)+Math.abs((value-1)/dim-i/dim);
        }
        return manhattan; 
    }

    //is goal board?
    public boolean isGoal(){
        for (int i=0;i<n-1;i++)
            if(blocks[i]!=i+1) return false;
        return true;
    }

    //twin blocks :swap any pair of the original array(blank block is not a block)
    public Board twin(){
        int [][] twin=convertTo2dArray();
        if(blocks[0]==BLANK||blocks[1]==BLANK) exch(twin,dim-1,dim-1,dim-1,dim-2);
        else exch(twin,0,0,0,1);

        return new Board(twin);
    }

    //implements equals subject to java convention
    public boolean equals(Object y){
        if (this==y) return true;
        if (y==null) return false;
        if(this.getClass()!=y.getClass()) return false;
        Board that=(Board) y;
        if(this.n!=that.n) return false;
        for (int i=0;i<n;i++){
            if(this.blocks[i]!=that.blocks[i]) return false;
        }
        return true;
    }

    /**************************************************************
    *   return an iterable instance that iterates over the neighbors
    *   simply add the neighbors to a stack<Board> or queue<Board>
    **************************************************************/
    public Iterable<Board> neighbors(){
        ResizingArrayStack<Board> stack=new ResizingArrayStack<Board>();
        for (int i=0;i<n;i++){
            if (blocks[i]==BLANK){
                int row=i/dim, col=i%dim;
                if(row-1<dim && row-1>=0){
                    int [][]neighbor=convertTo2dArray();
                    exch(neighbor,row,col,row-1,col);
                    stack.push(new Board(neighbor));
                }
                if(row+1<dim && row+1>=0){
                    int [][]neighbor=convertTo2dArray();
                    exch(neighbor,row, col, row+1, col);
                    stack.push(new Board(neighbor));
                } 
                if(col-1<dim && col-1>=0){
                    int [][]neighbor=convertTo2dArray();
                    exch(neighbor,row, col, row, col-1);
                    stack.push(new Board(neighbor));
                } 
                if(col+1<dim && col+1>=0){
                    int [][]neighbor=convertTo2dArray();
                    exch(neighbor,row, col, row, col+1);
                    stack.push(new Board(neighbor));
                }  
            }
        }
        return stack;
    }

    public String toString(){
        StringBuilder s=new StringBuilder();
        s.append(dim);
        s.append("\n");
        for(int i=0;i<dim;i++){
            for(int j=0;j<dim;j++){
                s.append(blocks[i*dim+j]);
                s.append("  ");
            }
            s.append("\n");
        }
        return s.toString();
    }


    //convert to 2d blocks array 
    private int[][] convertTo2dArray(){
        int[][] blocks2d=new int[dim][dim];
        for (int i=0;i<dim;i++){
            for (int j=0;j<dim;j++){
                blocks2d[i][j]=blocks[i*dim+j];
            }
        }
        return blocks2d;
    }

    //swap blocks2d[i][j] with blocks2d[k][l]
    private void exch(int[][]blocks2d, int i, int j, int k, int l){
        int tmp=blocks2d[i][j];
        blocks2d[i][j]=blocks2d[k][l];
        blocks2d[k][l]=tmp;
    }

    public static void main(String[] args){
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial);
        StdOut.println(initial.dimension()+" "+initial.hamming()+" "+initial.manhattan());
        StdOut.println(initial.twin());
        for (Board item:initial.neighbors())
            StdOut.println(item);

    }
}