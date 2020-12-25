/**
*Author: Changling Li
*Date: 10/14/19
*Name: KeyValuePair.java
*key value pair object to associate the key with the value. Dictionary like.
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