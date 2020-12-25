/**
*Author: Changling Li
*Date: 10/4/19
*Name: CatSocialAgent.java
*Catsocial agent object, two types of agents are created
*/
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class CatSocialAgent extends SocialAgent{
	private int category;

	public CatSocialAgent(double x0, double y0, int cat, int radius){
		//calls the parent constructor and set the category
		super(x0, y0);
		r = radius;
		category = cat;
	}

	public int getCategory(){
		//returns the category value
		return category;
	}

	public String toString(){
		//returns a single character string indicating the category
		return Integer.toString(category);
	}

	public void draw(Graphics g){
		//draws a circle of radius 5 at agent's location. Different categories should have different colors
		if (category == 1){
			if (moved){
            	g.setColor(new Color(208,205,246));
            	g.fillArc((int) getX(), (int) getY(), 10, 10, 0, 360);
        	}
        	else{
           		g.setColor(new Color(105,95,240));
            	g.fillArc((int) getX(), (int) getY(), 10, 10, 0, 360);
        }
		}
		if(category == 2){
			if (moved){
            	g.setColor(new Color(252,249,162));
            	g.fillArc((int) getX(), (int) getY(), 10, 10, 0, 360);
        	}
        	else{
            	g.setColor(new Color(239,231,9));
            	g.fillArc((int) getX(), (int) getY(), 10, 10, 0, 360);
        	}
		}  
    }//end draw() 

	public void updateState(Landscape scape){
		//identify how many neighbors within the cell's radius of sensitivity have the same category and how many have different category
		ArrayList<Agent> neighbors1 = scape.getNeighbors(getX(),getY(),r);
		//cast agent arraylist to catsocialagent list
		ArrayList<CatSocialAgent> neighbors = (ArrayList<CatSocialAgent>)(ArrayList<?>)(neighbors1);
		int category1 = 0;
		int category2 = 0;
		Random r = new Random();

		for(int i=0; i<neighbors.size();i++){
			if(neighbors.get(i).getCategory()==1){
				category1++;
			}
			else{
				category2++;
			}
		}

		//if there are more than 2 other agents with radius r and more of them are the same category
		if(this.category==1){
			if(neighbors.size()>=2&&category1>category2){
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
		if(this.category==2){
			if(neighbors.size()>=2&&category2>category1){
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

	}
}