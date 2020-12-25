/**
*Author: Changling Li
*Date: 10/6/19
*Name: Landscape.java
*create the landscape where the agents are stored in a linkedlist
*/
import java.util.ArrayList;
import java.lang.Math;
import java.awt.Graphics;
import java.util.Random;

public class Landscape{
	private int width;
	private int height;
	private LinkedList<Agent> list;

	public Landscape(int w, int h){
		//sets the width and height and initialize the agent list
		width = w;
		height = h;
		list = new LinkedList<Agent>();
	}

	public int getHeight(){
	//return the height
		return height;
	}

	public int getWidth(){
	//return the width
		return width;
	}

	public void addAgent(Agent a){
		//insets an agent at the beginning of its list of agnets
		list.addFirst(a);
	}

	public String toString(){
		String returnstr = "";
		returnstr = "The number of agents on the landscape is "+ Integer.toString(list.size());
		return returnstr;
	}

	public ArrayList<Agent> getNeighbors(double x0, double y0, double radius){
		// return a list of Agents within radius distance of the location X0, y0;
		ArrayList<Agent> agents = new ArrayList<Agent>();
		for(Agent a: list){
			double x = a.getX();
			double y = a.getY();
			if(x==x0&&y==y0){continue;}
			double d = Math.sqrt(Math.pow(x-x0,2)+Math.pow(y-y0,2));
			if(d<=radius){
				agents.add(a);
			}
		}
		return agents;
	}

	public void draw(Graphics g){
		//for(int i=0; i<list.size(); i++){
		for(Agent a: list){
			a.draw(g);
		//	list.toArrayList().get(i).draw(g);
		}
	}

	public void updateAgents(){
		ArrayList<Agent> shuffled = list.toShuffledList();
		for(Agent a: shuffled){
			a.updateState(this);
		}
	}

	public static void main(String[] args){
		Landscape ls = new Landscape(500,500);
        Random r =  new Random();
        ls.addAgent(new SocialAgent(r.nextInt(500), r.nextInt(500)));
        ls.addAgent(new SocialAgent(r.nextInt(500), r.nextInt(500)));
        ls.addAgent(new SocialAgent(r.nextInt(500), r.nextInt(500)));
        System.out.println(ls.getNeighbors(150,200,250));
        System.out.println(ls);	
	}
}