/**
*Author: Changling Li
*Date: 10/3/19
*Name: Agent.java
*the agent parent class where the basic functions of agent are implemented
*/

import java.awt.Graphics;

public class Agent{
	private double x;
	private double y;

	public Agent(double x0, double y0){
		//constructor that sets the position
		x=x0;
		y=y0;
	}

	public double getX(){
		//returns the x position
		return x;
	}

	public double getY(){
		//returns the y position
		return y;
	}

	public void setX(double newX){
		//sets the x position
		x=newX;
	}

	public void setY(double newY){
		//sets the y position
		y=newY;
	}
	
	public String toString(){
		//returns a String containing the x and y positions
		String returnstr ="";
		returnstr = "(" +Double.toString(x) + "," + Double.toString(y) +")";
		return returnstr;
	}

	public void updateState(Landscape scape){;}

	public void draw(Graphics g){

	}

	public static void main(String[] args){
		Agent agent = new Agent(2.0,3.0);
		System.out.println(agent.getX());
		System.out.println(agent.getY());
		agent.setX(3.0);
		agent.setY(4.0);
		System.out.println(agent.toString());
	}
}