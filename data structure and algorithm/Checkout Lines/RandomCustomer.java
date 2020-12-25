/**
*Author: Changling Li
*Date: 10/12/19
*Name: RandomCustomer.java
*This will extend the customer class and implement the random line-choosing strategy
*/
import java.util.Random;
import java.util.ArrayList;

public class RandomCustomer extends Customer{

	public RandomCustomer(int num_items){
		//constructor
		super(num_items, 1);
	}

	public int chooseLine(ArrayList<CheckoutAgent> checkouts){
		//return an integer randomly chosen from the range 0 to the length of the list
		Random ran = new Random();
		int checkout = ran.nextInt(checkouts.size());
		return checkout;
	}
}