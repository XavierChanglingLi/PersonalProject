/*
*Author: Changling Li
*Date: 12/1/19
*Name: Hunter.java
*The hunter object class
*/
import java.awt.Graphics;
import java.awt.Color;

public class Hunter extends Agent{
	//represents the player moving around the cave.
	private Vertex location;

	public Hunter(Vertex v){
		super(0, 0);
		location = v;
		location.setVisible(true);
	}

	public void updatePosition(Vertex v){
		location = v;
		this.setX(v.getX());
		this.setY(v.getY());
	}

	public Vertex getPosition(){
		return location;
	}

	public void draw(Graphics g, int scale){
		g.setColor(Color.blue);
		g.fillOval(this.getX()*scale+(int)(0.35*scale), this.getY()*scale+(int)(0.05*scale),
		(int)(0.3*scale),(int)(0.3*scale));
		g.setColor(Color.black);
		g.fillRect(this.getX()*scale+(int)(0.4*scale), this.getY()*scale+(int)(0.25*scale),
		(int)(0.2*scale),(int)(0.5*scale));
	}
}