/**
*Author: Changling Li
*Date: 9/16/19
*Name: Grid.java
*Grid class, created to show the 2D grid print out result
*/

import java.util.Random;

public class Grid{

	public static void main(String[] args)
	{
		if(args.length < 1)
		{
			System.out.println("usage: java Grid [row][column]");
		}
		else
		{
			for(int i =0; i<args.length; i++)
			{
				System.out.println(args[i]);
			}
			int yogi = Integer.parseInt(args[0]);
			int booboo = Integer.parseInt(args[1]);
			String[][] ranger = new String[yogi][booboo];
			for (int i=0; i<yogi;i++){
				for(int j=0; j<booboo;j++){
					Random ran = new Random();
					char c = (char)('a' + (char)(ran.nextInt(25)));
					ranger[i][j] = "" + c;
				}
			}
			for (int b=0;b<ranger.length; b++){
				for (int d=0; d<ranger[b].length; d++){
					System.out.print(ranger[b][d]+" ");
				}
				System.out.println("");
			}
		}
	}

}