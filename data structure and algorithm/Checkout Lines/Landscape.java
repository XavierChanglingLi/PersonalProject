/**
*Author: Changling Li
*Date: 10/12/19
*Name: Landscape.java
*The landscape will maintain a list of CheckoutAgents as well as a list of customer's who have paid
for all their goods. The landscape will report statistic summarizing the amount of time if took for
the customer's to pay for all of their items.
*/

import java.util.ArrayList;
import java.awt.Graphics;

public class Landscape{
	private int width;
	private int height;
	private ArrayList<CheckoutAgent> checkouts;
	private LinkedList<Customer> completed;

	public Landscape(int w, int h, ArrayList<CheckoutAgent> checkouts){
		//constructor
		this.width = w;
		this.height = h;
		this.checkouts = checkouts;
		this.completed = new LinkedList<Customer>();
	}

	public int getHeight(){
		//return the height of the landscape
		return this.height;
	}

	public int getWidth(){
		//return the width of the landscape
		return this.width;
	}

	public String toString(){
		//return a string indicating how many checkouts and finshed customers are in the landscape;
		String returnstr = "The number of checkouts is "+ Integer.toString(checkouts.size()) +
		". The number of finished customers is " + Integer.toString(completed.size());
		return returnstr;
	}

	public void addFinshedCustomer(Customer c){
		completed.addLast(c);
	}

	public void draw(Graphics g){
		for(CheckoutAgent c: checkouts){
			c.draw(g);	
		}
	}

	public void updateCheckouts(){
		for(CheckoutAgent c: checkouts){
			c.updateState(this);
			//System.out.println("landscape");
		}
	}

	public void printFinishedCustomerStatistics(){
		//loop through the linked list for completed customer and add the time
		//together and get the average and the standard deviation;

		int total = 0;
		for(Customer c: completed){
			total += c.getTime();
		}
		double average = total/completed.size();
		int standard = 0;
		for(Customer c: completed.toArrayList()){
			standard += Math.pow((c.getTime()-average),2);
		}
		int variance = standard/completed.size();
		double standardDeviation = Math.pow(variance, 0.5);
		System.out.println("The average time is " + String.valueOf(average) + ". The standard deviation is "+ String.valueOf(standardDeviation) + ".");
	}
}