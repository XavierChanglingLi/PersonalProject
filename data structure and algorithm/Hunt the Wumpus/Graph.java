/*
*Author: Changling Li
*Date: 11/28/19
*Name: Graph.java
*The graph class where the vertex are connected
*/

import java.util.ArrayList;
import java.util.PriorityQueue;


public class Graph{
	//maintains a list of vertices
	private ArrayList<Vertex> graph;
	private int number;


	public Graph(){
	//constructor
		graph = new ArrayList<Vertex>();
		number = 0;
	}

	public int vertexCount(){
	//returns the number of the vertices in the graph
		return graph.size();
	}

	public void addEdge(Vertex v1, Direction dir, Vertex v2){
	//adds v1 and v2 to the graph and adds an edge connecting v1 to v2 via direction dir
	//and a second edge connecting v2 to v1 in the opposite direction, creating a bi-directional link
		if(graph.contains(v1)!=true){
			graph.add(v1);
			number++;
		}
		if(graph.contains(v2)!=true){
			graph.add(v2);
			number++;
		}
		v1.connect(v2, dir);
		v2.connect(v1, v2.opposite(dir));
	}

	public void shortestPath(Vertex v0){
	//a single-source shortest-path algorithm for the Graph, Dijkstra's algorith.
	//This method finds the cost of the shortest path between v0 and every other connected
	//vertex in the graph, counting every edge as having unit cost.
	//output the cost of each vertex v in G is the shortest distance from v0 to v;
		for(Vertex vertex: graph){
			vertex.setMarked(false);
			vertex.setCost(Integer.MAX_VALUE);
		}
		PriorityQueue<Vertex> pq = new PriorityQueue<Vertex>();
		v0.setCost(0);
		pq.add(v0);
		while(pq.size()!=0){
			Vertex v = pq.poll();
			v.setMarked(true);
			for(Vertex w: v.getNeighbors()){
				if(w.isMarked()==false&&(v.getCost()+1)<w.getCost()){
					w.setCost(v.getCost()+1);
					pq.remove(w);
					pq.add(w);
				}
			}
		}
	}

	public static void main(String[] args){
		Graph g = new Graph();
		Vertex v1 = new Vertex(0,0,"A");
		Vertex v2 = new Vertex(0,0,"B");
		Vertex v3 = new Vertex(0,0,"C");
		Vertex v4 = new Vertex(0,0,"D");
		Vertex v5 = new Vertex(0,0,"E");
		Vertex v6 = new Vertex(0,0,"F");

		g.addEdge(v1, Direction.EAST, v2);
		g.addEdge(v2, Direction.EAST, v3);
		g.addEdge(v2, Direction.NORTH, v4);
		g.addEdge(v4, Direction.NORTH, v5);
		g.addEdge(v1, Direction.WEST, v6);
		g.shortestPath(v1);
		for(Vertex v: g.graph){
			System.out.println("The minimum cost from A to " + v.getLabel() + " is "+ v.getCost());
		}

	}
}
