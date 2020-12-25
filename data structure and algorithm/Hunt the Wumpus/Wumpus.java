/*
*Author: Changling Li
*Date: 12/1/19
*Name: Wumpus.java
*The wumpus object class
*/

import java.awt.Graphics;
import java.awt.Color;
public class Wumpus extends Agent{
	private Vertex home;
	private boolean shot;

	public Wumpus(Vertex v){
		super(v.getX(), v.getY());
		home = v;
	}

	public void shot(boolean s){
		shot=s;
	}

	public boolean isVisible(Hunter h){
		if(h.getPosition()==home){
			return true;
		}
		return false;
	}

	public Vertex getPosition(){
		return home;
	}

	public void draw(Graphics g, int scale){
		if(!this.home.isVisible()){return;}
		int x0 = (int)(getX()*scale);
		int y0 = (int)(getY()*scale);
		if(shot){
			g.setColor(Color.red);
			g.fillOval(x0, y0,(int)(1*scale),(int)(1*scale));
		}
		else{
			g.setColor(Color.black);
			g.fillOval(x0, y0,(int)(1*scale),(int)(1*scale));
		}
	}

}