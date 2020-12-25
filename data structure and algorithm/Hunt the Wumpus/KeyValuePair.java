/*
*Author: Changling Li
*Date: 11/28/19
*Name: KeyValuePair.java
*/

public class KeyValuePair<Key, Value>{
	private Key key;
	private Value value;

	public KeyValuePair(Key k, Value v){
		this.key = k;
		this.value = v;
	}

	public Key getKey(){
		return this.key;
	}

	public Value getValue(){
		return this.value;
	}

	public void setValue(Value v){
		this.value = v;
	}

	public String toString(){
		String returnstr = "";
		returnstr = "The key is " + String.valueOf(key) + ". The value is " +String.valueOf(value)+".";
		return returnstr;
	}

	public static void main(String[] args){
		KeyValuePair<Integer, Integer> kv = new KeyValuePair<Integer, Integer>(1, 1);
		System.out.println(kv.toString());
		kv.setValue(2);
		System.out.println(kv.getValue());
		System.out.println(kv.getKey());
	}
}