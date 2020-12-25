/**
*Author: Changling Li
*Date: 9/29/19
*Name: Board.java
*Use 2D array to locate the numbers for the Sudoku
*/
import java.awt.Graphics;
import java.io.*;

public class Board{
	private Cell[][] cells;
	public static final int Size=9;
	//cannot be changed

	public Board(){
		cells = new Cell[Board.Size][Board.Size];
		for(int i=0;i<Board.Size;i++){
			for(int j=0;j<Board.Size;j++){
				cells[i][j] = new Cell(i,j,0);
			}
		}
	}

	public int getCols(){
		//returns the number of columns
		return cells[0].length;
	}

	public int getRows(){
		//returns the number of rows
		return cells.length;
	}

	public Cell get(int r, int c){
		//returns the Cell at location r,c
		return cells[r][c];
	}

	public boolean isLocked(int r, int c){
		//returns whether the Cell at r,c is locked
		return cells[r][c].isLocked();
	}

	public int numLocked(){
		//returns the number of locked Cells on the board
		int numlocked = 0;
		for(int i=0;i<Board.Size;i++){
			for(int j=0;j<Board.Size;j++){
				if(cells[i][j].isLocked()){
					numlocked += 1;
				}
			}
		}
		return numlocked;
	}

	public int value(int r, int c){
		//returns the value at Cell r,c
		return cells[r][c].getValue();
	}

	public void set(int r, int c, int value){
		//sets the value of the Cell at r,c
		cells[r][c].setValue(value);
	}

	public void set(int r, int c, int value, boolean locked){
		//sets the value and locked fields of the Cell at r,c
		cells[r][c].setValue(value);
		cells[r][c].setLocked(locked);
	}
	
	public String toString(){
		String returnstr = "";
		for(int i=0;i<Board.Size;i++){
			for(int j=0;j<Board.Size;j++){
				returnstr += Integer.toString(cells[j][i].getValue());
				returnstr += " ";
				if((j+1)%3==0){
					returnstr += " ";
				}
			}
			returnstr += "\n";
		}
		return returnstr;
	}

	public boolean validValue(int row, int col, int value){
		//tests if the given value is a valid value at the given row and column of the board. 
		//It should make sure the value is unique in its row, column and local 3x3 square
		if(value<1||value>9){//test whether value is within the range 1 to 9
			return false;
		}
		for(int j=0;j<9;j++){//test whether the value is unique at its row
			if(j!=col){
				if(value==cells[row][j].getValue()){
					return false;
				}
			}
		}
		for(int i=0;i<9;i++){//test whether the value is unique at its column
			if(i!=row){//does not include itself
				if(value==cells[i][col].getValue()){
					return false;
				}
			}
		}
		int r = row/3;
		int c = col/3;
		for(int a=r*3; a<r*3+3;a++){//test whether the value is unique at its square
			for(int b=c*3;b<c*3+3;b++){
				if(a!=row||b!=col){
					if(value==cells[a][b].getValue()){
						return false;
					}
				}
			}
		}
		return true;
	}

	public boolean validSolution(){
	//check whether it is a valid solution
		for(int i=0; i<Size; i++){
			for(int j=0; j<Size; j++){
				if(!this.validValue(i,j,cells[i][j].getValue())){
					return false;
				}
			}
		}
		return true;
	}

	public void draw(Graphics g, int scale){
	//draw each cells
		for(int i=0; i<Size; i++){
			for(int j=0; j<Size; j++){
				cells[i][j].draw(g,i,j,scale);
			}
		}
	}

	public boolean read(String filename){
		try{
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);

			int row = 0;
			String line = br.readLine();
			while(line!=null){
				String[]array = line.split("[ ]+");
				for(int idx=0;idx<9;idx++){
					this.set(row, idx, Integer.parseInt(array[idx]));
				}
				row++;
				//System.out.println(line);
				//System.out.println(array.length);
				line = br.readLine();
			}
			br.close();
			return true;
		}
		catch(FileNotFoundException ex){
			System.out.println("Board.read()::unable to open file"+filename);
		}
		catch(IOException ex){
			System.out.println("Board.read():: error reading file"+filename);
		}
		return false;
	}

	public static void main(String[] args){
		Board bd = new Board();
		//System.out.println(bd.toString());
		//System.out.println(bd.getCols());
		//System.out.println(bd.getRows());
		//System.out.println(bd.get(1,1));
		//bd.set(1,1,5);
		//System.out.println(bd.value(1,1));
		//System.out.println(bd.isLocked(1,1));
		//System.out.println(bd.numLocked());
		//bd.set(1,1,5,true);ß
		//System.out.println(bd.numLocked());
		//System.out.println(bd);
		bd.read(args[0]);
		System.out.println(bd.validValue(1,8,2));


	}
}