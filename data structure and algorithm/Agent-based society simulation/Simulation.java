/**
*Author: Changling Li
*Date: 10/8/19
*Name: Simulation.java
*Simulate the society depends on the user's setting
*/


import java.util.Random;
import java.util.Scanner;

public class Simulation{
	public static void main(String[] args) throws InterruptedException{
		Scanner scan = new Scanner(System.in);//create a scanner object
		System.out.println("enter the width");
		int width = scan.nextInt();
		System.out.println("enter the height");
		int height = scan.nextInt();
		System.out.println("enter the radius");
		int radius = scan.nextInt();
		System.out.println("enter the type of simulation 1(single agent), 2(two different agents)");
		int sim = scan.nextInt();
       	Random gen = new Random();

		if(sim ==1){
			System.out.println("enter the number of agents");
			int numAgents = scan.nextInt();
			Landscape scape = new Landscape(width,height);
        	//use for loop to lop through number of agents to create them
        	for(int i=0;i<numAgents;i++) {
            	scape.addAgent( new SocialAgent( gen.nextDouble() * scape.getWidth(),gen.nextDouble() * scape.getHeight(),radius ) );
        	}
        	LandscapeDisplay scapeplay = new LandscapeDisplay(scape);
			for(int i=0; i<200; i++){
			//update the state of the agents
			scape.updateAgents();
			scapeplay.repaint();
			Thread.sleep(250);
			}
		}
		

		if(sim ==2){
			System.out.println("enter the number of agents 1");
			int numAgents1 = scan.nextInt();
			System.out.println("enter the number of agents 2");
			int numAgents2 = scan.nextInt();
			Landscape scape = new Landscape(width,height);
       	 //use for loop to lop through number of agents to create them
        	for(int i=0;i<numAgents1;i++) {
            	scape.addAgent( new CatSocialAgent( gen.nextDouble() * scape.getWidth(),gen.nextDouble() * scape.getHeight(),1, radius ) );
        	}	
        	for(int i=0;i<numAgents2;i++) {
            	scape.addAgent( new CatSocialAgent( gen.nextDouble() * scape.getWidth(),gen.nextDouble() * scape.getHeight(),2, radius ) );
        	}
			LandscapeDisplay scapeplay = new LandscapeDisplay(scape);
			for(int i=0; i<200; i++){
			//update the state of the agents
				scape.updateAgents();
				scapeplay.repaint();
				Thread.sleep(250);
			}
		}
	}
}
