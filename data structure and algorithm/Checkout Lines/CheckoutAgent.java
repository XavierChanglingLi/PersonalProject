/**
*Author: Changling Li
*Date: 10/12/19
*Name: CheckoutAgent.java
*each checkout agent will maintain a queue of customers. At each time-step, then checkout agent will either
remove an item from the customer at the head of the queue, or if the customer has no more items, remove the 
customer herself from the queue. The checkoutagent will be displayed graphically as a tall rectangle, which
its height reflecting the number of customers in the queue.
*/

import java.awt.Graphics;
import java.awt.Color;

public class CheckoutAgent{
	private int x;
	private int y;
	private MyQueue<Customer> myqueue;

	public CheckoutAgent(int x, int y){
		//constructor
		this.x = x;
		this.y = y;
		myqueue = new MyQueue<Customer>();
	}

	public void addCustomerToQueue(Customer c){
		//add a customer to its queue
		myqueue.offer(c);
	}

	public int getNumInQueue(){
		//returns the number of customers in its queue
		return myqueue.size();
	}
//extension 1, compare the number of the items and then choose a lien
	public int getNumItemsInQueue(){
		int numItems=0;
		for(Customer c: myqueue){
			numItems += c.getNumItems();
		}
		return numItems;
	}

	public void draw(Graphics g){
		//draws the checkoutAgent as a rectangle near the bottom of the window with a height
		//proportional to the number of customers in the queue. A height of 10*N(where N is the number)
		//of customers in the queue) and width of 10 should work. You should assume that(this.x, this.y) is 
		//the bottom left corner of the rectangle

		//the base project
		//for(int i=0;i<this.getNumInQueue(); i++){
		//	g.setColor(new Color(208,205,246));
		//	g.fillRect(this.x, this.y-i*10,10,10);
		//}

		//extension2
		for(int i=0;i<this.getNumInQueue(); i++){
			if(myqueue.get(i).getNumItems()>=5){
				g.setColor(new Color(0,0,153));
				g.fillRect(this.x, this.y-i*10,10,10);
			}
			if(myqueue.get(i).getNumItems()>=3&&5>myqueue.get(i).getNumItems()){
				g.setColor(new Color(0,0,255));
				g.fillRect(this.x, this.y-i*10,10,10);
			}
			if(3>myqueue.get(i).getNumItems()){
				g.setColor(new Color(51,204,255));
				g.fillRect(this.x, this.y-i*10,10,10);
				
			}
		}
	//	g.setColor(new Color(208, 205, 246));
	//	g.fillRect(this.x, this.y, 10, 10*this.getNumInQueue());
	}

	public void updateState(Landscape scape){
		for(Customer c: myqueue){
			c.incrementTime();
			//System.out.println("if");
		}
		if(myqueue.peek()!=null){
			if(myqueue.peek().getNumItems()==0){
				//System.out.println("if");
				scape.addFinshedCustomer(myqueue.poll());
			}
			else{
				myqueue.peek().giveUpItem();
			}
		}
		else{;}
	}
}