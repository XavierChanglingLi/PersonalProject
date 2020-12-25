/**
 * File:Deck.java
 * Author: Changling Li
 * Date: 09/11/2019
 *The Blackjack Class
 */
import java.util.ArrayList;

public class Blackjack{

	private Deck deck = new Deck(); //field for deck
	private Hand handPlayer= new Hand(); //field for a hand for the player
	private Hand handDealer= new Hand(); //field for a hand for the dealer

	public Blackjack()
	{
	//constructor, set up and reset the game
		this.deck = deck;
		this.handPlayer = handPlayer;
		this.handDealer = handDealer;
		this.reset(true);
	}

	public void reset(boolean newDeck)
	{
	//set up and reset the game. If newDeck is true, the function creates a new deck.
		if(newDeck)
		{
			this.deck = new Deck();
			this.deck.shuffle();
		}
		else
		{
			handPlayer.reset();
			handDealer.reset();
		}
	}

	public void deal()
	{
	//deal out two cards to both players
		for(int i = 0; i<2; i++)
		{	this.handPlayer.add(deck.deal());
			this.handDealer.add(deck.deal());
		}
	}

	public String toString()
	{
		String player = "";
		String dealer = "";

		player = this.handPlayer.toString();
		dealer = this.handDealer.toString();
		return "the cards in the player's hand is " + player + "\n" + "the cards in the dealer's hand is" + dealer;

	//returna a string that describes the state of the game( the total value of in both player's and dealer's hand)
	}

	public boolean playerTurn()
	{
	//have the player draw cards until the total value is equal or above 16. Return false if the value goes over 21.
		while(this.handPlayer.getTotalValue() < 17)
		{
			this.handPlayer.add(this.deck.deal());
			ace(this.handPlayer);
		}
		if (this.handPlayer.getTotalValue() > 21)
		{
			return false;
		}
		else{
			return true;
		}
	}

	public boolean dealerTurn()
	{
	//have the dealer draw cards until the total of the dealer's hand is equal or above 17. Return false if the value is over 21.
		while(this.handDealer.getTotalValue() < 18)
		{
			this.handDealer.add(this.deck.deal());
			ace(this.handDealer);
		}
		if (this.handDealer.getTotalValue() > 21)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

//change the ace from 11 to 1
	public void ace(Hand h)
	{
		if(h.getTotalValue() >21)
		{
			//loop through the hand
			for(int i=0; i<h.size();i++)
			{
				if(h.getCard(i).getValue()==11)
				{
					//change the value
					h.getCard(i).setValue(1);
				}
			}
		}
	}

	public int game(boolean verbose)
	{
		//reset the game
		this.reset(true);
		this.deal();

		//check for blackjack
		if (this.handDealer.getTotalValue()==21&&this.handPlayer.getTotalValue()==21)
		{
			System.out.println("Push with two Blackjacks");
			return 0;
		}
		else if(this.handPlayer.getTotalValue()==21)
		{
			System.out.println("The player has a Blackjack");
			return 1;
		}
		else if(this.handDealer.getTotalValue()==21)
		{
			System.out.println("The dealer has a Blackjack");
			return -1;
		}
		//create variables to represent the initial and final hand content as well as the result
		String initPlayHand = this.handPlayer.toString();
		String initDealHand = this.handDealer.toString();

		int result;
		boolean playerResult = this.playerTurn();
		boolean dealerResult = this.dealerTurn();

		String finalPlayHand = this.handPlayer.toString();
		String fianlDealHand = this.handDealer.toString();

		//use if statement to first check if anyone's total value is over 21
		if (playerResult == false)
		{
			result = -1;
		}
		else if (dealerResult == false)
		{
			result = 1;
		}
		else
		{
		//continue to check the differenc between the total values to decide the situation
			int difference = this.handPlayer.getTotalValue() - this.handDealer.getTotalValue();
			if(difference>0)
			{
				result = 1;
			}
			else if (difference<0)
			{
				result = -1;
			}
			else
			{
				result = 0;
			}
		}
		if(verbose)
		{
			//transalte the result to text content
			String returnStr = "";
			if(result == 1)
			{
				returnStr = "The player won";
			}
			else if(result == -1)
			{
				returnStr = "The dealer won";
			}
			else if(result == 0)
			{
				returnStr = "It is a tie";
			}
			System.out.println("the player's initial hand is " + initPlayHand + "\n" + 
				"the player's final hand is " + finalPlayHand + "\n" + 
				"the dealer's initial hand is " + initDealHand + "\n" + 
				"the dealer's final hand is " + fianlDealHand + "\n" + 
				returnStr);
		}
		//reset the hand of both players
		this.handPlayer.reset();
		this.handDealer.reset();
		return result;
	}

	public static void main(String[] args)
	{
		//run the game 3 times
		Blackjack bj = new Blackjack();
		bj.game(true);
		bj.game(true);
		bj.game(true);
	}
}