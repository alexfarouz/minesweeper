package com.minesweeper;
/**
 * Dynamic Array class.
 * @param <T> generic item stored in array.
 */
public class DynArr310<T> {

	/**
	 * Storage for DynArr310.
	 */
	private T[] storage;
	/**
	 * Minimum size of array.
	 */
	private static final int MINCAP = 2;
	/**
	 * Current number of elements in array.
	 */
	private int size = 0;

	/**
	 * Default Constructor for Dynamic Array.
	 * @MINCAP is the default size of the Dynamic Array (2).
	 */
	@SuppressWarnings("unchecked")
	public DynArr310(){
		//MINCAP is the original size of the array
		storage = (T[]) new Object[MINCAP];
	}

	/**
	 * Second constructor for specified size Dynamic Array.
	 * @param initCap the size of the array.
	 * @throws IllegalArgumentException if initCap < MINCAP.
	 */
	@SuppressWarnings("unchecked")
	public DynArr310(int initCap){
		if(initCap < MINCAP){
			throw new IllegalArgumentException("Capacity must be at least 2!");
		}
		// Initial capacity of the storage should be initCap.
		storage = (T[]) new Object[initCap];		
	}
	
	/**
	 * Number of elements currently in storage, O(1).
	 * @return current number of elements.
	 */
	public int size(){	
		return size;
	}  	
	
	/**
	 * Max capacity of storage, O(1).
	 * @return current capacity of dynamic array.
	 */
	public int capacity(){
		return storage.length;
	} 
		
	/**
	 * Returns item at the index and replaces it with a new item, O(1).
	 * @param index the index which is being referenced to.
	 * @param value the value which is replacing the return.
	 * @return the value which was originally stored in the index.
	 */
	public T set(int index, T value) {
		if(index < 0 || index >= size){ // Check for invalid indices
			throw new IndexOutOfBoundsException("Index: " + index + " out of bounds!");
		}
		if(value == null){ // Check if value is null
			throw new IllegalArgumentException("Null values not accepted!");
		}
		T ret = storage[index]; // Store return value
		storage[index] = value;
		return ret; 		
	}

	/**
	 * Returns item at the given index, O(1).
	 * @param index in the dynamic array.
	 * @return element at the index.
	 */
	public T get(int index){
		if(index >= storage.length || index < 0){
			throw new IndexOutOfBoundsException("Index: " + index + " out of bounds!");
		}
		return storage[index];
	}

	/**
	 * Adds a value into storage.
	 * If storage is full the size doubles.
	 * @param value value added into the storage.
	 */
	@SuppressWarnings("unchecked")
	public void add(T value){
		if(value == null){
			throw new IllegalArgumentException("Null values not accepted!");
		}
		if(size()+1 == capacity()){ // Check if resizing is needed.
			T[] newStorage = (T[]) new Object[capacity()*2]; // Double the capacity.
			for(int i = 0; i < size; ++i){
				if(storage[i] == null){
					break;
				}
				newStorage[i] = storage[i]; // Assign existing values to expanded dynamic array.
			}
			storage = newStorage; // Set the storage.
		}
		storage[size] = value; // Set the new value.
		size++; // Increment the size.
	}
	
	/**
	 * Insert the value into the given index, shifting elements if necessary, O(N).
	 * @param index the index where the value should be inserted.
	 * @param value the value being inserted.
	 */
	public void insert(int index, T value){
		if(index < 0 || index > size){ // Checks for invalid indices
			throw new IndexOutOfBoundsException("Index: " + index + " out of bounds!");
		} else if(value == null){ // Checks if value is null
			throw new IllegalArgumentException("Null values not accepted!");
		} else if(index == size){
			add(value);
		} else if(size+1 == capacity()){ // Inserts and expands capacity
			insertCapacity(true, index, value);
		} else { 					   // Inserts without expanding capacity
			insertCapacity(false, index, value);
		}
	}
	
	/**
	 * Remove and return the element at the index, shift elements if needed, O(N).
	 * Error if index is invalid.
	 * If the number of elements after removal falls below or at 1/3 of the capacity, 
	 * halve capacity (rounding down) of the storage. However, capacity should NOT go below MINCAP.
	 * @param index the index of the element to be removed.
	 * @return the element which is removed.
	 */
	@SuppressWarnings("unchecked")
	public T remove(int index){
		
		if(index < 0 || index >= capacity()){ // Check if out of bounds.
			throw new IndexOutOfBoundsException("Index: " + index + " out of bounds!");
		}

		T ret = storage[index]; // Store the return value
		T[] newStorage; // Create a new generic array
		double div = (double)capacity()/3;
		if((size-1 <= div) && (capacity()/2 >= MINCAP)){ // Checks conditions to see if the 
			newStorage = (T[]) new Object[capacity()/2];
		} else {
			newStorage = (T[]) new Object[capacity()];
		}
		byte gap = 0;
		for(int i = 0; i < size; ++i){
			if(i == index){
				gap++;
				continue;
			}
			newStorage[i-gap] = storage[i];
		}
		size--;
		storage = newStorage;
		return ret;	
	} 
	
	/**
	 * Helper method for insert().
	 * @param x2 boolean telling whether to double capacity of dynArr or not.
	 * @param index the index of insertion.
	 * @param value value being inserted.
	 */
	@SuppressWarnings("unchecked")
	private void insertCapacity(boolean x2, int index, T value){
		T[] newStorage;
		if(x2){
			newStorage = (T[]) new Object[capacity()*2]; // Double the capacity if needed.
		} else {
			newStorage = (T[]) new Object[capacity()];
		}
		byte j = 0;
		for(int i = 0; i < size; ++i){
			if(i == index){ 
				newStorage[i] = value;
				size++; // If the iteration is at the current index, increase the size. 
				j++; // Increase j by 1 to account for the difference
				continue; // End the current iteration;
			}
			newStorage[i] = storage[i-j]; // Assign existing values to expanded dynamic array.
		}
		storage = newStorage; // Set the storage.
	}
	
	//******************************************************
	//*******     BELOW THIS LINE IS PROVIDED code   *******
	//*******             Do NOT edit code!          *******
	//*******		   Remember to add JavaDoc		 *******
	//******************************************************

	@Override
	public String toString() {
		//This method is provided. Add JavaDoc and comments.
		
		StringBuilder s = new StringBuilder("[");
		for (int i = 0; i < size(); i++) {
			s.append(get(i));
			if (i<size()-1)
				s.append(", ");
		}
		s.append("]");
		return s.toString().trim();
		
	}
	
	//******************************************************
	//*******     BELOW THIS LINE IS TESTING CODE    *******
	//*******      Edit it as much as you'd like!    *******
	//*******		Remember to add JavaDoc			 *******
	//******************************************************
	
	/**
	 * Visual Representation of the dynamic array.
	 * @return string value.
	 */
	public String toStringDebug() {
		//This method is provided for debugging purposes
		//(use/modify as much as you'd like), it just prints
		//out the DynArr310 details for easy viewing.
		StringBuilder s = new StringBuilder("DynArr310 with " + size()
			+ " items and a capacity of " + capacity() + ":");
		for (int i = 0; i < size(); i++) {
			s.append("\n  ["+i+"]: " + get(i));
		}
		return s.toString().trim();
		
	}

	
	/**
	 * Main method for testing.
	 * @param args arguments.
	 */
	public static void main (String args[]){
		//These are _sample_ tests. If you're seeing all the "yays" that's
		//an excellend first step! But it does NOT guarantee your code is 100%
		//working... You may edit this as much as you want, so you can add
		//own tests here, modify these tests, or whatever you need!

		//create a DynArr310 of integers
		DynArr310<Integer> ida = new DynArr310<>();
		if ((ida.size() == 0) && (ida.capacity() == 2)){
			System.out.println("Yay 1");
		}

		//add some numbers at the end
		for (int i=0; i<3; i++)
			ida.add(i*5);

		//uncomment to check details
		//System.out.println(ida);
		
		//checking dynamic array details
		if (ida.size() == 3 && ida.get(2) == 10 && ida.capacity() == 4){
			System.out.println("Yay 2");
		}
		
		//insert, set, get
		ida.insert(1,-10);
		ida.insert(4,100);
		if (ida.set(1,-20) == -10 && ida.get(2) == 5 && ida.size() == 5 
			&& ida.capacity() == 8 ){
			System.out.println("Yay 3");
		}
		
		//create a DynArr310 of strings
		DynArr310<String> letters = new DynArr310<>(6);
		
		//insert some strings
		letters.insert(0,"c");
		letters.insert(0,"a");
		letters.insert(1,"b");
		letters.insert(3,"z");
		
		//get, toString()
		if (letters.get(0).equals("a") && letters.toString().equals("[a, b, c, z]")){
			System.out.println("Yay 4");
		}
		
		//remove
		if (letters.remove(0).equals("a") && letters.remove(1).equals("c") &&
			letters.get(1).equals("z") && letters.size()==2 && letters.capacity()==3){
			System.out.println("Yay 5");			
		}

		//exception checking
		try{
			letters.set(-1,null);
		}
		catch (IndexOutOfBoundsException ex){
			if (ex.getMessage().equals("Index: -1 out of bounds!")){
				System.out.println("Yay 6");			
			}
		}
	}
        

}