/**
*Author: Changling Li
*Date: 10/7/19
*Name: MyQueue.java
*/

import java.util.Iterator;

public class MyQueue<T> implements Iterable<T>{
	private int size;
	private Node<T> head;
	private Node<T> tail;

	private class LLIterator implements Iterator<T>{
		//Iterator subclass that handles traversing the list
		private Node<T> curnode;
		//the item contained in the current node which is also the next item in the list

		public LLIterator(Node head){
			//the constructor for the LLIterator given the head of a list
			this.curnode = head;
		}

		public boolean hasNext(){
			//returns true if there are still values to traverse(if the current node reference is not null)
			return curnode!=null;
		}

		public T next(){
			//returns the next item in the queue, which is the item contained in the current node
			T data = curnode.getThing();
			curnode = curnode.getNext();
			return data;
		}

		public void remove(){
			//optional for an iterator
		}
	}

	private class Node<T>{
		//private class for node
		private Node<T> next;
		//the node at the back field
		private T data;
		//the data of the current node
		private Node<T> prev;
		//the node at the front field

		public Node(T item){
			//constructor
			this.next = null;
			this.data = item;
			this.prev = null;
		}
		public T getThing(){
			//get the data of the current node
			return data;
		}
		public Node getNext(){
			//return the next node
			return next;
		}
		public Node getPrev(){
			//return the last node
			return prev;
		}
		public void setNext(Node b){
			//connect the current node with the next node
			this.next = b;
		}
		public void setPrev(Node f){
			//connect the current node with the last node
			this.prev = f;
		}
	}

	public MyQueue(){
		//constructor initialize the queue so that it is empty 
		this.size = 0;
		this.head = null;
		this.tail = null;
	}

	public void clear(){
		//clear the queue 
		this.size = 0;
		this.head = null;
		this.tail = null;
	}

	public int size(){
		//return the size of the queue
		return size;
	}

	public Iterator<T> iterator(){
		//return a new LLIterator with head passed to the constructor
		return new LLIterator(this.head);
	}

	public void offer(T item){
		//inserts an element at the end of the queue(tail) if possible otherwise return false
		Node<T> newNode = new Node<T>(item);
		if(size==0){this.head = newNode;}
		else{this.tail.setNext(newNode);}//connect the tail to the newNode
		newNode.setPrev(tail);
		this.tail = newNode;
		size++;
	}

	public T poll(){
		//remove and return the head of the queue
//		if(size == 0){return false;}
//		else{
		if(size==0){return null;}
		if(size==1){
			T removed = peek();
			clear();
			return removed;}
		else{
			T removed = head.getThing();
			Node<T> newhead = head.getNext();
			head.getNext().setPrev(null);
			head.setNext(null);
			this.head = newhead;
			size--;
			return removed;
		}
//		}
	}

	public T peek(){
	//return but do not remove the head of the queue
		if(size==0){return null;}
		else{return head.getThing();}
	}


//extension2
	public T get(int index){
		if(index==0){
			return head.getThing();
		}
		else{
			Node<T> currNode = head;
			for(int i=0;i<index;i++){
				currNode = currNode.getNext();
			}
			return currNode.getThing();
		}
	}

	public static void main(String[] args){
		MyQueue<Integer> q = new MyQueue<Integer>();
		q.offer(3);
		q.offer(4);
		q.offer(5);
		q.offer(6);
		System.out.println(q.size());
		System.out.println(q.peek());
		System.out.println(q.get(0));
		System.out.println(q.get(1));
		q.poll();
		System.out.println(q.peek());
		System.out.println(q.size());
		q.clear();
		System.out.println(q.size());
	}

}