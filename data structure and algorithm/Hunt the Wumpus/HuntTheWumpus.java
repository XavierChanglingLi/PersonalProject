/*
*Author: Changling Li
*Date: 12/2/19
*Name: HuntTheWumpus.java
*Creates the game board and game control
*/

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.util.Random;

import javax.imageio.ImageIO;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;


import java.util.*;

/**
 * Creates a window with two text fields, one buttons, and a large
 * display area. The app then tracks the mouse over the display area.
 **/
public class HuntTheWumpus {

    // These four fields are as in the LandscapeDisplay class (though 
    // they are now all private)
    private JFrame win;
    private LandscapePanel canvas;    
    private Landscape scape; 
    private int scale;
    private Graph graph;
    private Hunter hunter;
    private Wumpus wumpus;
    private boolean lost;//whether the player lose or win
    private boolean shoot;//whether the player is armed

    /** fields related to demo of mouse interaction **/
    // Unless you have a good reason to report the mouse position in
    // HuntTheWumpus, you should remove these fields and all the
    // code that affects them.
    // There here to demonstrate how you would add a message to the bottom
    // of the window. For HuntTheWumpus, you may want to use it to indicate
    // that the Hunter is armed or close to the Wumpus, or dead.
    JLabel Armed; // Label field 1, displays the X location of the mouse 
    JLabel WinText; // Label field 2, displays the Y location of the mouse 

    // controls whether the game is playing or exiting
    private enum PlayState { PLAY, STOP, WIN, LOSE }
    private PlayState state;

    /**
     * Creates the main window
     * @param scape the Landscape that will hold the objects we display
     * @param scale the size of each grid element
     **/		 
    public HuntTheWumpus() {
        // The game should begin in the play state.
        this.state = PlayState.PLAY; 
        
        // Create the elements of the Landscape and the game.
        this.scale = 64; // determines the size of the grid
        this.scape = new Landscape(scale*10,scale*10);

        Vertex[][] grid = new Vertex[6][6];
        for(int x=0; x<6;x++){
        	for(int y=0; y<6; y++){
        		grid[x][y]= new Vertex(x,y);
        		this.scape.addBackgroundAgent(grid[x][y]);
        	}
        }
        graph = new Graph();
        //connect horizontal vertices
        for(int x=0; x<5;x++){
        	for(int y=0; y<6; y++){
        		graph.addEdge(grid[x][y], Direction.EAST, grid[x+1][y]);
        	}
        }
        //connect vertical vertices 
        for(int x=0; x<6;x++){
        	for(int y=0; y<5; y++){
        		graph.addEdge(grid[x][y], Direction.SOUTH, grid[x][y+1]);
        	}
        }

        hunter = new Hunter(grid[0][0]);
        Random ran = new Random();
        wumpus = new Wumpus(grid[ran.nextInt(5)+1][ran.nextInt(5)+1]);

        //calculate the shortest path
        graph.shortestPath(wumpus.getPosition());
        //make the room where the hunter is visible
        hunter.getPosition().setVisible(true);

        this.scape.addHunter(hunter);
        this.scape.addWumpus(wumpus);
        // Make the main window
        this.win = new JFrame("Hunt the Wumpus");
        win.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);

        // make the main drawing canvas (this is the usual
        // LandscapePanel we have been using). and put it in the window
        this.canvas = new LandscapePanel( this.scape.getWidth(),
					                                        this.scape.getHeight() );
        this.win.add( this.canvas, BorderLayout.CENTER );
        this.win.pack();

        // make the labels and a button and put them into the frame
        // below the canvas.
        this.Armed = new JLabel("Unarmed");
        JButton quit = new JButton("Quit");
        JPanel panel = new JPanel( new FlowLayout(FlowLayout.RIGHT));
        panel.add( this.Armed );
        panel.add( quit );
        this.win.add( panel, BorderLayout.SOUTH);
        this.win.pack();

        // Add key and button controls.
        Control control = new Control();
        this.win.addKeyListener(control);
        this.win.setFocusable(true);
        this.win.requestFocus();
        quit.addActionListener( control );

        // for mouse control
        // Make a MouseControl object and then bind it to the canvas
        MouseControl mc = new MouseControl();
        this.canvas.addMouseListener( mc );
        this.canvas.addMouseMotionListener( mc );

        // The last thing to do is make it all visible.
        this.win.setVisible( true );

    }



    private class LandscapePanel extends JPanel {
		
        /**
         * Creates the drawing canvas
         * @param height the height of the panel in pixels
         * @param width the width of the panel in pixels
         **/
        public LandscapePanel(int height, int width) {
            super();
            this.setPreferredSize( new Dimension( width, height ) );
            this.setBackground(Color.white);
        }

        /**
         * Method overridden from JComponent that is responsible for
         * drawing components on the screen.  The supplied Graphics
         * object is used to draw.
         * 
         * @param g		the Graphics object used for drawing
         */
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            scape.draw( g );
        }
    } // end class LandscapePanel

    // This is the class where you define functions that are 
    // executed when certain mouse events take place.
    private class MouseControl extends MouseInputAdapter {
        public void mouseMoved(MouseEvent e) {;}

        public void mouseDragged(MouseEvent e) {;}
        
        public void mousePressed(MouseEvent e) {
            System.out.println( "Pressed: " + e.getClickCount() );
        }

        public void mouseReleased(MouseEvent e) {
            System.out.println( "Released: " + e.getClickCount());
        }

        public void mouseEntered(MouseEvent e) {
            System.out.println( "Entered: " + e.getPoint() );
        }

        public void mouseExited(MouseEvent e) {
            System.out.println( "Exited: " + e.getPoint() );
        }

        public void mouseClicked(MouseEvent e) {
    	    System.out.println( "Clicked: " + e.getClickCount() );
        }
    } // end class MouseControl

    private class Control extends KeyAdapter implements ActionListener {

        public void keyTyped(KeyEvent e) {
            System.out.println( "Key Pressed: " + e.getKeyChar() );
            if( ("" + e.getKeyChar()).equalsIgnoreCase("q") ) {
                dispose();
            }

            if((""+e.getKeyChar()).equalsIgnoreCase("z")){
            	if(!shoot){
            		shoot = true;
            		Armed.setText("armed");
            	}
            	else{
            		shoot = false;
            		Armed.setText("unarmed");
            	}
            }
            if(!shoot&&state==PlayState.PLAY){
            	if((""+e.getKeyChar()).equalsIgnoreCase("w")){
            		if(hunter.getPosition().getNeighbor(Direction.NORTH) != null){
						hunter.updatePosition(hunter.getPosition().getNeighbor(Direction.NORTH));
					}
					hunter.getPosition().setVisible(true);
            	}
            	if((""+e.getKeyChar()).equalsIgnoreCase("s")){
            		if(hunter.getPosition().getNeighbor(Direction.SOUTH) != null){
						hunter.updatePosition(hunter.getPosition().getNeighbor(Direction.SOUTH));
					}
					hunter.getPosition().setVisible(true);
            	}
            	if((""+e.getKeyChar()).equalsIgnoreCase("a")){
            		if(hunter.getPosition().getNeighbor(Direction.WEST) != null){
						hunter.updatePosition(hunter.getPosition().getNeighbor(Direction.WEST));
					}
					hunter.getPosition().setVisible(true);
            	}
            	if((""+e.getKeyChar()).equalsIgnoreCase("d")){
            		if(hunter.getPosition().getNeighbor(Direction.EAST) != null){
						hunter.updatePosition(hunter.getPosition().getNeighbor(Direction.EAST));
					}
					hunter.getPosition().setVisible(true);
            	}
            }//if we did not shoot;
            else if(shoot&&state==PlayState.PLAY){
            	if((""+e.getKeyChar()).equalsIgnoreCase("w")){
            		if(hunter.getPosition().getNeighbor(Direction.NORTH)==wumpus.getPosition()){
            			wumpus.getPosition().setVisible(true);
            			wumpus.shot(true);
            			state = PlayState.WIN;
            		}
            		else{state = PlayState.LOSE;}
            	}
            	if((""+e.getKeyChar()).equalsIgnoreCase("s")){
            		if(hunter.getPosition().getNeighbor(Direction.SOUTH)==wumpus.getPosition()){
            			wumpus.getPosition().setVisible(true);
            			wumpus.shot(true);
            			state = PlayState.WIN;
            		}
            		else{state = PlayState.LOSE;}
            	}
            	if((""+e.getKeyChar()).equalsIgnoreCase("a")){
            		if(hunter.getPosition().getNeighbor(Direction.WEST)==wumpus.getPosition()){
            			wumpus.getPosition().setVisible(true);
            			wumpus.shot(true);
            			state = PlayState.WIN;
            		}
            		else{state = PlayState.LOSE;}
            	}
            	if((""+e.getKeyChar()).equalsIgnoreCase("d")){
            		if(hunter.getPosition().getNeighbor(Direction.EAST)==wumpus.getPosition()){
            			wumpus.getPosition().setVisible(true);
            			wumpus.shot(true);
            			state = PlayState.WIN;
            		}
            		else{state = PlayState.LOSE;}
            	}
            }
            if((""+e.getKeyChar())!=null){
            	lost = wumpus.isVisible(hunter);
            }
        }

        public void actionPerformed(ActionEvent event) {
            // If the Quit button was pressed
            if( event.getActionCommand().equalsIgnoreCase("Quit") ) {
		        System.out.println("Quit button clicked");
                state = PlayState.STOP;
            }
        }
    } // end class Control

    public void repaint() {
    	this.win.repaint();
    }

    public void dispose() {
	    this.win.dispose();
    }

    public void winscreen(){
    	this.WinText = new JLabel("You killed the Wumpus");
    	this.WinText.setLocation(0, this.scape.getWidth()/2);
    	this.canvas.add(this.WinText, BorderLayout.CENTER);
    	this.win.setVisible(true);
    }

    public void losescreen(){
    	this.WinText = new JLabel("You lost the game");
    	this.WinText.setLocation(0, this.scape.getWidth()/2);
    	this.canvas.add(this.WinText, BorderLayout.CENTER);
    	this.win.setVisible(true);
    }

    public static void main(String[] argv) throws InterruptedException {
        HuntTheWumpus w = new HuntTheWumpus();
        while (w.state == PlayState.PLAY) {
            w.repaint();
            Thread.sleep(33);
            if(w.lost){
				w.state = PlayState.LOSE;
				w.repaint();
			}
        }
        if(w.state == PlayState.WIN){
			w.winscreen();
			w.repaint();
			System.out.println("You Won!");
		}
		if(w.state == PlayState.LOSE){
			w.losescreen();
			w.repaint();
			System.out.println("You Lost >_<");
		}
        if(w.state == PlayState.STOP){
            System.exit(1);
        }

    }
	
} // end class InteractiveLandscapeDisplay

