/**
 * File:Simulation.java
 * Author: Changling Li
 * Date: 09/11/2019
 *The Simulation Class, simulate the game for 1000 times to calculate the winning percentage for the player and dealer
 */

public class Simulation{


	public static void main(String[] args)
	{
		//create blackjack object and variables for the total number of each situation.
		Blackjack bj = new Blackjack();
		float playerwin = 0;
		float dealerwin = 0;
		float pushes = 0;
		float numgame = 1000;

		//use for loop to loop through all games
		for(int i=0; i<numgame; i++)
		{
			int a = bj.game(true);
			//if statement is used here to check the case and add 1 to the related variable
			if(a== 1)
			{
				playerwin +=1;
			}

			else if(a== -1)
			{
				dealerwin +=1;
			}
			else if(a== 0)
			{
				pushes +=1;
			}

		}
		//calculate the percentage of each situation.
		float perplayer = playerwin/numgame;
		float perdealer = dealerwin/numgame;
		float perpushes = pushes/numgame;

		System.out.println( "player wins "+ Math.round(playerwin) + " games" + ". The percentage of winning is "+ perplayer +"\n"
			+"dealer wins "+ Math.round(dealerwin) +" games" + ". The percentage of winning is "+ perdealer +"\n"
			+"there are "+ Math.round(pushes) + " pushes.(None wins) " + "The percentage is "+ perpushes);
	}
}