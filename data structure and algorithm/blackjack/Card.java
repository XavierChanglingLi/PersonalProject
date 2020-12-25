/**
 * File:Card.java
 * Author: Changling Li
 * Date: 09/09/2019
 *The Card class
 */

public class Card{
	private int value; 
	//the card class which hold all information unique to the card
	public Card(int v){
	//constructor with the value of the card
		if(0 <v && v< 12)
		{
			value = v;
		}
	}
	public void setValue(int n){
		value = n;
	}
	
	
	public int getValue(){
	//get method, get the value of the card
		return value;
	}
	public static void main (String[] args){
		Card c1 = new Card(11);
		System.out.println(c1.getValue());

	}
}