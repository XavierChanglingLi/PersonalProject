/**
*Author: Changling Li
*Date: 10/12/19
*Name: Customer.java
*an abstract class which creates a customer has a set of items that need to be paid for 
and a counter to keep track of how many time-steps it takes for them to choose a line and paid
for all the items. Each customer will also have a strategy for choosing a line
*/

import java.util.ArrayList;


abstract class Customer{
	private int items;
	private int steps;

	public Customer(int num_items){
	//constructor method
		this.items = num_items;
		this.steps = 0;

	}

	public Customer(int num_items, int time_steps){
		//constructor method
		this.items = num_items;
		this.steps = time_steps;
	}

	public void incrementTime(){
		//increments the number of time steps
		steps++;
	}

	public int getTime(){
		//returns the number of time steps
		return steps;
	}

	public void giveUpItem(){
		//decrements the number of items(indicating another item has been paid for)
		items--;
	}

	public int getNumItems(){
		//returns the number of items
		return items;
	}

	public abstract int chooseLine(ArrayList<CheckoutAgent> checkouts);
}