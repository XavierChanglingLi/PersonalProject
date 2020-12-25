/**
*Author: Changling Li
*Date: 9/29/19
*Name: Automate.java
*Solve the sudoku 100 times for different numbers of locked cells and count the solved cases
*/

public class Automate{

	/**create new sudoku objects and runs it with a certain number of starting set values
	for 100 times
	*/
	public int simulation(int startingvalues){
		int solved = 0;
		for(int i=0; i<100;i++){//loop through 100 times
			Sudoku sudoku = new Sudoku(startingvalues);
			if(sudoku.solve(0)){
				solved++;//count the number of solved cases
			}
			else{continue;}
		}
		return solved;
	}

	public static void main(String[] args){
		Automate a = new Automate();
		//store the results in array for nice format
		int[]startingValues = new int [5];
		int[] numSolved = new int[5];
		int idex = 0;
		for(int i =10; i<41; i+=10){//test the cases for 10,20,30,40
			startingValues[idex]=i;
			numSolved[idex]=a.simulation(i);
			idex++;
		}
		for(int j=0;j<5;j++){
			System.out.println("intial number of starting values:" + startingValues[j]+". The number of solved cases out of 100 is" + numSolved[j]);
		}
	}
}

