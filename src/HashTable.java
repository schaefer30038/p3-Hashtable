////////////////////////////////////////////////////////////////////////////////
// Semester:         CS400 Spring 2018
// PROJECT:          cs400_p3 (Performance Analysis of Data Structures:
//                   Hashtables)
// FILES:            HashTable.java, AnalysisTest.java, HashTableADT.java,
//                   PerformanceAnalysisHash.java, PerformanceAnalysis.java
//                   
//
//
// USER:             Andrew Schaefer (acschaefer@wisc.edu)
//
// Instructor:       Deb Deppeler (deppeler@cs.wisc.edu)
// Bugs:             no known bugs
//
//
//////////////////////////// 80 columns wide ///////////////////////////////////

import java.util.NoSuchElementException;

/*
 * Makes use of the a bucket for each index in the array table. Similar to a
 * LinkedList with a key, a value, and the next element in the bucket
 */
class Bucket<K, V>  {
    K key;  // key used to hash
    V value;    // actual value of the element we want to store
    Bucket<K, V> next;  // the next element inserted in the bucket
    
    /*
     * Constructs the bucket and keeps track of the key and value of the element
     */
    Bucket(K key, V value) {
        this.key = key;
        this.value = value;
        next = null;
    }
}


/*
 * Contains all the hash methods. Makes use of generics to allow keys and values of
 * different objects
 */
public class HashTable<K, V> implements HashTableADT<K, V> {
    
    int capacity; // the number of elements the array can hold
    
    double loadFactor;  // multiply this with the number of elements in the array to
                        // get the number of elements that can be added before the
                        // table needs to be resized
    
    Bucket[]table; // the array based hashtable. Elements will be inserted in here
    
    int size;   // this keeps track of number of elements in hashtable

    
    /* 
     * Constructs the hashtable with an initial capacity. Capacity will change
     * depending on the load factor. Creates an array of buckets
     */
    public HashTable(int initialCapacity, double loadFactor) {
        capacity = initialCapacity;
        this.loadFactor = loadFactor;
        table = new Bucket[capacity];
    }
    
    
    
    /*
     * inserts an element at a specific index depending on the key. Throws
     * NullPointerException if key is equal to null
     */
    @Override
    public V put(K key, V value) throws NullPointerException {
        if (key==null) {
            throw new NullPointerException();
        }
        
        int hash = Math.abs(key.hashCode() % capacity); // index is calculated here
        Bucket thisIndex = null;
        
        // insert new bucket if index is empty
        if (table[hash] == null) {
            table[hash] = new Bucket(key, value);
            size++;
        } else {    // add onto the bucket if the index already contains an element
        
            thisIndex = table[hash];
            
            // exit if the same key is found or end of bucket is reached
            while (thisIndex.next != null && !thisIndex.key.equals(key)) {
                thisIndex = thisIndex.next;
            }
            
            // if the key is equivalent, change the value
            if (thisIndex.key.equals(key)) {
                thisIndex.value = value;
            } else {
                // otherwise link this element to the new inserted element
                thisIndex.next = new Bucket(key, value);
                size++;
            }
        }
        checkSize();
        return value;
    }

    
    
    /*
     * Multiplies the capacity by 2 and adds 1 when making a new table. Rehashes
     * all the old elements into the new table
     */
    private void resizeTable() {
        capacity = (capacity*2) + 1;
        Bucket[]oldTable = table;   // store the oldTable
        table = new Bucket[capacity];
        Bucket thisIndex = null;
        
        for (int i=0; oldTable.length>i; i++) {
            // get reference from oldtable
            thisIndex = oldTable[i];
            
            // rehashes and puts all the elements from the old bucket into the new table
            while (thisIndex != null) {
                put((K)thisIndex.key, (V)thisIndex.value);
                thisIndex = thisIndex.next;
            }
        }
        
        
    }
    
    
    
    /*
     * checks to see if the size of the array has exceeded the loadFactor
     * amount. Resizes the table if size exceeds, otherwise returns nothing
     */
    private void checkSize() {
        double fraction = ((double)size())/((double)capacity);  
        // checks loadFactor with division
        
        if (fraction >= loadFactor) {
            resizeTable();
        }
    }
    
    
    
    
    /*
     * clears the entire array by setting each element to null
     */
    @Override
    public void clear() {
        for (int i=0; i<table.length; i++) {
            table[i] = null;
        }
    }

    
    
    /*
     * returns element with the specified key. Returns null if no element
     * is found at the key. Throws NoSuchElementException if no element is found
     */
    @Override
    public V get(K key) throws NoSuchElementException{
        int hash = Math.abs(key.hashCode() %capacity);
        Bucket thisIndex = table[hash]; // store reference to bucket at index
        
        // search through every element at the particular index
        while (thisIndex!=null) {
            if (thisIndex.key.equals(key)) {
                return (V)thisIndex.value;  // return the value if key is found
            }
            thisIndex = thisIndex.next;
        }
        throw new NoSuchElementException();
    }

    
    
    /*
     * returns false if the array contains any values. Returns true otherwise
     */
    @Override
    public boolean isEmpty() {
        return size==0;
    }

    
    
    
    /*
     * removes the element at the specified key from the hashtable. Returns null
     * when key is not found. Throws NullPointerException if key is null
     */
    @Override
    public V remove(K key) throws NullPointerException{
        if (key==null) {
            throw new NullPointerException();
        }
        
        int hash = Math.abs(key.hashCode()%capacity);
        V element = null;
        Bucket prevIndex = null;
        Bucket thisIndex = table[hash]; // the bucket indicated by the key
        
        // iterate through all the elements in the bucket in this particular index
        while(thisIndex!=null) {
            if (thisIndex.key.equals(key)) {
                // if there is a previous element, make the previous element
                // link up with the current index next element
                if (prevIndex!=null) {
                    element = (V)thisIndex.value;
                    prevIndex.next = thisIndex.next;
                    size--;
                    return element;
                }
                // if no previous element, make the current hash location equal
                // to the next index
                if (prevIndex==null) {
                    element = (V)thisIndex.value;
                    thisIndex = thisIndex.next;
                    size--;
                    return element;
                }
            }   // iterate through the next element in bucket
            prevIndex = thisIndex;
            thisIndex = thisIndex.next;
        }
        return element;
    }

    
    
    
    
    /*
     * returns the number of elements in the hashtable
     */
    @Override
    public int size() {
        return size;
    }
    
    
}


