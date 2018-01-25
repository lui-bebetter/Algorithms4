import  edu.princeton.cs.algs4.*;

public class Percolation{
  private boolean []open;
  private final int N;
  private  final WeightedQuickUnionUF wuf;
  
  public Percolation(int n){
    if(n<=0) throw new IllegalArgumentException("n>0 is needed");
    int arraysize=n*n;
    open=new boolean[arraysize];
    for (int i=0;i<arraysize;i++) open[i]=false;
    wuf=new WeightedQuickUnionUF(arraysize+2);
    N=n;
  }
  
  
  public void open(int row,int col){
    if(!validate(row,col)) throw new IllegalArgumentException("index of row and col should between 1 and n. ");
    int pos=id(row,col);
    if (isOpen(row,col)) return;
    open[pos]=true;
    if (row==1) wuf.union(pos,N*N);
    if (row==N) wuf.union(pos,N*N+1);
    if(validate(row-1,col)&&isOpen(row-1,col)) wuf.union(pos,id(row-1,col));
    if(validate(row+1,col)&&isOpen(row+1,col)) wuf.union(pos,id(row+1,col));
    if(validate(row,col-1)&&isOpen(row,col-1)) wuf.union(pos,id(row,col-1));
    if(validate(row,col+1)&&isOpen(row,col+1)) wuf.union(pos,id(row,col+1));
  }
  
  
  public int numberOfOpenSites(){
    int num=0;
    for(int i=1;i<=N;i++){
      for(int j=1;j<=N;j++){
        if(isOpen(i,j)) num++;
      }
    }
    return num;
  }
  

  public boolean percolates(){
    return wuf.find(N*N)==wuf.find(N*N+1);
  }
    
  
  public boolean isOpen(int row,int col){
    if(!validate(row,col)) throw new IllegalArgumentException("index of row and col should between 1 and n. ");
    return open[id(row,col)];
  }
  
  public boolean isFull(int row,int col){
    if(!validate(row,col)) throw new IllegalArgumentException("index of row and col should between 1 and n. ");
    return wuf.find(id(row,col))==wuf.find(N*N);
  }
  
  
  //validate (row,col)
  private boolean validate(int row,int col){
    boolean exist=true;
    if(row<1||row>N||col<1||col>N) exist=false;
    return exist;
  }
  
  //get the id of (row,col)
  private final int id(int row,int col){
    return (row-1)*N+col-1;
  }
  
  //test main
  public static void main(String[] args){
    /*File f=new File("percolationTest/input20.txt");
    String [] line=null;
    String s;
    try{
      BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(f)));
      line=(br.readLine()).split();
      int n=Integer.parseInt(line[0]);
      StdOut.println(n);
      Percolation p=new Percolation(n);
      while((s=br.readLine())!=null){
        line=s.split();
        if(line[0].equals("")) continue;
        StdOut.println(line[0]+" "+line[1]);
        if(p.percolates()) break;
        int row=Integer.parseInt(line[0]);
        int col=Integer.parseInt(line[1]);
        p.open(row,col);
      }
      double threshold=p.numberOfOpenSites()/(double)(p.N*p.N);
      StdOut.println("threshold:"+threshold);
    }
    catch(Exception e){
      e.printStackTrace();  
      return;
    }*/
    StdOut.println("the grid size is:");
    int n=StdIn.readInt();
    long startTime=System.currentTimeMillis();
    Percolation p=new Percolation(n);
    int [] randomList=StdRandom.permutation(n*n);
    for(int i=0;i<n*n;i++){
      if(p.percolates()) break;
      int row=(randomList[i]/n)+1;
      int col=(randomList[i]%n)+1;
      p.open(row,col);
    }
    /*while(!p.percolates()){
      int row=StdRandom.uniform(1,n+1);
      int col=StdRandom.uniform(1,n+1);
      p.open(row,col);
    }*/
    double threshold=p.numberOfOpenSites()/(double)(p.N*p.N);
    long endTime=System.currentTimeMillis();
    StdOut.println("threshold:"+threshold+"runTime:"+(endTime-startTime)+"ms");
  }
}

        
  
  
  
  