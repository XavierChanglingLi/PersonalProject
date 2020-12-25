/**
*Author: Changling Li
*Date: 9/20/19
*Name: LifeSimulation.java
*Runs the simulation by entering the desired number of rows, columns, iterations and density
*/

import java.util.Random;
import java.util.Scanner;


public class LifeSimulation{

	public static void main(String[] args) throws InterruptedException{


		//extension 2 take the input from the users 
		Scanner scan = new Scanner(System.in);//create a scanner object
		System.out.println("enter the rows");
		int rows = scan.nextInt();
		System.out.println("enter the columns");
		int cols = scan.nextInt();
		System.out.println("enter the iteration number");
		int iteration = scan.nextInt();
		System.out.println("enter the initial density of alive cells");
		double density = scan.nextDouble();


		//int rows=100;
		//int cols=100;
		//int iteration=200;
		Landscape scape = new Landscape(rows,cols);

		Random gen = new Random();
        //double density = 0.6;

			// initialize the grid to be 30% full
        for (int i = 0; i < scape.getRows(); i++) {
            for (int j = 0; j < scape.getCols(); j++ ) { 
                scape.getCell( i, j ).setAlive( gen.nextDouble() <= density );
            }
        }
       // for(int j=0; j<3;j++){
        //	scape.getCell(1,j).setAlive(true);
        //}



		LandscapeDisplay scapeplay = new LandscapeDisplay(scape, 8);
		for(int i=0; i<iteration; i++){
			scape.advance();
			scapeplay.repaint();
			Thread.sleep(250);

		}
	}
}