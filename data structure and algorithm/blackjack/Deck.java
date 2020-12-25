/**
 * File:Deck.java
 * Author: Changling Li
 * Date: 09/11/2019
 *The Deck Class
 */

import java.util.ArrayList;
import java.util.Random;


public class Deck{

	private ArrayList<Card> deck= new ArrayList<Card>();

	public Deck()
	//Constructor, builds a deck of 52 cards
	{
		this.deck = deck;
		this.build();
	}

	public void build()
	//builds a deck of 52 cards
	{
		for(int b=0; b<4; b++)//loop four times 
		{
			for(int i=2; i<10; i++)
			{
				this.deck.add(new Card(i)); //add a new card of value from 2 to 9
			}
			this.deck.add(new Card(11)); //add a new card of value 11 four times
		}
		for(int a=0; a<16; a++)//loop 16 times to add 10
		{
			this.deck.add(new Card(10));
		}
	}

	public int size()
	//return the number of cards in the deck
	{
		return this.deck.size();
	}

	public Card deal()
	//returns the top card(position zero) and removes it from the deck
	{
		Card top = deck.get(0);
		this.deck.remove(0);
		return top;
	}

	public Card pick(int i)
	//returns the card at position i and removes it from deck.
	{
		Card chosen = deck.get(i);
		this.deck.remove(i);
		return chosen;
	}

	public void shuffle()
	//shuffles the deck by putting the deck in random order
	{
		Random ran = new Random();
		for(int i = this.deck.size() - 1; i>1; i--)//loop from the back throught the deck to swap the cards
		{
			Card curr = this.deck.get(i);
			int index = ran.nextInt(i-1);
			//swap the card at index i with a card at a random index 
			this.deck.set(i, this.deck.get(index));
			this.deck.set(index, curr);
		}
	}

	public String toString()
	//returns a String that has the contents of the deck in order
	{
		String returnStr = "";
		for (int c=0; c< this.deck.size();c++)
		//use for loop to loop through the elements in the arraylist
		{
			returnStr += this.deck.get(c).getValue() + " ";
		}
		return returnStr; 

	}

	public static void main(String[] args)
	{
		//testing
		Deck deck1 = new Deck();
		deck1.shuffle();
		System.out.println(deck1.toString());
		deck1.deal();
		deck1.size();
	}
}