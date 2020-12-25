/**
*Author: Changling Li
*Date: 10/6/19
*Name: LinkedList.java
*/

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;

public class LinkedList <T> implements Iterable<T> {
	//T defualt type is object.
	private int size;
	private Node<T> top;

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
			if(this.curnode !=null){
				return true;
			}
			else{
				return false;
			}
		}

		public T next(){
			//returns the next item in the list, which is the item contained in the current node
			if(this.hasNext()){
				Node<T> toreturn = this.curnode;
				this.curnode = this.curnode.getNext();
				return toreturn.getThing();
			}
			else{
				return null;
			}
		}


		public void remove(){
			//optional for an iterator
		}
	}

	private class Node<T>{
		//container class
		private Node<T> next;
		private T data;

		public Node(T item){
		//a constructor that initializes next to null and container field to item
			this.next=null;
			this.data=item;
		}

		public T getThing(){
		//returns the value of the container field
			return data;
		}

		public void setNext(Node n){
		//sets next to the give node
			next = n;
		}

		public Node getNext(){
			//returns the next field
			return next;
		}
	}

	public LinkedList(){
		//constructor that initializes the fields so it is an empty list.
		size = 0;
		top = null;
	}

	public Iterator<T> iterator(){
		//return a new LLIterator with head passed to the constructor
		return new LLIterator(top);
	}

	public void clear(){
		//empties the list
		size = 0;
		top=null;
	}

	public int size(){
		//returns the size of the list
		return size;
	}

	public void addFirst(T item){
		//inserts the item at the beginning of ths list
		Node<T> newNode = new Node<T>(item);
		newNode.setNext(top);
		top = newNode;
		size++;
	}

	public void addLast(T item){
		//appends the item at the end of the list
		//create a new node, point the new node to null and point the previous one to the new node
		if(top==null){
			top=new Node<T>(item);
			size++;
		}
		else{
			Node<T> newNode = new Node<T>(item);
			Node<T> currNode = top;
			while(currNode.getNext()!=null){
				currNode = currNode.getNext();
			}
			currNode.setNext(newNode);
			size++;
		}
	}

	public void add(int index, T item){
		//inserts the item at the specified position in the list
		if(index<0||index>size){
			throw new IndexOutOfBoundsException();
		}
		if(index==0){
			addFirst(item);
		}
		else{
			Node<T> newNode = new Node<T>(item);
			Node<T> currNode = top;
			for(int i=1;i<index;i++){
				currNode = currNode.getNext();
			}
			newNode.setNext(currNode.getNext());
			currNode.setNext(newNode);
			size++;
		}
	}

	public T remove(int index){
		//removes the item at the specified position in the list
		Node<T> removed;
		if(index<0||index>size){
			throw new IndexOutOfBoundsException();
		}
		if(index==0){
			removed = top;
			this.top=this.top.getNext();
		}
		else{
			Node<T> currNode =top;
			for(int i=0;i<index-1;i++){
				currNode = currNode.getNext();
			}
			removed = currNode.getNext();
			currNode.setNext(removed.getNext());
		}
		size--;
		return removed.getThing();
	}

	public ArrayList<T> toArrayList(){
		ArrayList<T>list = new ArrayList<T>();
		for(T item: this){
			list.add(item);
		}
		return list;
	}

	public ArrayList<T> toShuffledList(){
		ArrayList<T> shuffled=this.toArrayList();
		Collections.shuffle(shuffled);
		return shuffled;
	}
}

