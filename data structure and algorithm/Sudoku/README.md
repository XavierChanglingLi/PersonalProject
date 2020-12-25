# Sudoku

Author: Changling Li

Date: 09/29/2019

This project aims to solve the game sudoku. Given a random number of locked numbers, the program will show up a window where the current sudoku is displayed. Clicking the start button, it will start to solve the sudoku. To reset, click the reset button.

The implementation uses 2D array to create the grid where the numbers are located. The solve function is based on backtracking which uses a stack data structure to keep track of the solutions.

The automate file runs the sudoku for 100 times with numbers of locked numbers from 0 to 4 and counts the number of solved cases. This intends to show the statistics of the solution.

In the following example, we selected 2 locked numbers as showing in first image and then run the program to find the solution as shown in second image.


<img width="323" alt="start" src="https://user-images.githubusercontent.com/59809140/103112607-90616100-4624-11eb-88f3-25564040363c.png">

<img width="319" alt="solution" src="https://user-images.githubusercontent.com/59809140/103112606-8fc8ca80-4624-11eb-8eb6-9e43e95139a0.png">

To run the simulation:

Compile: javac Simulation.java

Run: java Simulation (number of locked numbers)
