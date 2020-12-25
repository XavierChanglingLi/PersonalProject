/*
*Author: Changling Li
*Date: 11/28/19
*Name: Vertex.java
*individual vertex object class
*/

import java.util.ArrayList;
import java.util.Comparator;
import java.awt.Graphics;
import java.awt.Color;

public class Vertex extends Agent implements Comparable<Vertex>{
	//represent the nodes in a graph.
	private boolean visible;
	private int cost;//distance from the Wumpus
	private boolean marked; //whether it is visited
	private Vertex [] adjacentVertices;
	private String label;

	public Vertex(int x0, int y0){
	//constructor 
		super(x0, y0);
		adjacentVertices = new Vertex[4];
		visible = false;
		cost = 0;
		marked = false;
		label = "";
	}

	public Vertex(int x0, int y0, String lab){
	//constructor 
		super(x0, y0);
		adjacentVertices = new Vertex[4];
		visible = false;
		cost = 0;
		marked = false;
		label = lab;
	}

	//set and get functions for fields
	public void setVisible(boolean v){visible = v;}
	public void setCost(int c){cost = c;}
	public void setMarked(boolean m){marked = m;}

	public boolean isVisible(){return visible;}
	public int getCost(){return cost;}
	public boolean isMarked(){return marked;}
	public String getLabel(){return label;}

	public static Direction opposite(Direction d){
	//returns the compass opposite of a direction
		if(d==Direction.NORTH){return Direction.SOUTH;}
		if(d==Direction.SOUTH){return Direction.NORTH;}
		if(d==Direction.EAST){return Direction.WEST;}
		if(d==Direction.WEST){return Direction.EAST;}
		return null;
	}

	public void connect(Vertex other, Direction dir){
	//modifies the vertex's adjacency list/map so that it connects with the other vertex
		adjacentVertices[dir.ordinal()]=other;
	}

	public Vertex getNeighbor(Direction dir){
	//returns the vertex in the specified direction or null 
		if(adjacentVertices[dir.ordinal()]==null){return null;}
		return adjacentVertices[dir.ordinal()];
	}

	public ArrayList<Vertex> getNeighbors(){
	//returns a collection, which could be an ArrayList, of all of the object's neighbors
		ArrayList<Vertex> neighbors = new ArrayList<Vertex>();
		for(int i=0; i<adjacentVertices.length;i++){
			if(adjacentVertices[i]!=null){
				neighbors.add(adjacentVertices[i]);
			}
		}
		return neighbors;
	}

	public int compareTo(Vertex v){
		return v.getCost()-this.getCost();
	}

	public void draw(Graphics g, int scale){
	//draw the vertex as a room. 
	//It show possible exits from the room and
	//indicate whether the Wumpus is two rooms away or closer.
		if(!this.visible){
			return;
		}
		int xpos = (int)this.getX()*scale;
		int ypos = (int)this.getY()*scale;
		int border = 2;
		int half = scale/2;
		int eighth = scale/8;
		int sixteenth = scale/16;

		//draw rectangle for the walls of the room
		if (this.cost<=2){
			//wumpus is nearby
			g.setColor(Color.red);
		}
		else{
			//wumpus is not nearby
			g.setColor(Color.black);
		}
		g.drawRect(xpos+border, ypos+border, scale-2*border, scale-2*border);

		//draw doorways as boxes
		g.setColor(Color.black);
		if (this.getNeighbor(Direction.NORTH)!=null){
            g.fillRect(xpos + half - sixteenth, ypos, eighth, eighth + sixteenth);
		}
        if (this.getNeighbor(Direction.SOUTH)!=null){
            g.fillRect(xpos + half - sixteenth, ypos + scale - (eighth + sixteenth), 
                       eighth, eighth + sixteenth);
        }
        if (this.getNeighbor(Direction.WEST)!=null){
            g.fillRect(xpos, ypos + half - sixteenth, eighth + sixteenth, eighth);
        }
        if (this.getNeighbor(Direction.EAST)!=null){
            g.fillRect(xpos + scale - (eighth + sixteenth), ypos + half - sixteenth, 
                       eighth + sixteenth, eighth);
        }
	}

	public String toString(){
	//return a String containing the number of the neighbors, the cost and the marked flag
		String tostring = "";
		tostring += this.getLabel();
		tostring += "\nThe number of neighbors is ";
		tostring += String.valueOf(getNeighbors().size());
		tostring += ".\nThe cost is ";
		tostring += String.valueOf(getCost());
		tostring += ".\nThe marked is ";
		tostring += Boolean.valueOf(isMarked());
		tostring += ".\nThe visible is ";
		tostring += Boolean.valueOf(isVisible());
		return tostring;
	}

	public static void main(String[] argv){
		Vertex v1 = new Vertex(0,0);
		Vertex v2 = new Vertex(0,0);
		Vertex v3 = new Vertex(0,0);
		System.out.println(v1.opposite(Direction.NORTH));
		System.out.println(v1.opposite(Direction.SOUTH));
		System.out.println(v1.opposite(Direction.WEST));
		System.out.println(v1.opposite(Direction.EAST));
		v1.setCost(2);
		v2.setCost(3);
		v3.setCost(5);
		v1.connect(v2, Direction.SOUTH);
		v1.connect(v3, Direction.NORTH);
		v1.compareTo(v2);
		System.out.println(v1.toString());
	}
}

enum Direction{
		//enumerated type
	NORTH, SOUTH, EAST, WEST;
}