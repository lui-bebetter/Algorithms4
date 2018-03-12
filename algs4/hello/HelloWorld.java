
package algs4.hello;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Comparator;


public class HelloWorld {
  private  String s;
  public HelloWorld(String s){
    this.s=s;
  }
  public static void main(String [] args) {
    /*File f=new File("PercolationStats.java");
    try{
        BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(f)));
    }
    catch(Exception e){
        e.printStackTrace();  
        return;
    }*/
    int [][]a={{1,2,3},{4,5,6}};
    int []b=a[0];
    StringBuilder s=new StringBuilder();
    char c=65;
    char z=1;
    s.append(c+z);
    System.out.println(s);
  }

}