/**********************************************************************
 *FileName: BaseballElimination
 *Author:   lui1993
 *Date:     2018/2/2212:15
 *Description:The BaseballElimination class represents a data type for
  solving the baseball elimination problem.Given the standings in a
  sports division at some point during the season, determine which teams
  have been mathematically eliminated from winning their division.
 **********************************************************************/

package algs4.MaxFlow_MinCut;

import algs4.HashTable.LinearProbingHashST;
import algs4.HashTable.SeparateChainingHashST;
import algs4.queue.LinkedQueue;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/********************************
 *@author lui1993
 *@create 2018/2/22
 *Description:
 ********************************/
public class BaseballElimination {
	private LinearProbingHashST<String,Integer> teams;//team name - integer value pair
	private String [] TName;//TName[i]=team name corresponding to integer i
	private int []wins;//wins[i]=number of wins for team i
	private int [] losses;//losses[i]=number of losses for team i
	private int [] remaining;//remaining[i]=number of remaining games for team i
	private int [][] against;//against[i][j]=number of remaining games between team i and team j
	private FordFulkerson maxFlow;

	// create a baseball division from given filename
	public BaseballElimination(String filename){
		Scanner in;
		try{
			in=new Scanner(new BufferedInputStream(new FileInputStream(filename)));
		}catch(IOException e){
			throw new IllegalArgumentException("can't open the file "+filename);
		}

		//read the file and initialize the private variable
		Pattern WHITESPACE_PATTERN=Pattern.compile("\\p{javaWhitespace}+");
		int n=in.nextInt();
		assert n>=1;
		wins=new int[n];
		losses=new int[n];
		remaining=new int[n];
		against=new int[n][n];
		teams=new LinearProbingHashST<>();
		TName=new String [n];

		in.nextLine();
		for (int i = 0; i < n; i++) {
			String []lines=WHITESPACE_PATTERN.split(in.nextLine().trim());
			teams.put(lines[0],i);
			TName[i]=lines[0];
			wins[i]=Integer.parseInt(lines[1]);
			losses[i]=Integer.parseInt(lines[2]);
			remaining[i]=Integer.parseInt(lines[3]);
			for (int j = 0; j < n; j++) {
				against[i][j]=Integer.parseInt(lines[4+j]);
			}
		}
	}

	//number of teams
	public int numberOfTeams(){
		return teams.size();
	}

	//all of the teams
	public Iterable<String> teams(){
		return teams.keys();
	}

	// number of wins for given team
	public int wins(String team){
		if(!teams.contains(team)) throw new IllegalArgumentException("The given team isn't in the baseball division.");
		return wins[teams.get(team)];
	}

	// number of losses for given team
	public int losses(String team){
		if(!teams.contains(team)) throw new IllegalArgumentException("The given team isn't in the baseball division.");
		return losses[teams.get(team)];
	}

	// number of remaining games for given team
	public int remaining(String team){
		if(!teams.contains(team)) throw new IllegalArgumentException("The given team isn't in the baseball division.");
		return remaining[teams.get(team)];
	}

	// number of remaining games between team1 and team2
	public int against(String team1, String team2){
		if(!teams.contains(team1) || !teams.contains(team2)) throw new IllegalArgumentException("The given teams isn't in the baseball division.");
		return against[teams.get(team1)][teams.get(team2)];
	}

	// is given team eliminated?
	public boolean isEliminated(String team){
		if(!teams.contains(team)) throw new IllegalArgumentException("The given team isn't in the baseball division.");
		int specified=teams.get(team);

		//trivial elimination
		int n=numberOfTeams();
		int V=n*(n+1)/2+2;//number of vertices in the flow network
		for (int i = 0; i < n; i++) {
			if(i==specified) continue;
			if(wins[specified]+remaining[specified]-wins[i]<0) {
				maxFlow=null;
				return true;
			}
		}

		FlowNetwork f=genFlowNetwork(team);

		maxFlow=new FordFulkerson(f,V-2,V-1);
		int total=0;
		for (int i = 0; i < n; i++) {
			if(i==specified) continue;
			for (int j = i+1; j < n; j++) {
				if(j==specified) continue;
				total+=against[i][j];
			}
		}

		return total!=maxFlow.value();
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team){
		if(!isEliminated(team)) return null;
		int n=numberOfTeams();
		int specified=teams.get(team);
		LinkedQueue<String> queue=new LinkedQueue<>();

		//trivial elimination
		if(maxFlow==null){
			for (int i = 0; i < n; i++) {
				if(i==specified) continue;
				if(wins[specified]+remaining[specified]-wins[i]<0) {
					queue.enqueue(TName[i]);
					return queue;
				}
			}

		}

		//non-trivial elimination

		for (int i = 0; i < n; i++) {
			if(maxFlow.inCut(i)) queue.enqueue(TName[i]);
		}

		return queue;
	}

	//constructing the flow net work corresponding to specified team
	private FlowNetwork genFlowNetwork(String team){
		/**Flow net work vertices:
		 * 0~n-1 :team vertices
		 * n~V-3 ：game vertices
		 * V-2 and V-1: source and target vertex
		 */
		int specified=teams.get(team);
		int n=numberOfTeams();
		int V=n*(n+1)/2+2;
		FlowNetwork f=new FlowNetwork(V);
		int count=n;//game vertices start from {@code n}
		int s=V-2,t=V-1;
		for (int i = 0; i < n; i++) {
			if(i==specified) continue;
			for (int j = i+1; j < n; j++) {
				if(j==specified) continue;
				FlowEdge e1=new FlowEdge(s,count,against[i][j]);
				FlowEdge e2=new FlowEdge(count,i,Double.POSITIVE_INFINITY);
				FlowEdge e3=new FlowEdge(count,j,Double.POSITIVE_INFINITY);
				f.addEdge(e1);
				f.addEdge(e2);
				f.addEdge(e3);
				count++;
			}
			FlowEdge e=new FlowEdge(i,t,wins[specified]+remaining[specified]-wins[i]);
			f.addEdge(e);
		}
		return f;
	}

	//Unit test
	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination(args[0]);
		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				System.out.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team)) {
					System.out.print(t + " ");
				}
				System.out.println("}");
			}
			else {
				System.out.println(team + " is not eliminated");
			}
		}
	}
}
