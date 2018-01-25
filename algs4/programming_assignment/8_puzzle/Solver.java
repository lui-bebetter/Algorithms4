/********************************************************
*   Solver class for 8 Puzzle Problem
*********************************************************/
import java.util.Comparator;
import java.util.Iterator;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.ResizingArrayStack;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver{

    private final boolean solvable;//is the initial board solvable
    private final SearchNode goal;//the goal search node

    //find the solution for a certain board
    public Solver(Board initial){
        if(initial==null) throw new IllegalArgumentException();

        //using two MinPQ to determin whther the initial board is solvable
        MinPQ<SearchNode> pq=new MinPQ<SearchNode>();
        pq.insert(new SearchNode(initial, true));
        pq.insert(new SearchNode(initial.twin(),false));
        SearchNode pseudoGoal;

        while(true){
            //whther the dequeued one is the goal
            pseudoGoal=pq.delMin();
            if(pseudoGoal.board.isGoal()){
                solvable=pseudoGoal.isOriginal;
                break;
            }

            //if the dequeued one isn't the goal, put the neighbor searchnodes into the MinPQ
            for( SearchNode item:pseudoGoal.neighbors())
                pq.insert(item);
        }
        if(solvable) goal=pseudoGoal;
        else goal=null;
    }

    //solvable?
    public boolean isSolvable(){
        return solvable;
    }

    //the minimal number of moves lead to the goal
    public int moves(){
        if(!isSolvable()) return -1;
        return goal.moves();
    }

    //return an Iterable class instance that iterates over the sequence of board
    public Iterable<Board> solution(){
        if(!isSolvable()) return null;
       ResizingArrayStack<Board> stack=new ResizingArrayStack<Board>();
       SearchNode current=goal;
       while (current!=null){
        stack.push(current.board);
        current=current.prenode;
       }
       return stack;
    }

    
    /***************************************************
    * inner class SearchNode containing a board, 
      number of moves and the predecessor board

    *the natrual order based on Hamming priority function
    *****************************************************/
    private static  class SearchNode implements Comparable<SearchNode>{

        public  final Board board;
        public  final SearchNode prenode;//the pre-searchnode
        private final int moves;//number of moves made to reach the current board
        
        private final int manhattan; //the Manhattan priority value

        public final boolean isOriginal;

        //the constructor for initial search node with a board
        public  SearchNode(Board board, boolean isOriginal){
            this.board=board;
            moves=0;
            manhattan=board.manhattan();
            prenode=null;
            this.isOriginal=isOriginal;
        }

        public SearchNode(Board board, boolean isOriginal, int moves, SearchNode prenode){
            this.board=board;
            this.moves=moves;
            manhattan=board.manhattan()+moves;
            this.prenode=prenode;
            this.isOriginal=isOriginal;
        }

        //return the manhattan priority value of this search node
        public int manhattan(){
            return manhattan;
        }

        //return the number of moves to reach the current board
        public int moves(){
            return moves;
        }

        //return the neighbor search node
        public Iterable<SearchNode> neighbors(){
            ResizingArrayStack<SearchNode> stack=new ResizingArrayStack<SearchNode>();
            for (Board item :board.neighbors()){
                if(this.prenode==null) stack.push(new SearchNode(item, this.isOriginal, moves+1,this));
                else if(!item.equals(this.prenode.board)) stack.push(new SearchNode(item, this.isOriginal, moves+1,this));
            }
            return stack;
        }

        //using the manhattan priority as the natrual order
        public int compareTo(SearchNode that){
            if(this.manhattan<that.manhattan) return -1;
            else if(this.manhattan>that.manhattan) return 1;
            else return 0;
        }
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
    
        // solve the puzzle
        Solver solver = new Solver(initial);
    
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}