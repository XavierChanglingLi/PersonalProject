/**
*Author: Changling Li
*Date: 10/20/19
*Name: BSTMap.java
*implementation of binary search tree that based on linked node
*/
import java.util.ArrayList;
import java.util.Comparator;

public class BSTMap<K, V> implements MapSet<K, V> {
	//fields
	private TNode root;
	private Comparator<K> comp;
	private int size;
	// fields here
	// need a TNode root
	// need a Comparator<K> to compare Keys
	// probably want a size field

	// constructor: takes in a Comparator object
	public BSTMap( Comparator<K> comp ) {
		// initialize fields here
		this.comp = comp;
		root = null;
		size = 0;
	}

	// adds or updates a key-value pair
	// If there is already a piar with new_key in the map, then update
	// the pair's value to new_value.
	// If there is not already a pair with new_key, then
	// add pair with new_key and new_value.
	// returns the old value or null if no old value existed
	public V put( K key, V value ) {
				// check for and handle the special case
				// call the root node's put method
		if(root==null){
			root = new TNode(key, value);
            size++;
			return null;
		}
        if(this.containsKey(key)){
            return root.put(key, value, comp);
        }
		else{
            size++;
            return root.put(key, value, comp);
		}
    }

    // gets the value at the specified key or null
    public V get( K key ) {
            // check for and handle the special case
            // call the root node's get method
    	if(root==null){
    		return null;
    	}
    	return root.get(key, comp);
            // stub code
       //     return null;
    }

    // Write stubs (functions with no code) for the remaining
    // functions in the MapSet interface
    public boolean containsKey(K key){
    	//returns true if the map already contains a node with the specified key
        if(root==null){return false;}
        return root.containsKey(key,comp);
    }

	public ArrayList<K> keySet(){
        //returns an Arraylist that contains all the key in preorder
		ArrayList<K> keyset = new ArrayList<>();
        if(root!=null){
            keyset.add(root.getKey());
            return root.keySet(keyset);
        }
        else{return null;}
	}

	public ArrayList<V> values(){
		//returns an Arraylist that contains all of the values in the map. Their order should
		//match the order returned by keyset
		ArrayList<V> valueset = new ArrayList<>();
        if(root!=null){
            valueset.add(root.getValue());
            return root.values(valueset);
        }
        else{return null;}
	}

	public ArrayList<KeyValuePair<K,V>> entrySet(){
		//returns an Arraylist of KeyValuePair objects. The pairs should be in a pre-order traversal ordering 
		ArrayList<KeyValuePair<K,V>> entryset = new ArrayList<>();
        if(root!=null){
            entryset.add(root.getPair());
            return root.entrySet(entryset);
        }
        else{return null;}
	}

    public int size(){
        return size;
    }

	public void clear(){
		root = null;
		size = 0;
	}
    // You can make this a nested class, doesn't have to be
    private class TNode {
            // need fields for the left and right children
            // need a KeyValuePair to hold the data at this node
    	private TNode left;
    	private TNode right;
    	private KeyValuePair<K, V> pair;

            // constructor, given a key and a value
        public TNode( K k, V v ) {
                    // initialize all of the TNode fields
            left = null;
            right = null;
            pair = new KeyValuePair<K,V>(k,v);
        }

        // Takes in a key, a value, and a comparator and inserts the TNode
        // Returns the old value of the node, if replaced, or null if inserted
        public V put( K key, V value, Comparator<K> comp ) {
                // implement the binary search tree put
                // insert only if the key doesn't already exist
        	if(comp.compare(key, this.getKey())<0){
        		if(this.getLeft()==null){this.setLeft(new TNode(key, value));return null;}
        		else{return this.left.put(key, value, comp);}
        	}
        	if (comp.compare(key, this.getKey())>0){
        		if(this.getRight()==null){this.setRight(new TNode(key, value));return null;}
        		else{return this.right.put(key, value, comp);}
        	}
            else{
                V oldvalue = this.getValue();
                this.setValue(value);
                return oldvalue;}
            // stub code
           //return null;	
        }

        // Takes in a key and a comparator
        // Returns the value associated with the key or null
        public V get( K key, Comparator<K> comp ) {
        	int cmp = comp.compare(key,this.getKey());
        	if(cmp<0){
                if(this.left==null){return null;}
                else{return this.left.get(key, comp);}}
        	if(cmp>0){
                if(this.right==null){return null;}
                else{return this.right.get(key, comp);}}
        	else{return this.getValue();}
                // stub code
        }

        public boolean containsKey(K key, Comparator<K> comp){
            if(key.equals(this.pair.getKey())){return true;}
            else if(comp.compare(key,this.pair.getKey())<0){
                if(this.left!=null){return this.left.containsKey(key,comp);}
            }
            else{
                if(this.right!=null){return this.right.containsKey(key,comp);}
            }
            return false;
        }

        public ArrayList<K> keySet(ArrayList<K> keyset){
            if(this.left!=null){
                keyset.add(this.left.getKey());
                this.left.keySet(keyset);
            }
            if(this.right!=null){
                keyset.add(this.right.getKey());
                this.right.keySet(keyset);
            }
            return keyset;
        }

        public ArrayList<V> values(ArrayList<V> valueset){
            if(this.left!=null){
                valueset.add(this.left.getValue());
                this.left.values(valueset);
            }
            if(this.right!=null){
                valueset.add(this.right.getValue());
                this.right.values(valueset);
            }
            return valueset;
        }

        public ArrayList<KeyValuePair<K,V>> entrySet(ArrayList<KeyValuePair<K,V>> entryset){
            if(this.left!=null){
                entryset.add(this.left.getPair());
                this.left.entrySet(entryset);
            }
            if(this.right!=null){
                entryset.add(this.right.getPair());
                this.right.entrySet(entryset);
            }
            return entryset;
        }

        public K getKey(){return pair.getKey();}//get the value of the pair

        public V getValue(){return pair.getValue();}//get the key of the pair

        public void setValue(V value){pair.setValue(value);}//set the value of the pair

        public KeyValuePair<K, V> getPair(){return pair;}//get the pair

        public TNode getLeft(){return left;}//get the left child 

        public TNode getRight(){return right;}//get the right child

        public void setLeft(TNode node){left = node;}//set the left child
        
        public void setRight(TNode node){right = node;}//set the right child

        // Remaining methods below, mostly for building ArrayLists
        
        
    }// end of TNode class

    // test function
    public static void main( String[] argv ) {
            // create a BSTMap
            BSTMap<String, Integer> bst = new BSTMap<String, Integer>( new StringAscending() );

            bst.put( "twenty", 20 );
            bst.put( "ten", 10 );
            bst.put( "eleven", 11 );
            bst.put( "five", 5 );
            bst.put( "six", 6 );

            System.out.println( bst.get( "eleven" ) );
            bst.put( "eleven", 12 );
            System.out.println( bst.get( "eleven" ) );
            System.out.println( bst.size());
            System.out.println(bst.containsKey("six"));
            ArrayList<KeyValuePair<String, Integer>> bstree = bst.entrySet();
            for(KeyValuePair<String, Integer> pair: bstree){
                System.out.print(pair.getKey());
                System.out.print(" ");
                System.out.println(pair.getValue());
            }

            ArrayList<String> key = bst.keySet();
            for(String k: key){
                System.out.println(k);
            }

            ArrayList<Integer> value = bst.values();
            for(Integer i: value){
                System.out.println(i);
            }

            // put more test code here
    }

}//end of the BSTMap class


// comparator class separate
class StringAscending implements Comparator<String> {
		public int compare( String a, String b ) {
				return a.compareTo(b);
		}
}

