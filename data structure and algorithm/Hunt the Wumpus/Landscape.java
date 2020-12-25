/*
*Author: Changling Li
*Date: 12/1/19
*Name: Landscape.java
*/

import java.util.ArrayList;
import java.lang.Math;
import java.awt.Graphics;
import java.util.Random;

public class Landscape{
	private int width;
	private int height;
	private LinkedList<Agent> foreground;
	private LinkedList<Agent> background;
	private Hunter hunter;
	private Wumpus wumpus;

	public Landscape(int w, int h){
		//sets the width and height and initialize the agent list
		width = w;
		height = h;
		foreground = new LinkedList<Agent>();
		background = new LinkedList<Agent>();
	}

	public int getHeight(){
	//return the height
		return height;
	}

	public int getWidth(){
	//return the width
		return width;
	}

	public void addBackgroundAgent(Agent a){
		//insets an agent at the beginning of its list of agnets
		background.addFirst(a);
	}

	public void addForegroundAgent(Agent a){
		//insets an agent at the beginning of its list of agnets
		foreground.addFirst(a);
	}

	public void addHunter(Hunter h){
		hunter = h;
	}

	public void addWumpus(Wumpus w){
		wumpus = w;
	}

	public String toString(){
		String returnstr = "";
		returnstr += "The number of agents on the background is "+ Integer.toString(background.size());
		returnstr += "\nThe number of agents on the foreground is "+ Integer.toString(foreground.size());
		return returnstr;
	}


	public void draw(Graphics g){
		for(Agent a: background){
			a.draw(g,64);
		}

		for(Agent a: foreground){
			a.draw(g, 64);
		}
		hunter.draw(g, 64);
		wumpus.draw(g, 64);

	}


	public static void main(String[] args){
		Landscape ls = new Landscape(500,500);
    //    Random r =  new Random();
    //    ls.addAgent(new SocialAgent(r.nextInt(500), r.nextInt(500)));
    //    ls.addAgent(new SocialAgent(r.nextInt(500), r.nextInt(500)));
    //    ls.addAgent(new SocialAgent(r.nextInt(500), r.nextInt(500)));
    //    System.out.println(ls.getNeighbors(150,200,250));
    //    System.out.println(ls);	
	}
}