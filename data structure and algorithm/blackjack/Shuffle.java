/**
 * File: Shuffle.java
 * Author: Changling Li
 * Date: 09/09/2019
 */

import java.util.ArrayList;
import java.util.Random;


public class Shuffle
{ 
	public static void main (String[] args){
		ArrayList<Integer> arr = new ArrayList<Integer>();
		ArrayList<Integer> newarr = new ArrayList<Integer>(); 
		Random ran = new Random();
		for(int i=0;i<10;i++){
			//int val = ran.nextInt(100);
			int val = i+1;
			arr.add(val);
		}
		for(int a=0;a<10;a++){
			Integer x = arr.get(a);
			System.out.println(x);
		}
		System.out.println(arr);
		for(int b=0;b<10;b++){
			int index = ran.nextInt(arr.size()); 
			int removed = arr.get(index);
			arr.remove(index);
			newarr.add(removed);
			//arr.remove(index);
			//System.out.println("The removed number is "+Integer.toString(removed) + ".The array is" + arr);
		}
		System.out.println(newarr);
	}

}
