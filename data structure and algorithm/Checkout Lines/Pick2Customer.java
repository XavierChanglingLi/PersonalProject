/**
*Author: Changling Li
*Date: 10/7/19
*Name: Pick2Customer.java
*This will extend the customer class and implement the strategy in which the customer randomly chooses two lines
, then joins the shorter of those two
*/

import java.util.ArrayList;
import java.util.Random;

public class Pick2Customer extends Customer{
	public Pick2Customer(int num_items){
		//constructor
		super(num_items,2);
	}

	public int chooseLine(ArrayList<CheckoutAgent> checkouts){
		Random ran = new Random();
		int line1 = ran.nextInt(checkouts.size());
		int line2 = ran.nextInt(checkouts.size());
		while(line2==line1){
			line2 = ran.nextInt(checkouts.size());
		}
		int num1 = checkouts.get(line1).getNumInQueue();
		int num2 = checkouts.get(line2).getNumInQueue();
		if(num1<=num2){
			return line1;
		}
		else{return line2;}
	}
}