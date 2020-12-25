/**
*Author: Changling Li
*Date: 10/6/19
*Name: SocialAgent.java
*social agent class, only a single kind of agent is used 
*/

import java.awt.Graphics;
import java.awt.Color;
import java.awt.*;
import java.lang.*;
import java.io.*;
import java.awt.geom.*;
import java.util.Random;
import java.util.ArrayList;


public class SocialAgent extends Agent{
	//override the constructor, updateState, and draw methods
	protected boolean moved;
	//indicate whether the agent moved during its last updateState
	protected int r;
	//radius within which it considers another agent a neighbor;

	public SocialAgent(double x0, double y0){
		//constructor which calls the super class construtor and sets the radius field
		super(x0, y0);
		r=10;
	}

	public SocialAgent(double x0, double y0, int radius){
		//constructor which calls the super class construtor and sets the radius field
		super(x0, y0);
		r=radius;
	}

	public void setRadius(int radius){
		//sets the radius of sensitivity
		r = radius;
	}

	public int getRadius(){
		//returns the radius of sensitivity
		return r;
	}

	public void draw(Graphics g){
		//draws a circle of radius 5 at agent location. If the agent moved, the color change from a darker color to a lighter color
        if (moved){
            g.setColor(new Color(208,205,246));
            g.fillArc((int) getX(), (int) getY(), 10, 10, 0, 360);
        }
        else{
            g.setColor(new Color(105,95,240));
            g.fillArc((int) getX(), (int) getY(), 10, 10, 0, 360);
        }
    }//end draw() 
	

	public void updateState(Landscape scape){
		int numNeighbors = scape.getNeighbors(getX(),getY(),r).size();
		Random r = new Random();
		if(numNeighbors>=3){
			if(r.nextDouble()<=0.01){
				setX(getX()+r.nextDouble()*20-10);
				setY(getY()+r.nextDouble()*20-10);
				moved = true;
			}
			else{moved=false;}
		}
		else{
			setX(getX()+r.nextDouble()*20-10);
			setY(getY()+r.nextDouble()*20-10);
			moved = true;
		}
	}

	public static void main(String[] args){
		//SocialAgent agent = new SocialAgent(2.0,3.0,5);
		//System.out.println(agent.getX());
	//	System.out.println(agent.getY());
	//	agent.setX(3.0);
	//	agent.setY(4.0);
	//	System.out.println(agent.getX());
	//	System.out.println(agent.getY());
	//	agent.setRadius(8);
	//	System.out.println(agent.getRadius());
		Landscape ls = new Landscape(500,500);
		SocialAgent a1= new SocialAgent(5,5);
		SocialAgent a2 = new SocialAgent(10,10);
		SocialAgent a3= new SocialAgent(15,15);
		SocialAgent a4 = new SocialAgent(20,20);
		ls.addAgent(a1);
		ls.addAgent(a2);
		ls.addAgent(a3);
		ls.addAgent(a4);
		ArrayList <Agent> neighbor = ls.getNeighbors(a1.getX(),a1.getY(),15);
		a1.updateState(ls);
		System.out.println(neighbor);
		System.out.println(neighbor.size());
	} 

}