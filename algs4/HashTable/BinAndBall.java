/**********************************************************************
 *FileName: BinAndBall
 *Author:   Dell
 *Date:     2017/12/3012:07
 *Description:compute the m bins and n balls probability
 * Theory:Expect two balls in the same bins after sqrt(2*pi*m/2) tosses with >0.5 probability
 * m the bins
 **********************************************************************/

package algs4.HashTable;

import java.util.Scanner;


/********************************
 *@author: Dell
 *@create: 2017/12/30
 *Description:
 ********************************/
public class BinAndBall {
	private final int bins;//number of bins
	private final int balls;//number of balls

	public  BinAndBall(int bins,int balls){
		this.bins=bins;
		this.balls=balls;
	}

	public double probability(){
		if(balls<2) return Double.POSITIVE_INFINITY;
		return probability(balls);
	}

	private double probability(int m){
		/********************************
		 * Description: the probability of two balls in a bin when the balls number is m
		 *
		 * P(bins,balls)=P(bins,balls-1)+(balls-1)(balls-1)!/bins^(balls-1)(balls-n+1)!
		 *
		 * @param m the number of balls
		 * @return: double
		 * @Author: luibebetter
		 *********************************/
		if(m==2) return 1/(double)bins;
		double tmp=1;
		for (int i=1;i<=m-2;i++) {
			tmp*=bins-i;
		}
		double p=(m-1)*tmp/(Math.pow(bins,m-1));
		return p+probability(m-1);
	}

	public static void main(String[] args) {
		System.out.println("bins and balls:");
		Scanner in=new Scanner(System.in);
		int bins=in.nextInt();
		int balls=in.nextInt();
		BinAndBall b=new BinAndBall(bins,balls);
		System.out.println(b.probability());
	}
}
