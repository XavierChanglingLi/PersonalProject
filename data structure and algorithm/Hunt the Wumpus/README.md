# Hunt the Wumpus

Author: Changling Li

Date: 12/4/2019

This program implements a simple version of game hunt the wumpus where the user controls the character and moves around the graph to hunt the wumpus. To control the character, use w, a, s, d for movement and z for getting armed. 
The implementation uses graph data structure where vertex are connected. A Dijkstra's algorithm is also implemented to find the cost of the shortest path between the starting vertex and every other connected vertex in the graph. 

The game will start as shown in the following image and when the user wins the game, the Wumpus will turn from black to red.

<img width="638" alt="start" src="https://user-images.githubusercontent.com/59809140/103112549-4b3d2f00-4624-11eb-93ff-f5043f82cebd.png">

<img width="640" alt="win" src="https://user-images.githubusercontent.com/59809140/103112550-4b3d2f00-4624-11eb-8380-4dd1d4272066.png">

To run the simulation:

Compile: javac HuntTheWumpus.java

Run: java HuntTheWumpus

