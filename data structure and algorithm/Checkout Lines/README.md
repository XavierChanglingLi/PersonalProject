# Checkout Lines

Author: Changling Li

Date: 10/14/2019

This project simulates the check-out line at a large store to explore four different algorithms for decision-making. The four decision-making strategies are:
1. Select the queue with the least customers.
2. Select the queue with the least check out items.
3. Pick a queue at random.
4. Pick two queues randomly and then join the shorter queue.

The implementation uses queue data structure which is a node-based doubly-linked list. The simulation runs 10 times with the average checking-out time and standard deviation printed out.

One example is shown below where we selected the RandomCustomer model and the statistics is also calculated.

<img width="490" alt="checkout lines" src="https://user-images.githubusercontent.com/59809140/103112463-90149600-4623-11eb-9e19-2ac61b3c94c2.png">

<img width="506" alt="checkout" src="https://user-images.githubusercontent.com/59809140/103112462-8db23c00-4623-11eb-90df-d31fe6dc9a87.png">


To run the simulation:

Compile: javac Pick2CustomerSimulation.java or PickItemCustomerSimulation.java or PickyCustomerSimulation.java or RandomCustomerSimulation.java

Run: java Pick2CustomerSimulation or PickItemCustomerSimulation or PickyCustomerSimulation or RandomCustomerSimulation

