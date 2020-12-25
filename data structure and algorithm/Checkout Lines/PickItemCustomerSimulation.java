/**
*Author: Changling Li
*Date: 10/7/19
*Name: PickItemCustomerrSimulation.java
*Simulate the decision-making when using the pickItemCustomer model
*/

import java.util.Random;
import java.util.ArrayList;

public class PickItemCustomerSimulation {
    // test function that creates a new LandscapeDisplay and populates it with 5 checkouts and 99 customers.
    public static void main(String[] args) throws InterruptedException {
        Random gen = new Random();
        ArrayList<CheckoutAgent> checkouts = new ArrayList<CheckoutAgent>(5);

        for(int i=0;i<5;i++) {
            CheckoutAgent checkout = new CheckoutAgent( i*100+50, 480 );
            checkouts.add( checkout );
        }
        Landscape scape = new Landscape(500,500, checkouts);
        LandscapeDisplay display = new LandscapeDisplay(scape);
        
        for(int a=0; a<10; a++){
            for (int j = a*100; j < a*100+100; j++) {
            
                Customer cust = new PickItemCustomer(1+gen.nextInt(5), checkouts.size());
                int choice = cust.chooseLine( checkouts );
                checkouts.get(choice).addCustomerToQueue( cust );
                scape.updateCheckouts();
                display.repaint();
                Thread.sleep(10);
            }
            scape.printFinishedCustomerStatistics();
        }
 //       scape.printFinishedCustomerStatistics();
    }

}