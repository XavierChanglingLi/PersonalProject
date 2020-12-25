/**
*Author: Changling Li
*Date: 10/7/19
*Name: PickItemCustomer.java
*This will extend the customer class and implement the strategy in whcih the customer chooses to join the queue with least items
*/

import java.util.ArrayList;

public class PickItemCustomer extends Customer{
	public PickItemCustomer(int num_items, int num_lines){
		//constructor
		super(num_items, num_lines);
	}

	public int chooseLine(ArrayList<CheckoutAgent> checkouts){
		int least = checkouts.get(0).getNumItemsInQueue();
		int index = 0;
		for(int i=1; i<checkouts.size(); i++){
			int current = checkouts.get(i).getNumItemsInQueue();
			if(current<least){
				least = current;
				index = i;
			}
			else{continue;}
		}
		return index;
	}
}