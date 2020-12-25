/*
*Author: Changling Li
*Date: 11/28/19
*Name:Agent.java
*/

import java.awt.Graphics;

public class Agent{
	private int x;
	private int y;

	public Agent(int x0, int y0){
		//constructor that sets the position
		x=x0;
		y=y0;
	}

	public int getX(){
		//returns the x position
		return x;
	}

	public int getY(){
		//returns the y position
		return y;
	}

	public void setX(int newX){
		//sets the x position
		x=newX;
	}

	public void setY(int newY){
		//sets the y position
		y=newY;
	}
	
	public String toString(){
		//returns a String containing the x and y positions
		String returnstr ="";
		returnstr = "(" +Integer.toString(x) + "," + Integer.toString(y) +")";
		return returnstr;
	}

	public void updateState(Landscape scape){;}

	public void draw(Graphics g, int scale){

	}

	public static void main(String[] args){
		Agent agent = new Agent(2,3);
		System.out.println(agent.getX());
		System.out.println(agent.getY());
		agent.setX(3);
		agent.setY(4);
		System.out.println(agent.toString());
	}
}