/**
*Author: Changling Li
*Date: 9/20/19
*Name: CellStack.java
*Use array to implement the stack data structure to store the values of each cell for finding the solution.
*/

public class CellStack{
	private Cell[] stack; //2D array
	private int maximum;
	private int top;

	public CellStack(){
	//constructor with default values
		maximum = 20;
		stack = new Cell[maximum];
		top=0;
	}

	public CellStack(int max){
	//constructor which sets the maximum number of the elements in stack
		maximum = max;
		stack = new Cell[max];
		top = 0;
	}

	public void push(Cell c){
	// push an element on the top of the stack
		if (top<maximum-1){
			stack[top] = c;
			top++;
		}
		else{
			Cell[] stack1 = new Cell[maximum*2];
			for(int i=0;i<top;i++){
				stack1[i] = stack[i];
			}
			stack[top] = c;
			stack = stack1;
			top++;
		}
	}

	public Cell pop(){
	//pop the top element out
		if(!this.empty()){
			Cell cell= stack[top-1];
			top--;
			return cell;
		}
		else{
			return null;
		}
	}

	public int size(){
		return top;
	}

	public boolean empty(){
		return top==0;
	}

}