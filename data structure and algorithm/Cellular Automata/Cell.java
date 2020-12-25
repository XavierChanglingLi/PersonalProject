/**
*Author: Changling Li
*Date: 9/17/19
*Name: Cell.java
*Cell class, create each individual cell where the information of this cell is stored.
*/

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Cell{

	private boolean cell;

	public Cell(){
	//constructor method, by default, the Cell is dead
		cell= false;
	}

	public Cell(boolean alive){
	//constructor method
		cell = alive;
	}

	public boolean getAlive(){
	//returns whether the Cell is alive
		return cell;
	}

	public void reset(){
	//sets the Cell state to its default value(not alive)
		cell = false;
	}

	public void setAlive(boolean alive){
	//sets the Cell's alive state
		cell = alive;
	}

	public String toString(){
	//returns a string that indicates the alive state of the Cell as one-character string.
		String returnstr = "";
		if(this.cell==true){
			returnstr = String.valueOf(0);
		}
		else{
			returnstr = " ";
		}
		return returnstr;
	}

	public void draw(Graphics g, int x, int y, int scale){
		//extension 1 use RGB values for the color to create random colors each time when the cell is updated
		Random ran = new Random();
		if(this.getAlive()){
			g.setColor(new Color(ran.nextInt(240),ran.nextInt(240),ran.nextInt(240)));
			g.fillOval(x, y, scale, scale);
		}
		else{
			g.setColor(new Color(255,255,255));
			g.fillOval(x, y, scale, scale);
		}
	}

	public void updateState(ArrayList<Cell> neighbors){
		//if two or three live neighbors, then it will be set to alive.
		//if a dead cell has exactly three living neighbors, it will be set to alive.
		//Otherwise, the cell will be set to dead.
		int liveNeighbors = 0;
		for (Cell neighbor: neighbors){
			if(neighbor.getAlive()){
				liveNeighbors++;//get number of live numbers
			}
		}
		if(this.getAlive()&&liveNeighbors==2){
			this.setAlive(true);
		}
		else if(this.getAlive()&&liveNeighbors==3){
			this.setAlive(true);
		}
		else if((!this.getAlive())&&liveNeighbors==3){
			this.setAlive(true);
		}
		else{
			this.setAlive(false);
		}
	}

	public static void main(String[] args){
		Cell c1 = new Cell(true);
		Cell c2 = new Cell(false);
		c2.setAlive(true);

		ArrayList neighbors = new ArrayList<Cell>();
		for (int i = 0; i < 8;i++){

		}

		System.out.println(c1.toString());
		System.out.println(c2.getAlive());
		System.out.println(c2.toString());
		c2.reset();
		System.out.println(c2.toString());
	}
}
