/**
*Author: Changling Li
*Date: 9/20/19
*Name: Cell.java
*Cell object, each represent a location on the sudoku board where a number can be set and stored. 
*/
import java.awt.Graphics;

public class Cell{

	private int row;
	private int column;
	private int value;
	private boolean locked;

	public Cell(){
	//constructor for a cell with default values
		this.row = 0;
		this.column = 0;
		this.value = 0;
		this.locked = false;
	}

	public Cell(int row, int col, int value){
	//constructor with set values
		this.row = row;
		this.column = col;
		this.value = value;
		this.locked = false;
	}

	public Cell(int row, int col, int value, boolean locked){
	//constructor with whether the cell is locked or not
		this.row = row;
		this.column = col;
		this.value = value;
		this.locked = locked;
	}

	public int getRow(){
	//get the row of the cell
		return this.row;
	}

	public int getCol(){
	//get the column of the cell
		return this.column;
	}

	public int getValue(){
	//get the value of the cell
		return this.value;
	}

	public void setValue(int newval){
	//set the value of the cell
		this.value = newval;
	}

	public boolean isLocked(){
	//check whether the cell is locked
		return this.locked;
	}

	public void setLocked(boolean lock){
	//set the cell to locked or not
		this.locked = lock;
	}

	public Cell clone(){
		Cell newcell = new Cell(this.getRow(), this.getCol(), this.getValue(), this.isLocked());
		return newcell;
	}

	public void draw(Graphics g, int x0, int y0, int scale){
	//draw the cell as characters on a rectangle
		g.drawChars(new char[]{(char)('0'+this.value)},0,1,x0*scale+20, y0*scale+20);
		g.drawRect(x0*scale-scale/2+20, y0*scale-scale/2+20, scale, scale);
	}

	public String toString(){
		String returnstr = "";
		returnstr += "position:" + String.valueOf(this.getRow()) +","+ String.valueOf(this.getCol());
		returnstr += "locked:";
		if(this.isLocked()){
			returnstr += "true";
		} 
		else{
			returnstr += "false";
		}
		return returnstr;
	}

	public static void main(String[] argv) {
		Cell c1 = new Cell();        // default constructor
		Cell c2 = new Cell(1, 5, 7); // basic constructor
		Cell c3 = new Cell(2, 6, 8, true); // full constructor

		System.out.println( "c1 locked: " + c1.isLocked() );
		System.out.println( "c2 locked: " + c2.isLocked() );
		System.out.println( "c3 locked: " + c3.isLocked() );

		c1.setLocked(true);
		System.out.println( "c1 should be locked: " + c1.isLocked() );

		c3.setLocked(false);
		System.out.println( "c3 should not be locked: " + c3.isLocked() );
		
		System.out.println( "c2 value should be 7: " + c2.getValue() );
		System.out.println( "c3 value should be 8: " + c3.getValue() );

		c1.setValue( 5 );
		System.out.println( "c1 value should be 5: " + c1.getValue() );

		System.out.println( "c1 row, col (0, 0): " + c1.getRow() + ", " + c1.getCol() );
		System.out.println( "c2 row, col (1, 5): " + c2.getRow() + ", " + c2.getCol() );
		System.out.println( "c3 row, col (2, 6): " + c3.getRow() + ", " + c3.getCol() );	

		Cell c4 = c3.clone();
		System.out.println( "c3: " + c3 );
		System.out.println( "c4: " + c4 );
		c4.setValue( 3 );
		c4.setLocked( true );
		System.out.println( "c3: " + c3 );
		System.out.println( "c4: " + c4 );
		
    }
}
