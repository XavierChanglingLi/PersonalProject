/**
*Changling Li
*CS231 - project 3
*Sudoku.java
*9/29/19
*/

import java.util.Random;
import javax.swing.JButton;
import javax.swing.*;
import java.awt.event.*;
//display.addButton();

public class Sudoku extends JFrame implements ActionListener{
	public Board board;
	private LandscapeDisplay display;
	static JButton start;
    static JButton reset;
    private int lockedValue;

	public Sudoku(){
		this.board = new Board();
		display = new LandscapeDisplay(board, 30);
		start = display.button;
		reset = display.button2;
		start.addActionListener(this);
		reset.addActionListener(this);
		lockedValue = 0;
	}

	public Sudoku(int startingValue){
		Random ran = new Random();
		this.board = new Board();
		display = new LandscapeDisplay(board, 30);
		start = display.button;
		reset = display.button2;
		start.addActionListener(this);
		reset.addActionListener(this);
		lockedValue = startingValue;
		for(int i=0;i<lockedValue;i++){
			int row = ran.nextInt(9);
			int col = ran.nextInt(9);
			int value = ran.nextInt(10);
			if(board.value(row,col)==0){
				if(board.validValue(row, col, value)){
					board.set(row,col,value,true);
				}
				else{
					i--;
					continue;
				}
			}
			else{
				i--;
				continue;
			}
		}
	}

	public void resetBoard(int startingValue){
		display.repaint();
		Random ran = new Random();
		start = display.button;
		reset = display.button2;
		start.addActionListener(this);
		reset.addActionListener(this);
		if(startingValue==0){
			this.board = new Board();
			display.resetLand(board, 30);
		}
		else{
			this.board = new Board();
			display.resetLand(board, 30);
			lockedValue = startingValue;
			for(int i=0;i<lockedValue;i++){
				int row = ran.nextInt(9);
				int col = ran.nextInt(9);
				int value = ran.nextInt(10);
				if(board.value(row,col)==0){
					if(board.validValue(row, col, value)){
						board.set(row,col,value,true);
					}
					else{
						i--;
						continue;
					}
				}
				else{
					i--;
					continue;
				}
			}
		}
	}

	public Cell findBestCell(){
		for(int i=0;i<board.Size;i++){
			for(int j=0;j<board.Size;j++){
				if(board.get(i,j).getValue()==0){
					for(int val=1;val<10;val++){
						if(board.validValue(i, j, val)){
							//System.out.println(val);
							Cell best = new Cell(i,j,val);
							return best;
						}//end of assign the value
					}//end of test value
					return null;
				}//end of testing whether the current cell has value
				else{continue;}//continue the for loop to find next cell
			}
		}
		return null;
	}



	public boolean solve(int delay){
		CellStack stack = new CellStack(100);
		int locked = this.board.numLocked();
		int time = 0;
		//int currValue = 1;
		//int curRow = 0;
		//int curCol = 0;
		while(stack.size()<board.Size*board.Size - locked){
			time++;
			if(delay>0){
				try{
					Thread.sleep(delay);
				}
				catch(InterruptedException ex){
					System.out.println("interrupted");
				}
				display.repaint();
			}
			//System.out.println(stack.size());
			//pick next cell to test, including its initial value guess
			Cell bestCell = this.findBestCell();
			//if(bestCell != null)
			//{
				//System.out.println(bestCell.getValue());
			//}
			if(bestCell!=null){
				stack.push(bestCell);
				board.set(bestCell.getRow(),bestCell.getCol(),bestCell.getValue());
			}
			//no valid value is found, backtrack to the last cell with a valid value to test 
			else{
				boolean stuck = true;
				//backtrack to a cell that has a valid value to test
				while(stuck&&(stack.size()>0)){
					Cell last = stack.pop();
					//check if there is another valid value to try at this location
					for(int v = last.getValue()+1;v<10;v++){
						if(board.validValue(last.getRow(),last.getCol(),v)){
							last.setValue(v);
							board.set(last.getRow(),last.getCol(),v);
							stack.push(last);
							stuck = false;
							break;
						}
					}
						//no valid value was found at the last location, so set it to 0
					if(stuck){
						board.set(last.getRow(),last.getCol(),0);
					}
				}	
				//check if there is no solution(backtracked all the way)
				if(stack.size()==0){
					System.out.println(time);
					return false;
				}
			}			
		}	
		System.out.println(time);
		return true;
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource()==start){
			solve(10);
		}
		if(e.getSource()==reset){
			resetBoard(lockedValue);
		}
	}


	public static void main(String[] args){
		int locked = Integer.parseInt(args[0]);
		Sudoku sudoku = new Sudoku(Integer.parseInt(args[0]));//create the sudoku game and initialize it
		System.out.println(sudoku.board);
		// JButton button = LandscapeDisplay1.button;//create the button
		// JButton button2 = LandscapeDisplay1.button2;//create the button for reset

		// button.addActionListener(new ActionListener(){
		// 	@Override
		// 	public void actionPerformed(ActionEvent e){//check whether the button is clicked
		// 		System.out.println(sudoku.solve(10));
		// 		System.out.println(sudoku.board);
		// 	}
		// });
		// button2.addActionListener(new ActionListener(){
		// 	@Override
		// 	public void actionPerformed(ActionEvent e){
		// 		Extension sudoku = new Extension(Integer.parseInt(args[0]));
		// 		System.out.println(sudoku.board);
		// 	}
		// });
	}
}
