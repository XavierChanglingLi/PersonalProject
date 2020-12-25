/**
 * File:Hand.java
 * Author: Changling Li
 * Date: 09/11/2019
 *The Hand class
 */

import java.util.ArrayList;

public class Hand{
	private ArrayList<Card> hand= new ArrayList<Card>();

	public Hand(){
		//constructor, initialize the arraylist
		this.hand = hand;
	}

	public void reset(){
		//reset the hand to empty
		hand.clear();
	}

	public void add(Card card){
		//add the card object to the hand
		hand.add(card);
	}

	public int size(){
		//returns the number of cards in the hand
		return hand.size();
	}

	public Card getCard(int i){
		//returns the card with index i. Cast as appropriate
		return this.hand.get(i);
	}

	public int getTotalValue(){
		//returns the sum of the values of the cards in the hand
		int sum = 0;
		for (int i = 0; i < hand.size(); i++)
		{
			sum += this.hand.get(i).getValue();
		}
		return sum;
	}

	public String toString(){
		//returns a String that has the contents of the hand "written" in a nice form which override the default toString method
		String returnStr = "";
		for (int i=0; i< this.hand.size();i++)
		//use for loop to loop through the elements in the arraylist
		{
			returnStr += this.hand.get(i).getValue() + " ";
		}
		return returnStr; 
	}

	public static void main(String[] args){
		//testing code
		Hand hand1 = new Hand();
		Card c1 = new Card(11);
		Card c2 = new Card(6);
		hand1.add(c2);
		hand1.add(c1);
		System.out.println(hand1.toString());
		System.out.println(hand1.getTotalValue());
		System.out.println(hand1.getCard(0).getValue());
		System.out.println(hand1.size());
	}


}