// Online Java Compiler
// Use this editor to write, compile and run your Java code online

/*
You should now test with:

Different key types (String, Integer, CustomClass).
Colliding hash codes to ensure chaining works.
Negative hashCodes for safe indexing.
Removing middle-chain nodes for collision buckets.
Adding and updating same key to ensure overwrite works.

*/
//test can we can we change the value of  static variable of parent class 
import java.util.Iterator;
import java.util.NoSuchElementException;
class MyHashMap<T1, T2> implements Iterable<MyHashMap.Entry<T1, T2>>
{
    //here in class T1 , T2 is needed because with the help of this only we will be able get the data in case of collision 
    
    class Node <T1 , T2>
    {
        public T1 key;
        public T2 value;
        public Node <T1,T2> next;
        //constructor to initialize 
        Node(T1 key , T2 value)
        {
            this.key = key;
            this.value = value;
            this.next = null;
        }
        
    }
    public T1 key;
    public T2 value;
    private int mapSize; //restrcict the access control , if this variable will be public it can be modified outside the class 
    private int bucketArraySize;
    private  Node<T1, T2>[] bucketArray; // it can not be accessed outside the class 
    //creating constructor 
    //default constructor
    MyHashMap()
    {
        //initiallly at the time of object creation size of the bucket array will be zero 
        this.bucketArraySize = 1024;
        
        // @SuppressWarnings("unchecked"); //by adding this line its giving error 
        this.bucketArray = new Node[bucketArraySize];
        this.mapSize = 0; //initialize the size of the map 
    }
    //parametarised constructor  - i will not allow this only with the help of put method you can add data into the MyHashMap 
    
    // MyHashMap(T1 key , T2 value)
    // {
    //     this.key = key;
    //     this.value = value;
    //     this.put(key ,value);//if during object creation key , value is passed it will put
    // }
    
    //implement the functionalities 
    // int MyHashFunction(T1 key)
    // {
    //     int hashvalue;
        
        
    //     //business logic 
        
    //     return hashvalue;
    // }
    
    // int getIndexFromKey(T1 key)
    // {
    //     int index = computeHashcode(key);
    //     //bussiness logic 
    //     return index;
    // }
    /** below is the implementation of the iterable **/
    
        /** 1. Entry holder **/
    public static class Entry<K, V> {
        public final K key;
        public final V value;
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    /** 2. Iterable interface method **/
    @Override
    public Iterator<Entry<T1, T2>> iterator() {
        return new MapIterator();
    }

    /** 3. Iterator implementation **/
    private class MapIterator implements Iterator<Entry<T1, T2>> {
        private int bucketIndex = 0;
        private Node<T1, T2> currentNode = null;

        MapIterator() {
            advanceToNext();
        }

        private void advanceToNext() {
            // If we’re in a chain, move to next node first
            if (currentNode != null && currentNode.next != null) {
                currentNode = currentNode.next;
                return;
            }
            // Otherwise, find the next non‐empty bucket
            currentNode = null;
            while (bucketIndex < bucketArraySize) {
                Node<T1, T2> head = bucketArray[bucketIndex++];
                if (head != null) {
                    currentNode = head;
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public Entry<T1, T2> next() {
            if (currentNode == null) {
                throw new NoSuchElementException();
            }
            Entry<T1, T2> entry = new Entry<>(currentNode.key, currentNode.value);
            advanceToNext();
            return entry;
        }
    }

    /** iterator implementation ends here **/
    
    T2 getValue(T1 key)
    {
        T2 ans = null;
        //get the value
        int index = computeHashcode(key);
        //here we need to check the length of the linked list 
        int size =0; // implement the length functio
        Node <T1, T2> head =  bucketArray[index%bucketArraySize];
        while(head != null)
        {
            size++;
            head = head.next;
        }
        if(size >= 1)
        {
             head = bucketArray[index%bucketArraySize];
            Node <T1, T2> temp = head;
            while(temp != null)
            {
                if(keysAreEqual(temp.key , key))
                {
                    // System.out.println("value = " + temp.value);
                     ans = (T2)temp.value;
                     break;
                }
                temp = temp.next;
            }
        }
        
        return ans;
    }
    //todo:only create when there is no Node <T1, T2> of the key exist;
    //update:
    void put(T1 key , T2 value)
    {
        //check the size of the bucketArray if we can add the data 
        int index = computeHashcode(key);
        Node <T1, T2> head = bucketArray[index%bucketArraySize];
        
        //check if value exist or not
        Node <T1, T2> temp2 = head;
        while(temp2 != null)
        {
            if(keysAreEqual(temp2.key , key))
            {
                temp2.value = value;
                return;
            }
            temp2 = temp2.next;
        }
        mapSize++;//if execution has come to this line it means this key does not exist in map 
        // System.out.println("head reference value for key =  " + key + " is : " + head);
        if(head == null)
        {
            // System.out.println("head reference value for key =  " + key + " is : " + head);
            Node <T1, T2> newnode = new Node(key , value);
            newnode.key = key;
            newnode.value = value;
            head = newnode;
            head.next = null;
            bucketArray[index%bucketArraySize] = head;
            
        }else{
            //add to the head , so that we can reduce the computation cost 
            //do here for already existing node 
            Node <T1, T2> temp = head;
            Node <T1, T2> newhead = new Node(key , value);
            newhead.key = key;
            newhead.value = value;
            newhead.next = head;
            bucketArray[index%bucketArraySize] = newhead;
        }
    }
    
    //this removes entire key bucket <--correct it , it should only delete the specific key , value ,
    //update:this function has been updated and should work as expected 
    //todo : update the mapSize
    //update: updated the mapSize value 
    private Integer computeHashcode(T1 key)
    {
        int index = 0;
        if(key != null)
        {
            index = key.hashCode();
        }
        return index;
    }
    
    private boolean keysAreEqual(T1 nodekey, T1 actkey) {
        if (nodekey == null && actkey == null) {
            return true;
        }
        // If nodekey is null but actkey is not, this call throws NPE
        if (nodekey == null || actkey == null) {
            return false;
        }
        return nodekey.equals(actkey);
    }
    
    
    void removeKeyValue(T1 key)
    {
        //improve this function , what if key is not available 
        
        //here i have to implement delete node  in linked list in case of 
        //collision because more than one node will be there
        int index = computeHashcode(key);
        // bucketArray[index%bucketArraySize] = null;
        // System.out.println("kye = " + key + " removed");
        
        //implement the delete of the selected key node from the linked list 
        
        Node <T1, T2> head = bucketArray[index%bucketArraySize];
        //now from the above linked list i have to delete the Node <T1, T2> of having corresponding key , value
        Node <T1, T2> prev = null , curr = head;
        Node <T1, T2> temp = head;
        while(temp != null)
        {
            //this is the head Node <T1, T2> 
			
            if((keysAreEqual(temp.key , key)  && head == temp))
            {
                Node <T1, T2> newhead = head.next;
                head.next = null;//remove the reference of the head Node <T1, T2> 
                head = newhead;//reassign the new head 
                mapSize--;
                break;
            }else if(temp.next == null && keysAreEqual(temp.key , key))
            {
                //last Node <T1, T2>
                prev.next = null; //just dereference
                // temp.next = null;
                mapSize--;
                
            }else{
                //body
                if(keysAreEqual(temp.key , key))
                {
                    Node <T1, T2> node = temp;
                    prev.next = curr.next;
                    node.next = null;
                    curr = prev.next;
                    temp = curr;
                    mapSize--;
                    break;
                }else{
                    prev = temp;
                    curr = temp.next;
                    temp = temp.next;
                }
                
            }
        }
        temp = head;
        //assign again the head after update
        bucketArray[index%bucketArraySize] = head;
        
    }
	public int getSize()
	{
		return mapSize;//already calcilated the size of the map just return it 
	}
    
    public void clear()
    {
        
        //should delete all key value present in the map 
        for(int i = 0; i<bucketArraySize; i++)
        {
            bucketArray[i] = null;//dereference garbage collector do its work 
        }
        mapSize = 0;//set mapSize to zero 
        // System.out.println("kye = " + key + " , all value removed");
        //just empty the bucketArray 
    }
	
	public boolean containsKey(T1 key)
	{
		boolean ans = false;
		int index = computeHashcode(key);
		Node <T1, T2> temp = bucketArray[index%bucketArraySize];
		//now i have iterate through the linked list to find if there is any key available or not 
		
		while(temp != null)
		{
			if(keysAreEqual(temp.key , key))
			{
				return true;
			}
			temp = temp.next;
		}
		return ans;
		
		//check whether key is present or not , if not present then return false otherwise true 
		
	}
    
    //store the data in the hashmap , for that first create the data structure 
    
    //for this i need to use the data structure in which we can fetcht the data efficiently 
    // 1 -> hashed linked list 
    //main discussion is that what data structre i will use to implement this 
    //so that we can store the data and we can fetch the data in the constant time 
    //for the given key 
    
    
    
}
class Main {
    public static void main(String[] args) {
        // System.out.println("Try programiz.pro");
        MyHashMap<Integer , Integer>  mymap1 = new MyHashMap<>();
        // mymap1.put(1 , "arjun");
        // mymap1.put(2 , "nishant");
        // System.out.println(mymap1.getValue(1));
        // System.out.println(mymap1.getValue(2));
        // mymap1.removeKeyValue(1);
        // System.out.println(mymap1.getValue(1));
        // System.out.println(mymap1.getValue(2));
        // mymap1.clear();
        // System.out.println(mymap1.getValue(1));
        // System.out.println(mymap1.getValue(2));
        
        for(int i = 0 ; i<56; i++)
        {
            mymap1.put(i , -5*i + 345);
            // System.out.print(i + " " +( -5*i + 345 )+ " must be equal to  ");
            // System.out.print(i + " " + mymap1.getValue(i) + " , ");
            // System.out.println();
        }
        
        System.out.println("length of the map = " +  mymap1.getSize());
        for(int i = 0; i<10; i++)
        {
            mymap1.removeKeyValue(i);
        }
        System.out.println("after deletion : ");
        mymap1.put(10 , 1);
        mymap1.put(11 , 2);
        
        for(int i = 0 ; i<56; i++)
        {
            // mymap1.put(i , -5*i + 345);
            // System.out.print(i + " " +( -5*i + 345 )+ " must be equal to  ");
            System.out.print(i + " " + mymap1.getValue(i) + " , ");
            System.out.println();
        }
        
        System.out.println("length of the map = " +  mymap1.getSize());
        //insertion and deletion is working fine 
      for(MyHashMap.Entry<Integer , Integer> item: mymap1)
      {
          System.out.println(item.key + " => " + item.value);
      }
      
      System.out.println("iterator is working as expected!!!");
      
      //lets check the null key andling 
      System.out.println("lets check the null key handling ");
      
      //updation of null key is also handled 
      mymap1.put(null , 2);
      mymap1.put(null , 234);
      System.out.println("value for null key = " + mymap1.getValue(null));

    }
}

/*
🚀 Potential final "basic" map feature checklist:
Feature	Status
put(key, value)	✅
get(key)	✅
remove(key)	✅
clear() / clear()	✅
containsKey()	🔄 //now it has been implemented
containsValue()	🔄 //this i dont want to implement 
size()	✅
supports collisions	✅
resizes dynamically	🔄
iterator / traversal	🔄 // about to complete , initial version is added 
null key handling	🔄 //ongoing... (implement the hashcode() calculation and equal method in different method to handle the null key )
//keysAreEqual() and computeHashcode() has been implemented , migrate to these to method for comparision of 
//key and for computing the hashcode to handle null key efficiently .
load factor support	🔄

*/

//another feedback for myhashmap to be production ready 
/*

Your code demonstrates a good foundational custom HashMap implementation with iterator support. To enhance it for production use and ensure robustness, consider completing/improving the following aspects:

No.	Area	Description / Missing Implementation
1	Resizing / Rehashing	Add automatic resizing of bucketArray when mapSize exceeds a threshold (e.g., load factor * bucketArraySize). This prevents performance degradation due to collisions. Rehash all existing entries during resize.
2	Robust Hash Function Usage	Currently, computeHashcode() returns raw key.hashCode(). Consider using a more sophisticated hash mixing and handle negative hashcodes safely for index calculation. E.g., index = (hash & 0x7FFFFFFF) % bucketArraySize.
3	Thread Safety	If used in concurrent environments, add synchronization or utilize concurrency-safe structures/methods. Currently, it is not thread-safe.
4	equals() and hashCode() Contract	Clarify key comparison: your keysAreEqual() uses .equals(), which is standard, but you should explicitly document key requirements.
5	Null Pointer Risk	Ensure null keys and null values are handled consistently and safely. Your code supports null keys; consider documenting this.
6	removeKeyValue() Bug Fix	The removeKeyValue() logic is somewhat complex. Simplify removal logic and confirm mapSize decrements only once per removal; currently, looping may cause multiple decrements unintentionally.
7	Overriding toString()	For better debugging and logging, override toString() method to provide readable map contents.
8	Error Handling	Add proper handling or exceptions for invalid operations, e.g., null inputs where not allowed, or methods like getValue() returning null when a key is not present.
9	Unit Tests & Benchmarks	Implement thorough unit tests covering all scenarios including collisions, removals, iteration, null keys/values, and resizing effects. Benchmark critical operations.
10	Generics Cleanup	In Node inner class, you don’t need to redeclare generic parameters <T1, T2> again as class-level generics suffice. This is not an error but can be cleaned for clarity.

*/