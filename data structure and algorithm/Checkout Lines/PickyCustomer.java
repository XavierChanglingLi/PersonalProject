/**
*Author: Changling Li
*Date: 10/7/19
*Name: PickyCustomer.java
*This will extend the customer class and implement the strategy in whcih the customer chooses to join the shortest queue
*/

import java.util.ArrayList;

public class PickyCustomer extends Customer{
	public PickyCustomer(int num_items, int num_lines){
		//constructor
		super(num_items, num_lines);
	}

	public int chooseLine(ArrayList<CheckoutAgent> checkouts){
		int shortest = checkouts.get(0).getNumInQueue();
		int index = 0;
		for(int i=1; i<checkouts.size(); i++){
			int current = checkouts.get(i).getNumInQueue();
			if(current<shortest){
				shortest = current;
				index = i;
			}
			else{continue;}
		}
		return index;
	}
}