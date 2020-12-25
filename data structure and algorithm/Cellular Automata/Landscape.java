/**
*Author: Changling Li
*Date: 9/20/19
*Name: Landscape.java
* Use 2D array to create the grid where the cells are stored. The landscape class also includes the method to update the state of the cell
*/

import java.util.ArrayList;
import java.lang.Math;
import java.awt.Graphics;

public class Landscape{
//hold a 2D grid of Cell object references
	private Cell[][] grid;
	private int rows;
	private int cols;

	public Landscape(int rows, int cols){
		//sets the number of rows and columns to the sepcified values and allocate a Cell for each location in the Grid
		this.rows = rows;
		this.cols = cols;
		grid = new Cell[rows][cols];
		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++){
				grid[i][j]= new Cell();
			}
		}
	}

	public void reset(){
		//calls the reset method for each cell
		for(int i=0; i<rows; i++){
			for(int j=0; j<cols; j++){
				grid[i][j].reset();
			}
		}
	}

	public int getRows(){
		//returns the number of rows in the landscape
		return rows;
	}

	public int getCols(){
		//returns the number of columns in the landscape
		return cols;
	}

	public Cell getCell(int row, int col){
		//returns a reference to the Cell located at postion(row, col)
		return grid[row][col];
	}

	public String toString(){
		//coverts the Landscape into a text-based string representation. At the end of each row, put a carriage return ("\n").
		String returnstr = "";
		for(int i=0; i<this.rows; i++){
			for(int j=0; j<this.cols; j++){
				returnstr += grid[i][j];
			}
			returnstr += "\n";
		}
		return returnstr;
	}


	public ArrayList<Cell> getNeighbors(int row, int col){
		//returns a list of references to the neighbors of the Cell at one location.
		ArrayList<Cell> neighbors = new ArrayList<>();
		int minr = row-1;
		int maxr = row+1;
		int minc = col-1;
		int maxc = col+1;

		for(int r=minr; r<=maxr; r++){
			for(int c=minc;c<=maxc;c++){
				if(r<0||r>(this.getRows()-1)||c<0||c>(this.getCols()-1)){continue;}
				else{
					if(r!=row||c!=col){
						neighbors.add(grid[r][c]);
					}//not included the cell itself
				}
			}
		}
		return neighbors;
	}

//extension 3
	public ArrayList<Cell> getNeighborsExt(int row, int col){
		//returns a list of references to the neighbors of the Cell at one location.
		ArrayList<Cell> neighbors = new ArrayList<>();
		//use modulo to get the indices based on rows and cols
		int minr = Math.floorMod(row-1, rows);   
		int maxr = Math.floorMod(row+1, rows);
		int minc = Math.floorMod(col-1, cols);
		int maxc = Math.floorMod(col+1, cols);

		for(int r=minr; r<=maxr; r++){
			for(int c=minc;c<=maxc;c++){
				if(r!=row||c!=col){neighbors.add(grid[r][c]);}//not included the cell itself
			}
		}
		return neighbors;
	}



	public void draw(Graphics g, int gridScale){
		//draw all the cells
		for(int i=0; i<this.getRows(); i++){
			for(int j=0; j< this.getCols(); j++){
				this.grid[i][j].draw(g, i*gridScale, j*gridScale, gridScale);
			}
		}
	}

	public void advance(){
		//create a temporary Cell grid of the same size to update the state of each cell simultaneously.
		Cell[][] newgrid = new Cell[this.getRows()][this.getCols()];
		for(int i=0; i<this.getRows(); i++){
			for(int j=0; j<this.getCols(); j++){
				newgrid[i][j]=new Cell();
				//newgrid[i][j].setAlive(this.getCell(i,j).getAlive());
			}

		}
		for(int a=0; a<this.getRows(); a++){
			for(int b=0; b<this.getCols(); b++){
				newgrid[a][b].setAlive(this.getCell(a,b).getAlive());
				newgrid[a][b].updateState(this.getNeighbors(a,b));
			}
		}
		this.grid=newgrid;
	}

	public static void main(String[] args){
		Landscape lsp = new Landscape(5,4);
		System.out.println(lsp.getRows());
		System.out.println(lsp.getCols());
		System.out.println(lsp.toString());
	}
}